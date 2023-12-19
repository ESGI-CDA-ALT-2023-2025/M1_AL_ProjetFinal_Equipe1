package fr.esgi.dvf.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("")
@Validated
public class DonneeFonciereRestController {

    @GetMapping("/calculer")
    public String calculer(
            @RequestParam(name="longitude", defaultValue="0") Double longitude,
            @RequestParam(name="latitude", defaultValue="0") Double latitude,
            @RequestParam(name="rayon", defaultValue="0") Double rayon
    ) {
        return "longitude: " + longitude + ", latitude: " + latitude + ", rayon: " + rayon + " m";
    }

}
