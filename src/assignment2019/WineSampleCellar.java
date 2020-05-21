package assignment2019;
/**
 * main handler for the wines and queries.
 * @author Bobby Williams
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import assignment2019.codeprovided.*;

public class WineSampleCellar extends AbstractWineSampleCellar {

	private String queryFilename;

	public WineSampleCellar(String redWineFilename, String whiteWineFilename, String queryFilename) {
		super(redWineFilename, whiteWineFilename, queryFilename);
		this.queryFilename = queryFilename;

	}

	//Displays the results of the queries from the text file.
	public void queryResultsFromFile() {
		List<Query> readInQueries = new ArrayList<Query>();
		readInQueries.addAll(readQueries(readQueryFile(queryFilename)));
		for (Query indivQuery : readInQueries) {
			displayQueryResults(indivQuery);
		}
	}
	//Calls parser and returns list of queries from text file.
	public List<Query> readQueries(List<String> queryList) {
		QueryParser parser = new QueryParser(queryList, wineSampleRacks);
		List<Query> queryL = parser.parse();
		return queryL;
	}
	
	
	public void updateCellar() {
		// Take the two lists of red and white wines, combine them, then call add an
		// entry in the hashmap
		List<WineSample> allSamples = new ArrayList<WineSample>();
		allSamples.addAll(wineSampleRacks.get(WineType.RED));
		allSamples.addAll(wineSampleRacks.get(WineType.WHITE));
		wineSampleRacks.put(WineType.ALL, allSamples);
	}

	//Displays a query result to the console.
	public void displayQueryResults(Query query) {
		System.out.println(
				"################### QUERY #############################################################################");
		List<WineSample> solvedQuery = new ArrayList<WineSample>();
		solvedQuery.addAll(query.solveQuery());
		System.out.print("For The Request for " + query.getWineType() + " wines with the conditions ");
		for (QueryCondition condition : query.getQueryConditionList()) {
			System.out.print(condition + ", ");
		}
		System.out.println(" The results were:");

		System.out.println("(" + solvedQuery.size() + " Result(s))");
		for (WineSample sample : solvedQuery) {

			displayWineSample(sample);
		}
		System.out.println(
				"#######################################################################################################");
		System.out.print("\n");
	}
	//Displays a wine sample to the console.
	public void displayWineSample(WineSample sample) {
		String[] winePropertyList = { "f_acid", "v_acid", "c_acid", "r_sugar", "chlorid", "f_sulf", "t_sulf", "dens",
				"ph", "sulph", "alc", "qual" };
		System.out.print("[ Wine: " + sample.getType() + " ID: " + sample.getId() + ", ");
		for (int i = 0; i < winePropertyList.length; i++) {
			System.out.print(winePropertyList[i] + ": "
					+ sample.getProperty(WineProperty.fromFileIdentifier(winePropertyList[i])) + " ");
		}
		System.out.print("]" + "\n");
	}
	//returns a query which when solved will return a list of the most or least of a given property.
	//by retrieving the value from the query condition, a max or min value can be found.
	public Query extremitiesQuery(Extremity ex, WineType wineType, WineProperty wineProperty,
			List<WineSample> sampleList) {
		WineSample firstElement = null;
		Query query;
		switch (wineProperty) {
		case Quality:
			sampleList.sort(Comparator.comparing(WineSample::getQuality));
			break;
		case PH:
			sampleList.sort(Comparator.comparing(WineSample::getpH));
			break;
		case Alcohol:
			sampleList.sort(Comparator.comparing(WineSample::getAlcohol));
			break;
		case Chlorides:
			sampleList.sort(Comparator.comparing(WineSample::getChlorides));
			break;
		case CitricAcid:
			sampleList.sort(Comparator.comparing(WineSample::getCitricAcid));
			break;
		case Density:
			sampleList.sort(Comparator.comparing(WineSample::getDensity));
			break;
		case FixedAcidity:
			sampleList.sort(Comparator.comparing(WineSample::getFixedAcidity));
			break;
		case FreeSulfurDioxide:
			sampleList.sort(Comparator.comparing(WineSample::getFreeSulfurDioxide));
			break;
		case ResidualSugar:
			sampleList.sort(Comparator.comparing(WineSample::getResidualSugar));
			break;
		case Sulphates:
			sampleList.sort(Comparator.comparing(WineSample::getSulphates));
			break;
		case TotalSulfurDioxide:
			sampleList.sort(Comparator.comparing(WineSample::getTotalSulfurDioxide));
			break;
		case VolatileAcidity:
			sampleList.sort(Comparator.comparing(WineSample::getVolatileAcidity));
			break;
		default:
			break;
		}

		if (!sampleList.isEmpty() && sampleList.size() > 0 && ex == Extremity.LOW) {
			firstElement = sampleList.get(0);
		} else if (!sampleList.isEmpty() && sampleList.size() > 0 && ex == Extremity.HIGH) {
			firstElement = sampleList.get(sampleList.size() - 1);
		}
		List<QueryCondition> condition = new ArrayList<QueryCondition>();
		condition.add(new QueryCondition(wineProperty, "=", firstElement.getProperty(wineProperty)));
		query = new Query(sampleList, condition, wineType);
		return query;
	}
	
	public List<WineSample> bestQualityWine(WineType wineType) {
		System.out.println("Best Quality Wine (see condition for value)");
		Query query = extremitiesQuery(Extremity.HIGH, wineType, WineProperty.Quality, wineSampleRacks.get(wineType));
		displayQueryResults(query);
		return query.solveQuery();
	}

	public List<WineSample> worstQualityWine(WineType wineType) {
		System.out.println("Worst Quality Wine (see condition for value)");
		Query query = extremitiesQuery(Extremity.LOW, wineType, WineProperty.Quality, wineSampleRacks.get(wineType));
		displayQueryResults(query);
		return query.solveQuery();
	}

	public List<WineSample> highestPH(WineType wineType) {
		System.out.println("Highest Ph Wine (see condition for value)");
		Query query = extremitiesQuery(Extremity.HIGH, wineType, WineProperty.PH, wineSampleRacks.get(wineType));
		displayQueryResults(query);
		return query.solveQuery();

	}

	public List<WineSample> lowestPH(WineType wineType) {
		System.out.println("Lowest Ph Wine (see condition for value)");
		Query query = extremitiesQuery(Extremity.LOW, wineType, WineProperty.PH, wineSampleRacks.get(wineType));
		displayQueryResults(query);
		return query.solveQuery();

	}

	public double highestAlcoholContent(WineType wineType) {
		List<WineSample> sampleList = new ArrayList<WineSample>();
		WineSample firstElement = null;
		sampleList.addAll(wineSampleRacks.get(wineType));
		sampleList.sort(Comparator.comparing(WineSample::getAlcohol));
		if (!sampleList.isEmpty() && sampleList.size() > 0) {
			firstElement = sampleList.get(sampleList.size() - 1);
		}
		return firstElement.getAlcohol();
	}

	public double lowestCitricAcid(WineType wineType) {
		List<WineSample> sampleList = new ArrayList<WineSample>();
		WineSample firstElement = null;
		sampleList.addAll(wineSampleRacks.get(wineType));
		sampleList.sort(Comparator.comparing(WineSample::getCitricAcid));
		if (!sampleList.isEmpty() && sampleList.size() > 0) {
			firstElement = sampleList.get(0);
		}

		return firstElement.getCitricAcid();
	}

	public double averageAlcoholContent(WineType wineType) {
		List<WineSample> sampleList = new ArrayList<WineSample>();
		sampleList.addAll(wineSampleRacks.get(wineType));
		double entrySum = 0;
		for (WineSample sample : sampleList) {
			entrySum += sample.getAlcohol();
		}
		return entrySum / sampleList.size();
	}

	public int wineSum(WineType wineType) {
		int sum = wineSampleRacks.get(wineType).size();
		return sum;
	}
}
