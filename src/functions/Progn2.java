package functions;

import model.MKPData;
import model.MKProblem;
import ec.util.Parameter;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;

public class Progn2 extends GPNode {

	private static final long serialVersionUID = 5933556719619805043L;

	public String toString() { return "Progn2"; }

	public void checkConstraints(
			final EvolutionState state, final int tree,
			final GPIndividual typicalIndividual, final Parameter individualBase) {
        
		super.checkConstraints(state, tree, typicalIndividual, individualBase);
        
        if (children.length != 2) {
            state.output.error("Incorrect number of children for node " +  toStringForError() + " at " + individualBase);
        }
    }

	public void eval(
			final EvolutionState state, final int thread,
			final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {
		
		state.output.println("Function.eval: Progn2", MKProblem.LOG_FILE);

		MKPData mkpd = (MKPData) input;
		MKProblem mkpp = (MKProblem) problem;
		
		children[0].eval(state, thread, mkpd, stack, individual, mkpp);
		state.output.println("Resultado del \"PROGN2(izq)\":\t" + mkpd.getResult(), MKProblem.LOG_FILE);
		children[1].eval(state, thread, mkpd, stack, individual, mkpp);
		state.output.println("Resultado del \"PROGN2(der)\":\t" + mkpd.getResult(), MKProblem.LOG_FILE);
		
		mkpd.setResult(true);
	}
}