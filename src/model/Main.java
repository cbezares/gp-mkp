package model;

import java.util.ArrayList;

import ec.util.Output;

public class Main {

	public static void main(String[] args) {
		//Hacer pruebas con heurísticas
	}
	
	public static void execHeuristics(ArrayList<MKPData> data, Output output) {
		ArrayList<MKPData> auxData = new ArrayList<MKPData>();
		for(int i = 0; i < data.size(); i++) {
			auxData.add(data.get(i).clone());
			double bestProfit, instanceRelErr, err;
			boolean result = true;
			int hits = 0;
			//Heuristic Exec
			double timeInit, timeEnd;
			timeInit = System.nanoTime();	//inicio cronometro
			while(result) {
				result = MKP.addMaxFrevillePlateau(auxData.get(i).getKnapsack());	//cambiar esta linea para elegir heuristica
			}
			timeEnd = System.nanoTime();
			//Relative Error
			if(auxData.get(i).getInstance().isOptimalKnown()) {
				err = auxData.get(i).getInstance().getOptimal() - auxData.get(i).getKnapsack().getCurrentProfit();
				instanceRelErr = Math.abs(err)/(auxData.get(i).getInstance().getOptimal());
				bestProfit = auxData.get(i).getInstance().getOptimal();
			}
			else {
				err = auxData.get(i).getInstance().getFeasible() - auxData.get(i).getKnapsack().getCurrentProfit();
				instanceRelErr = Math.abs(err)/(auxData.get(i).getInstance().getFeasible());
				bestProfit = auxData.get(i).getInstance().getFeasible();
			}
			//Hits
			if(instanceRelErr < MKProblem.IND_MAX_REL_ERR || err < 0) {
				hits++;
			}
			double execTime = ((timeEnd - timeInit) / 1000000);
			//[Instance Name] [N Items] [M Dimensions] [Tightness Ratio] [Exec Time] [Profit] [Optimal] [Optimal Known?] [Rel Error Optimus] [Fitness] [Hits] [Gen]
			output.print(auxData.get(i).getInstance().getName() + " ", MKProblem.HEURISTICS_FILE);
			output.print(auxData.get(i).getKnapsack().getNItems() + " ", MKProblem.HEURISTICS_FILE);
			output.print(auxData.get(i).getKnapsack().getMDimensions() + " ", MKProblem.HEURISTICS_FILE);
			output.print(auxData.get(i).getInstance().getTightnessRatio() + " ", MKProblem.HEURISTICS_FILE);
			output.print(execTime + " ", MKProblem.HEURISTICS_FILE);
			output.print(auxData.get(i).getKnapsack().getCurrentProfit() + " " + bestProfit + " ", MKProblem.HEURISTICS_FILE);
			output.print((auxData.get(i).getInstance().isOptimalKnown() ? "Yes" : "No") + " ", MKProblem.HEURISTICS_FILE);
			output.print(instanceRelErr + " " + (0.85*instanceRelErr) + " " + hits + " ", MKProblem.HEURISTICS_FILE);
			output.println(auxData.get(i).getKnapsack().genToString(), MKProblem.HEURISTICS_FILE);
		}
	}
}
lic static void others(Instance inst) {
		Instance auxInst = new Instance();
		auxInst.copy(inst);
		int i = 0;
		while(MKP.addMaxNormalized(auxInst.getKnapsack()))
			System.out.println("AMD: " + i);
		auxInst.copy(inst);
		i = 0;
		while(MKP.addMaxProfit(auxInst.getKnapsack()))
			System.out.println("AMP: " + i);
		
	}
}
