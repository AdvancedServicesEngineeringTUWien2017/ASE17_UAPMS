package ase.ts.RestInterface.Models;

import java.util.List;

/**
 * Created by Tommi on 15.06.2017.
 */
public class TourResponse {

    public TourResponseStatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(TourResponseStatusCode statusCode) {
        this.statusCode = statusCode;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public List<TourLocation> getLocations() {
        return locations;
    }

    public void setLocations(List<TourLocation> locations) {
        this.locations = locations;
    }

    public static enum TourResponseStatusCode{
        SUCCESS, ERROR, TIMEOUT
    };

    private TourResponseStatusCode statusCode;
    private double cost;

    private List<TourLocation> locations;

    public static class TourLocation{
        private double latitude, longitude;
        private int position;

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

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }




}
