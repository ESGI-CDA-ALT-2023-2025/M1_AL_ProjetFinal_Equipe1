package fr.esgi.dvf.service;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBeanBuilder;
import fr.esgi.dvf.business.DonneeFonciere;
import fr.esgi.dvf.repository.DonneeFonciereRepository;

@Service
public class CsvReaderService {

    private static final Logger LOGGER = LogManager.getLogger(CsvReaderService.class);

    @Autowired
    private DonneeFonciereRepository repository;
    private int linesToSkip = 0;
    private static final int LINES_TO_READ = 100000;

    public List<DonneeFonciere> readCsv(String filePath) {

        final String[] columnsToInclude = {"valeur_fonciere", "adresse_numero", "adresse_nom_voie", "code_postal",
                        "nom_commune", "code_departement", "nature_mutation"};

        List<DonneeFonciere> donneeFoncieres = null;
        try (CSVReader csvReader = new CSVReader(new FileReader(filePath))) {

            if (linesToSkip > 0) {
                csvReader.skip(linesToSkip);
            }
            csvReader.skip(1);


            donneeFoncieres = new CsvToBeanBuilder<DonneeFonciere>(csvReader)
                                                                             .withType(DonneeFonciere.class)
                                                                             .withMappingStrategy(new ColumnPositionMappingStrategy<>() {
                                                                                 {
                                                                                     setType(DonneeFonciere.class);
                                                                                     setColumnMapping(columnsToInclude);
                                                                                 }
                                                                             })
                                                                             .build()
                                                                             .parse();

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (donneeFoncieres != null) {
            return new ArrayList<>(donneeFoncieres.subList(0,
                                                           Math.min(LINES_TO_READ,
                                                                    donneeFoncieres.size())));
        } 
        
        return Collections.emptyList();

    }

    public void saveToDatabase() {
        String filePath = "src/main/resources/full.csv";

        List<DonneeFonciere> donneesFoncieres = readCsv(filePath);
        repository.saveAll(donneesFoncieres);
        LOGGER.info("100 000 lignes on été sauvegardé");

        this.linesToSkip += 100000;
    }
}
