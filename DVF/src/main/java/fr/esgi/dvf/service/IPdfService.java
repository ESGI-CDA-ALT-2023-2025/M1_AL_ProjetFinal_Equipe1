package fr.esgi.dvf.service;

import com.itextpdf.layout.Document;
import fr.esgi.dvf.business.DocumentWithFileName;
import fr.esgi.dvf.business.DonneeFonciere;
import java.net.MalformedURLException;
import java.util.List;
import org.springframework.core.io.Resource;

public interface IPdfService {

    /**
     * Ecrire les données dans Document
     * 
     * @param dataJson
     * @return
     */
    public void writeDataToPDF(Document document, List<DonneeFonciere> donnees);

    /**
     * Fournir un Document pour ecrire les données de DataFoncier
     * 
     * @param fileName
     * @return Document
     */
    public DocumentWithFileName pdfDocumentProvider(String fileName);

    /**
     * Produire ressource de pdf pour response
     * 
     * @return
     */
    public Resource resourceProducer(String fileName) throws MalformedURLException;
}
