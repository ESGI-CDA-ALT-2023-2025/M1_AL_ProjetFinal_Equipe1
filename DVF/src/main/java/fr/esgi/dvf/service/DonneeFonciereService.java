package fr.esgi.dvf.service;

import org.springframework.http.ResponseEntity;

public interface DonneeFonciereService {

    public ResponseEntity<byte[]> getResponseWithResource(final double latitude,
                                                          final double longitude,
                                                          final double radius);
}
