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
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@SpringBootApplication
@EnableScheduling
public class DvfApplication{

    private static final Logger LOGGER = LogManager.getLogger(DvfApplication.class);

    @Autowired
    private DataFonciersDownloadService dataFonciersDownloadService;
    @Autowired
    private CsvReaderService csvReaderService;

    public static void main(String[] args) {
        SpringApplication.run(DvfApplication.class, args);
    }

    @PostConstruct
    public void init() throws Exception {
        dataFonciersDownloadService.downloadAndSaveFile();

        dataFonciersDownloadService.unzip();
    }

    @Scheduled(fixedRate = 300000)
    public void scheduleSaveToDatabase() throws Exception{
        LOGGER.info("Tâche automatique : sauvegarde en base des 100 000 lignes suivantes");
        csvReaderService.saveToDatabase();
    }

}
