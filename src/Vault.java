import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Given a path, this class administrates the possible operations
 * on the files contained on that folder, corresponding to a user's files.
 */
class Vault {
    Path path;

    public Vault(Path path) {
        this.path = path;
    }

    public Path getFilePath(String filename) {
        return this.path.resolve(filename);
    }

    public byte[] getFileContent(String filename) throws IOException {
        return Files.readAllBytes(this.getFilePath(filename));
    }

    public void delete(String filename) throws IOException, FileNotFoundException {
        Path filepath = this.getFilePath(filename);

        if (!Files.exists(filepath))
            throw new FileNotFoundException("File '" + filename + "' not found");

        Files.delete(filepath);
    }

    public List<String> listFiles() throws IOException {
        return Files.list(this.path)
            .map(Path::getFileName)
            .map(Path::toString)
            .collect(Collectors.toList());
    }

    public void addFile(FilePacket filePacket) throws IOException, FileAlreadyExistsException {
        Path filepath = this.getFilePath(filePacket.name());

        if (Files.exists(filepath))
            throw new FileAlreadyExistsException("File '" + filePacket.name() + "' already exists");

        Files.write(filepath, filePacket.content());
    }
}
