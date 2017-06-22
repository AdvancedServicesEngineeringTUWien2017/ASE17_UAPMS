package ase.ts.RestInterface.Models;

/**
 * Created by Tommi on 15.06.2017.
 */
public class TourRequest {

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }


    public TourRequestQuality getQuality() {
        return quality;
    }

    public void setQuality(TourRequestQuality quality) {
        this.quality = quality;
    }

    public TourRequestPriority getPriority() {
        return priority;
    }

    public void setPriority(TourRequestPriority priority) {
        this.priority = priority;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public static enum TourRequestQuality{
        LOW,MEDIUM,HIGH
    };

    public static enum TourRequestPriority{
        LOW,MEDIUM,HIGH
    }

    private Double latitude;
    private Double longitude;

    public Double getRange() {
        return range;
    }

    public void setRange(Double range) {
        this.range = range;
    }

    private Double range;
    private TourRequestQuality quality;
    private TourRequestPriority priority;
    private int timeout;

}
