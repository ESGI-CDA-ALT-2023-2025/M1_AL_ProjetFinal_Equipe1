package fr.esgi.dvf.business;

import com.opencsv.bean.CsvBindByPosition;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Entity
@Table(name = "DonneeFonciere")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder(toBuilder = true)
public class DonneeFonciere implements
                            Serializable {

  private static final long serialVersionUID = -3151542965392735620L;

  @Id
  @GeneratedValue
  private Long id;

  @Column(name = "valeur_fonciere")
  @CsvBindByPosition(position = 4)
  private String valeurFonciere;

  @Column(name = "adresse_numero")
  @CsvBindByPosition(position = 5)
  private String adresseNumero;

  @Column(name = "adresse_nom_voie")
  @CsvBindByPosition(position = 7)
  private String adresseNomVoie;

  @Column(name = "code_postal")
  @CsvBindByPosition(position = 9)
  private String codePostal;

  @Column(name = "nom_commune")
  @CsvBindByPosition(position = 11)
  private String nomCommune;

  @Column(name = "code_departement")
  @CsvBindByPosition(position = 12)
  private String codeDepartement;

  @Column(name = "nature_mutation")
  @CsvBindByPosition(position = 3)
  private String natureMutation;

  @Column(name = "type_local")
  @CsvBindByPosition(position = 30)
  private String typeLocal;

  @Column(name = "surface_terrain")
  @CsvBindByPosition(position = 37)
  private String surfaceTerrain;

  @Column(name = "longitude")
  @CsvBindByPosition(position = 38)
  private Double longitude;

  @Column(name = "latitude")
  @CsvBindByPosition(position = 39)
  private Double latitude;

  public void setLongitude(String longitude) {
    this.longitude = Double.parseDouble(longitude);
  }

  public void setLatitude(String latitude) {
    this.latitude = Double.parseDouble(latitude);
  }

  public void setValeurFonciere(String valeurFonciere) {
    this.valeurFonciere = valeurFonciere;
  }

  public void setAdresseNomVoie(String adresseNomVoie) {
    this.adresseNomVoie = adresseNomVoie;
  }

  public void setCodePostal(String codePostal) {
    this.codePostal = codePostal;
  }

  public void setNomCommune(String nomCommune) {
    this.nomCommune = nomCommune;
  }

  public void setNatureMutation(String natureMutation) {
    this.natureMutation = natureMutation;
  }

  public void setTypeLocal(String typeLocal) {
    this.typeLocal = typeLocal;
  }
}
