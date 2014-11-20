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

public class IfThen extends GPNode {

	private static final long serialVersionUID = -2407365507599491251L;
	
	public String toString() { return "If_Then"; }
	
	public void checkConstraints(
			final EvolutionState state, final int tree,
			final GPIndividual typicalIndividual, final Parameter individualBase) {
        
		super.checkConstraints(state, tree, typicalIndividual, individualBase);
        
        if (children.length != 2) {
            state.output.error("Incorrect number of children for node " + toStringForError() + " at " + individualBase);
        }
    }
	
	@Override
	public void eval(
			final EvolutionState state, final int thread,
			final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {
		
		//state.output.println("Function.eval: IfThen", MKProblem.LOG_FILE);
		
		MKPData mkpd = (MKPData) input;
		MKProblem mkpp = (MKProblem) problem;
		
		children[0].eval(state, thread, mkpd, stack, individual, mkpp);
		//state.output.println("Resultado del \"IF_THEN(izq)\":\t" + mkpd.getResult(), MKProblem.LOG_FILE);
		
		if(mkpd.getResult()) {
			children[1].eval(state, thread, mkpd, stack, individual, mkpp);
			//state.output.println("Resultado del \"IF_THEN(der)\":\t" + mkpd.getResult(), MKProblem.LOG_FILE);
			
			mkpd.setResult(true);
			return;
		}
		
		mkpd.setResult(false);
		//state.output.println("Resultado del \"IF_THEN(out)\":\t" + mkpd.getResult(), MKProblem.LOG_FILE);
    }
}
