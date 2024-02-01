package fr.esgi.dvf.controlleur.it;

import static org.junit.jupiter.api.Assertions.fail;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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

// @ExtendWith(SpringExtension.class)
// @WebMvcTest(DonneeFonciereRestControllerIT.class)
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
    void cleanUp() {
        this.deleteCsv();
    }


    @Test
    void testControllerPDFEndpoint_whenRadius0_shouldNoContent() throws Exception {
        double longitude = 6.019949;
        double latitude = 46.247458;
        double rayon = 1000;

        // Effectuez la requête GET en utilisant les paramètres spécifiés
        mockMvc.perform(MockMvcRequestBuilders.get("/pdf")
                                              .param("longitude", String.valueOf(longitude))
                                              .param("latitude", String.valueOf(latitude))
                                              .param("rayon", String.valueOf(rayon))
                                              .contentType(MediaType.APPLICATION_JSON))
               .andDo(MockMvcResultHandlers.print()) // Afficher la sortie pour le débogage
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PDF));


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

        // Attendre 30 seconds pour sauvegarder en base des 100 000 lignes
        try { 
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }

        return true;
    }
}

