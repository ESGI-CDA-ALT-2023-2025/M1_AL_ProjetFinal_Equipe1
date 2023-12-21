package fr.esgi.dvf.service.impl;

import fr.esgi.dvf.DvfApplication;
import fr.esgi.dvf.business.DonneeFonciere;
import fr.esgi.dvf.repository.DonneeFonciereRepository;
import fr.esgi.dvf.service.CsvReaderService;
import fr.esgi.dvf.service.DonneeFonciereService;
import jakarta.transaction.Transactional;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

        final int EXPECTED_SIZE = 40275;
        final List<DonneeFonciere> donneeFoncieres = service.getDonneesFonciereByRadius(45.774799, 4.821381, 10000.0);

        Assertions.assertNotNull(repository.findAll());
        Assertions.assertEquals(repository.findAll().size(), EXPECTED_SIZE);
        Assertions.assertNotNull(donneeFoncieres);
        Assertions.assertEquals(donneeFoncieres.size(), 24370);
    }
}