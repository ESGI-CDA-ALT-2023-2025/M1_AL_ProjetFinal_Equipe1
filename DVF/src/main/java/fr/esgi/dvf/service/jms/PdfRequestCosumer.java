package fr.esgi.dvf.service.jms;

import com.itextpdf.layout.element.Paragraph;
import fr.esgi.dvf.business.DocumentWithFileName;
import fr.esgi.dvf.business.DonneeFonciere;
import fr.esgi.dvf.service.IPdfService;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class PdfRequestCosumer {

  private static final Logger LOGGER = LogManager.getLogger(PdfRequestCosumer.class);

  private DocumentWithFileName doc;

  private CompletableFuture<Void> pdfGenerationFuture = new CompletableFuture<>();

  @Autowired
  private IPdfService pdfService;

  @JmsListener(destination = "pdf-download-queue",
               concurrency = "1")
  public void receivePdfRequest(List<DonneeFonciere> donnees) {
    LOGGER.info("DONNEES = " + donnees.size());

    try {
      doc = this.pdfService.pdfDocumentProvider();

      this.pdfService.writeDataToPDF(doc.getDocument(), donnees);
      doc.getDocument()
         .add(new Paragraph("Pdf a été generé : " + LocalDateTime.now().toString()));
    } finally {
      if (doc != null) {
        doc.getDocument().close();
      }

      // Complétez le futur après que le traitement asynchrone est terminé
      pdfGenerationFuture.complete(null);
    }
  }

  public String getFileName() throws MalformedURLException {
    String fileName = doc.getFileName();
    LOGGER.info("NOM de fichier dans Queue " + fileName);
    return fileName;
  }

  public CompletableFuture<Void> getPdfGenerationFuture() {
    return pdfGenerationFuture;
  }
}
