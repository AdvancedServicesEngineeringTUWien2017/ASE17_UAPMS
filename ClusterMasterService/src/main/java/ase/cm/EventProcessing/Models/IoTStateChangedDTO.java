package ase.cm.EventProcessing.Models;

import java.util.UUID;

/**
 * Created by Tommi on 14.06.2017.
 */
public class IoTStateChangedDTO {

    public IoTChangedState getState() {
        return state;
    }

    public void setState(IoTChangedState state) {
        this.state = state;
    }

    public String getLicensePlateId() {
        return licensePlateId;
    }

    public void setLicensePlateId(String licensePlateId) {
        this.licensePlateId = licensePlateId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UUID getGuid() {
        return guid;
    }

    public void setGuid(UUID guid) {
        this.guid = guid;
    }

    public enum IoTChangedState{
        ARRIVED,
        LEFT
    };

    private IoTChangedState state;
    private String licensePlateId;


    private long id;
    private UUID guid;
}
