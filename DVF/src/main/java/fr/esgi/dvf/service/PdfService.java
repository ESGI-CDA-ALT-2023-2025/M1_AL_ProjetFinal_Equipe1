package fr.esgi.dvf.service;

import java.io.IOException;
import java.util.List;
import fr.esgi.dvf.business.DonneeFonciere;
import fr.esgi.dvf.controller.exception.TechniqueException;

public interface PdfService {

    
    /**
     * Generation complet
     * 
     * @param fileName
     * @param donneeFoncieres
     * @throws TechniqueException 
     */
    public void generateCompletePdf(String fileName, List<DonneeFonciere> donneeFoncieres) throws TechniqueException;

    /**
     * Produire ressource de pdf pour response
     * 
     * @return
     */
    public byte[] resourceProducer(String fileName) throws IOException;
}
