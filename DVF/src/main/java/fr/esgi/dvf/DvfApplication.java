package fr.esgi.dvf;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import fr.esgi.dvf.business.DonneeFonciere;
import fr.esgi.dvf.repository.DonneeFonciereRepository;
import fr.esgi.dvf.service.CsvReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import fr.esgi.dvf.init.DataFonciersDownloadService;
import jakarta.annotation.PostConstruct;

import java.util.List;

@SpringBootApplication
public class DvfApplication {

    private static final Logger LOGGER = LogManager.getLogger(DvfApplication.class);

    @Autowired
    private DataFonciersDownloadService dataFonciersDownloadService;
public class DvfApplication implements CommandLineRunner {

    @Autowired
    private CsvReaderService csvReaderService;

    @Autowired
    private DonneeFonciereRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(DvfApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String filePath = "src/main/resources/69.csv";
        int numberOfLinesToRead = 100000;
        List<DonneeFonciere> donneesFoncieres = csvReaderService.readCsv(filePath, numberOfLinesToRead);

        repository.saveAll(donneesFoncieres);
    }


    @PostConstruct
    public void init() {
        dataFonciersDownloadService.downloadAndSaveFile();

        dataFonciersDownloadService.unzip();
    }

}
