import java.io.*;
import java.net.Socket;
import java.nio.file.Path;

public class ClientHandler implements Runnable {
    static final Path VAULTS_DIR = Path.of("vaults");

    Socket clientSocket;
    String username;
    Vault vault;

    ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
    
    @Override
    public void run() {
        try (DataInputStream in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
             DataOutputStream out = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()))) {
            
            while (true) {
                int op;

                try {
                    // Read the operation code
                    op = in.readInt();
                } catch (IOException e) {
                    break;
                }

                // Handle the operation accordingly
                switch (op) {
                    case 0:
                        handleAuthentication(in, out);
                        break;
                    case 1:
                        handleNewFile(in, out);
                        break;
                    case 2:
                        handleGetFile(in, out);
                        break;
                    case 3:
                        handleDeleteFile(in, out);
                        break;
                    case 4:
                        handleListFiles(in, out);
                        break;
                    default:
                        System.err.println("Unknown operation: " + op);
                        handleUnknownOperation(in, out);
                        break;
                }
            }
            
            System.out.println("Client disconnected: " + clientSocket.getInetAddress());
        } catch (IOException e) {
            System.err.println("ClientHandler error: " + e.getMessage());
        }
    }

    void handleAuthentication(DataInputStream in, DataOutputStream out) throws IOException {
        String username = in.readUTF();
        String password = in.readUTF();
        
        Authenticator authenticator = new Authenticator();
        
        if (!authenticator.checkPassword(username, password)) {
            out.writeInt(ErrorCode.AUTHENTICATION_FAILED.getCode());
            out.flush();
            return;
        }

        // Store the authenticated username for future operations
        this.username = username;
        
        try {
            // Initialize the user's vault
            this.vault = new Vault(username);
        } catch (IOException e) {
            System.err.println("Error initializing vault for user " + username + ": " + e.getMessage());
            out.writeInt(ErrorCode.INTERNAL_SERVER_ERROR.getCode());
            out.flush();
            return;
        }

        out.writeInt(ErrorCode.SUCCESS.getCode());
        out.flush();
    }
    
    void handleNewFile(DataInputStream in, DataOutputStream out) {
        throw new UnsupportedOperationException("New file operation not implemented yet");
    }

    void handleGetFile(DataInputStream in, DataOutputStream out) {
        throw new UnsupportedOperationException("Get file operation not implemented yet");
    }

    void handleDeleteFile(DataInputStream in, DataOutputStream out) {
        throw new UnsupportedOperationException("Delete file operation not implemented yet");
    }

    void handleListFiles(DataInputStream in, DataOutputStream out) {
        throw new UnsupportedOperationException("List files operation not implemented yet");
    }

    void handleUnknownOperation(DataInputStream in, DataOutputStream out) throws IOException {
        out.writeInt(ErrorCode.INVALID_OPERATION.getCode());
        out.flush();
    }
}