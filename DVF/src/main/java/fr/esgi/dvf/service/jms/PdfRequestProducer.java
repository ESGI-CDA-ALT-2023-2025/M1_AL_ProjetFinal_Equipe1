package fr.esgi.dvf.service.jms;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.esgi.dvf.business.DonneeFonciere;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.jms.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class PdfRequestProducer {

  @Autowired
  private JmsTemplate jmsTemplate;

  @Autowired
  private Queue pdfDownloadQueue;

  public void sendPdfRequest(List<DonneeFonciere> donnees, String fileName) {
    Map<String, Object> message = new HashMap<>();
    message.put("donnees", donnees);
    message.put("fileName", fileName);

    // On change la map vers un JSON string
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      // Convertion de la map dans un byte[]
      byte[] byteMessage = objectMapper.writeValueAsBytes(message);

      // Envoie du message en format byte
      jmsTemplate.convertAndSend(pdfDownloadQueue, byteMessage);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
