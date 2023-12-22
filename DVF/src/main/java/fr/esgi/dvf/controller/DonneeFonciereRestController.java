package fr.esgi.dvf.controller;

import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import fr.esgi.dvf.business.DonneeFonciere;
import fr.esgi.dvf.service.DonneeFonciereService;
import fr.esgi.dvf.service.IPdfService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("")
@Validated
public class DonneeFonciereRestController {

    @Autowired
    private DonneeFonciereService donneeFonciereService;
    
    @Autowired
    private IPdfService pdfService;

    @Operation(summary = "Route à laquelle il faut renseigner une longitude, "
            + "une latitude et un rayon en mètre pour obtenir en retour un PDF "
            + "listant toutes les transactions qui se sont effectuées dans la zone demandée.")
    @GetMapping("/pdf")
    public ResponseEntity<Resource> pdf(
            @Parameter(description = "Longitude",       example = "0", schema = @Schema(type = "number")) @RequestParam(name = "longitude", defaultValue = "0") Double longitude,
            @Parameter(description = "Latitude",        example = "0", schema = @Schema(type = "number")) @RequestParam(name = "latitude", 	defaultValue = "0") Double latitude,
            @Parameter(description = "Rayon en mètre",  example = "0", schema = @Schema(type = "number")) @RequestParam(name = "rayon", 	defaultValue = "0") Double rayon
    ) {
        List<DonneeFonciere> donnees = donneeFonciereService.getDonneesFonciereByRadius(latitude, longitude, rayon);

        Document doc = pdfService.pdfDocumentProvider();

        pdfService.writeDataToPDF(donnees);
        doc.add(new Paragraph("Pdf a été generé : " + LocalDateTime.now().toString()));

        Resource resource;
        try {
            resource = pdfService.resourceProducer();
        } catch (MalformedURLException e) {
         // Gérer le cas où le chemin n'existe pas
            return ResponseEntity.notFound().build();
        }

        if (!resource.exists()) {
            // Gérer le cas où le fichier n'existe pas
            return ResponseEntity.noContent().build();
        }

        // Définir les en-têtes de réponse
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=exemple.pdf");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);

        return ResponseEntity.ok()
                             .headers(headers)
                             .body(resource);
    }
    
//    @Operation(summary = "Route à laquelle il faut renseigner une longitude, une latitude et un rayon en mètre pour obtenir en retour un PDF listant toutes les transactionsqui se sont effectuées dans la zone demandée.")
//    @GetMapping("/calculer")
//    public List<DonneeFonciere> calculer(
//            @Parameter(description = "Longitude", example = "0", schema = @Schema(type = "number")) @RequestParam(name = "longitude", defaultValue = "0") Double longitude,
//            @Parameter(description = "Latitude", example = "0", schema = @Schema(type = "number")) @RequestParam(name = "latitude", defaultValue = "0") Double latitude,
//            @Parameter(description = "Rayon en mètre", example = "0", schema = @Schema(type = "number")) @RequestParam(name = "rayon", defaultValue = "0") Double rayon
//    ) {
//        return donneeFonciereService.getDonneesFonciereByRadius(latitude, longitude, rayon);
//    }
}
