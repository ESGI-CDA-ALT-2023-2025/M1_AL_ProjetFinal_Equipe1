package fr.esgi.dvf.service.impl;

import fr.esgi.dvf.business.DonneeFonciere;
import fr.esgi.dvf.repository.DonneeFonciereRepository;
import fr.esgi.dvf.service.DonneeFonciereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonneeFonciereServiceImpl implements DonneeFonciereService {

    private DonneeFonciereRepository repository;
    private static final double EARTH_RADIUS = 6371000.0; // Earth radius in meters

    public DonneeFonciereServiceImpl(DonneeFonciereRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<DonneeFonciere> getDonneesFonciereByRadius(final double latitude, final double longitude, final double radius) {
         final double radiusEnDegres = radius/EARTH_RADIUS;

         final double minLatitude = latitude - Math.toDegrees(radiusEnDegres);
         final double maxLatitude = latitude + Math.toDegrees(radiusEnDegres);
         final double formuleLongitude = Math.toDegrees(radiusEnDegres) / Math.cos(Math.toRadians(latitude));
         final double minLongitude = longitude - formuleLongitude;
         final double maxLongitude = longitude + formuleLongitude;

         return repository.findByLatitudeBetweenAndLongitudeBetween(minLatitude,maxLatitude,minLongitude,maxLongitude);
    }
}
