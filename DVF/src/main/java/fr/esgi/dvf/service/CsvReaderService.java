package fr.esgi.dvf.service;

import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBeanBuilder;
import fr.esgi.dvf.DvfApplication;
import fr.esgi.dvf.business.DonneeFonciere;
import fr.esgi.dvf.repository.DonneeFonciereRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvReaderService {

    private static final Logger LOGGER = LogManager.getLogger(CsvReaderService.class);

    @Autowired
    private DonneeFonciereRepository repository;
    private int linesToSkip = 0;
    private static final int LINES_TO_READ = 100000;

    public List<DonneeFonciere> readCsv(String filePath) throws Exception{

        final String[] columnsToInclude = {"valeur_fonciere", "adresse_numero", "adresse_nom_voie", "code_postal", "nom_commune", "code_departement", "nature_mutation"};

        CSVReader csvReader = new CSVReader(new FileReader(filePath));

        if(linesToSkip > 0){
            csvReader.skip(linesToSkip);
        }
        csvReader.skip(1);

        List< DonneeFonciere> donneeFoncieres = new CsvToBeanBuilder<DonneeFonciere>(csvReader)
                .withType(DonneeFonciere.class)
                .withMappingStrategy(new ColumnPositionMappingStrategy<>(){
                    {
                        setType(DonneeFonciere.class);
                        setColumnMapping(columnsToInclude);
                    }
                })
                .build()
                .parse();

        List<DonneeFonciere> limitedList = new ArrayList<>(donneeFoncieres.subList(0, Math.min(LINES_TO_READ, donneeFoncieres.size())));

        return limitedList;
    }

    public void saveToDatabase() throws Exception {
        String filePath = "src/main/resources/full.csv";

        List<DonneeFonciere> donneesFoncieres = readCsv(filePath);
        repository.saveAll(donneesFoncieres);
        LOGGER.info("100 000 lignes on été sauvegardé");

        this.linesToSkip += 100000;
    }
}
