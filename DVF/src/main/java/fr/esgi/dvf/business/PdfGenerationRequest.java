package fr.esgi.dvf.business;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Setter
@Getter
public class PdfGenerationRequest implements
                                  Serializable {

  private static final long serialVersionUID = -1551827473942239955L;

  /** the longitude */
  private Double longitude;
  /** the latitude */
  private Double latitude;
  /** the rayon */
  private Double rayon;

  /**
   * ctor
   * 
   * @param longitude
   * @param latitude
   * @param rayon
   */
  public PdfGenerationRequest(Double longitude,
                              Double latitude,
                              Double rayon) {
    this.longitude = longitude;
    this.latitude = latitude;
    this.rayon = rayon;
  }
}
