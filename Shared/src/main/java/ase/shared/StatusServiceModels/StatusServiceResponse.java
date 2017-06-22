package ase.shared.StatusServiceModels;

import ase.shared.Models.SharedParkingspotStatus;

import java.util.List;
import java.util.UUID;

/**
 * Created by Tommi on 15.06.2017.
 */
public class StatusServiceResponse {

    public StatusServiceResponse(){

    }


    public List<StatusServiceResponseEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<StatusServiceResponseEntry> entries) {
        this.entries = entries;
    }

    public static class StatusServiceResponseEntry{

        private double longitude;
        private double latitude;
        private double distance;
        private SharedParkingspotStatus status;
        private UUID uniqueIdentifier;

        public StatusServiceResponseEntry(){

        }
        public StatusServiceResponseEntry(double latitude, double longitude, double distance, SharedParkingspotStatus status,UUID uniqueIdentifier){
            this.latitude = latitude;
            this.longitude = longitude;
            this.distance = distance;
            this.status = status;
            this.uniqueIdentifier = uniqueIdentifier;
        }


        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public SharedParkingspotStatus getStatus() {
            return status;
        }

        public void setStatus(SharedParkingspotStatus status) {
            this.status = status;
        }

        public UUID getUniqueIdentifier() {
            return uniqueIdentifier;
        }

        public void setUniqueIdentifier(UUID uniqueIdentifier) {
            this.uniqueIdentifier = uniqueIdentifier;
        }
    }

    List<StatusServiceResponseEntry> entries;

    public UUID getSource() {
        return source;
    }

    public void setSource(UUID source) {
        this.source = source;
    }

    private UUID source;

}
