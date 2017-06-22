package ase.cm.Models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Tommi on 13.06.2017.
 */
@Entity
/*@Table(name="parkingspot",
        uniqueConstraints=@UniqueConstraint(columnNames={"uniqueIdentifier"}))*/
public class Parkingspot implements Serializable{

    private static final long serialVersionUID = -3009157732242241606L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private UUID uniqueIdentifier;

    private double latitude;

    private double longitude;


    private double latitudeRadians;
    private double longitudeRadians;

    private double sinLatitude;
    private double cosLatitude;


    @Embedded
    private ParkingspotState currentState;


    public Parkingspot(){

    }

    public Parkingspot(UUID uniqueIdentifier, double lat, double lon, ParkingspotState state){
        this.uniqueIdentifier = uniqueIdentifier;
        this.latitude = lat;
        this.longitude = lon;
        this.currentState = state;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UUID getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public void setUniqueIdentifier(UUID uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
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

    public ParkingspotState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(ParkingspotState currentState) {
        this.currentState = currentState;
    }

    public double getLatitudeRadians() {
        return latitudeRadians;
    }

    public void setLatitudeRadians(double latitudeRadians) {
        this.latitudeRadians = latitudeRadians;
    }

    public double getLongitudeRadians() {
        return longitudeRadians;
    }

    public void setLongitudeRadians(double longitudeRadians) {
        this.longitudeRadians = longitudeRadians;
    }

    public double getSinLatitude() {
        return sinLatitude;
    }

    public void setSinLatitude(double sinLatitude) {
        this.sinLatitude = sinLatitude;
    }

    public double getCosLatitude() {
        return cosLatitude;
    }

    public void setCosLatitude(double cosLatitude) {
        this.cosLatitude = cosLatitude;
    }
}
