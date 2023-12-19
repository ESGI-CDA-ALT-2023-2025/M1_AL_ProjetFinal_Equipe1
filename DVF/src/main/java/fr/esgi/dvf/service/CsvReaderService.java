package fr.esgi.dvf.service;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBeanBuilder;
import fr.esgi.dvf.business.DonneeFonciere;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.util.List;

@Service
public class CsvReaderService {

    public List<DonneeFonciere> readCsv(String filePath, int numberOfLinesToRead) throws Exception{

        final String[] columnsToInclude = {"valeur_fonciere", "adresse_numero", "adresse_nom_voie", "code_postal", "nom_commune", "code_departement", "nature_mutation"};

        List< DonneeFonciere> donneeFoncieres = new CsvToBeanBuilder<DonneeFonciere>(new FileReader(filePath))
                .withType(DonneeFonciere.class)
                .withMappingStrategy(new ColumnPositionMappingStrategy<>(){
                    {
                        setType(DonneeFonciere.class);
                        setColumnMapping(columnsToInclude);
                    }
                })
                .build()
                .parse();

        return donneeFoncieres;
    }
}
