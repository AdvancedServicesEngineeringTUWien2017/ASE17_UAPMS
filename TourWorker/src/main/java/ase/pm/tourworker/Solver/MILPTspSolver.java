package ase.pm.tourworker.Solver;

import ase.pm.tourworker.Exceptions.NoSolutionException;
import ase.pm.tourworker.Exceptions.SolverTimeoutExecption;
import ase.pm.tourworker.Misc.NullStream;
import ase.pm.tourworker.Models.TspEdge;
import ase.pm.tourworker.Models.TspInstance;
import ase.pm.tourworker.Models.TspSolution;
import ase.pm.tourworker.Models.TspVertex;
import ilog.concert.*;
import ilog.cplex.IloCplex;
import ilog.cplex.IloCplexModeler;
import org.springframework.util.StopWatch;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * Created by tommi on 04.06.17.
 */
public class MILPTspSolver implements ITspSolver {

    @Override
    public TspSolution solve(TspInstance instance) throws Exception {
        IloIntVar[][] decisionVariables = null;
        IloNumVar[][] flowVariables;
        IloCplex cplex = new IloCplex();

        try {
            if(instance.getVertices().isEmpty() || instance.getEdges().isEmpty()) {
                throw new NoSolutionException();
            }
            StopWatch watch = new StopWatch();
            watch.start();

            TspEdgeRepository repository = new TspEdgeRepository(instance);

            int vertexCount = instance.getVertices().size();
            decisionVariables = createDecisionVariables(cplex, vertexCount);
            flowVariables = createFlowVariables(cplex, vertexCount);
            cityLeaveArrive(cplex, instance, decisionVariables);
            conservation(cplex, instance, decisionVariables, flowVariables);
            createObjective(cplex, instance, decisionVariables, repository);

            watch.stop();
            int timeLeft = (int)Math.ceil((1.0*instance.getTimeout()) - watch.getTotalTimeSeconds());

            cplex.setParam(IloCplex.Param.TimeLimit, timeLeft);
            cplex.setOut(new NullStream());

            if (cplex.solve())
            {
                TspSolution solution = getSolution(cplex, instance, decisionVariables);
                IloCplex.CplexStatus status = cplex.getCplexStatus();
                boolean optimalSolution = status == IloCplex.CplexStatus.Optimal || status == IloCplex.CplexStatus.OptimalTol;
                cleanup(cplex);
                if (!optimalSolution) {
                    GreedyTspSolver greedyTspSolver = new GreedyTspSolver();
                    TspSolution greedySolution = greedyTspSolver.solve(instance);
                    if (greedySolution.getCost() < solution.getCost()) {
                        return greedySolution;
                    }
                }
                return solution;
            }
            else
            {
                if(cplex.getCplexStatus() == IloCplex.CplexStatus.AbortTimeLim){
                    throw new SolverTimeoutExecption(instance.getTimeout());
                }
                throw new NoSolutionException();
            }
        }
        catch (IloException ex){
            throw new NoSolutionException();
        }
        catch (Exception e){
            throw new NoSolutionException();
        }
        finally {
            //cleanup(cplex);
        }
    }

    private void cleanup(IloCplex cplex) throws IloException {
        if(cplex != null) {
            cplex.clearModel();
            cplex.endModel();
            cplex.end();
            cplex = null;
            System.gc();
        }
    }

    private TspSolution getSolution(IloCplex cplex, TspInstance instance,IloIntVar[][] decisionVariables) throws IloException,TimeoutException {
        TspSolution solution = new TspSolution();
        solution.setTour(new ArrayList<>());
        int currentIndex = 0;

        for(int j = 0;j<instance.getVertices().size();j++){
            TspVertex vertex = instance.getVertices().get(currentIndex);
            solution.getTour().add(vertex);
            int nextIndex = -1;
            for (int i = 0; i < instance.getVertices().size(); i++) {
                if (i == currentIndex)
                    continue;
                int flag = (int)Math.rint(cplex.getValue(decisionVariables[currentIndex][i]));
                if (flag == 1 && nextIndex == -1) {
                    nextIndex = i;
                }
            }
            currentIndex = nextIndex;
        }
        solution.setCost(cplex.getBestObjValue());


        double cost = 0;
        TspVertex current = null;
        TspVertex first = null;
        for (TspVertex vertex :
                solution.getTour()) {
            if(current == null){
                current = vertex;
                first = vertex;
                continue;
            }
            else{
                cost += instance.findEdge(current, vertex).getCost();
                current = vertex;
            }
        }
        cost += instance.findEdge(current,first).getCost();
        System.out.println("OBJECT = "+solution.getCost() + "          computed = "+ cost);
        return solution;
    }

    private void cityLeaveArrive(IloCplex cplex, TspInstance instance,IloIntVar[][] decisionVariables) throws IloException,TimeoutException {

        for (int i = 0; i < instance.getVertices().size(); i++)
        {
            IloNumExpr exprLeave = cplex.constant(0);
            for (int j = 0; j < instance.getVertices().size(); j++)
            {
                if (i != j)
                {
                    exprLeave = cplex.sum(exprLeave, decisionVariables[i][j]);
                }
            }
            cplex.addEq(exprLeave, 1);
        }

        // each city is arrived once
        for (int j = 0; j < instance.getVertices().size(); j++)
        {
            IloNumExpr exprArrive = cplex.constant(0);
            for (int i = 0; i < instance.getVertices().size(); i++)
            {
                if (i != j)
                {
                    exprArrive = cplex.sum(exprArrive, decisionVariables[i][j]);
                }
            }
            cplex.addEq(exprArrive, 1);
        }
    }

    private void conservation(IloCplex cplex, TspInstance instance, IloIntVar[][] decisionVariables, IloNumVar[][] flows) throws IloException,TimeoutException {

        IloNumExpr tmpExpr = cplex.constant(0.0);
        for (int i = 1; i < instance.getVertices().size(); i++)
        {
            tmpExpr = cplex.sum(tmpExpr, flows[0][i]);
        }
        cplex.addEq(tmpExpr, instance.getVertices().size() - 1);

        // conservation
        for (int j = 1; j < instance.getVertices().size(); j++)
        {
            IloNumExpr leftSum = cplex.constant(0.0);
            for (int i = 0; i < instance.getVertices().size(); i++)
            {
                if (i != j)
                {
                    leftSum = cplex.sum(leftSum, flows[i][j]);
                }
            }

            IloNumExpr rightSum = cplex.constant(0.0);
            for (int k = 0; k < instance.getVertices().size(); k++)
            {
                if (k != j)
                {
                    rightSum = cplex.sum(rightSum, flows[j][k]);
                }
            }
            cplex.addEq(cplex.diff(leftSum, rightSum), 1);
            
        }


        for (int i = 0; i < instance.getVertices().size(); i++)
        {
            for (int j = 0; j < instance.getVertices().size(); j++)
            {
                if (i != j)
                {
                    cplex.addLe(flows[i][j], cplex.prod(instance.getVertices().size() - 1, decisionVariables[i][j]));
                }
            }
        }
    }

    private void createObjective(IloCplex cplex, TspInstance instance, IloIntVar[][] decisionVariables,TspEdgeRepository repository) throws IloException ,TimeoutException {
        // objective function
        IloNumExpr objectiveExpression = cplex.prod(cplex.constant(0), 1.0);
        for (int i = 0; i < instance.getVertices().size(); i++)
        {
            for (int j = 0; j < instance.getVertices().size(); j++)
            {
                if (i != j)
                {
                    TspVertex v1 = instance.getVertices().get(i);
                    TspVertex v2 = instance.getVertices().get(j);
                    TspEdge edge = repository.findEdge(v1,v2);
                    if(edge != null) {
                        objectiveExpression = cplex.sum(objectiveExpression, cplex.prod(edge.getCost(), decisionVariables[i][j]));
                    }
                }
            }
        }
        cplex.addMinimize(objectiveExpression);
    }



    private IloIntVar[][] createDecisionVariables(IloCplex cplex, int size) throws IloException,TimeoutException {
        IloNumVar[] ary = cplex.numVarArray(size*size, 0,1, IloNumVarType.Bool);
        int k = 0;
        IloIntVar[][] decisionVariables = new IloIntVar[size][size];
        for(int i = 0;i<size;i++) {
            for (int j = 0; j < size; j++) {
                if(i != j) {
                    decisionVariables[i][j] = (IloIntVar)ary[k];
                    k++;
                }
            }
        }
        return decisionVariables;
    }


    private IloNumVar[][] createFlowVariables(IloCplex cplex, int size) throws IloException,TimeoutException {

        IloNumVar[] ary = cplex.numVarArray(size*size, 0,size-1, IloNumVarType.Float);
        IloNumVar[][] flows = new IloNumVar[size][size];
        int k = 0;
        for(int i = 0;i<size;i++) {
            for (int j = 0; j < size; j++) {
                if(i != j) {
                    flows[i][j] = ary[k];
                    k++;
                }
            }
        }
        return flows;
    }
}
