package fr.esgi.dvf.service.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Paragraph;
import fr.esgi.dvf.business.DocumentWithFileName;
import fr.esgi.dvf.business.DonneeFonciere;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;

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
  void testWriteDataToPDF() throws IOException {
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

    DocumentWithFileName doc = pdfService.pdfDocumentProvider();

    // Act
    pdfService.writeDataToPDF(doc.getDocument(), donnees);
    doc.getDocument().add(new Paragraph(LocalDateTime.now().toString()));
    Resource res = pdfService.resourceProducer(doc.getFileName());

    // Assert
    assertNotNull(res);
    // List all files in the directory
    try (var stream = Files.list(Paths.get(PDF_DIRECTORY_PATH))) {
      // Check if the stream is not empty (i.e., at least one file exists)
      assertTrue(stream.findFirst().isPresent());
    }
  }
}
