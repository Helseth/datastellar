import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class QueryGalaxyPage {
	public static Composite createGalaxyPage(Composite parent, Connection conn) {
		Composite galaxyDeletePage = new Composite(parent, SWT.NONE);
		// planetInsertPage.setBackground(new Color(Display.getCurrent(), 255,
		// 0, 0));
		GridLayout queryGL = new GridLayout(4, true);

		GridData queryGD = new GridData(SWT.FILL, SWT.CENTER, true, false);

		Label name = new Label(galaxyDeletePage, SWT.FILL);
		queryGD.horizontalSpan = 4;
		name.setText("Name:");
		name.setLayoutData(queryGD);

		queryGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		queryGD.horizontalSpan = 2;
		Combo queryGalaxyNames = new Combo(galaxyDeletePage, SWT.READ_ONLY);
		queryGalaxyNames.setLayoutData(queryGD);

		queryGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label errorText = new Label(galaxyDeletePage, SWT.FILL);
		queryGD.horizontalSpan = 4;
		errorText.setText("");
		errorText.setLayoutData(queryGD);
		errorText.setVisible(false);
		errorText.setForeground(Display.getCurrent().getSystemColor(
				SWT.COLOR_RED));

		queryGD = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		Button submit = new Button(galaxyDeletePage, SWT.PUSH);
		queryGD.horizontalSpan = 1;
		submit.setText("Submit");
		submit.pack();
		submit.setLayoutData(queryGD);

		queryGD = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		Button refresh = new Button(galaxyDeletePage, SWT.PUSH);
		queryGD.horizontalSpan = 1;
		refresh.setText("Refresh");
		refresh.pack();
		refresh.setLayoutData(queryGD);

		refresh(queryGalaxyNames, conn);

		refresh.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				refresh(queryGalaxyNames, conn);
			}
		});

		submit.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				try {
					PreparedStatement deleteEntry = conn
							.prepareStatement("DELETE FROM galaxy WHERE name='"
									+ queryGalaxyNames.getText() + "';");
					// deleteEntry.execute();
					createGalaxyResults(conn, queryGalaxyNames.getText());

				} catch (SQLException e) {
					System.out.println("SQL Error");
					// e.printStackTrace();
					if (e.getMessage().contains("foreign key constraint fails")) {
						errorText
								.setText("Cannot delete entry, removing violates foreign key constraints.");
						errorText.setVisible(true);
					}
				}
			}
		});

		galaxyDeletePage.setLayout(queryGL);
		return galaxyDeletePage;
	}

	public static void refresh(Combo queryGalaxyNames, Connection conn) {

		try {
			queryGalaxyNames.removeAll();
			PreparedStatement getTableNames = conn
					.prepareStatement("SELECT name FROM galaxy ;");
			getTableNames.execute();
			ResultSet rs = getTableNames.getResultSet();
			ResultSetMetaData rsmd = rs.getMetaData();

			while (rs.next()) {
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					String columnValue = rs.getString(i);
					// This inserts the names of the tables into the
					// drop-down menu
					queryGalaxyNames.add(WordUtils.capitalize(columnValue));
				}
			}

		} catch (SQLException e) {
			System.out.println("SQL Error");
		}
	}

	public static void createGalaxyResults(Connection conn, String galaxyName) {
		Shell shell = new Shell(Display.getCurrent());
		// Composite c = new Composite(shell, SWT.BORDER);
		GridLayout gl = new GridLayout(1, true);

		GridData queryGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		// Label name = new Label(shell, SWT.FILL);
		// name.setText("Name:");
		// name.setLayoutData(queryGD);

		// queryGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		// Label nameResult = new Label(shell, SWT.FILL);
		// nameResult.setText(galaxyName);
		try {
			PreparedStatement queryEntry = conn
					.prepareStatement("SELECT * FROM galaxy WHERE name='"
							+ galaxyName + "';");
			queryEntry.execute();

			ResultSet rs = queryEntry.getResultSet();
			ResultSetMetaData rsmd = rs.getMetaData();

			while (rs.next()) {
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					String columnValue = rs.getString(i);
					// System.out.println(columnValue);

					queryGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
					Label result = new Label(shell, SWT.FILL);
					result.setText(WordUtils.capitalize(rsmd.getColumnLabel(i))
							+ ":");
					result.setLayoutData(queryGD);

					queryGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
					Label resultData = new Label(shell, SWT.FILL);

					if (rsmd.getColumnName(i).equalsIgnoreCase("Diameter"))
						resultData.setText(WordUtils.capitalize(columnValue)
								+ " kly");
					else
						resultData.setText(WordUtils.capitalize(columnValue));

					resultData.setLayoutData(queryGD);
				}
			}
		} catch (SQLException e) {

		}

		Label avgHostile = new Label(shell, SWT.FILL);
		avgHostile.setText("Average Hostility of Species in " + galaxyName + " Galaxy:");
		avgHostile.setLayoutData(queryGD);

		try {
			PreparedStatement queryEntry = conn
					.prepareStatement("SELECT AVG(hostility) from(Select distinct Species.name,"
							+ " hostility FROM Star,Inhabits,Planet,Species,Galaxy WHERE Planet.orbitsStar"
							+ " = Star.name and Inhabits.speciesName = Species.name and Inhabits.planetName"
							+ " = Planet.name and Star.inGalaxy = Galaxy.name and Galaxy.name"
							+ " = '" + galaxyName + "')as subquery​;");
			queryEntry.execute();
			ResultSet rs = queryEntry.getResultSet();
			ResultSetMetaData rsmd = rs.getMetaData();
			while (rs.next()) {
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					String columnValue = rs.getString(i);
					Label avgHostileVal = new Label(shell, SWT.FILL);
					avgHostileVal.setText(columnValue);
					avgHostileVal.setLayoutData(queryGD);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		Label starNames = new Label(shell, SWT.FILL);
		starNames.setText("Stars in " + galaxyName + " Galaxy");
		starNames.setLayoutData(queryGD);

		queryGD = new GridData(SWT.FILL, SWT.FILL, true, true);
		List stars = new List(shell, SWT.V_SCROLL);
		stars.setLayoutData(queryGD);
		try {
			PreparedStatement queryStars = conn
					.prepareStatement("SELECT name FROM star WHERE inGalaxy='"
							+ galaxyName + "';");
			queryStars.execute();
			ResultSet rs = queryStars.getResultSet();
			ResultSetMetaData rsmd = rs.getMetaData();

			while (rs.next()) {
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					String columnValue = rs.getString(i);
					stars.add(columnValue);
				}
			}
		} catch (SQLException e) {

		}

		shell.setLayout(gl);
		// shell.pack();
		shell.setSize(400, 400);
		shell.open();
		while (!shell.isDisposed()) {
			if (!Display.getCurrent().readAndDispatch()) {
				Display.getCurrent().sleep();
			}
		}
	}
}
