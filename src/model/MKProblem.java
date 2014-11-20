package model;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;

import ec.*;
import ec.gp.*;
import ec.gp.koza.KozaFitness;
import ec.simple.SimpleProblemForm;
import ec.util.*;

public class MKProblem extends GPProblem implements SimpleProblemForm {

	private static final long serialVersionUID = -8430160211244271537L;
	
	public static int LOG_FILE;
	public static int RESULTS_FILE;
	public static int HEURISTICS_FILE;
	public static int DOT_FILE;
	public static int JOB_NUMBER;
	
	public static long startGenerationTime;
	public static long endGenerationTime;
	
	public static final double IND_MAX_REL_ERR = 0.01;
	public static final double IND_MAX_NODES = 18.0;
	//private static final double IND_MAX_EXEC_TIME = 10000.0; //En milisegundos
	
	ArrayList<MKPData> data;
	
	@Override
	public MKProblem clone() {
		MKProblem mkpp = (MKProblem) super.clone();
		return mkpp;
	}
	
	@Override
	public void setup(final EvolutionState state, final Parameter base) {
		// very important, remember this
		JOB_NUMBER = ((Integer)(state.job[0])).intValue();
		super.setup(state, base);
		// verify our input is the right class (or subclasses from it)
		if (!(input instanceof MKPData))
			state.output.fatal("GPData class must subclass from " + MKPData.class, base.push(P_DATA), null);
				
		//Leemos la instancia desde archivo
		System.out.println("Obteniendo instancias de prueba desde archivo...");
		data = new ArrayList<MKPData>();
		try {
			//LOG_FILE = FileIO.newLog(state.output, "out/MKPLog.out");
			RESULTS_FILE = FileIO.newLog(state.output, "out/results/evolution5/mkpa" + (JOB_NUMBER + 16) + "/MKPResults.out");
			//HEURISTICS_FILE = FileIO.newLog(state.output, "out/MKPHeuristics.out");
			DOT_FILE = FileIO.newLog(state.output, "out/results/evolution5/mkpa" + (JOB_NUMBER + 16) + "/BestIndividual.dot");
			//MKP.setOutput(state.output);
			//MKPData.setOutput(state.output);
			
			final File folder = new File("data/evolution");
			FileIO.readInstances(data, folder);
			System.out.println("Lectura desde archivo terminada con éxito!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		for(int i = 0; i < data.size(); i++) {
			for(int j = 0; j < data.get(i).getInstance().getItems().size(); j++) {
				System.out.println(data.get(i).getInstance().getItem(j).toString(true));
			}
			//state.output.println(data.get(i).getInstance().toString(false), LOG_FILE);
		}*/
		//System.out.println("Ejecución de heurísticas...");
		//Main.execHeuristics(data, state.output);
		System.out.println("MKProblem: Evolucionando...");
		startGenerationTime = System.nanoTime();	//inicio cronómetro evolución
	}

	
	//Evalúa a un individuo
	@Override
	public void evaluate(
			final EvolutionState state,
	        final Individual individual,
	        final int subpopulation,
	        final int threadnum) {
		
		if (!individual.evaluated) {
			
			ArrayList<MKPData> auxData = new ArrayList<MKPData>();
			
			GPIndividual gpind = (GPIndividual) individual;
			
			//state.output.println("\n\nGeneración:" + state.generation + "\nMKPProblem: evaluando el individuo [" + gpind.toString() + "]\n", LOG_FILE);
			//gpind.printIndividualForHumans(state, LOG_FILE);
			
			int hits = 0;
			double relErrAcum = 0.0;
			double nodesResult = 0.0;
			//double timeAcum = 0.0;
			double instanceRelErr, err;
			//double instanceTimeErr;
			
			if(gpind.size() > IND_MAX_NODES)
				nodesResult = Math.abs(gpind.size() - IND_MAX_NODES) / IND_MAX_NODES;
			
			//state.output.println("\n---- Iniciando evaluación ---\nNum de Nodos:" + gpind.size(), LOG_FILE);
			for(int i = 0; i < data.size(); i++) {
				//System.out.println(data.get(i).getInstance().getName());
				auxData.add(data.get(i).clone());	//nuevo data (vaciar mochila)
				
				//state.output.println("\n## Evaluando en instancia: " + auxData.get(i).getInstance().getName() + " ##\n", LOG_FILE);
				gpind.trees[0].printStyle = GPTree.PRINT_STYLE_DOT;	//escribir individuos en formato dot
				
				long timeInit, timeEnd;
				timeInit = System.nanoTime();	//inicio cronometro
				gpind.trees[0].child.eval(state, threadnum, auxData.get(i), stack, gpind, this);	//evaluar el individuo gpind para la instancia i
				timeEnd = System.nanoTime();	//fin cronometro
				/*
				instanceTimeErr = 0.0;
				if((timeEnd - timeInit) / 1000000 > IND_MAX_EXEC_TIME)
					instanceTimeErr = Math.abs((timeEnd - timeInit) - IND_MAX_EXEC_TIME) / IND_MAX_EXEC_TIME; 
				*/
				double bestProfit = 0;
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
				if(instanceRelErr < IND_MAX_REL_ERR || err < 0) {
					hits++;
					//if(err < 0)
						//state.output.println("¡¡¡ATENCIÓN!!!\n¡¡NUEVO MEJOR RESULTADO ENCONTRADO!!", logFile);
				}

				//[Generation] [Individual ID] [Tightness Ratio] [Exec Time] [Profit] [Optimal] [Rel Error Optimus] [Depth] [Tree Size] [Max Nodes] [Rel Error Nodes] [Fitness] [Hits]
				state.output.print(state.generation + " " + gpind.toString() + " ", RESULTS_FILE);
				state.output.print(auxData.get(i).getInstance().getTightnessRatio() + " ", RESULTS_FILE);
				state.output.print((timeEnd - timeInit) / 1000000 + " ", RESULTS_FILE);
				state.output.print(auxData.get(i).getKnapsack().getCurrentProfit() + " " + bestProfit + " " + instanceRelErr + " ", RESULTS_FILE);
				state.output.print(gpind.trees[0].child.depth() + " " + gpind.size() + " " + IND_MAX_NODES + " " + nodesResult + " ", RESULTS_FILE);
				state.output.println((0.05*nodesResult + 0.95*instanceRelErr + " " + hits), RESULTS_FILE);
				
				relErrAcum += instanceRelErr;
				//timeAcum += instanceTimeErr;
				//state.output.print("Time: [init= " + timeInit + "], [end= " + timeEnd + "], [dif= " + (timeEnd - timeInit) + "]", LOG_FILE);
				//state.output.println(", [err= " + instanceTimeErr + "]", LOG_FILE);
			}
			
			Runtime garbage = Runtime.getRuntime();
			garbage.gc();
			
			//state.output.println("---- Evaluación terminada ----", LOG_FILE);
			
			double profitResult = relErrAcum / auxData.size();
			//double timeResult = timeAcum / auxData.size();
			
			//state.output.println("\n[totalTime= " + timeAcum + "], [size= " + auxData.size() + "]", LOG_FILE);
			//state.output.println(" Error relativo de la cantidad de nodos = " + nodesResult, LOG_FILE);
			//state.output.println(" Error relativo del profit = " + profitResult, LOG_FILE);
			//state.output.println(" Error relativo del tiempo = " + timeResult, logFile);
			
			KozaFitness f = ((KozaFitness) gpind.fitness);
			f.setStandardizedFitness(state, (float)(0.95*profitResult + 0.05*nodesResult));
			f.hits = hits;
			//state.output.println("Fitness del individuo = " + ind.fitness.fitnessToStringForHumans(), LOG_FILE);			

			gpind.evaluated = true;
		}	
	}
	
	@Override
	public void describe(final EvolutionState state,
			final Individual individual,
			final int subpopulation,
			final int threadnum,
			final int log) {
		
		endGenerationTime = System.nanoTime();	//fin cronometro evolución
		state.output.message("Evolution duration: " + (endGenerationTime - startGenerationTime) / 1000000 + " ms");	//duración evolución en ms
		state.output.println((endGenerationTime - startGenerationTime) / 1000000 + "", RESULTS_FILE);
		
		//Print BestIndividual.in
		PrintWriter dataOutput = null;
		Charset charset = Charset.forName("UTF-8");
		try {
			dataOutput = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("out/results/evolution5/mkpa" + (JOB_NUMBER + 16) + "/job." + JOB_NUMBER + ".BestIndividual.in"), charset)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		dataOutput.println(Population.NUM_SUBPOPS_PREAMBLE + Code.encode(1));
		dataOutput.println(Population.SUBPOP_INDEX_PREAMBLE + Code.encode(0));
		dataOutput.println(Subpopulation.NUM_INDIVIDUALS_PREAMBLE + Code.encode(1));
		dataOutput.println(Subpopulation.INDIVIDUAL_INDEX_PREAMBLE + Code.encode(0));
		individual.evaluated = false;
		((GPIndividual)individual).printIndividual(state, dataOutput);
		dataOutput.close();
		
		//Print BestIndividual.dot
		GPIndividual gpind = (GPIndividual) individual;
		gpind.trees[0].printStyle = GPTree.PRINT_STYLE_DOT;
		String indid = gpind.toString().substring(19);
		state.output.println("label=\"Individual=" + indid + " Fitness=" + ((KozaFitness) gpind.fitness).standardizedFitness() + " Hits=" + ((KozaFitness) gpind.fitness).hits + " Size=" + gpind.size() + " Depth=" + gpind.trees[0].child.depth() + "\";", DOT_FILE);
		gpind.printIndividualForHumans(state, DOT_FILE);
		try {
			FileIO.repairDot();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
