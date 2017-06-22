package ase.ts.Models;

import ase.shared.Models.SharedParkingspotStatus;

import java.util.List;
import java.util.UUID;

/**
 * Created by Tommi on 15.06.2017.
 */
public class NodeResponse {

    public List<NodeResponseEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<NodeResponseEntry> entries) {
        this.entries = entries;
    }

    public static class NodeResponseEntry{
        private UUID uniqueIdentifier;
        private String licensePlateId;
        private double latitude;
        private double longitude;
        private SharedParkingspotStatus status;

        public UUID getUniqueIdentifier() {
            return uniqueIdentifier;
        }

        public void setUniqueIdentifier(UUID uniqueIdentifier) {
            this.uniqueIdentifier = uniqueIdentifier;
        }

        public String getLicensePlateId() {
            return licensePlateId;
        }

        public void setLicensePlateId(String licensePlateId) {
            this.licensePlateId = licensePlateId;
        }

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

        public SharedParkingspotStatus getStatus() {
            return status;
        }

        public void setStatus(SharedParkingspotStatus status) {
            this.status = status;
        }
    }

    List<NodeResponseEntry> entries;





}
