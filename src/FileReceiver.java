import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

class FileReceiver {
    private final DataInputStream in;

    public FileReceiver(DataInputStream in) {
        this.in = in;
    }

    public void receive() throws IOException {
        String path = ""; //TODO: Retrieve the path where to save the file
        try {
            int nameLength = this.in.readInt();
            int fileLength = this.in.readInt();

            byte[] nameBytes = new byte[nameLength];
            this.in.readFully(nameBytes, 0, nameLength);
            String fileName = new String(nameBytes);

            FileOutputStream outputStream = new FileOutputStream(path + fileName, true);
            for (int i=0;i<fileLength; i+= 1024){
                byte[] filePacket = new byte[1024];
                this.in.readFully(filePacket, i, 1024);
                outputStream.write(filePacket);
            }
            byte[] filePacket = new byte[fileLength % 1024];
            this.in.readFully(filePacket, 0, fileLength % 1024);
            outputStream.write(filePacket);
            outputStream.close();

            // TODO: Implement checksum verification
            /*byte[] checksumBytes = new byte[256];
            this.in.readFully(checksumBytes, 0, 256);
            String checksum = new String(checksumBytes);
            return new FilePacket(fileName, fileContent, checksum);*/

        } catch (IOException e) {
            System.out.println("Error receiving file packet : " + e); 
        }
    }
}

