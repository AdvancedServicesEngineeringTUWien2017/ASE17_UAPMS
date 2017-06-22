package ase.pm.tourworker.Models;

/**
 * Created by tommi on 04.06.17.
 */
public class TspEdge {

    public TspEdge(){

    }

    public TspEdge(TspVertex v1, TspVertex v2, double cost){
        this.vertexA = v1;
        this.vertexB = v2;
        this.cost = cost;
    }

    public TspVertex getVertexA() {
        return vertexA;
    }

    public void setVertexA(TspVertex vertexA) {
        this.vertexA = vertexA;
    }

    public TspVertex getVertexB() {
        return vertexB;
    }

    public void setVertexB(TspVertex vertexB) {
        this.vertexB = vertexB;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    private TspVertex vertexA;
    private TspVertex vertexB;

    private double cost;

}
