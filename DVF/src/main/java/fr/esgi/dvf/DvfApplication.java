package fr.esgi.dvf;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import fr.esgi.dvf.init.DataFonciersDownloadService;
import fr.esgi.dvf.service.CsvReaderService;
import jakarta.annotation.PostConstruct;

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
    public void init() {
        dataFonciersDownloadService.downloadAndSaveFile();

        dataFonciersDownloadService.unzip();
    }

    @Scheduled(fixedRate = 300000)
    public void scheduleSaveToDatabase() {
        LOGGER.info("Tâche automatique : sauvegarde en base des 100 000 lignes suivantes");
        csvReaderService.saveToDatabase();
    }

}
