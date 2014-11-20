package model;

import java.io.*;
import java.util.*;

import ec.util.Output;


public class FileIO {
	
	public static int newLog(Output output, String filename) throws IOException {
		FileWriter fw = new FileWriter(filename, false);
		fw.write("");
	    fw.close();
	    
		File file = new File(filename);
		return output.addLog(file, true);
	}

	public static void readInstances(ArrayList<MKPData> data, final File folder) throws IOException {
		for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            readInstances(data, fileEntry);
	        } else {
	        	System.out.println("Leyendo: " + fileEntry.getName());
	        	MKPData mkpd = new MKPData();
	            readFile(mkpd.getInstance(), fileEntry.getPath());
	            data.add(mkpd);
	        }
	    }
	}
	
	private static void readOptimals(Instance inst) throws IOException {
		StringTokenizer st;
		File file = new File("data/results.txt");
		Scanner s = new Scanner(file);
		boolean found = false;
		
		while(s.hasNextLine() && !found) {
			String instName = "";
			boolean isOptimal = false;
			double objective = 0;
			st = new StringTokenizer(s.nextLine(), " ");
			while(st.hasMoreTokens()) {
				instName = st.nextToken();
				objective = Double.parseDouble(st.nextToken());
				if(st.nextToken().equals("Optimal")) {
					isOptimal = true;
				}
				st.nextToken();	//Solo para avanzar
			}
			if(inst.getName().equals(instName + ".dat")) {
				found = true;
				if(isOptimal) {
					inst.setOptimal(objective);
					inst.setIsOptimalKnown(true);
				}
				else {
					inst.setFeasible(objective);
					inst.setIsOptimalKnown(false);
				}
			}
		}
	}
	
	private static void readFile(Instance inst, String filename) throws IOException {
		File file = new File(filename);
		Scanner s = new Scanner(file);
		
		inst.setName(file.getName());	//Nombre de instancia
		inst.getKnapsack().setNItems(s.nextInt());	//Numero de items
		inst.getKnapsack().setMDimensions(s.nextInt());	//Numero de dimensiones
		inst.setOptimal(s.nextInt());	//Optimo de la instancia
		
		//Beneficios de cada item
		for(int i = 0; i < inst.getKnapsack().getNItems(); i++) {
			Item newItem = new Item();
			
			newItem.setId(i + 1);
			newItem.setProfit(s.nextInt());
			
			inst.addItem(newItem);
			inst.getKnapsack().addInOkl(newItem);
			inst.getKnapsack().addGen(false);
		}
		
		//Coeficientes de cada item para cada dimensión
		for(int i = 0; i < inst.getKnapsack().getMDimensions(); i++)
			for(int j = 0; j < inst.getKnapsack().getNItems(); j++)
				inst.getItem(j).addCoefficient(s.nextInt());
		
		//Restricciones de cada dimensión
		for(int i = 0; i < inst.getKnapsack().getMDimensions(); i++)
			inst.getKnapsack().addConstraint(s.nextInt());
		
		inst.getKnapsack().sortItems(inst.getItems());	//Llenar Colas de orden
		inst.setTightness();	//Radio de estrechez de la mochila
		readOptimals(inst);		//Leer resultados
	}
	
	public static void repairDot() throws IOException {
		File file = new File("out/results/evolution5/mkpa" + (MKProblem.JOB_NUMBER + 16) + "/job." + (MKProblem.JOB_NUMBER) + ".BestIndividual.dot");
		Scanner s = new Scanner(file);
		StringBuilder buffer = new StringBuilder();
		int i = 1;
		String label = "";
		while(s.hasNextLine()) {
			if(i == 1)
				label = s.nextLine();
			else if(i > 4) {
				buffer.append(s.nextLine() + "\n");
				if(i == 5)
					buffer.append(label + "\n");
			}
			else
				s.nextLine();
			i++;
		}
		writeFile(buffer.toString(), "out/results/evolution5/mkpa" + (MKProblem.JOB_NUMBER + 16) + "/job." + (MKProblem.JOB_NUMBER) + ".BestIndividual.dot");
	}
	
	public static void writeFile(String line, String filename) throws IOException {
		File file = new File(filename);
		
		if (!file.exists()) {
			file.createNewFile();
		}
		
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(line);
		bw.close();
	}
}