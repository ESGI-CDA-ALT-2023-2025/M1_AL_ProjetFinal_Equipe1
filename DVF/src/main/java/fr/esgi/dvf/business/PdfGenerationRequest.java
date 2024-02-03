package fr.esgi.dvf.business;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PdfGenerationRequest {

    /** the longitude */
    private Double longitude;
    /** the latitude */
    private Double latitude;
    /** the rayon */
    private Double rayon;

    /**
     * ctor
     * @param longitude
     * @param latitude
     * @param rayon
     */
    public PdfGenerationRequest(Double longitude, Double latitude, Double rayon) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.rayon = rayon;
    }
}
