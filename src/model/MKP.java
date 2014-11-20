package model;

import java.util.ArrayList;
import java.util.Collections;

//import ec.util.Output;

public class MKP {	
	
	/*private static Output output;
	
	public static void setOutput(final Output op) {
		output = op;
	}*/

	//Entrega el valor total de la mochila con los ítems actuales
	/*private static int knapsackProfit(Knapsack ks) {
		int currProfit = 0;
		//Para cada ítem en la mochila
		for(int i = 0; i < ks.getIKL().size(); i++) {
			//Sumar su valor
			currProfit += ks.getItemIKL(i).getProfit();
		}
		return currProfit;
	}*/
	
	//Entrega la lista de pesos usados con los ítems actuales
	private static ArrayList<Double> knapsackWeight(Knapsack ks) {
		ArrayList<Double> weights = new ArrayList<Double>(Collections.nCopies(ks.getMDimensions(), 0.0));
		//Para cada item en la mochila
		for(int i = 0; i < ks.getIKL().size(); i++) {
			//Para cada coeficiente del ítem
			for(int j = 0; j < ks.getItemIKL(i).getCoefficients().size(); j++) {
				weights.set(j, weights.get(j) + ks.getItemIKL(i).getCoefficient(j));
			}
		}
		return weights;
	}
	
	//Verifica si se cumplen las restricciones al agregar un ítem en la mochila
	public static boolean isValid(Item item, Knapsack ks) {
		ArrayList<Double> weightsList = knapsackWeight(ks);
		//Para cada dimensión del ítem
		for(int i = 0; i < weightsList.size(); i++) {
			//Si agregar el ítem a la mochila supera la capacidad de la dimensión
			if((weightsList.get(i) + item.getCoefficient(i)) > ks.getConstraint(i))
				return false;
		}
		return true;
	}
	
	/**************************
	 * 	TERMINALES
	 **************************/
	
	//Terminal que devuelve True si ningún elemento fuera de la mochila cabe en esta
	public static boolean isFull(Knapsack ks){
		//output.println("MKP: isFull", MKProblem.LOG_FILE);
		//Para cada elemento fuera de la mochila
		for(int i = 0; i < ks.getOKL().size(); i++) {
			if(!isValid(ks.getOKL().get(i), ks)) {
				//output.println("IF: The MKP is Full", MKProblem.LOG_FILE);
				return true;
			}
		}
		//output.println("IF: The MKP is not Full", MKProblem.LOG_FILE);
		return false;
	}
	
	
	/*
	 *  ADD FUNCTIONS
	 */
	
	//Terminal que agrega en la mochila el primer elemento encontrado que no esté en la mochila
	public static boolean addFirstFit(Knapsack ks){
		//output.println("MKP: addFirstFit", MKProblem.LOG_FILE);
		//Para cada elemento fuera de la mochila
		for (int index = 0; index < ks.getOKL().size(); index++) {
			//Si es posible agregar el item
			if(isValid(ks.getItemOKL(index), ks)) {
				//output.println("AFF: Adding... " + ks.getItemOKL(index).toString(true), MKProblem.LOG_FILE);
				//Agregarlo y terminar la búsqueda
				ks.addItemKnapsack(ks.getItemOKL(index));
				return true;
			}
			index ++;
		}
		//output.println("AFF: The ítem can't enter in MKP", MKProblem.LOG_FILE);
		return false;
	}

	
	//Terminal que agrega a la mochila el ítem con mejor beneficio que no esté en la mochila
	public static boolean addMaxProfit(Knapsack ks){
		//output.println("MKP: addMaxProfit", MKProblem.LOG_FILE);
		int itemPos = -1;
		int bestRank = Integer.MAX_VALUE;
		//Mientras haya elementos fuera de la mochila
		for (int i = 0; i < ks.getOKL().size(); i++) {
			//Si el ítem actual tiene mejor profit
			if(ks.getItemOKL(i).getProfitRanking() < bestRank) {
				//Se guarda como el mejor ítem rankeado
				bestRank = ks.getItemOKL(i).getProfitRanking();
				itemPos = i;
			}
		}
		//Si se encontró algún item
		if(itemPos != -1) {
			//Si el ítem encontrado cabe en la mochila
			if(isValid(ks.getItemOKL(itemPos), ks)) {
				//output.println("AMP: Adding... " + ks.getItemOKL(itemPos).toString(true), MKProblem.LOG_FILE);
				//Agregarlo en la mochila
				ks.addItemKnapsack(ks.getItemOKL(itemPos));
				return true;
			}
			else {
				//output.println("AMP: The ítem can't enter in MKP", MKProblem.LOG_FILE);
			}
		}
		else {
			//output.println("AMP: No item found...", MKProblem.LOG_FILE);
		}
		return false;
	}
	
	//Terminal que agrega el mejor elemento con ponderación beneficio/peso total que no esté en la mochila
	public static boolean addMaxNormalized(Knapsack ks){
		//output.println("MKP: addMaxNormalized", MKProblem.LOG_FILE);
		int itemPos = -1;
		int bestRank = Integer.MAX_VALUE;
		//Para cada elemento fuera de la mochila
		for (int i = 0; i < ks.getOKL().size(); i++) {
			//Si el ítem actual tiene mejor ranking normalized
			if(ks.getItemOKL(i).getNormalizedRanking() < bestRank) {
				//Se guarda como el mejor ítem rankeado
				bestRank = ks.getItemOKL(i).getNormalizedRanking();
				itemPos = i;
			}
		}
		//Si se encontró algún item
		if(itemPos != -1) {
			//Si el ítem encontrado cabe en la mochila
			if(isValid(ks.getItemOKL(itemPos), ks)) {
				//output.println("AMN: Adding... " + ks.getItemOKL(itemPos).toString(true), MKProblem.LOG_FILE);
				//Agregarlo en la mochila
				ks.addItemKnapsack(ks.getItemOKL(itemPos));
				return true;
			}
			else {
				//output.println("AMN: The ítem can't enter in MKP", MKProblem.LOG_FILE);
			}
		}
		else {
			//output.println("AMN: No item found...", MKProblem.LOG_FILE);
		}
		return false;
	}
	
	//Terminal que agrega el mejor elemento con ponderación beneficio/peso total/capacidad que no esté en la mochila
	public static boolean addMaxScaled(Knapsack ks){
		//output.println("MKP: addMaxScaled", MKProblem.LOG_FILE);
		int itemPos = -1;
		int bestRank = Integer.MAX_VALUE;
		//Para cada elemento fuera de la mochila
		for (int i = 0; i < ks.getOKL().size(); i++) {
			//Si el ítem actual tiene mejor ranking scaled
			if(ks.getItemOKL(i).getScaledRanking() < bestRank) {
				//Se guarda como el mejor ítem rankeado
				bestRank = ks.getItemOKL(i).getScaledRanking();
				itemPos = i;
			}
		}
		//Si se encontró algún item
		if(itemPos != -1) {
			//Si el ítem encontrado cabe en la mochila
			if(isValid(ks.getItemOKL(itemPos), ks)) {
				//output.println("AMS: Adding... " + ks.getItemOKL(itemPos).toString(true), MKProblem.LOG_FILE);
				//Agregarlo en la mochila
				ks.addItemKnapsack(ks.getItemOKL(itemPos));
				return true;
			}
			else {
				//output.println("AMS: The ítem can't enter in MKP", MKProblem.LOG_FILE);
			}
		}
		else {
			//output.println("AMS: No item found...", MKProblem.LOG_FILE);
		}
		return false;
	}
	
	//Terminal que agrega el mejor elemento con ponderación profit/max(peso/capacidad) que no esté en la mochila
	public static boolean addMaxGeneralized(Knapsack ks){
		//output.println("MKP: addMaxGeneralized", MKProblem.LOG_FILE);
		int itemPos = -1;
		int bestRank = Integer.MAX_VALUE;
		//Para cada elemento fuera de la mochila
		for (int i = 0; i < ks.getOKL().size(); i++) {
			//Si el ítem actual tiene mejor ranking generalized
			if(ks.getItemOKL(i).getGeneralizedRanking() < bestRank) {
				//Se guarda como el mejor ítem rankeado
				bestRank = ks.getItemOKL(i).getGeneralizedRanking();
				itemPos = i;
			}
		}
		//Si se encontró algún item
		if(itemPos != -1) {
			//Si el ítem encontrado cabe en la mochila
			if(isValid(ks.getItemOKL(itemPos), ks)) {
				//output.println("AMG: Adding... " + ks.getItemOKL(itemPos).toString(true), MKProblem.LOG_FILE);
				//Agregarlo en la mochila
				ks.addItemKnapsack(ks.getItemOKL(itemPos));
				return true;
			}
			else {
				//output.println("AMG: The ítem can't enter in MKP", MKProblem.LOG_FILE);
			}
		}
		else {
			//output.println("AMG: No item found...", MKProblem.LOG_FILE);
		}
		return false;
	}
	
	//Terminal que agrega el mejor elemento con ponderación profit/(peso total*ri) que no esté en la mochila
	public static boolean addMaxFrevillePlateau(Knapsack ks){
		//output.println("MKP: addMaxFrevillePlateau", MKProblem.LOG_FILE);
		int itemPos = -1;
		int bestRank = Integer.MAX_VALUE;
		//Para cada elemento fuera de la mochila
		for (int i = 0; i < ks.getOKL().size(); i++) {
			//Si el ítem actual tiene mejor ranking fp
			if(ks.getItemOKL(i).getFPRanking() < bestRank) {
				//Se guarda como el mejor ítem rankeado
				bestRank = ks.getItemOKL(i).getFPRanking();
				itemPos = i;
			}
		}
		//Si se encontró algún item
		if(itemPos != -1) {
			//Si el ítem encontrado cabe en la mochila
			if(isValid(ks.getItemOKL(itemPos), ks)) {
				//output.println("AMFP: Adding... " + ks.getItemOKL(itemPos).toString(true), MKProblem.LOG_FILE);
				//Agregarlo en la mochila
				ks.addItemKnapsack(ks.getItemOKL(itemPos));
				return true;
			}
			else {
				//output.println("AMFP: The ítem can't enter in MKP", MKProblem.LOG_FILE);
			}
		}
		else {
			//output.println("AMFP: No item found...", MKProblem.LOG_FILE);
		}
		return false;
	}
		
	//Terminal que agrega el mejor elemento con ponderación profit/(peso total*(peso total-capacidad) que no esté en la mochila
	public static boolean addMaxSenjuToyoda(Knapsack ks){
		//output.println("MKP: addMaxSenjuToyoda", MKProblem.LOG_FILE);
		int itemPos = -1;
		int bestRank = Integer.MAX_VALUE;
		//Para cada elemento fuera de la mochila
		for (int i = 0; i < ks.getOKL().size(); i++) {
			//Si el ítem actual tiene mejor ranking generalized
			if(ks.getItemOKL(i).getSTRanking() < bestRank) {
				//Se guarda como el mejor ítem rankeado
				bestRank = ks.getItemOKL(i).getSTRanking();
				itemPos = i;
			}
		}
		//Si se encontró algún item
		if(itemPos != -1) {
			//Si el ítem encontrado cabe en la mochila
			if(isValid(ks.getItemOKL(itemPos), ks)) {
				//output.println("AMST: Adding... " + ks.getItemOKL(itemPos).toString(true), MKProblem.LOG_FILE);
				//Agregarlo en la mochila
				ks.addItemKnapsack(ks.getItemOKL(itemPos));
				return true;
			}
			else {
				//output.println("AMST: The ítem can't enter in MKP", MKProblem.LOG_FILE);
			}
		}
		else {
			//output.println("AMST: No item found...", MKProblem.LOG_FILE);
		}
		return false;
	}

	
	//Terminal que agrega a la mochila el ítem con mayor peso total
	public static boolean addMaxWeight(Knapsack ks){
		//output.println("MKP: addMaxWeight", MKProblem.LOG_FILE);
		int itemPos = -1;
		int bestRank = Integer.MAX_VALUE;
		//Mientras haya elementos fuera de la mochila
		for (int i = 0; i < ks.getOKL().size(); i++) {
			//Si el ítem actual tiene mayor peso
			if(ks.getItemOKL(i).getWeightRanking() < bestRank) {
				//Se guarda como el mejor ítem rankeado
				bestRank = ks.getItemOKL(i).getWeightRanking();
				itemPos = i;
			}
		}
		//Si se encontró algún item
		if(itemPos != -1) {
			//Si el ítem encontrado cabe en la mochila
			if(isValid(ks.getItemOKL(itemPos), ks)) {
				//output.println("AMaxW: Adding... " + ks.getItemOKL(itemPos).toString(true), MKProblem.LOG_FILE);
				//Agregarlo en la mochila
				ks.addItemKnapsack(ks.getItemOKL(itemPos));
				return true;
			}
			else {
				//output.println("AMaxW: The ítem can't enter in MKP", MKProblem.LOG_FILE);
			}
		}
		else {
			//output.println("AMaxW: No item found...", MKProblem.LOG_FILE);
		}
		return false;
	}
	
	//Terminal que agrega a la mochila el ítem con menor peso total
	public static boolean addMinWeight(Knapsack ks){
		//output.println("MKP: addMinWeight", MKProblem.LOG_FILE);
		int itemPos = -1;
		int worstRank = Integer.MIN_VALUE;
		//Mientras haya elementos fuera de la mochila
		for (int i = 0; i < ks.getOKL().size(); i++) {
			//Si el ítem actual tiene menor peso
			if(ks.getItemOKL(i).getWeightRanking() > worstRank) {
				//Se guarda como el mejor ítem rankeado
				worstRank = ks.getItemOKL(i).getWeightRanking();
				itemPos = i;
			}
		}
		//Si se encontró algún item
		if(itemPos != -1) {
			//Si el ítem encontrado cabe en la mochila
			if(isValid(ks.getItemOKL(itemPos), ks)) {
				//output.println("AMinW: Adding... " + ks.getItemOKL(itemPos).toString(true), MKProblem.LOG_FILE);
				//Agregarlo en la mochila
				ks.addItemKnapsack(ks.getItemOKL(itemPos));
				return true;
			}
			else {
				//output.println("AMinW: The ítem can't enter in MKP", MKProblem.LOG_FILE);
			}
		}
		else {
			//output.println("AMinW: No item found...", MKProblem.LOG_FILE);
		}
		return false;
	}
	
	/*
	 *	DELETE FUNCTIONS 
	 */
	
	//Terminal que elimina de la mochila el elemento con peor beneficio
	public static boolean delMinProfit(Knapsack ks){
		//output.println("MKP: delMinProfit", MKProblem.LOG_FILE);
		int itemPos = -1;
		int worstRank = Integer.MIN_VALUE;
		//Mientras haya elementos en la mochila
		for (int i = 0; i < ks.getIKL().size(); i++) {
			//Si el ítem actual tiene peor ranking de beneficio que el actual
			if(ks.getItemIKL(i).getProfitRanking() > worstRank) {
				//Se guarda como el peor ítem rankeado
				worstRank = ks.getItemIKL(i).getProfitRanking();
				itemPos = i;
			}
		}
		//Si se encontró algún item
		if(itemPos != -1) {
			//output.println("DMP: Deleting... " + ks.getItemIKL(itemPos).toString(true), MKProblem.LOG_FILE);
			//Sacarlo de la mochila
			ks.delItemKnapsack(ks.getItemIKL(itemPos));
			return true;
		}
		else
			//output.println("DMP: No items in the MKP", MKProblem.LOG_FILE);
		return false;
	}
	
	//Terminal que elimina de la mochila el elemento con peor ponderación beneficio/peso total
	public static boolean delMinNormalized(Knapsack ks){
		//output.println("MKP: delMinNormalized", MKProblem.LOG_FILE);
		int itemPos = -1;
		int worstRank = Integer.MIN_VALUE;
		//Mientras haya elementos en la mochila
		for (int i = 0; i < ks.getIKL().size(); i++) {
			//Si el ítem actual tiene peor ranking de densidad beneficio/peso total que el actual
			if(ks.getItemIKL(i).getNormalizedRanking() > worstRank) {
				//Se guarda como el peor ítem rankeado
				worstRank = ks.getItemIKL(i).getNormalizedRanking();
				itemPos = i;
			}
		}
		//Si se encontró algún item
		if(itemPos != -1) {
			//output.println("DMN: Deleting... " + ks.getItemIKL(itemPos).toString(true), MKProblem.LOG_FILE);
			//Sacarlo de la mochila
			ks.delItemKnapsack(ks.getItemIKL(itemPos));
			return true;
		}
		else {
			//output.println("DMN: No items in the MKP", MKProblem.LOG_FILE);
		}
		return false;
	}
	
	//Terminal que elimina de la mochila el elemento con peor ponderación beneficio/peso total/capacidad
	public static boolean delMinScaled(Knapsack ks){
		//output.println("MKP: delMinScaled", MKProblem.LOG_FILE);
		int itemPos = -1;
		int worstRank = Integer.MIN_VALUE;
		//Mientras haya elementos en la mochila
		for (int i = 0; i < ks.getIKL().size(); i++) {
			//Si el ítem actual tiene peor ranking de densidad beneficio/peso total/capacidad que el actual
			if(ks.getItemIKL(i).getScaledRanking() > worstRank) {
				//Se guarda como el peor ítem rankeado
				worstRank = ks.getItemIKL(i).getScaledRanking();
				itemPos = i;
			}
		}
		//Si se encontró algún item
		if(itemPos != -1) {
			//output.println("DMS: Deleting... " + ks.getItemIKL(itemPos).toString(true), MKProblem.LOG_FILE);
			//Sacarlo de la mochila
			ks.delItemKnapsack(ks.getItemIKL(itemPos));
			return true;
		}
		else {
			//output.println("DMS: No items in the MKP", MKProblem.LOG_FILE);
		}
		return false;
	}
	
	//Terminal que elimina de la mochila el elemento con mayor peso total
	public static boolean delMaxWeight(Knapsack ks){
		//output.println("MKP: delMaxWeight", MKProblem.LOG_FILE);
		int itemPos = -1;
		int bestRank = Integer.MAX_VALUE;
		//Mientras haya elementos en la mochila
		for (int i = 0; i < ks.getIKL().size(); i++) {
			//Si el ítem actual tiene mejor ranking de peso total que el actual
			if(ks.getItemIKL(i).getNormalizedRanking() < bestRank) {
				//Se guarda como el peor ítem rankeado
				bestRank = ks.getItemIKL(i).getNormalizedRanking();
				itemPos = i;
			}
		}
		//Si se encontró algún item
		if(itemPos != -1) {
			//output.println("DMW: Deleting... " + ks.getItemIKL(itemPos).toString(true), MKProblem.LOG_FILE);
			//Sacarlo de la mochila
			ks.delItemKnapsack(ks.getItemIKL(itemPos));
			return true;
		}
		else {
			//output.println("DMW: No items in the MKP", MKProblem.LOG_FILE);
		}
		return false;
	}
}