package fr.esgi.dvf.service.impl;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import fr.esgi.dvf.business.DonneeFonciere;

public class PdfServiceImplTest {

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

    @Test
    public void testWriteDataToPDF() throws IOException {
        // Arrange
        String directoryPath = "static/pdf/";
        List<DonneeFonciere> donnees = new ArrayList<>();
        DonneeFonciere f1 = DonneeFonciere.builder()
                                          .adresseNomVoie("Test voie 1")
                                          .build();
        DonneeFonciere f2 = DonneeFonciere.builder()
                                          .adresseNomVoie("Test voie 2")
                                          .build();
        donnees.add(f1);
        donnees.add(f2);

        Document doc = pdfService.pdfDocumentProvider();

        // Act
        pdfService.writeDataToPDF(donnees);
        pdfService.resourceProducer();
        
        // Assert
        // List all files in the directory
        try (var stream = Files.list(Paths.get(directoryPath))) {
            // Check if the stream is not empty (i.e., at least one file exists)
            assertTrue(stream.findFirst().isPresent());
        }
    }
}
