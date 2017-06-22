package ase.cm.Models;

import javax.persistence.Embeddable;

/**
 * Created by Tommi on 13.06.2017.
 */
@Embeddable
public class ParkingspotState {
    private ParkingspotStatus status;
    private String licensePlateId;

    public ParkingspotState(){

    }

    public ParkingspotState(ParkingspotStatus status, String lcId){
        this.status = status;
        this.licensePlateId = lcId;
    }

    public ParkingspotState(ParkingspotStatus status){
        this.status = status;
    }


    public ParkingspotStatus getStatus() {
        return status;
    }

    public void setStatus(ParkingspotStatus status) {
        this.status = status;
    }

    public String getLicensePlateId() {
        return licensePlateId;
    }

    public void setLicensePlateId(String licensePlateId) {
        this.licensePlateId = licensePlateId;
    }
}
