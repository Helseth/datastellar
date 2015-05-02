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

public class UpdateSpeciesPage {
	public static Composite updateSpeciesPage(Composite parent, Connection conn,
			String speciesName, Combo updateSpeciesSelect) {
		Composite speciesUpdatePage = new Composite(parent, SWT.NONE);

		// Here is where we fetch the data for the planet and populate the widgets with the data from the DB
		// This part looks really similar to the insert planet page
		// For this page it is really important to pass in the planet selection Combo so that we can refresh it
		// after the planet is updated in case they change the name. Do this to avoid sending updates to the SQL
		// server for data that might not exist any more
		GridLayout updateGL = new GridLayout(4, true);

		GridData updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);

		Label name = new Label(speciesUpdatePage, SWT.FILL);
		updateGD.horizontalSpan = 4;
		name.setText("Name:");
		name.setLayoutData(updateGD);

		//Text is an editable textbox, you can set it to read-only by replacing the SWT.NONE in the contructor, but this is how
		// users will enter data to insert in the DB
		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		updateGD.horizontalSpan = 1;
		Text nameBox = new Text(speciesUpdatePage, SWT.NONE);
		nameBox.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label height = new Label(speciesUpdatePage, SWT.FILL);
		updateGD.horizontalSpan = 4;
		height.setText("Height:");
		height.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		updateGD.horizontalSpan = 1;
		Text heightBox = new Text(speciesUpdatePage, SWT.NONE);
		heightBox.setLayoutData(updateGD);
		
		Label heightUnit = new Label(speciesUpdatePage, SWT.FILL);
		updateGD.horizontalSpan = 1;
		heightUnit.setText("ft");
		heightUnit.setLayoutData(updateGD);
        //
		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label numberLiving = new Label(speciesUpdatePage, SWT.FILL);
		updateGD.horizontalSpan = 4;
		numberLiving.setText("Number Living:");
		numberLiving.setLayoutData(updateGD);
		
		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		updateGD.horizontalSpan = 1;
		Text numberLivingBox = new Text(speciesUpdatePage, SWT.NONE);
		numberLivingBox.setLayoutData(updateGD);
		//
		
		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label hostility = new Label(speciesUpdatePage, SWT.FILL);
		updateGD.horizontalSpan = 4;
		hostility.setText("Hostility:");
		hostility.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		updateGD.horizontalSpan = 1;
		Text hostilityBox = new Text(speciesUpdatePage, SWT.NONE);
		hostilityBox.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label errorText = new Label(speciesUpdatePage, SWT.FILL);
		updateGD.horizontalSpan = 4;
		errorText.setText("");
		errorText.setLayoutData(updateGD);
		errorText.setVisible(false);
		errorText.setForeground(Display.getCurrent().getSystemColor(
				SWT.COLOR_RED));

		updateGD = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		Button submit = new Button(speciesUpdatePage, SWT.PUSH);
		updateGD.horizontalSpan = 1;
		submit.setText("Submit");
		submit.pack();
		submit.setLayoutData(updateGD);

		updateGD = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		Button refresh = new Button(speciesUpdatePage, SWT.PUSH);
		updateGD.horizontalSpan = 1;
		refresh.setText("Refresh");
		refresh.pack();
		refresh.setLayoutData(updateGD);


		// Here is where we query the DB for the planet's info
		// It's important to pass in all of the editable boxes and drop-downs
		// because we set their text to be the queried data
		// For other object types we need to do the same, just tailor it for the
		// data fields for that object
		getSpeciesValues(conn, speciesName, nameBox, heightBox, numberLivingBox, hostilityBox);
//////////////////From here on down it gets a little messy//////////////////////////////
		submit.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				boolean error = false;
				// Just some basic error checking before we make the SQL statement
				if (nameBox.getText().equals("")) {
					errorText.setText("Name must not be empty.");
					errorText.setVisible(true);
					error = true;
				}
				if (!NumberUtils.isNumber(heightBox.getText())) {
					errorText.setText("Height must be a number.");
					errorText.setVisible(true);
					error = true;
				}
				if (NumberUtils.isNumber(heightBox.getText())) {
					if (Integer.parseInt(heightBox.getText()) <= 0) {
						errorText.setText("Height must be > 0.");
						errorText.setVisible(true);
						error = true;
					}

				}
				if (!NumberUtils.isNumber(numberLivingBox.getText())) {
					errorText.setText("Number Living must be a number.");
					errorText.setVisible(true);
					error = true;
				}
				if (NumberUtils.isNumber(numberLivingBox.getText())) {
					if (Integer.parseInt(numberLivingBox.getText()) < 0) {
						errorText.setText("Number Living must be >= 0.");
						errorText.setVisible(true);
						error = true;
					}

				}
				if (!NumberUtils.isNumber(hostilityBox.getText())) {
					errorText.setText("Hostility must be a number.");
					errorText.setVisible(true);
					error = true;
				}
				if (NumberUtils.isNumber(hostilityBox.getText())) {
					if (Long.parseLong(hostilityBox.getText()) < 1 || Long.parseLong(hostilityBox.getText()) > 10) {
						errorText.setText("Hostility must be a value in the range 1 to 10.");
						errorText.setVisible(true);
						error = true;
					}

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
						PreparedStatement updateSpecies = conn
								.prepareStatement("UPDATE species SET name=\""
										+ WordUtils.capitalize(nameBox.getText()) + "\", height="
										+ heightBox.getText() + ", numberLiving="
										+ numberLivingBox.getText() + ", hostility="
										+ hostilityBox.getText() + " WHERE "
										+ "name=\"" + speciesName + "\";");
						updateSpecies.execute();
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
						PreparedStatement getTableNames = conn
								.prepareStatement("SELECT * FROM species WHERE name='"
										+ speciesName + "';");
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
								if (columnName.equalsIgnoreCase("Height")) {
									heightBox.setText(columnValue);
								}
								if (columnName.equalsIgnoreCase("numberLiving")) {
									numberLivingBox.setText(columnValue);
								}
								if (columnName.equalsIgnoreCase("Hostility")) {
									hostilityBox.setText(columnValue);
								}
							}
						}

					} catch (SQLException e) {
						System.out.println("SQL Error");
						e.printStackTrace();
					}


				// Here we just refresh the input boxes with the updated data from the DB
				try {
					String name = nameBox.getText();
					updateSpeciesSelect.removeAll();
					PreparedStatement getTableNames = conn
							.prepareStatement("SELECT name FROM species;");
					getTableNames.execute();
					ResultSet rs = getTableNames.getResultSet();
					ResultSetMetaData rsmd = rs.getMetaData();

					while (rs.next()) {
						for (int i = 1; i <= rsmd.getColumnCount(); i++) {
							String columnValue = rs.getString(i);
							updateSpeciesSelect.add(WordUtils
									.capitalize(columnValue));
						}
					}
					updateSpeciesSelect.select(updateSpeciesSelect.indexOf(name));
					getSpeciesValues(conn, name, nameBox, heightBox, numberLivingBox,
							hostilityBox);

				} catch (SQLException e) {
					System.out.println("SQL Error");
					e.printStackTrace();
				}

			}
		});

		speciesUpdatePage.setLayout(updateGL);
		return speciesUpdatePage;
	}

	public static void getSpeciesValues(Connection conn, String planetName,
			Text nameBox, Text heightBox, Text numberLivingBox, Text hostilityBox) {
		// Query DB for the selected planet's data
		try {
			PreparedStatement getTableNames = conn
					.prepareStatement("SELECT * FROM species WHERE name='"
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
					if (columnName.equalsIgnoreCase("Height")) {
						heightBox.setText(columnValue);
					}
					if (columnName.equalsIgnoreCase("numberLiving")) {
						numberLivingBox.setText(columnValue);
					}
					if (columnName.equalsIgnoreCase("Hostility")) {
						hostilityBox.setText(columnValue);
					}
				}
			}

		} catch (SQLException e) {
			System.out.println("SQL Error");
			e.printStackTrace();
		}
	}
}
