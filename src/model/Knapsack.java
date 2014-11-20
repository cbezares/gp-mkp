package model;

import java.util.ArrayList;
import java.util.Collections;

public class Knapsack {
	
	public static final int ZERO = 0;
	public static final int ONE = 1;
	
	private int nItems;				//Cantidad de elementos del problema
	private int mDimensions;		//Cantidad de restricciones del problema
	public ArrayList<Double> constraints;
	
	private double currentProfit;			//Valor del contenido de la mochila
	private ArrayList<Double> currentWeights;	//Lista con los pesos ya utilizados en cada dimensión
	
	private ArrayList<Item> inKnapsack;		//Elementos dentro de la mochila
	private ArrayList<Item> outKnapsack;	//Elementos fuera de la mochila
	
	private ArrayList<Integer> profitsQueue;	//Cola de items ordenados por beneficio
	private ArrayList<Integer> weightsQueue;	//Cola de items ordenados por peso
	private ArrayList<Integer> normalizedsQueue;	//Cola de items ordenados por la razón beneficio/peso total
	private ArrayList<Integer> scaledsQueue;		//Cola de items ordenados por la razón beneficio/peso total/capacidades
	private ArrayList<Integer> generalizedsQueue;	//Cola de items ordenados por la razón beneficio/max(peso/capacidad)
	private ArrayList<Integer> stQueue;	//Cola de items ordenados por la razón beneficio/max(peso/capacidad)
	private ArrayList<Integer> fpQueue;	//Cola de items ordenados por la razón beneficio/max(peso/capacidad)
	
	private ArrayList<Integer> gen;		//Vector que representa qué items están en la mochila
	
	private ArrayList<Double> sumCoeffsItems;

	public Knapsack() {
		this.currentProfit = 0.0;
		this.currentWeights = new ArrayList<Double>();
		this.constraints = new ArrayList<Double>();
		
		this.inKnapsack = new ArrayList<Item>();
		this.outKnapsack = new ArrayList<Item>();
		
		this.profitsQueue = new ArrayList<Integer>();
		this.weightsQueue = new ArrayList<Integer>();
		this.normalizedsQueue = new ArrayList<Integer>();
		this.scaledsQueue = new ArrayList<Integer>();
		this.generalizedsQueue = new ArrayList<Integer>();
		this.fpQueue = new ArrayList<Integer>();
		this.stQueue = new ArrayList<Integer>();
		
		this.gen = new ArrayList<Integer>();
		
		this.sumCoeffsItems = new ArrayList<Double>();
	}
	
	/*public Knapsack (ArrayList<Item> items, ArrayList<Double> constr, int m, int n) {
		this.nItems = n;
		this.mDimensions = m;
		this.constraints = constr;
		this.currentProfit = 0.0;
		this.currentWeights = new ArrayList<Double>(Collections.nCopies(m, 0.0));
		this.inKnapsack = new ArrayList<Item>();
		this.outKnapsack = items;
		this.gen = new ArrayList<Integer>(Collections.nCopies(n, 0));
	}*/
	
	
	//GETS
		
	public int getNItems() {
		return nItems;
	}
	
	public int getMDimensions() {
		return mDimensions;
	}

	public ArrayList<Double> getConstraints() {
		return constraints;
	}
	
	public double getConstraint(int dim) {
		return constraints.get(dim);
	}
	
	public double getCurrentProfit() {
		return currentProfit;
	}
	
	public ArrayList<Double> getCurrentWeights() {
		return currentWeights;
	}
	
	public ArrayList<Item> getIKL() {
		return inKnapsack;
	}
	
	public ArrayList<Item> getOKL() {
		return outKnapsack;
	}
	
	public Item getItemIKL(int index) {
		return inKnapsack.get(index);
	}
	
	public Item getItemOKL(int index) {
		return outKnapsack.get(index);
	}
	
	public ArrayList<Integer> getProfitQueue() {
		return profitsQueue;
	}
	
	public ArrayList<Integer> getCoefficientsQueue() {
		return weightsQueue;
	}
	
	public ArrayList<Integer> getNormalizedsQueue() {
		return normalizedsQueue;
	}
	
	public ArrayList<Integer> getScaledsQueue() {
		return scaledsQueue;
	}
	
	public ArrayList<Integer> getGeneralizedsQueue() {
		return generalizedsQueue;
	}
	
	public ArrayList<Integer> getFPQueue() {
		return fpQueue;
	}
	
	public ArrayList<Integer> getSTQueue() {
		return stQueue;
	}
	
	public ArrayList<Integer> getGen() {
		return gen;
	}
	
	public ArrayList<Double> getSumCoeffsItems() {
		return sumCoeffsItems;
	}
	
	public String genToString() {
		StringBuilder buffer = new StringBuilder();
		for(int i = 0; i < this.gen.size(); i++)
			buffer.append(this.gen.get(i));
		return buffer.toString();			
	}
	

	//SETS Y ADDS
	
	public void setNItems(int n) {
		this.nItems = n;
	}
	
	public void setMDimensions(int m) {
		this.mDimensions = m;
	}
	
	public void addConstraint(double cons) {
		this.constraints.add(cons);
	}

	public void setProfit(int profit) {
		this.currentProfit = profit;
	}

	public void setCurrentWeight(int dim, double value) {
		this.currentWeights.set(dim, value);
	}
	
	public void setWeightQueue(int pos, int itemId) {
		this.weightsQueue.set(pos, itemId);
	}
	
	public void setProfitQueue(int pos, int itemId) {
		this.profitsQueue.set(pos, itemId);
	}
	
	public void setNormalizedQueue(int pos, int itemId) {
		this.normalizedsQueue.set(pos, itemId);
	}
	
	public void setScaledQueue(int pos, int itemId) {
		this.scaledsQueue.set(pos, itemId);
	}
	
	public void setGeneralizedQueue(int pos, int itemId) {
		this.generalizedsQueue.set(pos, itemId);
	}
	
	public void setFPQueue(int pos, int itemId) {
		this.fpQueue.set(pos, itemId);
	}

	public void setSTQueue(int pos, int itemId) {
		this.stQueue.set(pos, itemId);
	}

	public void setGen(int item, boolean in) {
		if(in)
			this.gen.set(item, ONE);
		else
			this.gen.set(item, ZERO);
	}

	public boolean addGen(boolean in) {
		if(in)
			return (this.gen.add(ONE));
		else
			return (this.gen.add(ZERO));
	}
	
	public boolean addInOkl(Item item) {
		return (this.outKnapsack.add(item));
	}
	
	public void addInSumCoeffsItems(Double sum) {
		getSumCoeffsItems().add(sum);
	}
	
	public void setSumsCoeffsItems(ArrayList<Item> items) {
		for(int i = 0; i < getMDimensions(); i++) {
			double sum = 0;
			for(int j = 0; j < getNItems(); j++) {
				sum += items.get(j).getCoefficient(i);
			}
			addInSumCoeffsItems(sum);
		}
	}
	
	public boolean addItemKnapsack(Item item) {
		boolean added = false;
		boolean removed = false;
		try {
			item.setInKnapsack(true);
			added = this.inKnapsack.add(item);
			removed = this.outKnapsack.remove(item);
		}
		catch (Exception e) {
			System.err.print(e);
		}
		finally {
			if(added && !removed) {
				this.inKnapsack.remove(item);
				item.setInKnapsack(false);
			}
			if(added && removed) {
				this.setGen(item.getId() - 1, true);
				this.currentProfit += item.getProfit();
				for(int i = 0; i < mDimensions; i++)
					setCurrentWeight(i, getCurrentWeights().get(i) + item.getCoefficient(i));
			}
		}
		return (added && removed);
	}
	
	public boolean delItemKnapsack(Item item) {
		boolean added = false;
		boolean removed = false;
		try {
			item.setInKnapsack(false);
			added = this.outKnapsack.add(item);
			removed = this.inKnapsack.remove(item);
		}
		catch (Exception e) {
			System.err.print(e);
		}
		finally {
			if(!added && removed) {
				this.inKnapsack.remove(item);
				item.setInKnapsack(true);
			}
			if(added && removed) {
				this.setGen(item.getId() - 1, false);
				this.currentProfit -= item.getProfit();
				for(int i = 0; i < mDimensions; i++)
					setCurrentWeight(i, getCurrentWeights().get(i) - item.getCoefficient(i));
			}
		}
		return (added & removed);
	}
	
	public void resetKnapsack(ArrayList<Item> items) {
		this.currentProfit = 0.0;
		this.currentWeights = new ArrayList<Double>(Collections.nCopies(mDimensions, 0.0));		
		this.inKnapsack = new ArrayList<Item>();
		this.outKnapsack = new ArrayList<Item>();
		for(int i = 0; i < nItems; i++) {
			setGen(i, false);
			items.get(i).setInKnapsack(false);
			this.outKnapsack.add(items.get(i));
		}
	}
	
	public void sortItems(ArrayList<Item> items) {
		setSumsCoeffsItems(items);
		this.profitsQueue.addAll(Collections.nCopies(nItems, -1));
		this.weightsQueue.addAll(Collections.nCopies(nItems, -1));
		this.normalizedsQueue.addAll(Collections.nCopies(nItems, -1));
		this.scaledsQueue.addAll(Collections.nCopies(nItems, -1));
		this.generalizedsQueue.addAll(Collections.nCopies(nItems, -1));
		this.fpQueue.addAll(Collections.nCopies(nItems, -1));
		this.stQueue.addAll(Collections.nCopies(nItems, -1));
		int t, u, v, w, x, y, z;
		for(int i = 0; i < nItems; i++) {
			t = 0;
			u = 0;
			v = 0;
			w = 0;
			x = 0;
			y = 0;
			z = 0;
			for(int j = 0; j < nItems; j++) {
				if(i != j) {
					//Ordenar items por Beneficio
					if((items.get(j).getProfit() > items.get(i).getProfit()) ||
							((items.get(j).getProfit() == items.get(i).getProfit())
									&& (j < i)))
							v++;
					//Ordenar items por Peso total
					if((items.get(j).getTotalWeight() > items.get(i).getTotalWeight()) ||
							((items.get(j).getTotalWeight() == items.get(i).getTotalWeight())
									&& (j < i)))
							w++;
					//Ordenar items por razón Beneficio/Peso total
					if(((items.get(j).getProfit() / items.get(j).getTotalWeight()) > (items.get(i).getProfit() / items.get(i).getTotalWeight())) ||
							(((items.get(j).getProfit() / items.get(j).getTotalWeight()) == (items.get(i).getProfit() / items.get(i).getTotalWeight()))
									&& (j < i)))
							x++;
					//Ordenar items por razón Beneficio/Peso total/Capacidad
					if(((items.get(j).getProfit() / items.get(j).getScaledWeight(getConstraints())) > (items.get(i).getProfit() / items.get(i).getScaledWeight(getConstraints()))) ||
							(((items.get(j).getProfit() / items.get(j).getScaledWeight(getConstraints())) == (items.get(i).getProfit() / items.get(i).getScaledWeight(getConstraints())))
									&& (j < i)))
							y++;
					//Ordenar items por razón Beneficio/max(Peso/Capacidad)
					if(((items.get(j).getProfit() / items.get(j).getGeneralizedDensity(getConstraints())) > (items.get(i).getProfit() / items.get(i).getGeneralizedDensity(getConstraints()))) ||
							(((items.get(j).getProfit() / items.get(j).getGeneralizedDensity(getConstraints())) == (items.get(i).getProfit() / items.get(i).getGeneralizedDensity(getConstraints())))
									&& (j < i)))
							z++;
					//Ordenar items por razón Beneficio/(Peso total*(Peso total-Capacidad))
					if(((items.get(j).getProfit() / items.get(j).getSenjuToyoda(getConstraints(), getSumCoeffsItems())) > (items.get(i).getProfit() / items.get(i).getSenjuToyoda(getConstraints(), getSumCoeffsItems()))) ||
							(((items.get(j).getProfit() / items.get(j).getSenjuToyoda(getConstraints(), getSumCoeffsItems())) == (items.get(i).getProfit() / items.get(i).getSenjuToyoda(getConstraints(), getSumCoeffsItems())))
									&& (j < i)))
							t++;
					//Ordenar items por razón Beneficio/(Peso total*ri)
					if(((items.get(j).getProfit() / items.get(j).getFrevillePlateau(getConstraints(), getSumCoeffsItems())) > (items.get(i).getProfit() / items.get(i).getFrevillePlateau(getConstraints(), getSumCoeffsItems()))) ||
							(((items.get(j).getProfit() / items.get(j).getFrevillePlateau(getConstraints(), getSumCoeffsItems())) == (items.get(i).getProfit() / items.get(i).getFrevillePlateau(getConstraints(), getSumCoeffsItems())))
									&& (j < i)))
							u++;
				}
			}
			this.getItemOKL(i).setSTRanking(t);
			this.setProfitQueue(t, i);
			
			this.getItemOKL(i).setFPRanking(u);
			this.setProfitQueue(u, i);
			
			this.getItemOKL(i).setProfitRanking(v);
			this.setProfitQueue(v, i);
			
			this.getItemOKL(i).setWeightRanking(w);
			this.setWeightQueue(w, i);
			
			this.getItemOKL(i).setNormalizedRanking(x);
			this.setNormalizedQueue(x, i);
			
			this.getItemOKL(i).setScaledRanking(y);
			this.setScaledQueue(y, i);
			
			this.getItemOKL(i).setGeneralizedRanking(z);
			this.setGeneralizedQueue(z, i);
		}
	}
	
	public String toString(boolean all) {
		StringBuilder buffer = new StringBuilder("[");
		buffer.append(genToString());
		buffer.append("], KS Profit: ").append(currentProfit).append(", ");
		if(all) {
			buffer.append("\nItems in: ").append(inKnapsack);
			buffer.append("\nItems out: ").append(outKnapsack);
			buffer.append("\nWeights").append(currentWeights);
		}
		return buffer.toString();
	}
}
