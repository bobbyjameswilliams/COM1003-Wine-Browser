package assignment2019;

/**
 * 
 * @author Bobby Williams
 * Visual GUI for the browser.
 */
import assignment2019.codeprovided.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WineSampleBrowserPanel extends AbstractWineSampleBrowserPanel {
	String[] headerRow = { "Fixed Acidity", "Volatile Acidity", "Citric Acidity", "Residual Sugar", "Chlorides",
			"FreeSulfDio", "TotalSulfDio", "Density", "pH", "Sulphates", "Alcohol", "Quality" };
	private List<Query> queries = new ArrayList<Query>();
	private WineSampleCellar cellar;
	final Double toD = 100000d;

	// Constructor
	public WineSampleBrowserPanel(AbstractWineSampleCellar cellar) {
		super(cellar);
		this.cellar = (WineSampleCellar) cellar;
		executeQuery();
	}

	// instantiates new handler and adds appropriate
	public void addListeners() {
		EventHandler handler = new EventHandler();
		buttonAddFilter.addActionListener(handler);
		buttonClearFilters.addActionListener(handler);
		comboWineTypes.addActionListener(handler);
	}

	// New eventhandler class to add listeners to from components above
	private class EventHandler implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (event.getSource() == buttonAddFilter) {
				addFilter();
				executeQuery();
			} else if (event.getSource() == buttonClearFilters) {
				clearFilters();
				executeQuery();
			} else if (event.getSource() == comboWineTypes) {
				executeQuery();
			}
		}
	}
	//CitricAcidity
	// When filter button is pressed, adds a filter to the list and executes query
	// with added condition
	public void addFilter() {
		QueryParser numChck = new QueryParser();
		String enumName = (String) comboProperties.getSelectedItem();
		//due to inconsistency in name of enum and name in gui
		if (enumName.equals("Citric Acidity")) {
			enumName = "CitricAcid";
		}
		// Check if value is numeric, if not, disallowed and error messaged displayed.
		if (numChck.strIsNumeric(value.getText())) {
			QueryCondition conditionToAdd = new QueryCondition(WineProperty.valueOf(enumName.replaceAll("\\s", "")),
					(String) comboOperators.getSelectedItem(), Math.round(Double.parseDouble(value.getText())* toD)/toD);
			// Checks to see if the condition is already included.
			if (!includesCondition(conditionToAdd)) {
				queryConditionList.add(conditionToAdd);
				updateConditions();
			} else if (includesCondition(conditionToAdd)) {
				// If the condition is applied already, it is disallowed and error displayed.
				queryBoxError("Condition Already Applied");
			}

		} else {
			value.setText("INVALID");
		}
	}

	// Clear filters and updates the conditions.
	public void clearFilters() {
		queryConditionList.clear();
		updateConditions();
	}

	// Updates the displayed wine list with the query object.
	public void updateWineList() {
		String output = "";
		output = output.concat("WineType\t");
		// Outputs header
		for (int i = 0; i < 12; i++) {
			output = output.concat(headerRow[i] + "\t");
		}
		// If list in abstract method was accurate to the properties, this would be a
		// loop. indicidually retrieves and displays
		// properties from sample list.
		for (WineSample sample : filteredWineSampleList) {
			output = output.concat("\n" + sample.getType() + "\t" + Math.round(sample.getFixedAcidity() * toD) / toD
					+ "\t" + Math.round(sample.getVolatileAcidity() * toD) / toD + "\t"
					+ Math.round(sample.getCitricAcid() * toD) / toD + "\t"
					+ Math.round(sample.getResidualSugar() * toD) / toD + "\t"
					+ Math.round(sample.getChlorides() * toD) / toD + "\t"
					+ Math.round(sample.getFreeSulfurDioxide() * toD) / toD + "\t"
					+ Math.round(sample.getTotalSulfurDioxide() * toD) / toD + "\t"
					+ Math.round(sample.getDensity() * toD) / toD + "\t" + Math.round(sample.getpH() * toD) / toD + "\t"
					+ Math.round(sample.getSulphates() * toD) / toD + "\t" + Math.round(sample.getAlcohol() * toD) / toD
					+ "\t" + Math.round(sample.getQuality() * toD) / toD + "\t");
		}

		filteredWineSamplesTextArea.setText(output);
		filteredWineSamplesTextArea.setCaretPosition(0);
	}

	// Executes the query and calls for stats and list to be recalculated.
	public void executeQuery() {
		queries.clear();
		filteredWineSampleList.clear();
		WineType wineType = WineType.valueOf((String) comboWineTypes.getSelectedItem());
		// Depending on what winetype depends on how list is displayed
		// If no conditions, display all the wines.
		if (queryConditionList.isEmpty()) {
			if (comboWineTypes.getSelectedItem().equals("ALL")) {
				// "ALL" separated into lists of red and white.
				filteredWineSampleList.addAll(cellar.getWineSampleList(WineType.RED));
				filteredWineSampleList.addAll(cellar.getWineSampleList(WineType.WHITE));
			} else {
				filteredWineSampleList.addAll(cellar.getWineSampleList(wineType));
			}
		} else {
			// if are conditions, display list after filter applied.
			if (comboWineTypes.getSelectedItem().equals("ALL")) {
				// "ALL" separated into lists of red and white.
				queries.add(new Query(cellar.getWineSampleList(WineType.RED), queryConditionList, WineType.RED));
				queries.add(new Query(cellar.getWineSampleList(WineType.WHITE), queryConditionList, WineType.WHITE));
				for (Query query : queries) {
					filteredWineSampleList.addAll(query.solveQuery());
				}
			} else {
				Query query = new Query(cellar.getWineSampleList(wineType), queryConditionList, wineType);
				filteredWineSampleList = query.solveQuery();
			}
		}
		updateWineList();
		updateStatistics();
	}

	// displays the conditions to the user.
	public void updateConditions() {
		String toOutput = "";
		for (QueryCondition qCondition : queryConditionList) {
			toOutput += qCondition + "; ";
		}
		queryConditionsTextArea.setText(toOutput);
	}

	// Error is parsed in param and is displayed at the end of whatever is in the
	// query box.
	public void queryBoxError(String error) {
		updateConditions();
		queryConditionsTextArea.setText(queryConditionsTextArea.getText() + " " + error);
	}

	public Boolean includesCondition(QueryCondition condition) {
		for (QueryCondition lCondition : queryConditionList) {
			if (condition.toString().equals(lCondition.toString())) {
				return true;
			}
		}
		return false;
	}

	// Updates and displays statistics
	public void updateStatistics() {
		String output = "\t";
		for (int i = 0; i < 12; i++) {
			output = output.concat(headerRow[i] + "\t");
		}
		List<Extremity> property = new ArrayList<Extremity>();
		property.add(Extremity.HIGH);
		property.add(Extremity.LOW);
		property.add(Extremity.AVERAGE);
		if (!filteredWineSampleList.isEmpty()) {
			for (Extremity p : property) {
				// As the list given in the abstract class does not match the property names, i
				// was unable to produce a loop.
				output = output.concat(
						"\n" + p + "\t" + Math.round(statisticCalculator(WineProperty.FixedAcidity, p) * toD) / toD
								+ "\t" + Math.round(statisticCalculator(WineProperty.VolatileAcidity, p) * toD) / toD
								+ "\t" + Math.round(statisticCalculator(WineProperty.CitricAcid, p) * toD) / toD + "\t"
								+ Math.round(statisticCalculator(WineProperty.ResidualSugar, p) * toD) / toD + "\t"
								+ Math.round(statisticCalculator(WineProperty.Chlorides, p) * toD) / toD + "\t"
								+ Math.round(statisticCalculator(WineProperty.FreeSulfurDioxide, p) * toD) / toD + "\t"
								+ Math.round(statisticCalculator(WineProperty.TotalSulfurDioxide, p) * toD) / toD + "\t"
								+ Math.round(statisticCalculator(WineProperty.Density, p) * toD) / toD + "\t"
								+ Math.round(statisticCalculator(WineProperty.PH, p) * toD) / toD + "\t"
								+ Math.round(statisticCalculator(WineProperty.Sulphates, p) * toD) / toD + "\t"
								+ Math.round(statisticCalculator(WineProperty.Alcohol, p) * toD) / toD + "\t"
								+ Math.round(statisticCalculator(WineProperty.Quality, p) * toD) / toD + "\t");
			}
		} else {
			output = output.concat("\t");
			for (Extremity p : property) {
				output = output.concat("\n" + p + "\t");
				for (int i = 0; i < 12; i++) {
					output = output.concat(0 + "\t");
				}
			}
		}
		int sampleSize;
		if (comboWineTypes.getSelectedItem() == "ALL") {
			sampleSize = cellar.getWineSampleCount(WineType.RED)+ cellar.getWineSampleCount(WineType.WHITE);
		}
		else {
			sampleSize = cellar.getWineSampleCount(WineType.valueOf((String) comboWineTypes.getSelectedItem()));
		}
		output = output.concat("\n" + filteredWineSampleList.size() + " out of "
				+ sampleSize);
		statisticsTextArea.setText(output);
		statisticsTextArea.setCaretPosition(0);
	}

	// Takes the property and extremity (avg,max,min), returns result to be
	// displayed.
	public double statisticCalculator(WineProperty property, Extremity extremity) {
		Query query;
		List<QueryCondition> condition = new ArrayList<QueryCondition>();
		// Using extremitiesQuery and the value from the query returned, max and min can
		// be found of a property from the list.
		if (extremity == Extremity.HIGH) {
			query = cellar.extremitiesQuery(Extremity.HIGH, WineType.valueOf((String) comboWineTypes.getSelectedItem()),
					property, filteredWineSampleList);
			condition = query.getQueryConditionList();
			return condition.get(0).getValue();
		} else if (extremity == Extremity.LOW) {
			query = cellar.extremitiesQuery(Extremity.LOW, WineType.valueOf((String) comboWineTypes.getSelectedItem()),
					property, filteredWineSampleList);
			condition = query.getQueryConditionList();
			return condition.get(0).getValue();
		}
		// Calculates Average
		else if (extremity == Extremity.AVERAGE) {
			double entrySum = 0;
			for (WineSample sample : filteredWineSampleList) {
				entrySum += sample.getProperty(property);
			}
			return entrySum / filteredWineSampleList.size();
		} else
			return -1;

	}

}