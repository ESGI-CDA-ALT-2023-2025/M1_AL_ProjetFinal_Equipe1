package fr.esgi.dvf.service.impl;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import fr.esgi.dvf.business.DonneeFonciere;
import fr.esgi.dvf.service.PdfService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class PdfServiceImpl implements PdfService {

    private static final Logger LOGGER = LogManager.getLogger(PdfServiceImpl.class);

    private static final String PDF_DIR = "static/pdf/";

    @Override
    public void generateCompletePdf(String fileName, List<DonneeFonciere> donneeFoncieres) {

        try{
            // Création du writter
            PdfWriter writer = new PdfWriter(new File(PDF_DIR + fileName + ".pdf"));

            // Création objet PDF
            PdfDocument pdf = new PdfDocument(writer);

            // Création du objet document pour écrire
            Document document = new Document(pdf);

            // On ajoute les donnees
            writeDonneesToDocument(document, donneeFoncieres);

            document.close();

            LOGGER.info("Le PDF à été crée : " + fileName);
        } catch (IOException e){
            e.printStackTrace();
        }
    }


    private void writeDonneesToDocument(Document document, List<DonneeFonciere> donneeFoncieres) {
        for (Object obj : donneeFoncieres) {
            if (obj instanceof DonneeFonciere) {
                addDonneeFonciereToPdf(document, (DonneeFonciere) obj);
            } else {
                LOGGER.error("Invalid type found in donneeFoncieres list: " + obj.getClass());
            }
        }
    }


    private void addDonneeFonciereToPdf(Document doc, DonneeFonciere donneeFonciere){
        doc.add(new Paragraph("ID: " + donneeFonciere.getId()));
        doc.add(new Paragraph("Valeur Foncière: " + donneeFonciere.getValeurFonciere()));
        doc.add(new Paragraph("Adresse Numéro: " + donneeFonciere.getAdresseNumero()));
        doc.add(new Paragraph("Adresse Nom Voie: " + donneeFonciere.getAdresseNomVoie()));
        doc.add(new Paragraph("Code Postal: " + donneeFonciere.getCodePostal()));
        doc.add(new Paragraph("Nom Commune: " + donneeFonciere.getNomCommune()));
        doc.add(new Paragraph("Code Département: " + donneeFonciere.getCodeDepartement()));
        doc.add(new Paragraph("Nature Mutation: " + donneeFonciere.getNatureMutation()));
        doc.add(new Paragraph("Type Local: " + donneeFonciere.getTypeLocal()));
        doc.add(new Paragraph("Surface Terrain: " + donneeFonciere.getSurfaceTerrain()));
        doc.add(new Paragraph("Longitude: " + donneeFonciere.getLongitude()));
        doc.add(new Paragraph("Latitude: " + donneeFonciere.getLatitude()));
        doc.add(new Paragraph("\n"));
    }
}
