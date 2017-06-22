package ase.eval.RestInterface.Models;

import java.util.Date;

/**
 * Created by Tommi on 16.06.2017.
 */
public class MonitoringRequest {
    private Date timeStart;
    private Date timeEnd;

    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }
}
