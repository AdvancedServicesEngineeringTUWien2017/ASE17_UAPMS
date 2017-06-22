package ase.stat.Models;

import ase.shared.Models.SharedParkingspotStatus;

import java.util.UUID;

/**
 * Created by Tommi on 15.06.2017.
 */
public class ParkingspotStatusChangeProcessingModel {

    private UUID uniqueIdentifier;
    private String licensePlateId;
    private double latitude;
    private double longitude;
    private SharedParkingspotStatus status;
    private long timestamp;

    public ParkingspotStatusChangeProcessingModel(){

    }

    public ParkingspotStatusChangeProcessingModel(UUID uniqueIdentifier, String licensePlateId, double latitude, double longitude, SharedParkingspotStatus status, long timestamp) {
        this.uniqueIdentifier = uniqueIdentifier;
        this.licensePlateId = licensePlateId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.timestamp = timestamp;
    }


    public UUID getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public void setUniqueIdentifier(UUID uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
    }

    public String getLicensePlateId() {
        return licensePlateId;
    }

    public void setLicensePlateId(String licensePlateId) {
        this.licensePlateId = licensePlateId;
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

    public SharedParkingspotStatus getStatus() {
        return status;
    }

    public void setStatus(SharedParkingspotStatus status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
