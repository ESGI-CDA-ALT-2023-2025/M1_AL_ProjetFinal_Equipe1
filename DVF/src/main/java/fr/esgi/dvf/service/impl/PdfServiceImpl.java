package fr.esgi.dvf.service.impl;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import fr.esgi.dvf.business.DocumentWithFileName;
import fr.esgi.dvf.business.DonneeFonciere;
import fr.esgi.dvf.service.IPdfService;
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

@Service
public class PdfServiceImpl implements
                            IPdfService {

  private static final String PDF_DIR = "static/pdf/";
  private static final Logger LOGGER = LogManager.getLogger(PdfServiceImpl.class);

  @Override
  public void writeDataToPDF(Document document, List<DonneeFonciere> donnees) {
    for (DonneeFonciere donnee : donnees) {
      this.addDonneeFonciereToDocument(document, donnee);
    }
  }

  @Override
  public DocumentWithFileName pdfDocumentProvider() {

    Document document = null;

    String fileName = "pdf_" + UUID.randomUUID();

    PdfWriter writer = null;
    try {
      writer = new PdfWriter(PDF_DIR + fileName + ".pdf");
    } catch (FileNotFoundException e) {
      LOGGER.atError().log("Erreur pendant creation PdfWriter {}",
                           e.getLocalizedMessage());
    }

    PdfDocument pdf = new PdfDocument(writer);

    document = new Document(pdf);

    return new DocumentWithFileName(document, fileName);
  }

  @Override
  public Resource resourceProducer(String fileName) throws MalformedURLException {
    Path path = Paths.get(PDF_DIR + fileName + ".pdf");

    // Fermez le document après avoir créé la ressource

    Resource res = new UrlResource(path.toUri());
    LOGGER.atInfo().log("FICHIER dans pdfService {}",
                        res.getFilename());

    return res;
  }

  private void addDonneeFonciereToDocument(Document document, DonneeFonciere donnee) {
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
