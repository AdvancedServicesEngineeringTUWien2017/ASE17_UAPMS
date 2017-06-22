package ase.cm.EventProcessing.Models;

/**
 * Created by Tommi on 14.06.2017.
 */
public class IoTRequest {

    public IoTStateChangedDTO getStateChangedDTO() {
        return stateChangedDTO;
    }

    public void setStateChangedDTO(IoTStateChangedDTO stateChangedDTO) {
        this.stateChangedDTO = stateChangedDTO;
    }

    private IoTStateChangedDTO stateChangedDTO;
    private IoTNewSourceDTO newSourceDTO;




    public IoTNewSourceDTO getNewSourceDTO() {
        return newSourceDTO;
    }

    public void setNewSourceDTO(IoTNewSourceDTO newSourceDTO) {
        this.newSourceDTO = newSourceDTO;
    }
}
