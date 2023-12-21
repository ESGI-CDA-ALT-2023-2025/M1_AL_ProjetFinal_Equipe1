package fr.esgi.dvf.init;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class DataFonciersDownloadService {

    private static final String FILE_NAME_GZ = "full.csv.gz";
    private static final String FILE_NAME_CSV = "full.csv";
    private static final String FILE_URL = "https://files.data.gouv.fr/geo-dvf/latest/csv/2023/";
    private static final String FILE_DIR = "src/main/resources/";

    private static final StringBuilder SB = new StringBuilder();

    private static final Logger LOGGER = LogManager.getLogger(DataFonciersDownloadService.class);

    public void downloadAndSaveFile() {

        String fileUrl = SB.append(FILE_URL).append(FILE_NAME_GZ).toString();
        SB.setLength(0);
        String destinationPath = SB.append(FILE_DIR).append(FILE_NAME_GZ).toString();
        SB.setLength(0);

        try {
            FileUtils.copyURLToFile(new URL(fileUrl), new File(destinationPath));

            LOGGER.atInfo().log("Téléchargement réussi, fichier sauvegarde ici : {}",
                                destinationPath);
        } catch (IOException e) {
            LOGGER.atError().log("Erreur {}", e.getLocalizedMessage());
        }
    }


    public void unzip() {

        String inputFile = SB.append(FILE_DIR).append(FILE_NAME_GZ).toString();
        SB.setLength(0);
        String outputFile = SB.append(FILE_DIR).append(FILE_NAME_CSV).toString();
        SB.setLength(0);

        try {
            // Flux d'entrée pour le fichier gzip
            FileInputStream fis = new FileInputStream(inputFile);
            GZIPInputStream gzis = new GZIPInputStream(fis);

            // Flux de sortie pour le fichier décompressé
            FileOutputStream fos = new FileOutputStream(outputFile);

            byte[] buffer = new byte[1024];
            int len;

            // Lecture du fichier compressé et écriture dans le fichier décompressé
            while ((len = gzis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }

            gzis.close();
            fis.close();
            fos.close();

            LOGGER.atInfo().log("Fichier décompressé avec succès {}",
                                outputFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
