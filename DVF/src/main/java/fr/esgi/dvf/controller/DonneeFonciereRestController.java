package fr.esgi.dvf.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("")
@Validated
public class DonneeFonciereRestController {

    @Operation(summary = "Route à laquelle il faut renseigner une longitude, une latitude et un rayon en mètre pour obtenir en retour un PDF listant toutes les transactionsqui se sont effectuées dans la zone demandée.")
    @GetMapping("/calculer")
    public String calculer(
            @Parameter(description = "Longitude", example = "0", schema = @Schema(type = "number")) @RequestParam(name = "longitude", defaultValue = "0") Double longitude,
            @Parameter(description = "Latitude", example = "0", schema = @Schema(type = "number")) @RequestParam(name = "latitude", defaultValue = "0") Double latitude,
            @Parameter(description = "Rayon en mètre", example = "0", schema = @Schema(type = "number")) @RequestParam(name = "rayon", defaultValue = "0") Double rayon
    ) {
        return "longitude: " + longitude + ", latitude: " + latitude + ", rayon: " + rayon + " m";
    }

}
