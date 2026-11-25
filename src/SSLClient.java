import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.security.KeyStore;

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
    static final String PASSWORD = "changeit";

    public static void main(String[] args) throws Exception {
        // Load the client truststore
        KeyStore trustStore = KeyStore.getInstance("PKCS12");
        try (FileInputStream fis = new FileInputStream(TRUSTSTORE_PATH)) {
            trustStore.load(fis, PASSWORD.toCharArray());
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

                // Send structured data
                out.writeUTF("Hello Secure World");
                out.writeInt(42);
                out.flush(); // Must flush to ensure data is transmitted over TLS

                // Read server response
                String response = in.readUTF();
                System.out.println("Server replied: " + response);
            }
        }
    }
}