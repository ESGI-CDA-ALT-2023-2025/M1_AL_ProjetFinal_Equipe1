package fr.esgi.dvf.service.impl;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import fr.esgi.dvf.business.DonneeFonciere;
import fr.esgi.dvf.repository.DonneeFonciereRepository;
import fr.esgi.dvf.service.CsvReaderService;

@SpringBootTest
class DonneeFonciereServiceImplTest {

    @Autowired
    private DonneeFonciereRepository repository;
    @Autowired
    private DonneeFonciereServiceImpl service;

    @Autowired
    private CsvReaderService csvReaderService;

    @BeforeEach
    public void init() throws Exception {
        List<DonneeFonciere> donneeFoncieres = csvReaderService.readCsv("src/main/resources/69.csv");
        repository.saveAll(donneeFoncieres);
    }

    @Test
    void getDonneesFonciereByRadius() {

        final int EXPECTED_SIZE = 100; // plus des 100 lignes
        final int DATA_FONCIERS_EXPECTED_SIZE_LISTE = 24370;
        final List<DonneeFonciere> donneeFoncieres = service.getDonneesFonciereByRadius(45.774799, 4.821381, 10000.0);

        Assertions.assertNotNull(repository.findAll());
        Assertions.assertTrue(repository.findAll().size() > EXPECTED_SIZE);
        Assertions.assertNotNull(donneeFoncieres);
        Assertions.assertEquals(donneeFoncieres.size(), DATA_FONCIERS_EXPECTED_SIZE_LISTE);
    }
}