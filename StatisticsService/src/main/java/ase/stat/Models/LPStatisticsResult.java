package ase.stat.Models;

/**
 * Created by Tommi on 15.06.2017.
 */
public class LPStatisticsResult {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
