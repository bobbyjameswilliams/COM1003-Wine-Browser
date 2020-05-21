package assignment2019;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import assignment2019.codeprovided.*;
/**
 * methods for use with parsing queries from text files
 * @author Bobby Williams
 *
 */
public class QueryParser {
	private List<String> queryWords;
	private List<QueryCondition> conditions = new ArrayList<QueryCondition>();
	private List<Query> queries = new ArrayList<Query>();
	private String[] conditionParam = new String[3];
	private Map<WineType, List<WineSample>> wineList;

	public QueryParser(List<String> queryWords, Map<WineType, List<WineSample>> wineList) {
		this.queryWords = queryWords;
		this.wineList = wineList;
	}

	// For use in GUI to access the strIsNumeric method
	public QueryParser() {
	}
	//Takes tokenised input and parses them into Query objects
	public List<Query> parse() {
		List<WineType> wineTypes = new ArrayList<WineType>();
		Boolean whereFound = false;
		for (String word : queryWords) {
			//Starts new query
			if (word.equals("select") && whereFound == true) {
				queryPackager(wineTypes);
				wineTypes.clear();
				conditions.clear();
				whereFound = false;
			}
			//Wine Tyoe
			else if (word.equals("white")) {
				wineTypes.add(WineType.WHITE);
			} else if (word.equals("red")) {
				wineTypes.add(WineType.RED);
			}
			//Conditions
			else if (word.equals("where")) {
				whereFound = true;
				Arrays.fill(conditionParam, null);
			}
			else if (WineProperty.fromFileIdentifier(word) != null) {
				conditionParam[0] = word;
			} else if (isOperator(word)) {
				conditionParam[1] = word;
			} else if (strIsNumeric(word)) {
				conditionParam[2] = word;
				conditionPackager(conditionParam);
			} else if (isConcatenated(word)) {
				String[] separatedString = concatenatedSeparator(word);
				conditionParam[1] = separatedString[0];
				conditionParam[2] = separatedString[1];
				conditionPackager(conditionParam);
			}

		}
		queryPackager(wineTypes);
		return queries;
	}

	//Returns true if a token is an unseparated operator and value
	public boolean isConcatenated(String word) {
		boolean isConcatenated = false;
		for (int i = 1; i < 3; i++) {
			if (isOperator(word.substring(0, i))) {
				if (i != (word.length())) {
					isConcatenated = true;
				}
			}
		}
		return isConcatenated;
	}
	
	//Separates tokens into  operator and value and populates their space in the array
	public String[] concatenatedSeparator(String word) {
		String[] tuple = new String[2];
		for (int i = 1; i < 3; i++) {
			if (isOperator(word.substring(0, i))) {
				tuple[0] = word.substring(0, i);
				tuple[1] = word.substring(i, word.length());
			}
		}
		return tuple;
	}
	
	//returns true if token is operator
	public boolean isOperator(String word) {
		switch (word) {
		case ">":
			return true;
		case ">=":
			return true;
		case "<":
			return true;
		case "<=":
			return true;
		case "=":
			return true;
		case "!=":
			return true;
		}
		return false;
	}
	
	//returns true if token is numeric
	public boolean strIsNumeric(String word) {
		return word != null && word.matches("[-+]?\\d*\\.?\\d+");
	}
	
	//Takes conditions already produced and packages them into QueryCondition object, adds to list.
	public void conditionPackager(String[] conditionParam) {

		conditions.add(new QueryCondition(WineProperty.fromFileIdentifier(conditionParam[0]), conditionParam[1],
				Double.parseDouble(conditionParam[2])));
	}
	
	//Using all Query Conditions, packages into Query object.
	public void queryPackager(List<WineType> type) {

		List<QueryCondition> conditionsCopy = new ArrayList<QueryCondition>();
		conditionsCopy.addAll(conditions);

		if (type.contains(WineType.RED) && type.contains(WineType.WHITE)) {
			queries.add(new Query(wineList.get(WineType.ALL), conditionsCopy, WineType.ALL));
		} else if (type.contains(WineType.RED)) {
			queries.add(new Query(wineList.get(WineType.RED), conditionsCopy, WineType.RED));
		} else if (type.contains(WineType.WHITE)) {
			queries.add(new Query(wineList.get(WineType.WHITE), conditionsCopy, WineType.WHITE));
		}
	}

}
