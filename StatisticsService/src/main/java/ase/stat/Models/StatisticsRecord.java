package ase.stat.Models;

import org.springframework.data.annotation.Id;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Tommi on 15.06.2017.
 */
public class StatisticsRecord {

    @Id
    private String id;

    private String licensePlateId;

    private Calendar arrival;
    private Calendar left;

    private int startHour;
    private int totalHours;

    private UUID parkingSpotId;


    private double latitude;
    private double longitude;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLicensePlateId() {
        return licensePlateId;
    }

    public void setLicensePlateId(String licensePlateId) {
        this.licensePlateId = licensePlateId;
    }



    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(int totalHours) {
        this.totalHours = totalHours;
    }

    public UUID getParkingSpotId() {
        return parkingSpotId;
    }

    public void setParkingSpotId(UUID parkingSpotId) {
        this.parkingSpotId = parkingSpotId;
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

    public Calendar getArrival() {
        return arrival;
    }

    public void setArrival(Calendar arrival) {
        this.arrival = arrival;
    }

    public Calendar getLeft() {
        return left;
    }

    public void setLeft(Calendar left) {
        this.left = left;
    }
}
