import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import javax.crypto.*;
import java.security.*;
import java.util.HexFormat;

public class Server {
    static int PORT = 4444;

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(PORT)) {
            Socket client = server.accept();

            // PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            // BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            DataInputStream in = new DataInputStream(client.getInputStream());
            DataOutputStream out = new DataOutputStream(client.getOutputStream());

            KeyGenerator kg = KeyGenerator.getInstance("AES");
            Key key = kg.generateKey();

            byte[] keyArray = key.getEncoded();

            System.out.println(key.getClass());
            System.out.println(keyArray.length * 8);
            System.out.println(HexFormat.of().formatHex(keyArray));

            out.writeInt(keyArray.length);
            out.write(keyArray);

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);

            while (true) {
                int ciphertextLength = in.readInt();
                System.out.println(ciphertextLength);
                byte[] ciphertext = new byte[ciphertextLength];
                in.readFully(ciphertext);
                System.out.println(HexFormat.of().formatHex(ciphertext));
                String plaintext = new String(cipher.doFinal(ciphertext));
                System.out.println(plaintext);
                if (plaintext.equals("bye"))
                    break;
            }

            in.close();
            out.close();
            client.close();

            System.out.println("Connection closed");
        } catch (IOException e) {
            System.out.printf("Could not listen on port %d\n", PORT);
            System.exit(-1);
        } catch (NoSuchAlgorithmException
                 | NoSuchPaddingException
                 | IllegalBlockSizeException
                 | BadPaddingException
                 | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
