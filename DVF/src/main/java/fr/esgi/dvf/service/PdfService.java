package fr.esgi.dvf.service;

import fr.esgi.dvf.business.DocumentWithFileName;
import fr.esgi.dvf.business.DonneeFonciere;
import org.springframework.core.io.Resource;

import java.util.List;

public interface PdfService {

    public void generateCompletePdf(String fileName, List<DonneeFonciere> donneeFoncieres);

}
