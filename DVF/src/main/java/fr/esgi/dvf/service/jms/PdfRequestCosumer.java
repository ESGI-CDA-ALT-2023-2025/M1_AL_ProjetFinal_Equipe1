package fr.esgi.dvf.service.jms;

import fr.esgi.dvf.business.PdfGenerationRequest;
import fr.esgi.dvf.service.DonneeFonciereService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.concurrent.CompletableFuture;

@Component
public class PdfRequestCosumer {

    private ResponseEntity response;

    private CompletableFuture<Void> pdfGenerationFuture = new CompletableFuture<>();
    @Autowired
    private DonneeFonciereService donneeFonciereService;

    @JmsListener(destination = "pdf-download-queue")
    public void receivePdfRequest(PdfGenerationRequest request){
        response = donneeFonciereService.getResponseWithResource(request.getLatitude(), request.getLongitude(), request.getRayon());
        pdfGenerationFuture.complete(null);
    }

    public ResponseEntity getResponse() {
        return response;
    }

    public CompletableFuture<Void> getPdfGenerationFuture() {
        return pdfGenerationFuture;
    }
}
