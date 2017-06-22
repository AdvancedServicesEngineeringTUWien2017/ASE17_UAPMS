package ase.ts.Models;

import ase.shared.Models.SharedParkingspotStatus;

/**
 * Created by Tommi on 15.06.2017.
 */
public class NodeRequest {

    private double latitude;
    private double longitude;
    private double range;
    private SharedParkingspotStatus status;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public SharedParkingspotStatus getStatus() {
        return status;
    }

    public void setStatus(SharedParkingspotStatus status) {
        this.status = status;
    }
}
