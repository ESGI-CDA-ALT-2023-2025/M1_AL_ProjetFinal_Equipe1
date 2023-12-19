package fr.esgi.dvf;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import fr.esgi.dvf.init.DataFonciersDownloadService;
import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class DvfApplication {

    private static final Logger LOGGER = LogManager.getLogger(DvfApplication.class);

    @Autowired
    private DataFonciersDownloadService dataFonciersDownloadService;

    public static void main(String[] args) {
        SpringApplication.run(DvfApplication.class, args);
    }


    @PostConstruct
    public void init() {
        dataFonciersDownloadService.downloadAndSaveFile();

        dataFonciersDownloadService.unzip();
    }

}
