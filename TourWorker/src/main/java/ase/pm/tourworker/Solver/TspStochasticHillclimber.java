package ase.pm.tourworker.Solver;

import ase.pm.tourworker.Models.TspEdge;
import ase.pm.tourworker.Models.TspInstance;
import ase.pm.tourworker.Models.TspSolution;
import ase.pm.tourworker.Models.TspVertex;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by tommi on 07.06.2017.
 */
public class TspStochasticHillclimber implements ITspSolver {

    private double k = 0;
    private double d = 0;
    private Random random;
    //private static final int COST_SCALING_FACTOR = 1000000;
    private static final int NOT_IMPROVE_THRESHOLD = 10000000;

    public TspStochasticHillclimber(){
        k = -0.000000005;
        d = 0.4;
        random = new Random();
    }

    @Override
    public TspSolution solve(TspInstance instance) throws Exception {

        int size = instance.getVertices().size();
        long[][] costs = new long[size][size];

        for (TspEdge edge :
                instance.getEdges()) {
            int a = edge.getVertexA().getLocalId();
            int b = edge.getVertexB().getLocalId();
            long longCost = (long)(edge.getCost());// * COST_SCALING_FACTOR);
            costs[a][b] = longCost;
            costs[b][a] = longCost;
        }
        int[] solution = new int[size];
        int[] overallBestSolution = new int[size];
        long overallBestCost;
        initSolution(solution, instance);
        long iteration = 0;
        long cost = costOfSolution(solution, costs);
        overallBestCost = cost;
        System.arraycopy(solution,0,overallBestSolution,0, size);

        long improvements = 0;
        long notImprovedIteration = 0;
        long t = System.currentTimeMillis();
        long end = t + instance.getTimeout() * 1000;

        while (System.currentTimeMillis() < end)
        {
            iteration++;
            int best_i = -1, best_j = -1;
            long best_diff = Long.MAX_VALUE;
            for (int i = 0; i < size - 3; i++)
            {
                for (int j = i + 2; j < size - 1; j++)
                {
                    int vi = solution[i];
                    int vj = solution[j];
                    int vi1 = solution[i+1];
                    int vj1 = solution[j+1];
                    long tmpCost = -costs[vi][vi1] - costs[vj][vj1] + costs[vi][vj1] + costs[vj][vi1];

                    if (tmpCost < best_diff)
                    {
                        best_diff = tmpCost;
                        best_i = i;
                        best_j = j;
                    }
                }
            }

            if (best_i > -1 && best_j > -1)
            {
                long tmpCost = best_diff;
                tmpCost -= costs[solution[best_i + 1]][solution[(best_i + 2)%size]];
                tmpCost += costs[solution[best_j + 1]][solution[(best_i + 2)%size]];

                tmpCost -= costs[solution[best_j + 1]][solution[(best_j + 2)%size]];
                tmpCost += costs[solution[best_i + 1]][solution[(best_j + 2)%size]];

                long best_cost = cost + tmpCost;
                if (best_cost < cost || getCoin(iteration))
                {
                    int tmp = solution[best_i + 1];
                    solution[best_i + 1] = solution[best_j + 1];
                    solution[best_j + 1] = tmp;

                    cost = best_cost;

                    /*long checkcost = costOfSolution(solution,costs);
                    if(checkcost != cost){
                        System.out.println("AAAAAAAAAAAAAAAAAAAA");
                        System.exit(1);
                    }*/

                    if (cost < overallBestCost)
                    {
                        overallBestCost = cost;
                        System.arraycopy(solution,0,overallBestSolution,0, size);
                        notImprovedIteration = 0;
                        improvements++;
                    }
                    else{
                        notImprovedIteration++;
                        if(notImprovedIteration > NOT_IMPROVE_THRESHOLD){
                            break;
                        }
                    }
                }
            }
        }
        return getSolution(overallBestSolution, instance);
    }

    private TspSolution getSolution(int[] solution, TspInstance instance){

        TspSolution tspSolution = new TspSolution();
        tspSolution.setTour(new ArrayList<>());

        for (int i = 0;i<solution.length;i++){
            int id = solution[i];
            TspVertex vertex = instance.vertices.stream().filter(x->x.getLocalId() == id).findFirst().get();
            tspSolution.getTour().add(vertex);
        }
        double cost = 0;
        for(int i = 0;i<solution.length;i++){
            TspVertex vertexA = tspSolution.getTour().get(i);
            TspVertex vertexB = tspSolution.getTour().get((i+1)%solution.length);
            cost += instance.findEdge(vertexA,vertexB).getCost();
        }
        tspSolution.setCost(cost);
        return tspSolution;
    }

    private void initSolution(int[] solution, TspInstance instance){
        GreedyTspSolver greedyTspSolver = new GreedyTspSolver();
        try {
            TspSolution greedySolution = greedyTspSolver.solve(instance);
            int i = 0;
            for (TspVertex vertex :
                    greedySolution.getTour()) {
                solution[i] = vertex.getLocalId();
                i++;
            }
            /*int len = instance.getVertices().size()/10;
            for(int j = 0;j<len;j++){
                int index1 = random.nextInt(solution.length-1) + 1;
                int index2 = random.nextInt(solution.length-1) + 1;

                int tmp = solution[index1];
                solution[index1] = solution[index2];
                solution[index2] = tmp;
            }*/

        }catch (Exception e){
            for(int i = 0;i<solution.length;i++){
                solution[i] = i;
            }
        }
    }

    private long costOfSolution(int[] solution, long[][] costs){
        long cost = 0;
        for (int i = 0; i < solution.length; i++)
        {
            int vi = solution[i];
            int vj = solution[(i+1) % solution.length];
            cost += costs[vi][vj];
        }
        return cost;
    }

    private boolean getCoin(long iteration)
    {
        return random.nextDouble() < (k * iteration + d);
    }
}
