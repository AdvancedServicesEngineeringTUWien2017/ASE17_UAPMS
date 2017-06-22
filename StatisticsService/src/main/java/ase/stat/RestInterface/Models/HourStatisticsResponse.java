package ase.stat.RestInterface.Models;

import ase.stat.Models.HourStatisticsModel;

import java.util.List;

/**
 * Created by Tommi on 15.06.2017.
 */
public class HourStatisticsResponse {
    public static class HourStatisticsResponseEntry{

        public HourStatisticsResponseEntry(){

        }

        private String hour;
        private int count;

        public HourStatisticsResponseEntry(String district, int count) {
            this.hour = district;
            this.count = count;
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
    }

    public List<HourStatisticsResponseEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<HourStatisticsResponseEntry> entries) {
        this.entries = entries;
    }

    List<HourStatisticsResponseEntry> entries;
}
