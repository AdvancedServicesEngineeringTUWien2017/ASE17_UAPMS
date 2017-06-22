package ase.stat.Models;

import java.util.List;

/**
 * Created by Tommi on 15.06.2017.
 */
public class LicenseplateStatisticsModel {

    public static class LicensePlateStatisticsEntry{

        public LicensePlateStatisticsEntry(){

        }

        private String district;
        private int count;

        public LicensePlateStatisticsEntry(String district, int count) {
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

    public List<LicensePlateStatisticsEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<LicensePlateStatisticsEntry> entries) {
        this.entries = entries;
    }

    List<LicensePlateStatisticsEntry> entries;

}
