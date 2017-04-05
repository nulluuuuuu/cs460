import java.io.*;
import java.sql.*; // For access to the SQL interaction methods
import java.util.Scanner;

public class JDBC {

	// primary key (School_EntityID)

	public static void main(String[] args) throws SQLException, IOException {

		final String oracleURL = // Magic lectura -> aloe access spell
				"jdbc:oracle:thin:@aloe.cs.arizona.edu:1521:oracle";

		String username = null, // Oracle DBMS username
				password = null; // Oracle DBMS password

		if (args.length == 2) { // get username/password from cmd line args
			username = args[0];
			password = args[1];
		} else {
			System.out.println("\nUsage:  java JDBC <username> <password>\n"
					+ "    where <username> is your Oracle DBMS" + " username,\n    and <password> is your Oracle"
					+ " password (not your system password).\n");
			System.exit(-1);
		}

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
		String mainQuery = "Main menu: Please Type q1, q2, q3 or q4 for specific query.\n You can input quit to end.";
		Scanner keyboard = new Scanner(System.in); // your input
		boolean working = true;
		while (working) {
			System.out.println(mainQuery);
			switch (keyboard.next()) {
			case "q1":
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
								"You input a wrong year or wrong command. Please reinput or Input quit1 to end query q1.");
					}
				}
				break;
			case "q2":
				System.out.println("For each of the five years, display the num of charter schools and how many of"
						+ "them had a sum of the Math percentages \"Falls Far Below\" and \"Approaches\" that was"
						+ "less than the percent \"Passing\"\n" + "You can quit2 to end query Q2.");
				Query2(dbconn);
				break;
			case "q3":
				System.out.println("instruction for query 3");
				Query3(dbconn);
				break;
			case "q4":
				// to do case 4 for query 4
				System.out.println("instruction for query4");
				Query4();
				break;
			case "quit":
				working = false;
				break;
			default:
				System.out.println("You insert wrong command, follow the instruction. \n"
						+ "Please Type q1, q2, q3 or q4 for specific query.\n Or input quit to end.");
			}
		}

	}

	private static void Query4() {
		System.out.println("run query4  ");

	}

	private static void Query3(Connection dbconn) {
		Statement stmt = null;
		ResultSet CountyList = null;
		try {
			String queryForCL = "select distinct county from luye.AIMS2014 order by county";
			stmt = dbconn.createStatement();
			CountyList = stmt.executeQuery(queryForCL);

			if (CountyList != null) {

				while (CountyList.next()) {
					String curCounty = CountyList.getString("County");
					System.out.println(curCounty);
					System.out.println("==============");
					Statement stmt2 = null;
					ResultSet answer = null;
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
						String sname = answer.getString("School_Name");
						int maxL = 60;
						for (int i = sname.length(); i <= maxL; i++) {
							sname += " ";
						}
						String formatP = "";
						if (pos < 10) {
							formatP = pos + "  \t";
						} else {
							formatP = pos + " \t";
						}
						System.out.println(" " + formatP + sname + answer.getInt("Reading_PercPassing") + "  \t\t\t\t "
								+ answer.getInt("Writing_PercPassing") + "  \t\t\t\t "
								+ answer.getInt("abs_difference"));
						temp = answer.getInt("abs_difference");
					}
					System.out.println();
				}
			}
		} catch (SQLException e) {
			System.err.println("*** SQLException:  " + "Could not fetch query results.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);
		}

	}

	private static void Query2(Connection dbconn) {
		Statement stmt = null;
		ResultSet answer = null;
		int counter = 0, counter2 = 0;
		try {
			int year;
			for (year = 2010; year < 2015; year++) {
				String subQuery1 = "select School_EntityID from luye.AIMS" + year + " where S_charter = 'Y'";
				stmt = dbconn.createStatement();
				answer = stmt.executeQuery(subQuery1);
				if (answer != null) {
					while (answer.next()) {
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
				stmt = dbconn.createStatement();
				answer = stmt.executeQuery(subQuery2);
				if (answer != null) {
					while (answer.next()) {
						counter2++;
					}
				}
				System.out.println("The number of Charter Schools where the sum of math percentages "
						+ "falls far below and approaches is less than the percentage of passing " + year + ":"
						+ counter2);
				counter2 = 0;

			}
		} catch (SQLException e) {

			System.err.println("*** SQLException:  " + "Could not fetch query results.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);
		}

	}

	private static void Query1(Connection dbconn, String year) {
		// System.out.println("run query1 " + year);
		Statement stmt = null;
		ResultSet answer = null;
		int counter = 0;
		String query = "select distinct School_Name from AIMS" + year + " where School_"
				+ "Name like '%High_School%' and School_name not like '%Junior%' and School_name not like '%Jr%'";
		try {
			stmt = dbconn.createStatement();
			answer = stmt.executeQuery(query);

			if (answer != null) {

				System.out.println("\nThe results of the query \n[" + query + "] are:\n");
				while (answer.next()) {
					
					counter++;
				}
			}
			System.out.println("The num of High School for AIMS" + year + "is " + counter + ".");
			System.out.println();

		} catch (SQLException e) {

			System.err.println("*** SQLException:  " + "Could not fetch query results.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);

		}

	}
}