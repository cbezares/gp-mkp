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

public class Repeat extends GPNode {

	private static final long serialVersionUID = 5933556666619805043L;

	public String toString() { return "Repeat"; }

	public void checkConstraints(
			final EvolutionState state, final int tree,
			final GPIndividual typicalIndividual, final Parameter individualBase) {
        
		super.checkConstraints(state, tree, typicalIndividual, individualBase);
        
        if (children.length != 1) {
            state.output.error("Incorrect number of children for node " +  toStringForError() + " at " + individualBase);
        }
    }

	@Override
	public void eval(
			final EvolutionState state, final int thread,
			final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {
		
		//state.output.println("Function.eval: Repeat", MKProblem.LOG_FILE);
		
		boolean x, y;
		int i, n;
		double lastProfit;
		MKPData mkpd = (MKPData) input;
		MKProblem mkpp = (MKProblem) problem;
		
		children[0].eval(state, thread, mkpd, stack, individual, mkpp);
		
		n = mkpd.getInstance().getKnapsack().getNItems();
		x = mkpd.getResult();
		i = 0;
		lastProfit = -1;
		y = true;
		
		while(x && y && i < n) {
			children[0].eval(state, thread, mkpd, stack, individual, mkpp);
			if(mkpd.getKnapsack().getCurrentProfit() == lastProfit) {
				//state.output.println("\"REPEAT(lastProfit)\":\t" + mkpd.getKnapsack().getCurrentProfit() + ", " + lastProfit, MKProblem.LOG_FILE);
				y = false;
			}
			else {
				lastProfit = mkpd.getKnapsack().getCurrentProfit();
				//state.output.println("Resultado del \"REPEAT(in)\":\t" + mkpd.getResult(), MKProblem.LOG_FILE);
				x = mkpd.getResult();
				i++;
			}
		}
		if(i > 0)
			mkpd.setResult(true);
		else
			mkpd.setResult(false);
		//state.output.println("Resultado del \"REPEAT(out)\":\t" + mkpd.getResult(), MKProblem.LOG_FILE);
	}
}