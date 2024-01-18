package fr.esgi.dvf.service;

import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface DonneeFonciereService<T> {

    public List<T> getDonneesFonciereByRadius(final double latitude, final double longitude, final double radius);

    public ResponseEntity<Resource> getResponseWithResource(final double latitude, final double longitude, final double radius);
}
