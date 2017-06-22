package ase.shared.TspModels;

import java.util.List;

/**
 * Created by Tommi on 05.06.2017.
 */
public class TspRequest {

    public static enum TspRequestQuality{
        LOW,MEDIUM,HIGH
    };

    private List<TspLocation> locations;
    private int timeout;
    private TspRequestQuality quality;

    public List<TspLocation> getLocations() {
        return locations;
    }

    public void setLocations(List<TspLocation> locations) {
        this.locations = locations;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public TspRequestQuality getQuality() {
        return quality;
    }

    public void setQuality(TspRequestQuality quality) {
        this.quality = quality;
    }
}
