package fr.esgi.dvf.service.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import fr.esgi.dvf.business.DonneeFonciere;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

@TestInstance(Lifecycle.PER_CLASS)
class PdfServiceImplTest {

  // Directory path where PDF files are stored
  private static final String PDF_DIRECTORY_PATH = "static/pdf/";

  @Mock
  private PdfWriter pdfWriter;

  @Mock
  private PdfDocument pdfDocument;

  @InjectMocks
  private PdfServiceImpl pdfService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @AfterAll
  static void cleanUpPdfFiles() {

    // Delete all PDF files in the specified directory
    File directory = new File(PDF_DIRECTORY_PATH);
    File[] pdfFiles = directory.listFiles((dir, name) -> name.toLowerCase()
                                                             .endsWith(".pdf"));

    if (pdfFiles != null) {
      for (File pdfFile : pdfFiles) {
        assertTrue(pdfFile.delete(), "Failed to delete PDF file: " + pdfFile.getName());
      }
    }
  }


  @Test
  void testGeneratePdf() throws IOException {
    // Arrange
    List<DonneeFonciere> donnees = new ArrayList<>();
    DonneeFonciere f1 = DonneeFonciere.builder()
                                      .adresseNomVoie("Test voie 1")
                                      .build();
    DonneeFonciere f2 = DonneeFonciere.builder()
                                      .adresseNomVoie("Test voie 2")
                                      .build();
    donnees.add(f1);
    donnees.add(f2);

    // Act
    pdfService.generateCompletePdf("testPdf", donnees);
    Resource resource = new UrlResource(Paths.get(PDF_DIRECTORY_PATH + "testPdf.pdf").toUri());

    // Assert
    Assertions.assertNotNull(resource);
    try (var stream = Files.list(Paths.get(PDF_DIRECTORY_PATH))){
      Assertions.assertTrue(stream.findFirst().isPresent());
    }
  }

}
