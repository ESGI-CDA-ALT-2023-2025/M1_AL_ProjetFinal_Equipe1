package fr.esgi.dvf.service.impl;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
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
    private static final Logger LOGGER = LogManager.getLogger(DonneeFonciereServiceImpl.class);

    private Document document;
    private String fileName;

    @Override
    public void writeDataToPDF(List<DonneeFonciere> donnees) {
        for (DonneeFonciere donnee : donnees) {
            this.document.add(new Paragraph("Ligne de texte : " + donnee.getAdresseNomVoie()));
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
    public Resource resourceProducer() {
        this.document.close();

        return new ClassPathResource(PDF_DIR + this.fileName);
    }

}
