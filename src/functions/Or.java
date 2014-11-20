package functions;

import model.MKPData;
import model.MKProblem;
import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;
import ec.util.Parameter;

public class Or extends GPNode {

	private static final long serialVersionUID = 1589755750993162501L;
	
	public String toString() { return "Or"; }
	
	public void checkConstraints(
			final EvolutionState state, final int tree,
			final GPIndividual typicalIndividual, final Parameter individualBase) {
		
        super.checkConstraints(state, tree, typicalIndividual, individualBase);
        
        if (children.length != 2) {
            state.output.error("Incorrect number of children for node " + toStringForError() + " at " + individualBase);
        }
    }
	
	public void eval(
			final EvolutionState state, final int thread,
			final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {
		
		//state.output.println("Function.eval: Or", MKProblem.LOG_FILE);
		
		boolean x;
		MKPData mkpd = (MKPData) input;
		MKProblem mkpp = (MKProblem) problem;
		
		children[0].eval(state, thread, input, stack, individual, mkpp);
		//state.output.println("Resultado del \"OR(izq)\":\t" + mkpd.getResult(), MKProblem.LOG_FILE);
		
		x = mkpd.getResult();
		
		if(x) {
			mkpd.setResult(true);
			//state.output.println("Resultado del \"OR(final)\":\t" + mkpd.getResult(), MKProblem.LOG_FILE);
			return;
		}
		children[1].eval(state, thread, input, stack, individual, mkpp);
		//state.output.println("Resultado del \"OR(der)\":\t" + mkpd.getResult(), MKProblem.LOG_FILE);
		
		mkpd.setResult(mkpd.getResult() || x);
		//state.output.println("Resultado del \"OR(final)\":\t" + mkpd.getResult(), MKProblem.LOG_FILE);
	}
}