package ase.eval.RestInterface.Models;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Tommi on 16.06.2017.
 */
public class MonitoringResponse {

    public static class MonitoringResponseEntry{
        private UUID requestId;
        private Calendar time;
        private double responseTime;
        private String operation;
        private String request;

        public String getResponse() {
            return response;
        }

        public void setResponse(String response) {
            this.response = response;
        }

        private String response;

        public MonitoringResponseEntry(){

        }

        public MonitoringResponseEntry(UUID requestId, Calendar time, double responseTime, String operation, String request, String response) {
            this.requestId = requestId;
            this.time = time;
            this.responseTime = responseTime;
            this.operation = operation;
            this.request = request;
            this.response = response;
        }


        public UUID getRequestId() {
            return requestId;
        }

        public void setRequestId(UUID requestId) {
            this.requestId = requestId;
        }

        public Calendar getTime() {
            return time;
        }

        public void setTime(Calendar time) {
            this.time = time;
        }

        public double getResponseTime() {
            return responseTime;
        }

        public void setResponseTime(double responseTime) {
            this.responseTime = responseTime;
        }

        public String getOperation() {
            return operation;
        }

        public void setOperation(String operation) {
            this.operation = operation;
        }

        public String getRequest() {
            return request;
        }

        public void setRequest(String request) {
            this.request = request;
        }
    }

    public List<MonitoringResponseEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<MonitoringResponseEntry> entries) {
        this.entries = entries;
    }

    private List<MonitoringResponseEntry> entries;

}
