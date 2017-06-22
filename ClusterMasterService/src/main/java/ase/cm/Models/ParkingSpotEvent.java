package ase.cm.Models;

/**
 * Created by Tommi on 13.06.2017.
 */
public class ParkingSpotEvent {

    private long id;
    private String name;
    private ParkingspotStatus status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ParkingspotStatus getStatus() {
        return status;
    }

    public void setStatus(ParkingspotStatus status) {
        this.status = status;
    }
}
