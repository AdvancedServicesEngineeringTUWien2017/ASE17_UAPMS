package ase.pm.tourworker.Solver;

import ase.pm.tourworker.Exceptions.SolverTimeoutExecption;
import ase.pm.tourworker.Models.TspEdge;
import ase.pm.tourworker.Models.TspInstance;
import ase.pm.tourworker.Models.TspSolution;
import ase.pm.tourworker.Models.TspVertex;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Created by tommi on 04.06.17.
 */
public class GreedyTspSolver implements ITspSolver {
    @Override
    public TspSolution solve(TspInstance instance) throws Exception{

        if(instance.getVertices().isEmpty() || instance.getEdges().isEmpty()){
            System.out.println("INPUT ERROR GREEDY!");
            return null;
        }

        TspEdgeRepository repository = new TspEdgeRepository(instance);
        List<TspVertex> availableVertices = new ArrayList<>();
        availableVertices.addAll(instance.getVertices());
        TspVertex current = instance.vertices.get(0);
        availableVertices.remove(current);
        List<TspVertex> tour = new ArrayList<>();
        tour.add(current);
        double cost = 0;
        while (tour.size() < instance.vertices.size()){
            TspVertex finalCurrent = current;
            TspEdge res = repository.findEdges(finalCurrent).stream().filter(x->availableVertices.contains(x.getVertexA() != finalCurrent?x.getVertexA():x.getVertexB()))
                    .min(new Comparator<TspEdge>() {
                @Override
                public int compare(TspEdge o1, TspEdge o2) {
                    return Double.compare(o1.getCost(),o2.getCost());
                }
            }).get();
            current = res.getVertexA() == current ? res.getVertexB() : res.getVertexA();
            tour.add(current);
            cost += res.getCost();
            availableVertices.remove(current);
        }
        TspEdge backward = instance.findEdge(current, tour.get(0));
        cost+=backward.getCost();

        TspSolution solution = new TspSolution();
        solution.setCost(cost);
        solution.setTour(tour);
        return solution;
    }
}
