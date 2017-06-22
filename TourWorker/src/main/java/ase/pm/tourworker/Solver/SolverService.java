package ase.pm.tourworker.Solver;

import ase.shared.TspModels.TspLocation;
import ase.shared.TspModels.TspRequest;
import ase.shared.TspModels.TspResult;
import ase.shared.TspModels.TspResultStatus;
import ase.pm.tourworker.Models.TspInstance;
import ase.pm.tourworker.Models.TspSolution;
import ase.pm.tourworker.Models.TspVertex;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * Created by Tommi on 16.06.2017.
 */
@Service
public class SolverService {

    private SolverFactory solverFactory;
    private InstanceGenerator instanceGenerator;

    public SolverService(){
        solverFactory = new SolverFactory();
        instanceGenerator = new InstanceGenerator();
    }

    public TspResult solve(TspRequest request){

        TspResult result = new TspResult();
        try {
            ITspSolver solver = solverFactory.createSolver(request.getQuality(), request.getLocations().size());
            TspInstance instance = instanceGenerator.createInstance(request);
            try {
                TspSolution solution = solver.solve(instance);

                result.setTour(new ArrayList<>());
                for (TspVertex vertex :
                        solution.getTour()) {
                    TspLocation resVertex = request.getLocations().stream().filter(x->x.getId() == vertex.getId()).findFirst().orElse(null);
                    result.getTour().add(resVertex);
                }
                result.setCost(solution.getCost());
                result.setStatus(TspResultStatus.SUCCESS);

                String sequence = String.join(" - ", solution.getTour().stream().map(x->String.valueOf(x.getId())).collect(Collectors.toList()));
                System.out.println(solution.getCost() + "           " + sequence);

            }catch (ExecutionException | InterruptedException | TimeoutException e){
                result.setStatus(TspResultStatus.TIMEOUT);
            }
        } catch (Exception e) {
            System.out.println("ERROR!" + e.getMessage());
            result.setStatus(TspResultStatus.ERROR);
        }
        finally {
           return result;
        }
    }
}
