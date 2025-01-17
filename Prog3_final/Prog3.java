
/* 
 * This is a helper for understanding get into oracle created by dr. Mccann
 * At the time of this writing, the version of Oracle is 11.2g, and
 * the Oracle JDBC driver can be found at
 *   /opt/oracle/product/10.2.0/client/jdbc/lib/ojdbc14.jar
 * on the lectura system in the UofA CS dept.
 * (Yes, 10.2, not 11.2.  It's the correct jar file but in a strange location.)
 *
 * To compile and execute this program on lectura:
 *
 *   Add the Oracle JDBC driver to your CLASSPATH environment variable:
 *
 *         export CLASSPATH=/opt/oracle/product/10.2.0/client/jdbc/lib/ojdbc14.jar:${CLASSPATH}
 *
 *     (or whatever shell variable set-up you need to perform to add the
 *     JAR file to your Java CLASSPATH)
 *
 *   Compile this file:
 *
 *         javac JDBC.java
 *
 *   Finally, run the program:
 *
 *         java JDBC <oracle username> <oracle password>
 *
 * Author:  L. McCann (2008/11/19; updated 2015/10/28)
 */
import java.io.*;
import java.sql.*; // For access to the SQL interaction methods
import java.util.Scanner;

/*=============================================================================
 |   Assignment: Program # 3
 |    File Name: Prog3.java
 |       Author: Lu Ye
 |
 |       Course: CSc 460 
 |   Instructor: L. McCann
 | Sect. Leader: Yawen Chen and Jacob Combs
 |     Due Date: Apirl 5th, 2017, at the beginning of class
 |
 |     Language:  Java
 |     Packages:  java.io
 |
 +-----------------------------------------------------------------------------
 |	Description:  
 |		Embedding SQL within another programming language is nice for applications that require more
 |		complex calculations or manipulations than plain SQL can handle. Many DBMSes have add-ons that can be
 | 		used for developing nice applications, but even they may not be flexible enough to create the application you
 |		have in mind.
 |		As many of you know from being educated in Arizona. the state has used the Arizona Instrument to Measure
 |		Standards (AIMS) test to assess educational progress of grade-school students in recent years. Starting with
 |		the 2010 year and continuing through 2014, the Arizona Department of Education posted school-by-school
 |      results on its web site as Microsoft Excel (.xls) files. Its easy to use a spreadsheet program to save those files
 |      to CSV (comma-separated value) text format, which is a good format for reading into a program, and from
 |      there inserting into a database.
 |
 |	Techniques:  The program have steps are as follows:
 |   	1. using Libra office to change xls to csv file
 |	   	2. Using srubData I created to clean up data and use load.clt to import dataRead
 |     	3. Prog3 is a JDBC based program to connect to oracle 
 |	    4. run program to query the queries in database (including five tables I created)
 |
 |   Known Bugs:  None
 |
 *===========================================================================*/

public class Prog3 {
	// primary key (School_EntityID) for this program
	public static void main(String[] args) throws SQLException, IOException {

		final String oracleURL = // Magic lectura -> aloe access spell
				"jdbc:oracle:thin:@aloe.cs.arizona.edu:1521:oracle";

		String username = "luye", // Oracle DBMS username
				password = "a2575"; // Oracle DBMS password

		// if (args.length == 2) { // get username/password from cmd line args
		// username = args[0];
		// password = args[1];
		// } else {
		// System.out.println("\nUsage: java JDBC <username> <password>\n"
		// + " where <username> is your Oracle DBMS" + " username,\n and
		// <password> is your Oracle"
		// + " password (not your system password).\n");
		// System.exit(-1);
		// }

		// load the (Oracle) JDBC driver by initializing its base
		// class, 'oracle.jdbc.OracleDriver'.
		try {
			Class.forName("oracle.jdbc.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.err.println("*** ClassNotFoundException:  " + "Error loading Oracle JDBC driver.  \n"
					+ "\tPerhaps the driver is not on the Classpath?");
			System.exit(-1);
		}

		// make and return a database connection to the user's
		// Oracle database

		Connection dbconn = null;
		try {
			dbconn = DriverManager.getConnection(oracleURL, username, password);
		} catch (SQLException e) {
			System.err.println("*** SQLException:  " + "Could not open JDBC connection.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);
		}

		// Send the query to the DBMS, and get and display the results
		String mainQuery = "Main menu: \n" + "q1: Find High School in the list... \n"
				+ "q2: Display Charter School...\n" + "q3: Based on County in 2014, display top ten school...\n"
				+ "q4: Display School name in Pima area...\n"
				+ "Please Type q1, q2, q3 or q4 for specific query. Or you can input \"quit\" to end the program.\n"
				+ "=========================================================================================================";
		Scanner keyboard = new Scanner(System.in); // your input
		boolean working = true;
		while (working) {
			System.out.println(mainQuery);
			switch (keyboard.next()) {
			case "q1":// Send the query to the DBMS, and get and display the
						// results for query1
				// to do case 1 for query 1
				System.out.println("How many High Schools are listed in the results? Choose a year from 2010 to 2014.\n"
						+ "Or you can input quit1 and go back to main menu.");
				while (keyboard.hasNext()) {
					String year = keyboard.next();
					if (year.equals("quit1")) {
						break;
					}
					if (year.equals("2010") || year.equals("2011") || year.equals("2012") || year.equals("2013")
							|| year.equals("2014")) {
						Query1(dbconn, year);
					} else {
						System.out.println(
								"You input a wrong year or wrong command. Please reinput or Input \"quit1\" and go back to main menu.");
						System.out.println("============================");
					}
				}
				break;
			case "q2":// Send the query to the DBMS, and get and display the
						// results for query2
				System.out.println("For each of the five years, display the num of charter schools and how many of"
						+ "them had a sum of the Math percentages \"Falls Far Below\" and \"Approaches\" that was"
						+ "less than the percent \"Passing\"\n" + "You can quit2 to end query Q2.");
				Query2(dbconn);
				System.out.println();
				System.out.println("Input \"quit2\" and go back to main menu.");
				System.out.println("============================");
				while (keyboard.hasNext()) {// check the keyboard scanner
					if (keyboard.next().equals("quit2")) {
						break;// quit query2
					} else {
						System.out.println("Input \"quit2\" and go back to main menu.");
						System.out.println("============================");

					}
				}
				break;
			case "q3":// Send the query to the DBMS, and get and display the
						// results for query3
				System.out.println("For each county in 2014, which 10 schools had the greatest differences between the"
						+ "Passing percentages in reading and writing? Display one table for each county that includes position number."
						+ "'1' for the school with biggest difference and duplicate position numbers for ties.");
				Query3(dbconn);// run query3
				System.out.println();
				System.out.println("Input \"quit3\" and go back to main menu.");
				System.out.println("============================");
				while (keyboard.hasNext()) {// check the keyboard scanner
					if (keyboard.next().equals("quit3")) {
						break;// quit query3
					} else {
						System.out.println("Input \"quit3\" and go back to main menu.");
						System.out.println("============================");

					}
				}
				break;
			case "q4":// Send the query to the DBMS, and get and display the
						// results for query4
				// to do case 4 for query 4
				System.out.println("Choose whether a Charter School or not (Input Y or N, both Capital), to "
						+ "see how many school had an increased trend on Reading passing percentage for"
						+ "these three years(from 2010 to 2012).");
				while (keyboard.hasNext()) {// check the keyboard scanner
					String YorN = keyboard.next();
					if (YorN.equals("quit4")) {
						break;// quit query4
					}
					if (YorN.equals("Y") || YorN.equals("N")) {// check your
																// input Y or N
						Query4(dbconn, YorN);
						System.out.println("Keep inserting Y or N. Or insert \"quit4\" to go back main menu.");
						System.out.println("============================");
					} else {// instruction help
						System.out.println(
								"You input a wrong command. Please reinput or Input \"quit4\" and go back to main menu.");
						System.out.println("============================");
					}
				}
				break;
			case "quit":// terminate the project
				working = false;
				break;
			default:// instruction by default
				System.out.println("You insert wrong command, follow the instruction. \n"
						+ "Please Type q1, q2, q3 or q4 for specific query.\n Or input quit to end.");
			}
		}

	}

	/*
	 * =========================================================================
	 * Function name: private static void Query4(Connection dbconn, String yorN)
	 * Description: This is a simple help method designed by the author and it
	 * is asking you Choose whether a Charter School or not (Input Y or N, both
	 * Capital),
	 * " For School Entity ID in 2014, which school's sum of Writing mean percentage and "
	 * + "Reading mean percentage is greater than sum of Science " + "mean
	 * percentage and Math mean percentage?
	 * =========================================================================
	 * 
	 */
	private static void Query4(Connection dbconn, String yorN) {
		// set up connection variable
		Statement stmt = null;
		ResultSet answer = null;
		try {
			// the query you gonna to query for this function
			int counter = 0;
			String query = "select School_EntityID " + "from("
					+ "select luye.AIMS2010.School_EntityID, luye.AIMS2010.Reading_PercPassing as Passing2010, "
					+ "luye.AIMS2011.Reading_PercPassing as Passing2011, luye.AIMS2012.Reading_PercPassing as Passing2012 "
					+ "from luye.AIMS2010 left join luye.AIMS2011 on luye.AIMS2010.School_EntityID = luye.AIMS2011.School_EntityID"
					+ " left join luye.AIMS2012 on luye.AIMS2010.School_EntityID = luye.AIMS2012.School_EntityID "
					+ "where luye.AIMS2010.Reading_PercPassing > 0 and luye.AIMS2011.Reading_PercPassing > 0 "
					+ "and luye.AIMS2012.Reading_PercPassing > 0 and luye.AIMS2010.S_charter='" + yorN
					+ "' and luye.AIMS2011.S_charter='" + yorN + "' and luye.AIMS2012.S_charter='" + yorN + "') "
					+ "where Passing2010 < Passing2011 and Passing2011 < Passing2012";

			stmt = dbconn.createStatement();
			answer = stmt.executeQuery(query);
			//answer.getInt("School_EntityID");
			if (answer != null) {
				while (answer.next()) {
					//System.out.println(answer.getInt("School_EntityID"));
					counter++;
				}
			}
			//System.out.println(yorN);
			if(yorN.equals("Y")){
				System.out.println("From year 2010 to year 2012, there are " + counter + " Chater Schools "
						+ "got an increasing trend on Reading Passing Percentage.");
			}
			if(yorN.equals("N")){
				System.out.println("From year 2010 to year 2012, there are " + counter + " Non Chater Schools "
						+ "got an increasing trend on Reading Passing Percentage.");
			}
			

		} catch (SQLException e) {// catch the exception
			System.err.println("*** SQLException:  " + "Could not fetch query results.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);
		}
	}

	/*
	 * =========================================================================
	 * Function name: private static void Query3(Connection dbconn) {
	 * Description: This is a simple help method to help you query the first
	 * question | which is asking you For each county in 2014, which 10 schools
	 * had the greatest differences between the" "Passing percentages in reading
	 * and writing? Display one table for each county that includes position
	 * number." "'1' for the school with biggest difference and duplicate
	 * position numbers for ties
	 * =========================================================================
	 * 
	 */
	private static void Query3(Connection dbconn) {
		Statement stmt = null;
		ResultSet CountyList = null;
		try {
			// the query to query County list
			String queryForCL = "select distinct county from luye.AIMS2014 order by county";
			stmt = dbconn.createStatement(); // build connection
			CountyList = stmt.executeQuery(queryForCL); // excute query and now
														// we have 15 county
			if (CountyList != null) {
				while (CountyList.next()) {
					String curCounty = CountyList.getString("County");
					System.out.println();
					System.out.println(curCounty);
					System.out.println("==============");
					System.out.println();
					Statement stmt2 = null;
					ResultSet answer = null;
					// tracking 15 county now and we have the query to asking
					// For each county in 2014,
					// which 10 schools had the greatest differences between the
					// "Passing percentages in reading
					// and writing? Display one table for each county that
					// includes position number."
					// "'1' for the school with biggest difference and duplicate
					// position numbers for ties
					String query = "select School_Name, Reading_PercPassing, Writing_PercPassing, abs_difference "
							+ "from("
							+ "select Reading_PercPassing, Writing_PercPassing, School_Name, abs(Reading_PercPassing-Writing_PercPassing) as abs_difference "
							+ "from  luye.AIMS2014 "
							+ "where Reading_PercPassing > 0 and Writing_PercPassing > 0 and County = '" + curCounty
							+ "' order by abs_difference desc) where rownum <= 10";
					// System.out.println(query);
					stmt2 = dbconn.createStatement();
					answer = stmt2.executeQuery(query);

					// added postion column
					System.out.print(" POS  ");
					ResultSetMetaData answermetadata = answer.getMetaData();
					for (int i = 1; i <= answermetadata.getColumnCount(); i++) {
						System.out.print("   \t\t " + answermetadata.getColumnName(i) + "  ");
					}
					// fomatting
					System.out.println();
					System.out.println("----------------------------------------------"
							+ "-------------------------------------------------------------------------"
							+ "-------------------------------------------------");
					int pos = 1;
					int temp = 1;
					while (answer.next()) {
						if (temp != 1 && temp != answer.getInt("abs_difference")) {
							pos++;
						}
						// fomatting school name column
						String sname = answer.getString("School_Name");
						int maxL = 60;
						for (int i = sname.length(); i <= maxL; i++) {
							sname += " ";
						}
						// fomatting Postion column
						String formatP = "";
						if (pos < 10) {
							formatP = pos + "  \t";
						} else {
							formatP = pos + " \t";
						}
						// print out the final answer on the screen
						System.out.println(" " + formatP + sname + answer.getInt("Reading_PercPassing") + "  \t\t\t\t "
								+ answer.getInt("Writing_PercPassing") + "  \t\t\t\t "
								+ answer.getInt("abs_difference"));
						temp = answer.getInt("abs_difference");
					}
					System.out.println();
				}
			}
		} catch (SQLException e) { // catch the exception
			System.err.println("*** SQLException:  " + "Could not fetch query results.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);
		}

	}

	/*
	 * =========================================================================
	 * Function name: private static void Query2(Connection dbconn, String yorN)
	 * { Description: This is a simple help method to help you query the first
	 * question | which is asking you For each of the five years, display the
	 * num of charter schools and how many of"
	 * "them had a sum of the Math percentages \"Falls Far Below\" and \"Approaches\" that was"
	 * + "less than the percent \"Passing\"\n" + "You can quit2 to end query Q2
	 * | | What should I emphasized here is The definition for Junior High
	 * School here | is any name contains \"Junior High School\"."
	 * =========================================================================
	 * 
	 */
	private static void Query2(Connection dbconn) {
		// set up connection variables
		Statement stmt = null;
		ResultSet answer = null;
		int counter = 0, counter2 = 0;
		try {
			// the year chosen by users
			int year;
			for (year = 2010; year < 2015; year++) {
				String subQuery1 = "select School_EntityID from luye.AIMS" + year + " where S_charter = 'Y'";
				stmt = dbconn.createStatement(); // build connection
				answer = stmt.executeQuery(subQuery1); // execute query
				if (answer != null) {
					while (answer.next()) {
						// count the number of charter school for specific year
						counter++;
					}
				}
				System.out.println("The number of Charter Schools for " + year + ":" + counter);
				counter = 0;

				// Sub query 2 of query 2
				String subQuery2 = "select Sum, Math_PercPassing from (select Math_PercPassing, "
						+ "Math_PercFallsFarBelow + Math_PercApproaches as Sum from luye.AIMS" + year
						+ " where S_charter = 'Y' ) where Sum < Math_PercPassing and Sum > 0"
						+ " and Math_PercPassing > 0";
				// create the second connection
				stmt = dbconn.createStatement();
				// execute the query again
				answer = stmt.executeQuery(subQuery2);
				if (answer != null) {
					while (answer.next()) {
						// this count is counting the number of charter schools
						// where the sum of math percentages
						// fall far below and approaches is less than the
						// percentage of passing
						counter2++;
					}
				}
				System.out.println("The number of Charter Schools where the sum of math percentages "
						+ "falls far below and approaches is less than the percentage of passing " + year + ":"
						+ counter2);
				counter2 = 0;

			}
		} catch (SQLException e) {// catch the exception
			System.err.println("*** SQLException:  " + "Could not fetch query results.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);
		}

	}

	/*
	 * =========================================================================
	 * Function name: private static void Query1(Connection dbconn, String yorN)
	 * { | Description: This is a simple help method to help you query the first
	 * question | which is asking you Choose whether a Charter School or not
	 * (Input Y or N, both Capital), to see which school have a increased trend
	 * on Reading passing percentage for these five years(from 2010 to 2015)
	 * =========================================================================
	 * 
	 */
	private static void Query1(Connection dbconn, String year) {
		Statement stmt = null;
		ResultSet answer = null;
		int counter = 0;
		String query = "select distinct School_Name from AIMS" + year + " where School_"
				+ "Name like '%High_School%' and School_name not like '%Junior_High_School%'";
		// send data and query
		try {
			// build connection
			stmt = dbconn.createStatement();
			// execute Query
			answer = stmt.executeQuery(query);
			if (answer != null) {
				System.out.print(
						"alert: The definition for Junior High School here is any name contains \"Junior High School\".");
				System.out.println("\nThe results of the query \n[" + query + "] are:\n");
				while (answer.next()) {
					// Count the number of High school
					counter++;
				}
			}
			System.out.println("The num of High School for AIMS" + year + " is " + counter + ".");
			System.out.println();
			System.out.println("Keep inserting a year. Or insert \"quit1\" to go back main menu.");
			System.out.println("============================");
		} catch (SQLException e) { // try catch to check exception
			System.err.println("*** SQLException:  " + "Could not fetch query results.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);

		}

	}
}
