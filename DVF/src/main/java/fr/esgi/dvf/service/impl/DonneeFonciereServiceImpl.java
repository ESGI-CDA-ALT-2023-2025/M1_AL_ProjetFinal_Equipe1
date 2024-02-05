package fr.esgi.dvf.service.impl;

import fr.esgi.dvf.business.DonneeFonciere;
import fr.esgi.dvf.repository.DonneeFonciereRepository;
import fr.esgi.dvf.service.DonneeFonciereService;
import fr.esgi.dvf.service.IPdfService;
import fr.esgi.dvf.service.jms.PdfRequestCosumer;
import fr.esgi.dvf.service.jms.PdfRequestProducer;
import java.net.MalformedURLException;
import java.util.List;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class DonneeFonciereServiceImpl implements
                                       DonneeFonciereService<DonneeFonciere> {

  private static final Logger LOGGER = LogManager.getLogger(DonneeFonciereServiceImpl.class);
  private static final String FILE_PREFIX_FOR_CONTENT_DISPOSITION =
      "attachment; filename=donneeFonciere_";

  @Autowired
  private IPdfService pdfService;

  @Autowired
  private PdfRequestProducer pdfRequestProducer;

  @Autowired
  private PdfRequestCosumer pdfRequestCosumer;

  private DonneeFonciereRepository repository;
  private static final double EARTH_RADIUS = 6371000.0; // Earth radius in meters

  public DonneeFonciereServiceImpl(DonneeFonciereRepository repository) {
    this.repository = repository;
  }

  @Override
  public List<DonneeFonciere> getDonneesFonciereByRadius(final double latitude,
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
  public ResponseEntity<Resource> getResponseWithResource(final double latitude,
                                                          final double longitude,
                                                          final double radius) {

    List<DonneeFonciere> donnees = this.getDonneesFonciereByRadius(latitude,
                                                                   longitude,
                                                                   radius);

    Resource resource = null;

    if (donnees.isEmpty()) {
      LOGGER.atInfo()
            .log("Aucune données disponiple pour latitude : {}, longitude : {}, radius : {}",
                 latitude,
                 longitude,
                 radius);
      // le cas où n'as pas de données
      return ResponseEntity.noContent().build();
    }

    pdfRequestProducer.sendPdfRequest(donnees);

    pdfRequestCosumer.getPdfGenerationFuture().join();

    try {
      String fileName = pdfRequestCosumer.getFileName();
      resource = pdfService.resourceProducer(fileName);
    } catch (MalformedURLException e) {
      LOGGER.atError().log("Fichier {} n'est pas introuvable !",
                           e.getLocalizedMessage());
      // Gérer le cas où le chemin n'existe pas
      return ResponseEntity.notFound().build();
    }

    if (!resource.exists()) {
      LOGGER.atInfo().log("Fichier {} n'existe pas !",
                          resource.getFilename());
      // Gérer le cas où le fichier n'existe pas
      return ResponseEntity.noContent().build();
    }

    // Définir les en-têtes de réponse
    final String CONTENT_DISPOSITION_VALUES =
        FILE_PREFIX_FOR_CONTENT_DISPOSITION + UUID.randomUUID().toString() + ".pdf";
    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_DISPOSITION, CONTENT_DISPOSITION_VALUES);
    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);

    return ResponseEntity.ok()
                         .headers(headers)
                         .body(resource);
  }
}
