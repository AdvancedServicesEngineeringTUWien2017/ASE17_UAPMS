package ase.stat.Models;

import java.util.List;

/**
 * Created by Tommi on 15.06.2017.
 */
public class HourStatisticsModel {
    public static class HourStatisticsModelEntry{

        public HourStatisticsModelEntry(){

        }

        public String getHour() {
            return hour;
        }

        public void setHour(String hour) {
            this.hour = hour;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        private String hour;
        private int count;

        public HourStatisticsModelEntry(String district, int count) {
            this.hour = district;
            this.count = count;
        }


    }

    public List<HourStatisticsModelEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<HourStatisticsModelEntry> entries) {
        this.entries = entries;
    }

    List<HourStatisticsModelEntry> entries;
}
