package fr.esgi.dvf.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import fr.esgi.dvf.business.DonneeFonciere;
import fr.esgi.dvf.repository.DonneeFonciereRepository;
import fr.esgi.dvf.service.DonneeFonciereService;
import fr.esgi.dvf.service.PdfService;
import fr.esgi.dvf.service.jms.PdfRequestProducer;

@Service
public class DonneeFonciereServiceImpl implements
                                       DonneeFonciereService<DonneeFonciere> {

    private static final Logger LOGGER = LogManager.getLogger(DonneeFonciereServiceImpl.class);
    private static final String FILE_PREFIX_FOR_CONTENT_DISPOSITION =
            "attachment; filename=donneeFonciere_";

    @Autowired // NOSONAR
    private PdfService pdfService;

    @Autowired // NOSONAR
    private PdfRequestProducer pdfRequestProducer;

    @Autowired // NOSONAR
    private JmsTemplate jmsTemplate;

    private DonneeFonciereRepository repository;
    private static final double EARTH_RADIUS = 6371000.0; // Earth radius in meters
    private static final String PDF_RESPONSE_QUEUE = "pdf-response-queue";

    public DonneeFonciereServiceImpl(DonneeFonciereRepository repository) {
        this.repository = repository;
    }

    protected List<DonneeFonciere> getDonneesFonciereByRadius(final double latitude,
                                                              final double longitude,
                                                              final double radius) {
        final double radiusEnDegres = radius / EARTH_RADIUS;

        final double minLatitude = latitude - Math.toDegrees(radiusEnDegres);
        final double maxLatitude = latitude + Math.toDegrees(radiusEnDegres);
        final double formuleLongitude =
                Math.toDegrees(radiusEnDegres) / Math.cos(Math.toRadians(latitude));
        final double minLongitude = longitude - formuleLongitude;
        final double maxLongitude = longitude + formuleLongitude;

        return repository.findByLatitudeBetweenAndLongitudeBetween(minLatitude, maxLatitude,
                                                                   minLongitude,
                                                                   maxLongitude);
    }

    @Override
    public ResponseEntity<byte[]> getResponseWithResource(final double latitude,
                                                          final double longitude,
                                                          final double radius) {

        List<DonneeFonciere> donnees = this.getDonneesFonciereByRadius(latitude,
                                                                       longitude,
                                                                       radius);

        if (donnees.isEmpty()) {
            LOGGER.atInfo()
                  .log("NO CONTENT : Aucune données disponiple pour latitude : {}, longitude : {}, radius : {}",
                       latitude,
                       longitude,
                       radius);
            return ResponseEntity.noContent().build();
        }

        pdfRequestProducer.sendPdfRequest(donnees);
        String fileName = (String) this.jmsTemplate.receiveAndConvert(PDF_RESPONSE_QUEUE);

        byte[] fileEnByte = null;

        try {
            fileEnByte = this.pdfService.resourceProducer(fileName);
        } catch (IOException e) {
            LOGGER.atInfo().log("SERVICE_UNAVAILABLE : Le fichier {} n'existe pas !",
                                fileName);
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }

        final String CONTENT_DISPOSITION_VALUES =
                FILE_PREFIX_FOR_CONTENT_DISPOSITION + UUID.randomUUID().toString() + ".pdf";
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, CONTENT_DISPOSITION_VALUES);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);

        return ResponseEntity.ok()
                             .headers(headers)
                             .body(fileEnByte);
    }
}
