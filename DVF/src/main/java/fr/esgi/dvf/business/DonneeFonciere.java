package fr.esgi.dvf.business;

import jakarta.persistence.*;
import lombok.Cleanup;
import lombok.Getter;

@Entity
@Table(name = "DonneeFonciere")
public class DonneeFonciere {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "valeur_fonciere")
    private Integer valeurFonciere;

    @Column(name = "adresse_numero")
    private Integer adresseNumero;

    @Column(name = "adresse_nom_voie")
    private String adresseNomVoie;

    @Column(name = "code_postal")
    private String codePostal;

    @Column(name = "nom_commune")
    private String nomCommune;

    @Column(name = "code_departement")
    private String codeDepartement;

    @Column(name = "nature_mutation")
    private String natureMutation;

    @Column(name = "type_local")
    private String typeLocal;

    @Column(name = "surface_terrain")
    private Integer surfaceTerrain;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "latitude")
    private Double latitude;
}
