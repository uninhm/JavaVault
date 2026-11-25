import javax.net.ssl.*;
import java.io.*;
import java.security.KeyStore;

public class SSLServer {
    static final int PORT = 8443;
    static final String KEYSTORE_PATH = "keys/server.p12";
    static final String KEYSTORE_PASSWORD = "changeit";

    public static void main(String[] args) throws Exception {
        // Load the server keystore
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        try (FileInputStream fis = new FileInputStream(KEYSTORE_PATH)) {
            keyStore.load(fis, KEYSTORE_PASSWORD.toCharArray());
        }

        // Initialize KeyManagerFactory with the keystore
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, KEYSTORE_PASSWORD.toCharArray());

        // Initialize SSLContext
        SSLContext sslContext = SSLContext.getInstance("TLSv1.3");
        sslContext.init(kmf.getKeyManagers(), null, null);

        // Create SSLServerSocket
        SSLServerSocketFactory ssf = sslContext.getServerSocketFactory();
        try (SSLServerSocket serverSocket = (SSLServerSocket) ssf.createServerSocket(PORT)) {
            System.out.println("SSL Server listening on port " + PORT);

            while (true) {
                try (SSLSocket clientSocket = (SSLSocket) serverSocket.accept();
                     DataInputStream in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
                     DataOutputStream out = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()))) {

                    // Force handshake explicitly to catch TLS errors immediately
                    clientSocket.startHandshake();

                    // Read structured data from client
                    String message = in.readUTF();
                    int number = in.readInt();
                    System.out.println("Received: " + message + " | " + number);

                    // Send response back
                    out.writeUTF("ACK: " + message);
                    out.flush(); // Crucial when using BufferedOutputStream
                } catch (IOException e) {
                    System.err.println("Client connection error: " + e.getMessage());
                }
            }
        }
    }
}