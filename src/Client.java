import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class Client {
    static String HOST = "localhost";
    static int PORT = 4444;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(HOST, PORT);
            // PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            // BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

            int keyLength = in.readInt();
            byte[] keyArray = new byte[keyLength];
            in.readFully(keyArray);

            Cipher cipher = Cipher.getInstance("AES");
            Key key = new SecretKeySpec(keyArray, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            System.out.println(HexFormat.of().formatHex(keyArray));

            // Send encrypted messages from standard input
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                byte[] ciphertext = cipher.doFinal(userInput.getBytes());
                System.out.println(ciphertext.length);
                System.out.println(HexFormat.of().formatHex(ciphertext));
                out.writeInt(ciphertext.length);
                out.write(ciphertext);
            }

            in.close();
            out.close();
            stdIn.close();
            socket.close();

            System.out.println("Connection closed");
        } catch (UnknownHostException e) {
            System.err.printf("Don't know about host: %s\n", HOST);
            System.exit(-1);
        } catch (IOException e) {
            System.err.printf("Couldn't get IO for the connection to %s\n", HOST);
            System.exit(-1);
        } catch (NoSuchAlgorithmException
                 | NoSuchPaddingException
                 | IllegalBlockSizeException
                 | BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            System.err.println("The key sent by the server is invalid.");
        }
    }
}
