package fr.esgi.dvf.service;

import java.util.List;
import org.springframework.core.io.Resource;
import com.itextpdf.layout.Document;
import fr.esgi.dvf.business.DonneeFonciere;

public interface IPdfService {

    /**
     * Ecrire les données dans Document
     * 
     * @param dataJson
     * @return
     */
    public void writeDataToPDF(List<DonneeFonciere> donnees);

    /**
     * Fournir un Document pour ecrire les données de DataFoncier
     * 
     * @param fileName
     * @return Document
     */
    public Document pdfDocumentProvider();

    /**
     * Produire ressource de pdf pour response
     * 
     * @return
     */
    public Resource resourceProducer();
}
