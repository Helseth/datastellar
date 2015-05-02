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
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class UpdatePlanetPage {
	public static Composite updatePlanetPage(Composite parent, Connection conn,
			String planetName, Combo updatePlanetSelect) {
		Composite planetUpdatePage = new Composite(parent, SWT.NONE);

		// Here is where we fetch the data for the planet and populate the widgets with the data from the DB
		// This part looks really similar to the insert planet page
		// For this page it is really important to pass in the planet selection Combo so that we can refresh it
		// after the planet is updated in case they change the name. Do this to avoid sending updates to the SQL
		// server for data that might not exist any more
		GridLayout updateGL = new GridLayout(4, true);

		GridData updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);

		Label name = new Label(planetUpdatePage, SWT.FILL);
		updateGD.horizontalSpan = 4;
		name.setText("Name:");
		name.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		updateGD.horizontalSpan = 1;
		Text nameBox = new Text(planetUpdatePage, SWT.NONE);
		nameBox.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label mass = new Label(planetUpdatePage, SWT.FILL);
		updateGD.horizontalSpan = 4;
		mass.setText("Mass:");
		mass.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		updateGD.horizontalSpan = 1;
		Text massBox = new Text(planetUpdatePage, SWT.NONE);
		massBox.setLayoutData(updateGD);
		
		Label massUnit = new Label(planetUpdatePage, SWT.FILL);
		updateGD.horizontalSpan = 1;
		massUnit.setText("kg");
		massUnit.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label starOrbit = new Label(planetUpdatePage, SWT.FILL);
		updateGD.horizontalSpan = 4;
		starOrbit.setText("Orbits Star:");
		starOrbit.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, false, false);
		Combo starSelect = new Combo(planetUpdatePage, SWT.DROP_DOWN
				| SWT.READ_ONLY);
		updateGD.horizontalSpan = 2;
		starSelect.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label orbitalPeriod = new Label(planetUpdatePage, SWT.FILL);
		updateGD.horizontalSpan = 4;
		orbitalPeriod.setText("Orbital Period:");
		orbitalPeriod.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		updateGD.horizontalSpan = 1;
		Text periodBox = new Text(planetUpdatePage, SWT.NONE);
		periodBox.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label orbitalDays = new Label(planetUpdatePage, SWT.FILL);
		updateGD.horizontalSpan = 1;
		orbitalDays.setText("Earth days");
		orbitalDays.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label population = new Label(planetUpdatePage, SWT.FILL);
		updateGD.horizontalSpan = 4;
		population.setText("Population:");
		population.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		updateGD.horizontalSpan = 1;
		Text popBox = new Text(planetUpdatePage, SWT.NONE);
		popBox.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label inGalaxy = new Label(planetUpdatePage, SWT.FILL);
		updateGD.horizontalSpan = 4;
		inGalaxy.setText("In Galaxy:");
		inGalaxy.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, false, false);
		Combo galaxySelect = new Combo(planetUpdatePage, SWT.DROP_DOWN
				| SWT.READ_ONLY);
		updateGD.horizontalSpan = 2;
		galaxySelect.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label errorText = new Label(planetUpdatePage, SWT.FILL);
		updateGD.horizontalSpan = 4;
		errorText.setText("");
		errorText.setLayoutData(updateGD);
		errorText.setVisible(false);
		errorText.setForeground(Display.getCurrent().getSystemColor(
				SWT.COLOR_RED));

		updateGD = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		Button submit = new Button(planetUpdatePage, SWT.PUSH);
		updateGD.horizontalSpan = 1;
		submit.setText("Submit");
		submit.pack();
		submit.setLayoutData(updateGD);

		updateGD = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		Button refresh = new Button(planetUpdatePage, SWT.PUSH);
		updateGD.horizontalSpan = 1;
		refresh.setText("Refresh");
		refresh.pack();
		refresh.setLayoutData(updateGD);

		try {
			PreparedStatement getStarNames = conn
					.prepareStatement("SELECT DISTINCT name FROM star;");
			getStarNames.execute();
			ResultSet rs = getStarNames.getResultSet();
			ResultSetMetaData rsmd = rs.getMetaData();

			PreparedStatement getGalaxyNames = conn
					.prepareStatement("SELECT DISTINCT name FROM galaxy;");
			getGalaxyNames.execute();

			while (rs.next()) {
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					String columnValue = rs.getString(i);
					starSelect.add(WordUtils.capitalize(columnValue));
				}
			}

			rs = getGalaxyNames.getResultSet();
			rsmd = rs.getMetaData();

			while (rs.next()) {
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					String columnValue = rs.getString(i);
					galaxySelect.add(WordUtils.capitalize(columnValue));
				}
			}

		} catch (SQLException e) {
			System.out.println("SQL Error");
			e.printStackTrace();
		}

		// Here is where we query the DB for the planet's info
		// It's important to pass in all of the editable boxes and drop-downs
		// because we set their text to be the queried data
		// For other object types we need to do the same, just tailor it for the
		// data fields for that object
		getPlanetValues(conn, planetName, nameBox, massBox, popBox, periodBox,
				starSelect, galaxySelect);
//////////////////From here on down it gets a little messy//////////////////////////////
		submit.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				boolean error = false;
				// Error checking for the update
				if (nameBox.getText().equals("")) {
					errorText.setText("Name must not be empty.");
					errorText.setVisible(true);
					error = true;
				}
				if (!NumberUtils.isNumber(massBox.getText())) {
					errorText.setText("Mass must be a number.");
					errorText.setVisible(true);
					error = true;
				}
				if (NumberUtils.isNumber(massBox.getText())) {
					if (Integer.parseInt(massBox.getText()) <= 0) {
						errorText.setText("Mass must be > 0.");
						errorText.setVisible(true);
						error = true;
					}

				}
				if (starSelect.getText().equals("")) {
					errorText.setText("You must select a Star to orbit.");
					errorText.setVisible(true);
					error = true;
				}
				if (NumberUtils.isNumber(periodBox.getText())) {
					if (Integer.parseInt(periodBox.getText()) <= 0) {
						errorText.setText("Orbital Period must be > 0.");
						errorText.setVisible(true);
						error = true;
					}

				}
				if (!NumberUtils.isNumber(periodBox.getText())) {
					errorText.setText("Orbital Period must be a number.");
					errorText.setVisible(true);
					error = true;
				}
				if (NumberUtils.isNumber(popBox.getText())) {
					if (Long.parseLong(popBox.getText()) < 0) {
						errorText
								.setText("Population must be a non-negative value.");
						errorText.setVisible(true);
						error = true;
					}

				}
				if (!NumberUtils.isNumber(popBox.getText())) {
					errorText.setText("Population must be a number.");
					errorText.setVisible(true);
					error = true;
				}
				if (galaxySelect.getText().equals("")) {
					errorText.setText("You must select a Galaxy.");
					errorText.setVisible(true);
					error = true;
				}
				if (!error) {
					// Now that all the data is good send UPDATE statement to the sever
					try {
						// It's really important to turn off the check for foreign keys
						// If you don't, you will get an exception when executing the statement
						// We do this so if they decide to change what star it orbits or galaxy it's in
						PreparedStatement disableFKCheck = conn
								.prepareStatement("SET foreign_key_checks = 0;");
						disableFKCheck.execute();
						// the actual UPDATE statement
						PreparedStatement updatePlanet = conn
								.prepareStatement("UPDATE planet SET name=\""
										+ WordUtils.capitalize(nameBox.getText()) + "\", mass="
										+ massBox.getText() + ", orbitsStar=\""
										+ starSelect.getText()
										+ "\", orbitalPeriod="
										+ periodBox.getText() + ", population="
										+ popBox.getText() + ", inGalaxy=\""
										+ galaxySelect.getText() + "\" WHERE "
										+ "name=\"" + planetName + "\";");
						updatePlanet.execute();
						System.out.println("Updating " + nameBox.getText());
						// Turn foreign key checks back on
						disableFKCheck = conn
								.prepareStatement("SET foreign_key_checks = 1;");
						disableFKCheck.execute();

					} catch (SQLException e) {
						System.out.println("SQL Error");
						e.printStackTrace();
					}
				}
				// Now we want to refesh the planets drop-down with the new name (if they changed it)
				// We just tell the refresh button that it has been pushed, when it really hasn't
				refresh.notifyListeners(SWT.Selection, event);
			}
		});

		refresh.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				try {
					// Same as other refresh buttons, just force it to do an extra few steps
					PreparedStatement getStarNames = conn
							.prepareStatement("SELECT DISTINCT name FROM star;");
					getStarNames.execute();
					ResultSet rs = getStarNames.getResultSet();
					ResultSetMetaData rsmd = rs.getMetaData();

					PreparedStatement getGalaxyNames = conn
							.prepareStatement("SELECT DISTINCT name FROM galaxy;");
					getGalaxyNames.execute();

					starSelect.removeAll();
					while (rs.next()) {
						for (int i = 1; i <= rsmd.getColumnCount(); i++) {
							String columnValue = rs.getString(i);
							starSelect.add(WordUtils.capitalize(columnValue));
						}
					}

					rs = getGalaxyNames.getResultSet();
					rsmd = rs.getMetaData();

					galaxySelect.removeAll();
					while (rs.next()) {
						for (int i = 1; i <= rsmd.getColumnCount(); i++) {
							String columnValue = rs.getString(i);
							galaxySelect.add(WordUtils.capitalize(columnValue));
						}
					}

					try {
						PreparedStatement getTableNames = conn
								.prepareStatement("SELECT * FROM planet WHERE name='"
										+ planetName + "';");
						getTableNames.execute();
						//System.out.println(getTableNames.toString());
						ResultSet rs2 = getTableNames.getResultSet();
						ResultSetMetaData rsmd2 = rs2.getMetaData();

						while (rs2.next()) {
							for (int i = 1; i <= rsmd2.getColumnCount(); i++) {
								String columnName = rsmd2.getColumnName(i);
								String columnValue = rs2.getString(i);
								if (columnName.equalsIgnoreCase("Name")) {
									nameBox.setText(columnValue);
								}
								if (columnName.equalsIgnoreCase("Mass")) {
									massBox.setText(columnValue);
								}
								if (columnName.equalsIgnoreCase("Population")) {
									popBox.setText(columnValue);
								}
								if (columnName
										.equalsIgnoreCase("orbitalPeriod")) {
									periodBox.setText(columnValue);
								}
								if (columnName.equalsIgnoreCase("orbitsStar")) {
									starSelect.select(starSelect
											.indexOf(columnValue));
								}
								if (columnName.equalsIgnoreCase("inGalaxy")) {
									galaxySelect.select(galaxySelect
											.indexOf(columnValue));

								}
							}
						}

					} catch (SQLException e) {
						System.out.println("SQL Error");
						e.printStackTrace();
					}

				} catch (SQLException e) {
					System.out.println("SQL Error");
					e.printStackTrace();
				}

				// Here we just refresh the input boxes with the updated data from the DB
				try {
					String name = nameBox.getText();
					updatePlanetSelect.removeAll();
					PreparedStatement getTableNames = conn
							.prepareStatement("SELECT name FROM planet;");
					getTableNames.execute();
					ResultSet rs = getTableNames.getResultSet();
					ResultSetMetaData rsmd = rs.getMetaData();

					while (rs.next()) {
						for (int i = 1; i <= rsmd.getColumnCount(); i++) {
							String columnValue = rs.getString(i);
							updatePlanetSelect.add(WordUtils
									.capitalize(columnValue));
						}
					}
					updatePlanetSelect.select(updatePlanetSelect.indexOf(name));
					getPlanetValues(conn, name, nameBox, massBox, popBox,
							periodBox, starSelect, galaxySelect);

				} catch (SQLException e) {
					System.out.println("SQL Error");
					e.printStackTrace();
				}

			}
		});

		planetUpdatePage.setLayout(updateGL);
		return planetUpdatePage;
	}

	public static void getPlanetValues(Connection conn, String planetName,
			Text nameBox, Text massBox, Text popBox, Text periodBox,
			Combo starSelect, Combo galaxySelect) {
		// Query DB for the selected planet's data
		try {
			PreparedStatement getTableNames = conn
					.prepareStatement("SELECT * FROM planet WHERE name='"
							+ planetName + "';");
			getTableNames.execute();
			//System.out.println(getTableNames.toString());
			ResultSet rs = getTableNames.getResultSet();
			ResultSetMetaData rsmd = rs.getMetaData();

			// Populate the fields with the data
			while (rs.next()) {
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					String columnName = rsmd.getColumnName(i);
					String columnValue = rs.getString(i);
					if (columnName.equalsIgnoreCase("Name")) {
						nameBox.setText(columnValue);
					}
					if (columnName.equalsIgnoreCase("Mass")) {
						massBox.setText(columnValue);
					}
					if (columnName.equalsIgnoreCase("Population")) {
						popBox.setText(columnValue);
					}
					if (columnName.equalsIgnoreCase("orbitalPeriod")) {
						periodBox.setText(columnValue);
					}
					if (columnName.equalsIgnoreCase("orbitsStar")) {
						starSelect.select(starSelect.indexOf(columnValue));
					}
					if (columnName.equalsIgnoreCase("inGalaxy")) {
						galaxySelect.select(galaxySelect.indexOf(columnValue));

					}
				}
			}

		} catch (SQLException e) {
			System.out.println("SQL Error");
			e.printStackTrace();
		}
	}
}
