package ase.cm.EventProcessing.Models;

import ase.cm.Models.ParkingspotState;
import ase.cm.Models.ParkingspotStatus;

import java.util.UUID;

/**
 * Created by Tommi on 14.06.2017.
 */
public class ParkingspotStateChanged {

    public ParkingspotState getState() {
        return state;
    }

    public void setState(ParkingspotState state) {
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

    public static enum ParkingspotState{
        ARRIVED,
        LEFT,
        NONE;

        public static ParkingspotState parse(IoTStateChangedDTO.IoTChangedState state){
            if(state == IoTStateChangedDTO.IoTChangedState.ARRIVED)
                return ARRIVED;
            else if(state == IoTStateChangedDTO.IoTChangedState.LEFT)
                return LEFT;
            return NONE;
        }
    }

    public ParkingspotStateChanged(){

    }

    @Override
    public String toString() {
        return String.format("%d - %s - %s - %s",id, guid, licensePlateId, state);
    }

    public ParkingspotStateChanged(IoTStateChangedDTO stateChangedDTO){
        this.licensePlateId = stateChangedDTO.getLicensePlateId();
        this.guid = stateChangedDTO.getGuid();
        this.id = stateChangedDTO.getId();
        this.state = ParkingspotState.parse(stateChangedDTO.getState());
    }

    private ParkingspotState state;
    private String licensePlateId;

    private long id;
    private UUID guid;

}
