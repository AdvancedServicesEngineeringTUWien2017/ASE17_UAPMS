package ase.pm.tourworker.Solver;

import ase.pm.tourworker.Models.TspEdge;
import ase.pm.tourworker.Models.TspInstance;
import ase.pm.tourworker.Models.TspVertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Tommi on 06.06.2017.
 */
public class TspEdgeRepository {

    private HashMap<Integer,List<TspEdge>> vertexEdges = new HashMap<>();
    private HashMap<Integer,TspEdge> edgeMap = new HashMap<>();
    private int size;

    public TspEdgeRepository(TspInstance instance){

        size = instance.getVertices().size();
        /*for (int i = 0;i<instance.getVertices().size();i++){
            TspVertex vertex = instance.getVertices().get(i);
            int vertexId = vertex.getId();
            List<TspEdge> edges = instance.edges.stream().filter(x->x.getVertexA().getId() == vertexId || x.getVertexB().getId() == vertexId).collect(Collectors.toList());
            vertexEdges.put(vertexId,edges);
        }*/

        for (TspEdge edge :
                instance.edges) {
            TspVertex a = edge.getVertexA();
            TspVertex b = edge.getVertexB();

            int key = a.getId() * instance.getVertices().size() + b.getId();
            if(edgeMap.get(key) != null){
                System.out.println("!!!!!!!!!!!!!!!!!!");
                System.exit(1);
            }
            edgeMap.put(key, edge);

            if(!vertexEdges.containsKey(a.getId())){
                vertexEdges.put(a.getId(), new ArrayList<>());
            }
            if(!vertexEdges.containsKey(b.getId())){
                vertexEdges.put(b.getId(), new ArrayList<>());
            }
            vertexEdges.get(a.getId()).add(edge);
            vertexEdges.get(b.getId()).add(edge);
        }
    }

    public TspEdge findEdge(TspVertex a, TspVertex b){
        int k1 = a.getId() * size + b.getId();
        int k2 = b.getId() * size + a.getId();

        TspEdge edge1 = edgeMap.get(k1);
        TspEdge edge2 = edgeMap.get(k2);

        if(edge1 == null && edge2 == null){
            return null;
        }else if(edge1 != null && edge2 != null){
            throw new RuntimeException("TspEdge Repository Fatal Error!");
        }
        return edge1 != null ? edge1 : edge2;
    }

    public List<TspEdge> findEdges(TspVertex a){
        return vertexEdges.get(a.getId());
    }
}
