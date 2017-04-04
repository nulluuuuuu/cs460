import java.io.*;
import java.sql.*; // For access to the SQL interaction methods
import java.util.Scanner;

public class prog3 {

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
		while(working){
			System.out.println(mainQuery);
			switch(keyboard.next()){
			case "q1":
				//to do case 1 for query 1
				System.out.println("How many High Schools are listed in the results? Choose a year from 2010 to 2014.\n"
						+ "Or you can input quit1 and go back to main menu.");
				while(keyboard.hasNext()){
					String year = keyboard.next();
					System.out.println(year);
					if(year.equals("quit1")){
						break;
					}
					if(year.equals("2010")||year.equals("2011")||year.equals("2012")
							||year.equals("2013")||year.equals("2014")){
						Query1(dbconn,year);
					}
					else{
						System.out.println("You input a wrong year or wrong command. Please reinput or Input quit1 to end query q1.");
					}
				}
				break;
			case "q2":
				//to do case 2 for query 2
				System.out.println("For each of the five years, display the num of charter schools and how many of"
						+ "them had a sum of the Math percentages \"Falls Far Below\" and \"Approaches\" that was"
						+ "less than the percent \"Passing\"\n"
						+ "You can quit2 to end query Q2.");	
				break;
			case "q3":
				//to do case 3 for query 3
				System.out.println("instruction for query 3");
				Query3();
				break;
			case "q4":
				//to do case 4 for query 4
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
		System.out.println("run query4  " );
		
	}

	private static void Query3() {
		System.out.println("run query3  " );
		
	}

	private static void Query2() {
		System.out.println("run query2  " );
		
	}

	private static void Query1(Connection dbconn, String year) {
		System.out.println("run query1  " + year);
//		select distinct School_name from AIMS2010 
//		where School_name like '%High_School%'
//		and School_name not like '%Junior%'
//		and School_name not like '%Jr%';
		
		
	}
}
