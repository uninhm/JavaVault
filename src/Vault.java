import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Given a path, this class administrates the possible operations
 * on the files contained on that folder, corresponding to a user's files.
 */
class Vault {
    private final Path path;

    public Vault(Path path) throws IOException {
        this.path = path;

        if (!Files.exists(path))
            Files.createDirectory(path);
    }

    public Path getPath() {
        return this.path;
    }

    public Path getFilePath(String filename) {
        return this.path.resolve(filename);
    }

    public byte[] getFileContent(String filename) throws IOException {
        return Files.readAllBytes(this.getFilePath(filename));
    }

    public void delete(String filename) throws IOException {
        Path filepath = this.getFilePath(filename);

        Files.delete(filepath);
    }

    public List<String> listFiles() throws IOException {
        return Files.list(this.path)
            .map(Path::getFileName)
            .map(Path::toString)
            .sorted()
            .collect(Collectors.toList());
    }

    public void addFile(String filename, byte[] content) throws IOException {
        Path filepath = this.getFilePath(filename);

        Files.createFile(filepath);
        Files.write(filepath, content);
    }
}
