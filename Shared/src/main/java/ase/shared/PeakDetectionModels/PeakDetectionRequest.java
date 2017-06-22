package ase.shared.PeakDetectionModels;

/**
 * Created by tommi on 19.06.2017.
 */
public class PeakDetectionRequest {


    public PeakDetectionRequest(){

    }

    public PeakDetectionRequest(PeakDetectionMode mode) {
        this.mode = mode;
    }

    public enum PeakDetectionMode{
        PEAK_STARTED, PEAK_ENDED
    }

    public PeakDetectionMode getMode() {
        return mode;
    }

    public void setMode(PeakDetectionMode mode) {
        this.mode = mode;
    }

    private PeakDetectionMode mode;

}
