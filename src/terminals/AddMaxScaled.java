package terminals;

import model.MKP;
import model.MKPData;
import model.MKProblem;

import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;
import ec.util.Parameter;

public class AddMaxScaled extends GPNode {

	private static final long serialVersionUID = -2534468795456857335L;
	
	public String toString() { return "Add_Max_Scaled"; }
	
	public void checkConstraints(
        	final EvolutionState state, final int tree,
        	final GPIndividual typicalIndividual, final Parameter individualBase) {
        	super.checkConstraints(state, tree, typicalIndividual, individualBase);
        
        if (children.length != 0) {
            state.output.error("Incorrect number of children for node " + toStringForError() + " at " + individualBase);
        }
    }
	
	@Override
	public void eval(final EvolutionState state, final int thread,
			final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {
		
		//state.output.println("Terminal.eval: addMaxScaled", MKProblem.LOG_FILE);
		
		MKPData mkpd = (MKPData) input;
		
		////state.output.println("KS Antes:\t" + mkpd.getInstance().getKnapsack().toString(false), MKProblem.LOG_FILE);
		
		mkpd.setResult(MKP.addMaxScaled(mkpd.getInstance().getKnapsack()));
		
		//state.output.println("KS Despues:\t" + mkpd.getInstance().getKnapsack().toString(false), MKProblem.LOG_FILE);
	}
}