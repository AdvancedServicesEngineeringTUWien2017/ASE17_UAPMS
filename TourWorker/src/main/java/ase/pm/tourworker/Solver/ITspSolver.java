package ase.pm.tourworker.Solver;

import ase.pm.tourworker.Models.TspInstance;
import ase.pm.tourworker.Models.TspSolution;

/**
 * Created by tommi on 04.06.17.
 */
public interface ITspSolver {
    TspSolution solve(TspInstance instance) throws Exception;
}
