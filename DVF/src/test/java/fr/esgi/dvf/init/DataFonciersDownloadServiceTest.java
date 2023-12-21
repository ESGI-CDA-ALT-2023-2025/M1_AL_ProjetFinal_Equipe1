package fr.esgi.dvf.init;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
class DataFonciersDownloadServiceTest {

    private DataFonciersDownloadService dataFonciersDownloadService;
    
    @BeforeAll
    void setUp() {
        // Initialize your service before each test
        this.dataFonciersDownloadService = new DataFonciersDownloadService();
    }
    
    @Test
    void testThenDownloadAndSaveFile_whenSuccess() {
        String destinationPath = "src/main/resources/full.csv.gz";
        
        this.dataFonciersDownloadService.downloadAndSaveFile();
        
        assertTrue(Files.exists(Paths.get(destinationPath)));
    }

}
