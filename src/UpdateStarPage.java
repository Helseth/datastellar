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

public class UpdateStarPage {
	public static Composite updateStarPage(Composite parent, Connection conn,
			String starName, Combo updateStarSelect) {
		Composite starUpdatePage = new Composite(parent, SWT.NONE);

		// Here is where we fetch the data for the planet and populate the widgets with the data from the DB
		// This part looks really similar to the insert planet page
		// For this page it is really important to pass in the planet selection Combo so that we can refresh it
		// after the planet is updated in case they change the name. Do this to avoid sending updates to the SQL
		// server for data that might not exist any more
		GridLayout updateGL = new GridLayout(4, true);

		GridData updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);

		Label name = new Label(starUpdatePage, SWT.FILL);
		updateGD.horizontalSpan = 4;
		name.setText("Name:");
		name.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		updateGD.horizontalSpan = 1;
		Text nameBox = new Text(starUpdatePage, SWT.NONE);
		nameBox.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label mass = new Label(starUpdatePage, SWT.FILL);
		updateGD.horizontalSpan = 4;
		mass.setText("Mass:");
		mass.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		updateGD.horizontalSpan = 1;
		Text massBox = new Text(starUpdatePage, SWT.NONE);
		massBox.setLayoutData(updateGD);
		
		Label massUnit = new Label(starUpdatePage, SWT.FILL);
		updateGD.horizontalSpan = 1;
		massUnit.setText("kg");
		massUnit.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label classText = new Label(starUpdatePage, SWT.FILL);
		updateGD.horizontalSpan = 4;
		classText.setText("Class:");
		classText.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		updateGD.horizontalSpan = 1;
		Text classBox = new Text(starUpdatePage, SWT.NONE);
		classBox.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label inGalaxy = new Label(starUpdatePage, SWT.FILL);
		updateGD.horizontalSpan = 4;
		inGalaxy.setText("In Galaxy:");
		inGalaxy.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, false, false);
		Combo galaxySelect = new Combo(starUpdatePage, SWT.DROP_DOWN
				| SWT.READ_ONLY);
		updateGD.horizontalSpan = 2;
		galaxySelect.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label errorText = new Label(starUpdatePage, SWT.FILL);
		updateGD.horizontalSpan = 4;
		errorText.setText("");
		errorText.setLayoutData(updateGD);
		errorText.setVisible(false);
		errorText.setForeground(Display.getCurrent().getSystemColor(
				SWT.COLOR_RED));

		updateGD = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		Button submit = new Button(starUpdatePage, SWT.PUSH);
		updateGD.horizontalSpan = 1;
		submit.setText("Submit");
		submit.pack();
		submit.setLayoutData(updateGD);

		updateGD = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		Button refresh = new Button(starUpdatePage, SWT.PUSH);
		updateGD.horizontalSpan = 1;
		refresh.setText("Refresh");
		refresh.pack();
		refresh.setLayoutData(updateGD);

		try {

			PreparedStatement getGalaxyNames = conn
					.prepareStatement("SELECT DISTINCT name FROM galaxy ORDER BY name;");
			getGalaxyNames.execute();
			ResultSet rs = getGalaxyNames.getResultSet();
			ResultSetMetaData rsmd = rs.getMetaData();

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
		getStarValues(conn, starName, nameBox, massBox, classBox, galaxySelect);
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
						PreparedStatement updateStar = conn
								.prepareStatement("UPDATE star SET name=\""
										+ WordUtils.capitalize(nameBox.getText()) + "\", mass="
										+ massBox.getText() + ", class=\""
										+ classBox.getText() + "\", inGalaxy=\""
										+ galaxySelect.getText() + "\" WHERE "
										+ "name=\"" + starName + "\";");
						updateStar.execute();
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

					PreparedStatement getGalaxyNames = conn
							.prepareStatement("SELECT DISTINCT name FROM galaxy ORDER BY name;");
					getGalaxyNames.execute();

					ResultSet rs = getGalaxyNames.getResultSet();
					ResultSetMetaData rsmd = rs.getMetaData();

					galaxySelect.removeAll();
					while (rs.next()) {
						for (int i = 1; i <= rsmd.getColumnCount(); i++) {
							String columnValue = rs.getString(i);
							galaxySelect.add(WordUtils.capitalize(columnValue));
						}
					}

					try {
						PreparedStatement getTableNames = conn
								.prepareStatement("SELECT * FROM star WHERE name='"
										+ starName + "';");
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
								if (columnName.equalsIgnoreCase("Class")) {
									classBox.setText(columnValue);
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
					updateStarSelect.removeAll();
					PreparedStatement getTableNames = conn
							.prepareStatement("SELECT name FROM star ORDER BY name;");
					getTableNames.execute();
					ResultSet rs = getTableNames.getResultSet();
					ResultSetMetaData rsmd = rs.getMetaData();

					while (rs.next()) {
						for (int i = 1; i <= rsmd.getColumnCount(); i++) {
							String columnValue = rs.getString(i);
							updateStarSelect.add(WordUtils
									.capitalize(columnValue));
						}
					}
					updateStarSelect.select(updateStarSelect.indexOf(name));
					getStarValues(conn, name, nameBox, massBox, classBox, galaxySelect);

				} catch (SQLException e) {
					System.out.println("SQL Error");
					e.printStackTrace();
				}

			}
		});

		starUpdatePage.setLayout(updateGL);
		return starUpdatePage;
	}

	public static void getStarValues(Connection conn, String starName,
			Text nameBox, Text massBox, Text classBox, Combo galaxySelect) {
		// Query DB for the selected planet's data
		try {
			PreparedStatement getTableNames = conn
					.prepareStatement("SELECT * FROM star WHERE name='"
							+ starName + "';");
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
					if (columnName.equalsIgnoreCase("Class")) {
						classBox.setText(columnValue);
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
