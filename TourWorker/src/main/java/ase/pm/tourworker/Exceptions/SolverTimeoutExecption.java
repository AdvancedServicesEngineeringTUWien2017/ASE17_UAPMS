package ase.pm.tourworker.Exceptions;

/**
 * Created by tommi on 04.06.17.
 */
public class SolverTimeoutExecption extends Exception{

    public SolverTimeoutExecption(int timeout){
        super("Timeout of " + timeout + " exceeded!");
    }

    public SolverTimeoutExecption(){
        super("Timeout exceeded!");
    }
}
