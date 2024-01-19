package fr.esgi.dvf.service.impl;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import fr.esgi.dvf.business.DonneeFonciere;
import fr.esgi.dvf.service.IPdfService;

@Service
public class PdfServiceImpl implements
                            IPdfService {

    private static final String PDF_DIR = "static/pdf/";
    private static final Logger LOGGER = LogManager.getLogger(PdfServiceImpl.class);

    private Document document;
    private String fileName;

    @Override
    public void writeDataToPDF(List<DonneeFonciere> donnees) {
        for (DonneeFonciere donnee : donnees) {
            this.addDonneeFonciereToDocument(donnee);
        }
    }

    @Override
    public Document pdfDocumentProvider() {
        this.fileName = "pdf_" + UUID.randomUUID();

        PdfWriter writer = null;
        try {
            writer = new PdfWriter(PDF_DIR + fileName + ".pdf");
        } catch (FileNotFoundException e) {
            LOGGER.atError().log("Erreur pendant creation PdfWriter {}",
                                 e.getLocalizedMessage());
        }

        PdfDocument pdf = new PdfDocument(writer);

        this.document = new Document(pdf);

        return this.document;
    }

    @Override
    public Resource resourceProducer() throws MalformedURLException {
        this.document.close();

        Path path = Paths.get(PDF_DIR + fileName + ".pdf");

        return new UrlResource(path.toUri());
    }
    
    private void addDonneeFonciereToDocument(DonneeFonciere donnee) {
        this.document.add(new Paragraph("ID: " + donnee.getId()));
        this.document.add(new Paragraph("Valeur Foncière: " + donnee.getValeurFonciere()));
        this.document.add(new Paragraph("Adresse Numéro: " + donnee.getAdresseNumero()));
        this.document.add(new Paragraph("Adresse Nom Voie: " + donnee.getAdresseNomVoie()));
        this.document.add(new Paragraph("Code Postal: " + donnee.getCodePostal()));
        this.document.add(new Paragraph("Nom Commune: " + donnee.getNomCommune()));
        this.document.add(new Paragraph("Code Département: " + donnee.getCodeDepartement()));
        this.document.add(new Paragraph("Nature Mutation: " + donnee.getNatureMutation()));
        this.document.add(new Paragraph("Type Local: " + donnee.getTypeLocal()));
        this.document.add(new Paragraph("Surface Terrain: " + donnee.getSurfaceTerrain()));
        this.document.add(new Paragraph("Longitude: " + donnee.getLongitude()));
        this.document.add(new Paragraph("Latitude: " + donnee.getLatitude()));
        this.document.add(new Paragraph("\n"));
    }

}
