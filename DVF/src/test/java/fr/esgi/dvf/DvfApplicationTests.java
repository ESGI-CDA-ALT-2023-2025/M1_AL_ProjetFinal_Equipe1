package fr.esgi.dvf;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.File;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import fr.esgi.dvf.service.CsvReaderService;

@SpringBootTest
class DvfApplicationTests {

    @Mock
    private CsvReaderService csvReaderService;

    @Test
    void contextLoads() {
        // Specify the file names
        String csvGzFileName = "full.csv.gz";

        // Build the paths for the resources directory
        String resourcesPath = "src/main/resources/";
        String csvGzFilePath = resourcesPath + csvGzFileName;

        // Check if the files exist
        File csvGzFile = new File(csvGzFilePath);

        assertTrue(csvGzFile.exists(), csvGzFileName + " should exist in " + resourcesPath);
    }

}
