import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Database{
	
	static final String url = "jdbc:mysql://localhost:3306/datastellar";
	static final String username = "root";
	static final String password = "datastellar";
	static final String[] options = {" ", "INSERT INTO", "DELETE FROM", "UPDATE", "SELECT"};
	static final String[] tables = {" ", "Galaxy", "Star", "Planet", "Moon", "Species", "Inhabits"};
	static final String[] params = {" ", "('name', 'shape', diameter)", "('name', mass, 'class', 'in-galaxy')",
		"('name', mass, 'orbits-star', orbital-period, population, 'in-galaxy')",
		"('name', mass, 'orbits-planet', orbital-period, 'in-galaxy')",
		"('name', height, number-living, hostility)", "('species-name', 'planet-name')"};
	
	public static void main(String[] args){
		Connection conn = null;
		//Statement stmt = null;
		
		
		Scanner in = new Scanner(System.in);
		int input = 0;
		try {
			conn = DriverManager.getConnection(url, username, password);
			if (conn != null) {
				System.out.println("Connected");
			}
		} catch (SQLException ex) {
			System.out.println("Problem with opening Connection");
			ex.printStackTrace();
		}
		MainWindow window = new MainWindow(conn);
		do{
			System.out.println("Enter a command: \n" + 
		"1: Insert a value into a table\n" + "2: Update a value in a table\n" +
				"3: Delete a value from a table\n" + "4: Query options\n" + "5: Exit" );
			input = in.nextInt();
			
			switch(input){
			case 1:
				insert(conn, in);
				break;
				
			case 2:
				update(conn, in);
				break;
				
			case 3:
				delete(conn, in);
				break;
				
			case 4:
				select(conn, in);
				break;
				
			case 5:
				System.out.println("Exit");
				break;
			default: 
				System.out.println("Please enter 1-5");
				break;
			}
		}while(input != 5);
		
		try {
			conn.close();
		} catch (SQLException e) {
			System.out.println("Problem closing the connection");
		}
	}
	
	public static void insert(Connection conn, Scanner in){
		int tab = 0;
		String input = null;
		System.out.println("What table would you like to insert into?\n1:Galaxy\n2:Star\n" +
		"3:Planet\n4:Moon\n5:Species\n6:Inhabits");
		tab = in.nextInt();
		System.out.println("Enter values for the following " + tables[tab] + " parameters: " + params[tab]);
		System.out.println("Enter values(separated by commas and quotes around appropriate attributes as above): ");
		in.nextLine();
		input = in.nextLine();
		String sql = "INSERT INTO " + tables[tab] + " VALUES(" + input + ")";
		//String sql = "INSERT INTO Star VALUES (\"Test\", 1000, \"Blue Giant\", \"Milky Way\")";
		try{
			PreparedStatement statement = conn.prepareStatement(sql);

			int rowsInserted = statement.executeUpdate();
			if (rowsInserted > 0) {
				System.out.println("A new entry was inserted successfully!");
			}
		}catch(SQLException e){
			System.out.println("Entry could not be added");
		}
	}
	
	public static void update(Connection conn, Scanner in){
		int tab = 0;
		boolean flag = false;
		String attributes = null;
		String updates = null;
		String name = null;
		String addition = null;
		System.out.println("What table would you like to update?\n1:Galaxy\n2:Star\n" +
		"3:Planet\n4:Moon\n5:Species\n6:Inhabits");
		tab = in.nextInt();
		//System.out.println("Enter the NAME of the " + tables[tab] + " you wish to update");
		if(tab == 6){
			System.out.println("Enter 'species-name' to be updated");
			in.nextLine();
			name = "\"" + in.nextLine() + "\"";
			addition = "speciesName=";
		}
		else{
			System.out.println("Enter the NAME of the " + tables[tab] + " you wish to update");
			in.nextLine();
			name = "\"" + in.nextLine() + "\"";
			addition = "name=";
		}
		//in.nextLine();
		//name = "'" + in.nextLine() + "'";
		System.out.println("Attribute list for " + tables[tab] + ":" + params[tab]);
		System.out.println("Type the attributes you wish to update as they appear from above:");
		attributes = in.nextLine();
		System.out.println("Type the new values for these attributes in the same order: ");
		updates = in.nextLine();
		List<String> attList = Arrays.asList(attributes.split(","));
		List<String> upList = Arrays.asList(updates.split(","));
		//System.out.println(attList.size() + " " + upList.size());
		String sql = "UPDATE " + tables[tab] + " SET ";
		for(int i = 0; i < attList.size(); i++){
			if(i == attList.size() - 1){
				sql += attList.get(i) + "=" + upList.get(i) + " WHERE " + addition + name;
			}
			else{
				sql += attList.get(i) + "=" + upList.get(i) + ", ";
			}
		}
		//System.out.println(sql);
		int rowsUpdated = 0;
		try{
			PreparedStatement statement = conn.prepareStatement(sql);
			rowsUpdated = statement.executeUpdate();
			if (rowsUpdated > 0) {
				System.out.println("The entry was updated successfully!");
			}
		}catch(SQLException e){
			System.out.println("Entry could not be updated");
			System.out.println("Make sure no dependencies are being violated!");
			//e.printStackTrace();
			flag = true;
		}
		if(rowsUpdated == 0 && flag == false){
			System.out.println("Couldn't find entry with name: " + name);
		}
	}
	
	public static void delete(Connection conn, Scanner in){
		int tab = 0;
		boolean flag = false;
		String planet = null;
		String name = null;
		String sql = null;
		System.out.println("What table would you like to delete from?\n1:Galaxy\n2:Star\n" +
		"3:Planet\n4:Moon\n5:Species\n6:Inhabits");
		tab = in.nextInt();
		if(tab == 6){
			System.out.println("Enter 'species-name' to be deleted");
			in.nextLine();
			name = "\"" + in.nextLine() + "\"";
			System.out.println("Enter the 'planet-name' you want the species deleted from");
			planet = "\"" + in.nextLine() + "\"";
			sql = "DELETE FROM " + tables[tab] + " WHERE speciesName=" + name + " AND planetName=" + planet;
		}
		else{
			System.out.println("Enter the NAME of the " + tables[tab] + " you wish to delete");
			in.nextLine();
			name = "\"" + in.nextLine() + "\"";
			sql = "DELETE FROM " + tables[tab] + " WHERE name=" + name;
		}
		//System.out.println(sql);
		int rowsDeleted = 0;
		try{
			PreparedStatement statement = conn.prepareStatement(sql);
			rowsDeleted = statement.executeUpdate();
			if (rowsDeleted > 0) {
				System.out.println("The entry was deleted successfully!");
			}
		}catch(SQLException e){
			System.out.println("Entry could not be deleted");
			System.out.println("Make sure there are no dependicies violated!");
			flag = true;
			e.printStackTrace();
		}
		if(rowsDeleted == 0 && flag == false){
			System.out.println("Couldn't find entry with name: " + name);
		}
	}

	public static void select(Connection conn, Scanner in){
		String query = null;
		int tab = 0;
		String sql = null;
		System.out.println("Enter your option for query:\n1: List all entries in a table\n2: Get the names of all the Moons orbiting a Star\n"
				+ "3: Average mass of Planets in a Galaxy\n4: Get name of all Species on a Planet");
		tab = in.nextInt();
		in.nextLine();
		if(tab == 1){
			System.out.print("Enter one of these table names: ");
			for(int i = 0; i < tables.length; i++){
				System.out.print(tables[i] + " ");
			}
			System.out.println();
			query = in.nextLine();
			sql = "SELECT * from " + query;
		}
		if(tab == 2){
			System.out.println("Enter the NAME of the Star");
			query = in.nextLine();
			sql = "SELECT DISTINCT Moon.name from Moon, Star, Planet WHERE (Planet.orbitsStar = '" + query + "') AND (Moon.orbitsPlanet = Planet.name)";
		}
		if(tab == 3){
			System.out.println("Enter the NAME of the Galaxy");
			query = "\'" + in.nextLine() + "\'";
			sql = "SELECT AVG(mass) from Planet WHERE inGalaxy=" + query;
		}
		if(tab == 4){
			System.out.println("Enter the name of the Planet");
			query = "\'" + in.nextLine() + "\'";
			sql = "SELECT DISTINCT name from Species, Inhabits WHERE (Species.name = Inhabits.speciesName) AND (Inhabits.planetName = " + query + ")";
		}
		//query = in.nextLine();
		//System.out.println(sql);
		try{
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(sql);

			ResultSetMetaData rsmd = rs.getMetaData();

			System.out.println("");

			int numberOfColumns = rsmd.getColumnCount();

			for (int i = 1; i <= numberOfColumns; i++) {
				if (i > 1) System.out.print(",  ");
				String columnName = rsmd.getColumnName(i);
				System.out.print(columnName);
			}
			System.out.println("");

			while (rs.next()) {
				for (int i = 1; i <= numberOfColumns; i++) {
					if (i > 1) System.out.print(",  ");
					String columnValue = rs.getString(i);
					System.out.print(columnValue);
				}
				System.out.println();  
			}

			statement.close();
			System.out.println();
		} catch(SQLException ex) {
			System.err.print("SQLException: ");
			System.err.println(ex.getMessage());
		}  
	}

}
