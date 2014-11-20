package model;

import java.util.ArrayList;

public class Instance {

	private String name;			//Nombre de la instancia
	private double optimal;			//El valor óptimo conocido para la instancias. Evaluado en 0 si no se conoce
	private double feasible;			//El mejor valor conocido para la instancia. Evaluado en 0 si ya se conoce el ópimo
	private double tightnessRatio;	//Valor entre 0 y 1 (0:indica que  ningun elemento entra en la mochila; 1:indica que todos los elementos pueden entrar en la mochila)
	private boolean optimalKnown;	//Verdadero si el óptimo de la instancia es conocido.
	
	ArrayList<Item> items;
	
	private Knapsack knapsack;
	
	public Instance() {
		this.optimal = 0.0;
		this.feasible = 0.0;
		this.tightnessRatio = 0.0;
		this.optimalKnown = true;
		
		this.items = new ArrayList<Item>();
		
		this.knapsack = new Knapsack();
	}
	
	
	//GETS
	
	public Knapsack getKnapsack() {
		return knapsack;
	}
	
	public double getOptimal() {
		return optimal;
	}
	
	public double getFeasible() {
		return feasible;
	}
	
	public boolean isOptimalKnown() {
		return optimalKnown;
	}
	
	public String getName() {
		return name;
	}
	
	public double getTightnessRatio() {
		return tightnessRatio;
	}
	
	public ArrayList<Item> getItems() {
		return items;
	}
	
	public Item getItem(int item) {
		return items.get(item);
	}
	
	
	//SETS Y ADDS
	
	public void setKnapsack(Knapsack ks) {
		this.knapsack = ks;
	}
	
	public void setOptimal(double opt) {
		this.optimal = opt;
	}
	
	public void setFeasible(double feas) {
		this.feasible = feas;
	}
	
	public void setIsOptimalKnown(boolean isoptknown) {
		this.optimalKnown = isoptknown;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setTightnessRatio(double tr) {
		this.tightnessRatio = tr;
	}
	
	public void addItem(Item item) {
		this.items.add(item);
	}
	
	public void copy(Instance inst) {
		Knapsack ks = inst.getKnapsack();
		ArrayList<Item> newOKL = inst.getItems();
		ks.resetKnapsack(newOKL);
		
		this.setKnapsack(ks);
		this.name = inst.getName();
		this.optimal = inst.getOptimal();
		this.feasible = inst.getFeasible();
		this.optimalKnown = inst.isOptimalKnown();
		this.tightnessRatio = inst.getTightnessRatio();
	}
	
	public void setTightness() {
		double sum = 0.0;
		for(int j = 0; j < knapsack.getNItems(); j++)
			sum += items.get(j).getCoefficient(0);
		double rounded = Math.round((knapsack.getConstraint(0) / sum) * 100.0) / 100.0;
		this.tightnessRatio = rounded;	
	}
	
	
	//TO STRING
	
	public String toString(boolean all) {
		StringBuilder buffer = new StringBuilder();
		buffer.append("Instancia: " + name).append("\n");
		buffer.append("Cantidad elementos: " + knapsack.getNItems()).append("\n");
		buffer.append("Dimensiones: " + knapsack.getMDimensions()).append("\n");
		if(isOptimalKnown())
			buffer.append("Óptimo: " + optimal).append("\n");
		else
			buffer.append("Mejor: " + feasible);
		if(tightnessRatio > 0.0)
			buffer.append("Ratio: " + tightnessRatio);
		if(all) {
			buffer.append("\n");
			for(int o = 0; o < knapsack.getMDimensions() + 2; o++)
				buffer.append("+-------");
			buffer.append("+\n");
			buffer.append("| Ítem\t| B\t|");
			for(int k = 0; k < knapsack.getMDimensions(); k++)
				buffer.append(" m" + (k+1) + "\t|");
			buffer.append("\n");
			for(int j = 0; j < knapsack.getMDimensions() + 2; j++)
				buffer.append("+-------");
			buffer.append("+\n");
			for(int i = 0; i < knapsack.getNItems(); i++) {
				buffer.append("| " + (i+1) + "\t| " + this.items.get(i).getProfit() + "\t|");
				for(int l = 0; l < knapsack.getMDimensions(); l++) {
					buffer.append(" " + this.items.get(i).getCoefficient(l) + "\t|");
				}
				buffer.append("\n");
			}
			for(int p = 0; p < knapsack.getMDimensions() + 2; p++)
				buffer.append("+-------");
			buffer.append("+\n|\t| Ri\t|");
			for(int n = 0; n < knapsack.getMDimensions(); n++)
				buffer.append(" " + knapsack.constraints.get(n) + "|");
		}
		return buffer.toString();
	}
}