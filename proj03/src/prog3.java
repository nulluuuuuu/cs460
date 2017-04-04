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
		Statement stmt = null;
		stmt = dbconn.createStatement();

		File inputFile = new File("2010.csv");
		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		String currentLine;
		while ((currentLine = reader.readLine()) != null) {
		currentLine = reader.readLine();
			String[] field = currentLine.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", 35);
			for (int i = 0; i < 34; i++) {
				System.out.println(i+ " "+ field[i]);
			}
				
			String query = "INSERT INTO AIMS2010 VALUES (" + field[0] + "," +
				"q'[" +field[1]+ "]'" + "," +
				"q'[" +field[2]+ "]'" + "," +
				field[3] + "," + field[4] + "," + 
				"q'[" +field[5]+ "]'" + "," +
				field[6]+ "," + field[7]+ "," + 
				"q'[" +field[8]+ "]'" + "," +
				"q'[" +field[9]+ "]'" + "," +
				field[10]+ "," + field[11]+ "," +
				field[12]+ "," + field[13]+ "," + 
				field[14]+ "," + field[15]+ "," + 
				field[16]+ "," + field[17]+ "," +
				field[18]+ "," + field[19]+ "," + 
				field[20]+ "," + field[21]+ "," + 
				field[22]+ "," + field[23]+ "," +
				field[24]+ "," + field[25]+ "," + 
				field[26]+ "," + field[27]+ "," + 
				field[28]+ "," + field[29]+ "," +
				field[30]+ "," + field[31]+ "," + 
				field[32]+ "," + field[33]+ ");";
	
			stmt.executeQuery(query);
		}
		System.out.println("import success");

	}
}
