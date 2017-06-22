package ase.pm.tourworker.Solver;

import ase.pm.tourworker.Models.TspEdge;
import ase.pm.tourworker.Models.TspInstance;
import ase.pm.tourworker.Models.TspVertex;
import ase.shared.TspModels.TspLocation;
import ase.shared.TspModels.TspRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tommi on 05.06.2017.
 */
public class InstanceGenerator {

    private static int RADIUS = 6371;

    public static TspInstance createInstance(TspRequest requestDTO){

        List<TspVertex> vertices = new ArrayList<>();
        List<TspEdge> edges = new ArrayList<>();

        Map<TspVertex, TspLocation> tempMap = new HashMap<>();

        int id = 0;
        for (TspLocation location :
                requestDTO.getLocations()) {
            TspVertex vertex = new TspVertex(location.getId(), id);
            vertices.add(vertex);
            tempMap.put(vertex,location);
            id++;
        }

        for(int i = 0;i<vertices.size();i++){
            for(int j = i+1;j<vertices.size();j++){
                TspVertex v1 = vertices.get(i);
                TspVertex v2 = vertices.get(j);
                TspLocation l1 = tempMap.get(v1);
                TspLocation l2 = tempMap.get(v2);
                double distance = getDistance(l1.getLatitude(),l1.getLongitude(), l2.getLatitude(), l2.getLongitude());
                TspEdge edge = new TspEdge(v1,v2,distance);
                edges.add(edge);
            }
        }
        TspInstance instance = new TspInstance();
        instance.setVertices(vertices);
        instance.setEdges(edges);
        instance.setTimeout(requestDTO.getTimeout());
        return instance;
    }

    private static double getDistance(double lat1, double lon1, double lat2, double lon2){
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        double distanceKm = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2)) * RADIUS;
        int tt = (int)(distanceKm * 1000);
        return tt;
    }

}
