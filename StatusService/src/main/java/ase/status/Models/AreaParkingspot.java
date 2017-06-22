package ase.status.Models;

/**
 * Created by Tommi on 18.06.2017.
 */
public class AreaParkingspot {

    public AreaParkingspot(){

    }

    private double latitude, longitude;

    public AreaParkingspot(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

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
}
