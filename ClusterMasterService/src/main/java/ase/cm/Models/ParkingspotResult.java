package ase.cm.Models;

/**
 * Created by Tommi on 14.06.2017.
 */
public class ParkingspotResult {

    private Parkingspot parkingspot;
    private double distance;

    public ParkingspotResult(){

    }

    public ParkingspotResult(Parkingspot parkingspot, double distance){
        this.parkingspot = parkingspot;
        this.distance = distance;
    }

    public Parkingspot getParkingspot() {
        return parkingspot;
    }

    public void setParkingspot(Parkingspot parkingspot) {
        this.parkingspot = parkingspot;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
