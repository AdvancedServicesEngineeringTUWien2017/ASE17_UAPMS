package ase.pm.tourworker.Models;

import java.util.List;

/**
 * Created by tommi on 04.06.17.
 */
public class TspSolution {

    private List<TspVertex> tour;

    private double cost;

    public List<TspVertex> getTour() {
        return tour;
    }

    public void setTour(List<TspVertex> tour) {
        this.tour = tour;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
