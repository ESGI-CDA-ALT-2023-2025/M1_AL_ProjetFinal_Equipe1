package fr.esgi.dvf.repository;

import fr.esgi.dvf.business.DonneeFonciere;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DonneeFonciereRepository extends JpaRepository<DonneeFonciere, Long> {

    List<DonneeFonciere> findByLatitudeBetweenAndLongitudeBetween(final double minLatitude, final double maxLatitude, final double minLongitude, final double maxLongitude);
}
