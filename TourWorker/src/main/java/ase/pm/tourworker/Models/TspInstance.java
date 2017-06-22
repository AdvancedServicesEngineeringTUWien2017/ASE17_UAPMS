package ase.pm.tourworker.Models;

import java.util.List;

/**
 * Created by tommi on 04.06.17.
 */
public class TspInstance {
    public List<TspVertex> getVertices() {
        return vertices;
    }

    public void setVertices(List<TspVertex> vertices) {
        this.vertices = vertices;
    }

    public List<TspEdge> getEdges() {
        return edges;
    }

    public void setEdges(List<TspEdge> edges) {
        this.edges = edges;
    }

    public List<TspVertex> vertices;
    public List<TspEdge> edges;
    public TspEdge[][] edgeMatrix;


    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    private int timeout;

    private void initEdgeMatrix(){
        int size = this.getVertices().size();
        edgeMatrix = new TspEdge[size][size];
        for (TspEdge edge:
             edges) {
            int a = edge.getVertexA().getLocalId();
            int b = edge.getVertexB().getLocalId();
            edgeMatrix[a][b] = edge;
            edgeMatrix[b][a] = edge;
        }
    }

    public TspEdge findEdge(TspVertex v1, TspVertex v2) {
        if(edgeMatrix == null){
            initEdgeMatrix();
        }
        return edgeMatrix[v1.getLocalId()][v2.getLocalId()];
    }
}
