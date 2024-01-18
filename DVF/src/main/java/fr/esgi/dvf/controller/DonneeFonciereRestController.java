package fr.esgi.dvf.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
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
import fr.esgi.dvf.service.DonneeFonciereService;
import fr.esgi.dvf.service.IPdfService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("")
@Validated
@CrossOrigin
public class DonneeFonciereRestController {

    @Autowired
    private DonneeFonciereService<DonneeFonciere> donneeFonciereService;

    @Autowired
    private IPdfService pdfService;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur s'est produite");
    }

    @Operation(summary = "Route à laquelle il faut renseigner une longitude, "
            + "une latitude et un rayon en mètre pour obtenir en retour un PDF "
            + "listant toutes les transactions qui se sont effectuées dans la zone demandée.")
    @GetMapping("/pdf")
    public ResponseEntity<Resource> pdf(
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
        return this.donneeFonciereService.getResponseWithResource(latitude, longitude, rayon);
    }
}
