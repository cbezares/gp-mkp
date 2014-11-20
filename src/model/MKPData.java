package model;

import ec.gp.GPData;
//import ec.util.Output;

public class MKPData extends GPData {

	private static final long serialVersionUID = 1236137301060685291L;

	protected boolean result;
	protected Instance instance;
	/*private static Output output;
	
	public static void setOutput(final Output op) {
		output = op;
	}*/
	
	public MKPData(){
		result = false;
		instance = new Instance();
	}
	
	public String toString() {
		return ("[result=" + result + "]\n[instance=" + instance.toString(false) +"]\n[knapsack=" + instance.getKnapsack().toString(false));
		}
	
	@Override
	public MKPData clone() {
		//output.println("MKPData: Clonando...", MKProblem.LOG_FILE);
    	MKPData clon = new MKPData();
		clon.result = this.result;
		clon.instance.copy(this.instance);
        return clon;
    }
	
	public boolean getResult() {
    	return result;
    }

    public void setResult(boolean cond) {
    	this.result = cond;
    }

    public Instance getInstance() {
    	return instance;
    }
    
    public Knapsack getKnapsack() {
    	return instance.getKnapsack();
    }
    
    public void setKnapsack(Knapsack ks) {
    	this.instance.setKnapsack(ks);
    }
    
    public void setInstance(final Instance inst) {
    	this.instance = (inst);
    }
}