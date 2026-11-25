import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class SSLClient {
    static final String TRUSTSTORE_PATH = "keys/client_truststore.p12";
    static final String TRUSTSTORE_PASSWORD = "changeit";
    static final String HOST = "localhost";
    static final int PORT = 8443;

    public static void main(String[] args) throws Exception {
        // Load the client truststore
        KeyStore trustStore = KeyStore.getInstance("PKCS12");
        try (FileInputStream fis = new FileInputStream(TRUSTSTORE_PATH)) {
            trustStore.load(fis, TRUSTSTORE_PASSWORD.toCharArray());
        }

        // Initialize TrustManagerFactory
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);

        // Initialize SSLContext
        SSLContext sslContext = SSLContext.getInstance("TLSv1.3");
        sslContext.init(null, tmf.getTrustManagers(), null);

        // Create SSLSocket and connect
        SSLSocketFactory sf = sslContext.getSocketFactory();
        try (SSLSocket socket = (SSLSocket) sf.createSocket(HOST, PORT)) {
            
            SSLParameters sslParams = socket.getSSLParameters();

            // Enable hostname verification (this would be necessary in a real-world scenario)
            // sslParams.setEndpointIdentificationAlgorithm("HTTPS");
 
            socket.setSSLParameters(sslParams);

            try (DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                 DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()))) {

                // Authenticate as "testuser" with password "changeit"
                out.writeInt(OperationCode.AUTHENTICATE.getCode()); // Operation code for authentication
                out.writeUTF("testuser");
                out.writeUTF("changeit");
                out.flush();

                // Read server reply for authentication
                ErrorCode replyError = ErrorCode.fromCode(in.readInt());
                System.out.println("Server replied: " + replyError);
            }
        }
    }
    
    public ErrorCode authenticate(String username, String password) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    public ErrorCode sendFile(String filePath) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    public ErrorCode getFile(String fileName, String destinationPath) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    public ErrorCode deleteFile(String fileName) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    public Result<List<String>> listFiles() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}