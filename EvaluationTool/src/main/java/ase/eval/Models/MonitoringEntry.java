package ase.eval.Models;

import org.springframework.data.annotation.Id;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

/**
 * Created by Tommi on 16.06.2017.
 */
public class MonitoringEntry {

    @Id
    private String id;
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


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    @Override
    public String toString() {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return "MonitoringEntry{" +
                "id='" + id + '\'' +
                ", requestId=" + requestId +
                ", time=" + format.format(time.getTime()) +
                ", resposneTime=" + responseTime +
                ", operation='" + operation + '\'' +
                ", request='" + request + '\'' +
                ", response='" + response + '\'' +
                '}';
    }
}
