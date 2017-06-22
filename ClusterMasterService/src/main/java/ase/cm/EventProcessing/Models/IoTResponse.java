package ase.cm.EventProcessing.Models;

/**
 * Created by Tommi on 14.06.2017.
 */
public class IoTResponse {
    public IoTResponseStatus getStatus() {
        return status;
    }

    public void setStatus(IoTResponseStatus status) {
        this.status = status;
    }

    public long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(long identifier) {
        this.identifier = identifier;
    }

    public enum IoTResponseStatus{
        OK,
        FAIL
    };
    private IoTResponseStatus status;

    private long identifier;



}
