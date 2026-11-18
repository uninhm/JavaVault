import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

class VaultTest {
    Vault vault;

    @BeforeEach
    void setUp() throws IOException {
        Random rnd = new Random();

        vault = new Vault(
                Path.of(System.getProperty("java.io.tmpdir"))
                        .resolve("vault_test_" + rnd.nextInt())
        );
    }

    @Test
    void testOperations() throws IOException {
        vault.addFile("file1", "Hello1".getBytes());
        vault.addFile("file2", "Hello2".getBytes());

        // Test that the files were created correctly and the listing works
        assertEquals(
                List.of("file1", "file2"),
                vault.listFiles()
        );

        // Test the contents
        assertEquals(
                "Hello1",
                new String(vault.getFileContent("file1"))
        );
        assertEquals(
                "Hello2",
                new String(vault.getFileContent("file2"))
        );

        vault.delete("file1");

        // Test that the delete works
        assertEquals(
                List.of("file2"),
                vault.listFiles()
        );

        // A new vault on the same directory should find the same files
        Vault vault2 = new Vault(vault.getPath());

        assertEquals(
                List.of("file2"),
                vault2.listFiles()
        );
    }

    @Test
    void testExceptions() throws IOException {
        // We can't delete a file that doesn't exist
        assertThrows(NoSuchFileException.class,
                () -> vault.delete("file1"));

        // We can't add the same file twice
        vault.addFile("file1", "Hello".getBytes());
        assertThrows(FileAlreadyExistsException.class,
                () -> vault.addFile("file1", "Bye".getBytes()));

        vault.delete("file1");

        // We can't delete a file twice
        assertThrows(NoSuchFileException.class,
                () -> vault.delete("file1"));

        // Test that we can't read a deleted file
        assertThrows(NoSuchFileException.class,
                () -> vault.getFileContent("file1"));
    }
}