package model;

import java.util.ArrayList;

public class Item extends Knapsack {

	private int id;
	private double profit;
	private ArrayList<Double> coefficients;
	
	private int profitRanking;
	private int weightRanking;
	private int normalizedRanking;
	private int scaledRanking;
	private int generalizedRanking;
	private int fpRanking;
	private int stRanking;
	
	private boolean inKnapsack;
	
	public Item() {
		this.coefficients = new ArrayList<Double>();
		this.inKnapsack = false;
	}
	
	//GETS
	
	public int getId() {
		return this.id;
	}
	
	public double getProfit() {
		return this.profit;
	}
	
	public int getProfitRanking() {
		return this.profitRanking;
	}
	
	public int getWeightRanking() {
		return this.weightRanking;
	}
	
	public int getNormalizedRanking() {
		return this.normalizedRanking;
	}
	
	public int getScaledRanking() {
		return this.scaledRanking;
	}
	
	public int getGeneralizedRanking() {
		return this.generalizedRanking;
	}
	
	public int getFPRanking() {
		return this.fpRanking;
	}
	
	public int getSTRanking() {
		return this.stRanking;
	}
	
	public ArrayList<Double> getCoefficients() {
		return this.coefficients;
	}
	
	public boolean isInKnapsack() {
		return this.inKnapsack;
	}
	
	public double getCoefficient(int dim) {
		return this.coefficients.get(dim);
	}
	
	public double getAverageWeight() {
		double average = 0;
		double acum = 0;
		for(int i = 0; i < getCoefficients().size(); i++) {
			acum += getCoefficients().get(i);
		}
		average = acum / getCoefficients().size();
		return average;
	}
	
	public double getTotalWeight() {
		double sum = 0;
		for(int i = 0; i < getCoefficients().size(); i++) {
			sum += getCoefficients().get(i);
		}
		return sum;
	}
	
	public double getScaledWeight(ArrayList<Double> constraints) {
		double sum = 0;
		for(int i = 0; i < getCoefficients().size(); i++) {
			sum += getCoefficients().get(i) / constraints.get(i);
		}
		return sum;
	}
	
	public double getGeneralizedDensity(ArrayList<Double> constraints) {
		double max = Double.MIN_VALUE;
		for(int i = 0; i < getCoefficients().size(); i++) {
			double value = getCoefficient(i) / constraints.get(i);
			if(value > max)
				max = value;
		}
		return max;
	}
	
	public double getSenjuToyoda(ArrayList<Double> constraints, ArrayList<Double> sums) {
		//double sum1 = 0;
		double sum2 = 0;
		for(int i = 0; i < getCoefficients().size(); i++) {
			//sum1 = 0;
			/*for(int l = 0; l < items.size(); l++) {
				sum1 += items.get(l).getCoefficient(i);
			}*/
			sum2 += getCoefficient(i) * (sums.get(i) - constraints.get(i));
		}
		return sum2;
	}
	
	public double getFrevillePlateau(ArrayList<Double> constraints, ArrayList<Double> sums) {
		//double sum1 = 0;
		double sum2 = 0;
		double ri = 0;
		for(int i = 0; i < getCoefficients().size(); i++) {
			//sum1 = 0;
			/*for(int j = 0; j < items.size(); j++) {
				sum1 += items.get(j).getCoefficient(i);
			}*/
			ri = (sums.get(i) - constraints.get(i)) / sums.get(i);
			sum2 += getCoefficient(i) * ri;
		}
		return sum2;
	}
	
	//SETS Y ADDS
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setProfit(int prof) {
		this.profit = prof;
	}
	
	public void setProfitRanking(int prank) {
		this.profitRanking = prank;
	}
	
	public void setWeightRanking(int wrank) {
		this.weightRanking = wrank;
	}
	
	public void setNormalizedRanking(int nrank) {
		this.normalizedRanking =  nrank;
	}
	
	public void setScaledRanking(int srank) {
		this.scaledRanking = srank;
	}
	
	public void setGeneralizedRanking(int grank) {
		this.generalizedRanking = grank;
	}
	
	public void setFPRanking(int fprank) {
		this.fpRanking = fprank;
	}
	
	public void setSTRanking(int strank) {
		this.stRanking = strank;
	}
	
	public void setInKnapsack(boolean ink) {
		this.inKnapsack = ink;
	}
	
	public void addCoefficient(double coeff) {
		this.coefficients.add(coeff);
	}
	
	public void resetItem(Item item) {
		this.inKnapsack = false;
	}
	
	public String toString(boolean all) {
		StringBuilder buffer = new StringBuilder("Ítem id: [");
		buffer.append(id).append("], Item Profit: ").append(profit).append(", ");
		buffer.append("In KS: ").append(inKnapsack);
		if(all) {
			buffer.append("\nProfit Ranking: ").append(profitRanking + 1);
			buffer.append("\tWeight Ranking: ").append(weightRanking + 1);
			buffer.append("\tNormalized Ranking: ").append(normalizedRanking + 1);
			buffer.append("\tScaled Ranking: ").append(scaledRanking + 1);
			buffer.append("\tGeneralized Ranking: ").append(generalizedRanking + 1);
			buffer.append("\tFP Ranking: ").append(fpRanking + 1);
			buffer.append("\tST Ranking: ").append(stRanking + 1);
			buffer.append("\tAverage weight: ").append(getAverageWeight());
			buffer.append("\tTotal weight: ").append(getTotalWeight());
		}
		return buffer.toString();
	}
}
