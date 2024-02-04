package fr.esgi.dvf.service.jms;

import fr.esgi.dvf.business.DonneeFonciere;
import fr.esgi.dvf.business.PdfGenerationRequest;
import fr.esgi.dvf.service.DonneeFonciereService;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class PdfRequestCosumer {

  private ResponseEntity<Resource> response;

  private CompletableFuture<Void> pdfGenerationFuture = new CompletableFuture<>();
  
  @Autowired
  private DonneeFonciereService<DonneeFonciere> donneeFonciereService;

  @JmsListener(destination = "pdf-download-queue",
               concurrency = "1")
  public void receivePdfRequest(PdfGenerationRequest request) {
    this.response = donneeFonciereService.getResponseWithResource(request.getLatitude(),
                                                             request.getLongitude(),
                                                             request.getRayon());
    pdfGenerationFuture.complete(null);
  }

  public ResponseEntity<Resource> getResponse() {
    return response;
  }

  public CompletableFuture<Void> getPdfGenerationFuture() {
    return pdfGenerationFuture;
  }
}
