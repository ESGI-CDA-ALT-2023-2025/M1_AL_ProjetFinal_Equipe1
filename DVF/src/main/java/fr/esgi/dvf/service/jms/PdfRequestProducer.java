package fr.esgi.dvf.service.jms;

import fr.esgi.dvf.business.PdfGenerationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class PdfRequestProducer {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendPdfRequest(PdfGenerationRequest request){
        jmsTemplate.convertAndSend("pdf-download-queue", request);
    }
}
