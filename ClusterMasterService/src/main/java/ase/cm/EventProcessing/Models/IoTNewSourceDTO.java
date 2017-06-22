package ase.cm.EventProcessing.Models;

import java.util.UUID;

/**
 * Created by Tommi on 14.06.2017.
 */
public class IoTNewSourceDTO {

    private double latitude;
    private double longitude;
    private UUID guid;

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

    public UUID getGuid() {
        return guid;
    }

    public void setGuid(UUID guid) {
        this.guid = guid;
    }
}
