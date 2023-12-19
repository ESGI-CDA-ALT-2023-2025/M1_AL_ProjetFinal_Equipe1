package fr.esgi.dvf.init;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@TestInstance(Lifecycle.PER_CLASS)
class DataFonciersDownloadServiceTest {

  @InjectMocks
  private DataFonciersDownloadService dataFonciersDownloadService;

  @Mock
  private FileUtils fileUtilsMock;

  @BeforeAll
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testDownloadAndSaveFile_Success() throws IOException {
    String fileUrl = "https://files.data.gouv.fr/geo-dvf/latest/csv/2023/full.csv.gz";
    String destinationPath = "src/main/resources/full.csv.gz";

    // Mocking FileUtils.copyURLToFile to avoid actual file download
    doNothing().when(fileUtilsMock).copyURLToFile(any(URL.class), any(File.class));

    // Calling the actual method to test
    dataFonciersDownloadService.downloadAndSaveFile();

    // Verifying that FileUtils.copyURLToFile was called with the correct arguments
    verify(fileUtilsMock, times(1)).copyURLToFile(new URL(fileUrl),
                                                  new File(destinationPath));
  }

  @Test
  public void testDownloadAndSaveFile_Failure() throws IOException {
    String fileUrl = "https://files.data.gouv.fr/geo-dvf/latest/csv/2023/full.csv.gz";
    String destinationPath = "src/main/resources/full.csv.gz";

    // Mocking FileUtils.copyURLToFile to simulate IOException
    doThrow(new IOException("Simulated IOException")).when(fileUtilsMock)
                                                     .copyURLToFile(any(URL.class),
                                                                    any(File.class));

    // Calling the actual method to test
    dataFonciersDownloadService.downloadAndSaveFile();

    // Verifying that FileUtils.copyURLToFile was called with the correct arguments
    verify(fileUtilsMock, times(1)).copyURLToFile(new URL(fileUrl),
                                                  new File(destinationPath));

    // You can add additional assertions based on the expected behavior during a failure
  }
}
