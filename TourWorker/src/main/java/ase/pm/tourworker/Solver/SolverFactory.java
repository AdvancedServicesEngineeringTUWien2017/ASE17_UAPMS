package ase.pm.tourworker.Solver;

import ase.shared.TspModels.TspRequest;

/**
 * Created by Tommi on 05.06.2017.
 */
public class SolverFactory {

    public static ITspSolver createSolver(TspRequest.TspRequestQuality quality, int count) throws Exception{

        switch (quality){
            case HIGH:{
                if(count > 150){
                    return new TspStochasticHillclimber();
                } else {
                    return new MILPTspSolver();
                }
            }case MEDIUM:{
                return new TspStochasticHillclimber();
            }
            case LOW:{
                return new GreedyTspSolver();
            }
            default:{
                return new GreedyTspSolver();
            }
        }
    }
}
