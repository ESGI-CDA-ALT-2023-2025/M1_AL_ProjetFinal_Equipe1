package fr.esgi.dvf.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import fr.esgi.dvf.business.DonneeFonciere;
import fr.esgi.dvf.controller.exception.TechniqueException;
import fr.esgi.dvf.service.PdfService;

@Service
public class PdfServiceImpl implements
                            PdfService {

    private static final String PDF_DIR = "static/pdf/";
    private static final Logger LOGGER = LogManager.getLogger(PdfServiceImpl.class);

    @Override
    public void generateCompletePdf(String fileName, List<DonneeFonciere> donneeFoncieres) throws TechniqueException {

        try (PdfWriter writer = new PdfWriter(new File(PDF_DIR
                + fileName
                + ".pdf"))) {

            // Création objet PDF
            PdfDocument pdf = new PdfDocument(writer);

            // Création du objet document pour écrire
            Document document = new Document(pdf);

            // On ajoute les donnees
            writeDonneesToDocument(document, donneeFoncieres);
            document.add(new Paragraph("Pdf a été generé : " + LocalDateTime.now().toString()));
            document.close();

            LOGGER.atInfo().log("Le PDF à été crée {} ", fileName);
        } catch (IOException e) {
            LOGGER.atError().log("IOException : L'erreur pendant generation pdf {} ", e.getLocalizedMessage());
            throw new TechniqueException("Erreur techique pendant génération de pdf!");
        }
    }

    @Override
    public byte[] resourceProducer(String fileName) throws IOException {
        Path path = Paths.get(PDF_DIR + fileName + ".pdf");

        byte[] fileContent = Files.readAllBytes(path);

        LOGGER.atInfo()
              .log("Production fichier en byte {} -> OK", path.getFileName());

        return fileContent;
    }

    private void writeDonneesToDocument(Document document, List<DonneeFonciere> donneeFoncieres) {
        for (DonneeFonciere data : donneeFoncieres) {
            if (data instanceof DonneeFonciere) {
                this.addDonneeFonciereToPdf(document, data);
            } else {
                LOGGER.atError()
                      .log("Invalid type found in donneeFoncieres list {} in the page {}",
                           data.getClass(),
                           document.getPdfDocument()
                                   .getPage(0));
            }
        }
    }

    private void addDonneeFonciereToPdf(Document document, DonneeFonciere donnee) {
        document.add(new Paragraph("ID: " + donnee.getId()));
        document.add(new Paragraph("Valeur Foncière: " + donnee.getValeurFonciere()));
        document.add(new Paragraph("Adresse Numéro: " + donnee.getAdresseNumero()));
        document.add(new Paragraph("Adresse Nom Voie: " + donnee.getAdresseNomVoie()));
        document.add(new Paragraph("Code Postal: " + donnee.getCodePostal()));
        document.add(new Paragraph("Nom Commune: " + donnee.getNomCommune()));
        document.add(new Paragraph("Code Département: " + donnee.getCodeDepartement()));
        document.add(new Paragraph("Nature Mutation: " + donnee.getNatureMutation()));
        document.add(new Paragraph("Type Local: " + donnee.getTypeLocal()));
        document.add(new Paragraph("Surface Terrain: " + donnee.getSurfaceTerrain()));
        document.add(new Paragraph("Longitude: " + donnee.getLongitude()));
        document.add(new Paragraph("Latitude: " + donnee.getLatitude()));
        document.add(new Paragraph("\n"));
    }

}
