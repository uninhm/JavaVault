import java.io.DataOutputStream;
import java.io.IOException;

class FileSender {
    private DataOutputStream out;

    public FileSender(DataOutputStream out){
        this.out = out;
    }

    public void send(FilePacket file) {
        try {
            byte[] name = file.name().getBytes();
            
            this.out.writeInt(name.length);
            this.out.writeInt(file.content().length);

            this.out.write(name);
            this.out.write(file.content());
            
            this.out.write(file.checksum().getBytes());
        } catch(IOException e) {
            System.out.println("Erreur lors de l'envoi du fichier : " + file.name());
        }
    }
}