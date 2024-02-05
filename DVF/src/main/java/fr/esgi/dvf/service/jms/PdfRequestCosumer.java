package fr.esgi.dvf.service.jms;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.esgi.dvf.business.DonneeFonciere;
import fr.esgi.dvf.service.PdfService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

@Component
@Getter
@Setter
public class PdfRequestCosumer {

  @Autowired
  private PdfService pdfService;

  private CountDownLatch latch = new CountDownLatch(1);

  @JmsListener(destination = "pdf-download-queue")
  public void receivePdfRequest(Message<byte[]> message, @Headers Map<String, Object> headers) {
    byte[] payload = message.getPayload();

    // Convertion de l'objet byte en Map
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      Map<String, Object> pdfRequestMap = objectMapper.readValue(payload, new TypeReference<Map<String, Object>>() {});

      // Map contenant le fileName et les données
      List<DonneeFonciere> donnees = objectMapper.convertValue(pdfRequestMap.get("donnees"), new TypeReference<List<DonneeFonciere>>() {});
      String fileName = (String) pdfRequestMap.get("fileName");

      // Génération du PDF
      pdfService.generateCompletePdf(fileName, donnees);

      System.out.println("Received PDF request for fileName: " + fileName);

      latch.countDown();
    } catch (IOException e) {
      throw new MessageConversionException("Error processing PDF request", e);
    }
  }
}
