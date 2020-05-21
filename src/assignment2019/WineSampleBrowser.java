package assignment2019;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import assignment2019.codeprovided.*;
/**
 * Main method where all parts of program are called.
 * @author Bobby Williams
 *
 */
public class WineSampleBrowser {
	public static void main(String[] args) {
		if (args.length == 0) {
			args = new String[] { "resources/winequality-red.csv", "resources/winequality-white.csv",
					"resources/queries.txt" };
		}
		// Instance Variables
		String redWineFile = args[0];
		String whiteWineFile = args[1];
		String queriesFile = args[2];

		WineSampleCellar cellar = new WineSampleCellar(redWineFile, whiteWineFile, queriesFile);
		//Parts of program to run.
		runGUI(cellar);
		questionAnswers(cellar);
		fileQueries(cellar);

	}
	//Answers to the questions.
	public static void questionAnswers(WineSampleCellar cellar) {
		// Question 1
		System.out.println("THE QUESTIONS \n Question 1 \n All Samples Total: " + cellar.wineSum(WineType.ALL));
		// Question 2
		System.out.println("\n Question 2 \n Red Samples Total: " + cellar.wineSum(WineType.RED));
		// Question 3
		System.out.println("\n Question 3 \n White Samples Total: " + cellar.wineSum(WineType.WHITE));
		// Question 4
		System.out.println("\n Question 4");
		cellar.bestQualityWine(WineType.ALL);
		// Question 5
		System.out.println("\n Question 5");
		cellar.worstQualityWine(WineType.ALL);
		// Question 6
		System.out.println("\n Question 6");
		cellar.highestPH(WineType.ALL);
		// Question 7
		System.out.println("\n Question 7");
		cellar.lowestPH(WineType.ALL);
		// Question 8
		System.out.println(
				"\n Question 8 \n Highest Alcohol Grade for red Is: " + cellar.highestAlcoholContent(WineType.RED));
		// Question9
		System.out.println(
				"\n Question 9 \n Lowest Citric Acid Value for white Is: " + cellar.lowestCitricAcid(WineType.WHITE));
		// Question 10
		System.out.println("\n Question 10 \n Average Alcohol Grade for white Is: "
				+ cellar.averageAlcoholContent(WineType.WHITE) + "\n");
	}
	//Queries
	public static void fileQueries(WineSampleCellar cellar) {
		System.out.println("THE QUERIES");
		cellar.queryResultsFromFile(); // reads the queries from the file, then
		// displays the query results.
	}
	//Start the GUI
	public static void runGUI(WineSampleCellar cellar) {
		JFrame window = new JFrame();
		window.add(new WineSampleBrowserPanel(cellar));
		window.pack();
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}