package fr.esgi.dvf.controlleur.it;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@IfProfileValue(name = "integration-tests",
                value = "true")
@TestInstance(Lifecycle.PER_CLASS)
class DonneeFonciereRestControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    void setUp() {
        if (!waitForFile()) {
            fail("Setup erreur");
        }
    }

    @AfterAll
    void cleanUp() throws Exception {
        this.deleteCsv();
    }


    @Test
    @DisplayName("Test d'indisponibilité du serveur de fichiers")
    void testControllerPDFEndpoint_whenFileServerError_thenError5xx() {
        double longitude = 6.019949;
        double latitude = 46.247458;
        double rayon = 1000;

        CompletableFuture<Void> clientFuture = CompletableFuture.runAsync(() -> {
            try {
                mockMvc.perform(MockMvcRequestBuilders.get("/pdf")
                                                      .param("longitude", String.valueOf(longitude))
                                                      .param("latitude", String.valueOf(latitude))
                                                      .param("rayon", String.valueOf(rayon))
                                                      .contentType(MediaType.APPLICATION_JSON))
                       .andDo(MockMvcResultHandlers.print())
                       .andExpect(MockMvcResultMatchers.status().is5xxServerError());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        CompletableFuture<Void> staticPdfCleanupFuture = CompletableFuture.runAsync(() -> {
            executorService.scheduleAtFixedRate(() -> cleanUpPdfDirectory(), 0, 1, TimeUnit.MILLISECONDS);
        });

        CompletableFuture.allOf(clientFuture, staticPdfCleanupFuture)
                         .thenRun(() -> executorService.shutdownNow())
                         .join();
    }

    @Test
    void testControllerPDFEndpoint_whenRadius0_shouldNoContent() throws Exception {
        double longitude = 1.0;
        double latitude = 1.0;
        double rayon = 0.1;

        mockMvc.perform(MockMvcRequestBuilders.get("/pdf")
                                              .param("longitude", String.valueOf(longitude))
                                              .param("latitude", String.valueOf(latitude))
                                              .param("rayon", String.valueOf(rayon))
                                              .contentType(MediaType.APPLICATION_JSON))
               .andDo(MockMvcResultHandlers.print()) // Afficher la sortie pour le débogage
               .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    void testControllerPDFEndpoint_shouldHaveDifferenceGreaterThan3Seconds() throws Exception {
        double longitude = 6.019949;
        double latitude = 46.247458;
        double rayon = 100000;

        CompletableFuture<Long> client1 = CompletableFuture.supplyAsync(() -> {
            long startTime = System.nanoTime();
            try {
                mockMvc.perform(MockMvcRequestBuilders.get("/pdf")
                                                      .param("longitude", String.valueOf(longitude))
                                                      .param("latitude", String.valueOf(latitude))
                                                      .param("rayon", String.valueOf(rayon))
                                                      .contentType(MediaType.APPLICATION_JSON))
                       .andDo(MockMvcResultHandlers.print())
                       .andExpect(MockMvcResultMatchers.status().isOk())
                       .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PDF));
            } catch (Exception e) {
                e.printStackTrace();
            }
            long endTime = System.nanoTime();
            return endTime - startTime;
        });

        CompletableFuture<Long> client2 = CompletableFuture.supplyAsync(() -> {
            long startTime = System.nanoTime();
            try {
                mockMvc.perform(MockMvcRequestBuilders.get("/pdf")
                                                      .param("longitude", String.valueOf(longitude))
                                                      .param("latitude", String.valueOf(latitude))
                                                      .param("rayon", String.valueOf(rayon))
                                                      .contentType(MediaType.APPLICATION_JSON))
                       .andDo(MockMvcResultHandlers.print())
                       .andExpect(MockMvcResultMatchers.status().isOk())
                       .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PDF));
            } catch (Exception e) {
                e.printStackTrace();
            }
            long endTime = System.nanoTime();
            return endTime - startTime;
        });


        CompletableFuture<Long> combinedFuture = CompletableFuture.allOf(client1, client2)
                                                                  .thenApplyAsync(ignored -> {
                                                                      // resultats depuis deux client en asyncronne
                                                                      Long client1Time = client1.join();
                                                                      Long client2Time = client2.join();

                                                                      return Math.abs(client1Time - client2Time);
                                                                  });

        // doit etre different parce que demande de generation pdf est 1 par 1
        Long difference = combinedFuture.join();
        System.err.println("erreur: "+difference);
        assertTrue((double) difference / 1_000_000_000.0 > 3.0);
    }

    private static void cleanUpPdfDirectory() {
        try {
            Path pdfDirectoryPath = Paths.get("static/pdf");

            Files.walk(pdfDirectoryPath, FileVisitOption.FOLLOW_LINKS)
                 .filter(path -> Files.isRegularFile(path) && path.toString()
                                                                  .endsWith(".pdf"))
                 .forEach(file -> {
                     try {
                         Files.delete(file);
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                 });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteCsv() {
        if (Files.exists(Path.of("src/main/resources/full.csv"))) {
            try {
                Files.delete(Path.of("src/main/resources/full.csv"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean waitForFile() {
        // Attendre jusqu'à ce que le fichier soit présent
        while (!Files.exists(Path.of("src/main/resources/full.csv"))) {
            try {
                Thread.sleep(1000); // Attendre 1 seconde avant de vérifier à nouveau
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }

        // Attendre 40 seconds pour sauvegarder en base des 100 000 lignes
        try {
            Thread.sleep(40000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }

        return true;
    }
}

