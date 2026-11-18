import java.io.DataInputStream;
import java.io.IOException;

class FileReceiver {
    private final DataInputStream in;

    public FileReceiver(DataInputStream in) {
        this.in = in;
    }

    public FilePacket receive() throws IOException {
        int nameLength = this.in.readInt();
        byte[] nameBytes = new byte[nameLength];
        this.in.readFully(nameBytes, 0, nameLength);
        String name = new String(nameBytes);

        int fileLength = this.in.readInt();
        byte[] fileContent = new byte[fileLength];
        this.in.readFully(fileContent, 0, fileLength);

        byte[] checksumBytes = new byte[256];
        this.in.readFully(checksumBytes, 0, 256);
        String checksum = new String(checksumBytes);

        return new FilePacket(name, fileContent, checksum);
    }
}
