package fr.esgi.dvf.service;

import fr.esgi.dvf.business.DonneeFonciere;

import java.util.List;

public interface DonneeFonciereService {

    public List<DonneeFonciere> getDonneesFonciereByRadius(final double latitude, final double longitude, final double radius);
}
