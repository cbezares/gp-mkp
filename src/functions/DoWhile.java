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

public class DoWhile extends GPNode {

	private static final long serialVersionUID = 5933556719619805043L;

	public String toString() { return "Do_While"; }

	public void checkConstraints(
			final EvolutionState state, final int tree,
			final GPIndividual typicalIndividual, final Parameter individualBase) {
        
		super.checkConstraints(state, tree, typicalIndividual, individualBase);
        
        if (children.length != 2) {
            state.output.error("Incorrect number of children for node " +  toStringForError() + " at " + individualBase);
        }
    }

	@Override
	public void eval(
			final EvolutionState state, final int thread,
			final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {
		
		//state.output.println("Function.eval: DoWhile", MKProblem.LOG_FILE);
		
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
		
		//Revisar
		while(x && y && i < n) {
			children[1].eval(state, thread, mkpd, stack, individual, mkpp);
			if(mkpd.getKnapsack().getCurrentProfit() == lastProfit) {
				//state.output.println("\"WHILE(lastProfit)\":\t" + mkpd.getKnapsack().getCurrentProfit() + ", " + lastProfit, MKProblem.LOG_FILE);
				y = false;
			}
			else {
				lastProfit = mkpd.getKnapsack().getCurrentProfit();
				children[0].eval(state, thread, mkpd, stack, individual, mkpp);
				x = mkpd.getResult();
				//state.output.println("Resultado del \"WHILE(izq)\":\t" + mkpd.getResult(), MKProblem.LOG_FILE);
				i++;
			}
		}
		//Si el while iteró al menos una vez se considera resultado verdaderos, caso contrario es falso
		if(i > 0)
			mkpd.setResult(true);
		else
			mkpd.setResult(false);
		//state.output.println("Resultado del \"WHILE(out)\":\t" + mkpd.getResult(), MKProblem.LOG_FILE);
	}
}