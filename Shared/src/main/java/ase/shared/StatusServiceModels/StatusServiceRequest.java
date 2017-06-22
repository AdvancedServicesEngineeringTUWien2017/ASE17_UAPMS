package ase.shared.StatusServiceModels;

import ase.shared.Models.SharedParkingspotStatus;

/**
 * Created by Tommi on 15.06.2017.
 */
public class StatusServiceRequest {

    private Double longitude;
    private Double latitude;
    private Double range;
    private SharedParkingspotStatus status;



    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getRange() {
        return range;
    }

    public void setRange(Double range) {
        this.range = range;
    }

    public SharedParkingspotStatus getStatus() {
        return status;
    }

    public void setStatus(SharedParkingspotStatus status) {
        this.status = status;
    }
}
