package ase.stat.Models;

import ase.shared.Models.SharedParkingspotStatus;
import org.springframework.data.annotation.Id;

import java.util.Calendar;
import java.util.UUID;

/**
 * Created by tommi on 19.06.2017.
 */
public class StatusRecord {

    @Id
    private UUID parkingSpotId;
    private SharedParkingspotStatus status;
    private double latitude, longitude;
    private String licensePlateId;
    private Calendar stateChangeTime;

    public UUID getSource() {
        return source;
    }

    public void setSource(UUID source) {
        this.source = source;
    }

    private UUID source;

    public UUID getParkingSpotId() {
        return parkingSpotId;
    }

    public void setParkingSpotId(UUID parkingSpotId) {
        this.parkingSpotId = parkingSpotId;
    }

    public SharedParkingspotStatus getStatus() {
        return status;
    }

    public void setStatus(SharedParkingspotStatus status) {
        this.status = status;
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

    public String getLicensePlateId() {
        return licensePlateId;
    }

    public void setLicensePlateId(String licensePlateId) {
        this.licensePlateId = licensePlateId;
    }

    public Calendar getStateChangeTime() {
        return stateChangeTime;
    }

    public void setStateChangeTime(Calendar stateChangeTime) {
        this.stateChangeTime = stateChangeTime;
    }
}
