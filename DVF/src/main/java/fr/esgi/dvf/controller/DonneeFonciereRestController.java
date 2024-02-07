package fr.esgi.dvf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import fr.esgi.dvf.business.DonneeFonciere;
import fr.esgi.dvf.controller.exception.TechniqueException;
import fr.esgi.dvf.service.DonneeFonciereService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("")
@Validated
@CrossOrigin
@Log4j2
public class DonneeFonciereRestController {

    @Autowired
    private DonneeFonciereService<DonneeFonciere> donneeFonciereService;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.atError().log("INTERNAL_SERVER_ERROR : ERREUR {}",
                          e.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Une erreur s'est produite, veuillez contacter le support utilisateur");
    }

    @ExceptionHandler(TechniqueException.class)
    public ResponseEntity<String> handleTechniqueException(TechniqueException e) {
        log.atError().log("SERVICE_UNAVAILABLE : ERREUR {}",
                          e.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                             .body("Une erreur s'est produite :"
                                     + e.getLocalizedMessage());
    }

    @Operation(summary = "Route à laquelle il faut renseigner une longitude, "
            + "une latitude et un rayon en mètre pour obtenir en retour un PDF "
            + "listant toutes les transactions qui se sont effectuées dans la zone demandée.")
    @GetMapping("/pdf")
    public ResponseEntity<byte[]> pdf(
                                      @Parameter(description = "Longitude",
                                                 example = "0",
                                                 schema = @Schema(type = "number"))
                                      @RequestParam(name = "longitude",
                                                    defaultValue = "0")
                                      Double longitude,
                                      @Parameter(description = "Latitude",
                                                 example = "0",
                                                 schema = @Schema(type = "number"))
                                      @RequestParam(name = "latitude",
                                                    defaultValue = "0")
                                      Double latitude,
                                      @Parameter(description = "Rayon en mètre",
                                                 example = "0",
                                                 schema = @Schema(type = "number"))
                                      @RequestParam(name = "rayon",
                                                    defaultValue = "0")
                                      Double rayon) {
        // Envoie de message vers la JMS pour la génération du PDF

        return donneeFonciereService.getResponseWithResource(latitude,
                                                             longitude,
                                                             rayon);
    }
}
