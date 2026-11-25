import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

class FileSender {
    private final DataOutputStream out;

    public FileSender(DataOutputStream out){
        this.out = out;
    }

    public void send(File file) throws IOException {
        try {
            // this.out.writeByte(1); // Sends the type of operation (1 = send a new file)
            byte[] name = file.getName().getBytes();
            int fileSize = (int) file.length();
            String filePath = file.getAbsolutePath();

            this.out.writeInt(name.length);
            this.out.writeInt(fileSize);

            this.out.write(name);

            // Sends the file by packets of 1KB
            FileInputStream inputStream = new FileInputStream(filePath);
            for (int i=0 ; i<fileSize; i+=1024){
                byte[] packet = new byte[1024];
                for (int j=0 ; j<Math.min(1024, fileSize - i) ; j++){
                    packet[j] = (byte) inputStream.read();
                }
                this.out.write(packet);
            }
            // TODO : Implement checksum verification
            // this.out.write(file.checksum().getBytes());
        } catch(IOException e){
            System.out.println("Error receiving file packet : "+ e);
        }
    }
}