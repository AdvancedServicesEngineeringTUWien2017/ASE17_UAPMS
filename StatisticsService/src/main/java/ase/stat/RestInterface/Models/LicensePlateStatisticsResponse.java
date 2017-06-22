package ase.stat.RestInterface.Models;

import java.util.List;

/**
 * Created by Tommi on 15.06.2017.
 */
public class LicensePlateStatisticsResponse {
    public static class LicensePlateStatisticsResponseEntry{

        public LicensePlateStatisticsResponseEntry(){

        }

        private String district;
        private int count;

        public LicensePlateStatisticsResponseEntry(String district, int count) {
            this.district = district;
            this.count = count;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }

    public List<LicensePlateStatisticsResponseEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<LicensePlateStatisticsResponseEntry> entries) {
        this.entries = entries;
    }

    private List<LicensePlateStatisticsResponseEntry> entries;

}
