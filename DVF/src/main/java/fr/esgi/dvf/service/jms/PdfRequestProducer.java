package fr.esgi.dvf.service.jms;

import fr.esgi.dvf.business.PdfGenerationRequest;
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

  public void sendPdfRequest(PdfGenerationRequest request) {
    jmsTemplate.convertAndSend(pdfDownloadQueue, request);
  }
}
