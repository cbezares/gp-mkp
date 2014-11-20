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

public class IfThenElse extends GPNode {

	private static final long serialVersionUID = 1589755750993162501L;
	
	public String toString() { return "If_Then_Else"; }
	
	public void checkConstraints(
			final EvolutionState state, final int tree,
			final GPIndividual typicalIndividual, final Parameter individualBase) {
        
		super.checkConstraints(state, tree, typicalIndividual, individualBase);
        
        if (children.length != 3) {
            state.output.error("Incorrect number of children for node " + toStringForError() + " at " + individualBase);
        }
    }
	
	@Override
	public void eval(
			final EvolutionState state, final int thread,
			final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {
		
		//state.output.println("Function.eval: IfThenElse", MKProblem.LOG_FILE);
		
    	//Validations
		MKPData mkpd = (MKPData) input;
		MKProblem mkpp = (MKProblem) problem;
		
		//Evalua al hijo izquierdo
		children[0].eval(state, thread, mkpd, stack, individual, mkpp);
		//state.output.println("Resultado del \"IF_THEN_ELSE(izq)\":\t" + mkpd.getResult(), MKProblem.LOG_FILE);
		
		//Si es verdadero evaluar al hijo derecho 1
		if(mkpd.getResult()) {
			children[1].eval(state, thread, mkpd, stack, individual, mkpp);
			//state.output.println("Resultado del \"IF_THEN_ELSE(cent)\":\t" + mkpd.getResult(), MKProblem.LOG_FILE);
			mkpd.setResult(true);
			return;
		}
		//Si es falso evaluar al hijo derecho 2
		else {
			children[2].eval(state, thread, mkpd, stack, individual, mkpp);
			//state.output.println("Resultado del \"IF_THEN_ELSE(der)\":\t" + mkpd.getResult(), MKProblem.LOG_FILE);
			mkpd.setResult(true);
			return;
		}
    }
}
