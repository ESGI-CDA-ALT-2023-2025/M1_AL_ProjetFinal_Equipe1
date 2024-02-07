package fr.esgi.dvf.service.jms;

import java.util.List;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import fr.esgi.dvf.business.DonneeFonciere;
import fr.esgi.dvf.controller.exception.TechniqueException;
import fr.esgi.dvf.service.PdfService;

@Component
public class PdfRequestConsumer {

    private static final Logger LOGGER = LogManager.getLogger(PdfRequestConsumer.class);

    @Autowired
    private PdfService pdfService;

    @JmsListener(destination = "pdf-generator-queue",
                 concurrency = "1-1")
    @SendTo("pdf-response-queue")
    public String receivePdfRequest(List<DonneeFonciere> donnees) throws TechniqueException {
        String fileName = "pdf_" + UUID.randomUUID().toString();

        LOGGER.atInfo()
              .log("DONNEES {} dans le fichier pour serveur {}",
                   donnees.size(),
                   fileName);

        this.pdfService.generateCompletePdf(fileName, donnees);

        return fileName;
    }

}
