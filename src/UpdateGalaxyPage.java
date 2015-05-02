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

public class UpdateGalaxyPage {
	public static Composite updateGalaxyPage(Composite parent, Connection conn,
			String galaxyName, Combo updateGalaxySelect) {
		Composite galaxyUpdatePage = new Composite(parent, SWT.NONE);

		// Here is where we fetch the data for the planet and populate the widgets with the data from the DB
		// This part looks really similar to the insert planet page
		// For this page it is really important to pass in the planet selection Combo so that we can refresh it
		// after the planet is updated in case they change the name. Do this to avoid sending updates to the SQL
		// server for data that might not exist any more
		GridLayout updateGL = new GridLayout(4, true);

		GridData updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);

		Label name = new Label(galaxyUpdatePage, SWT.FILL);
		updateGD.horizontalSpan = 4;
		name.setText("Name:");
		name.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		updateGD.horizontalSpan = 1;
		Text nameBox = new Text(galaxyUpdatePage, SWT.NONE);
		nameBox.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label diameter = new Label(galaxyUpdatePage, SWT.FILL);
		updateGD.horizontalSpan = 4;
		diameter.setText("Diameter:");
		diameter.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		updateGD.horizontalSpan = 1;
		Text diameterBox = new Text(galaxyUpdatePage, SWT.NONE);
		diameterBox.setLayoutData(updateGD);
		
		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label klyText = new Label(galaxyUpdatePage, SWT.FILL);
		updateGD.horizontalSpan = 1;
		klyText.setText("kly");
		klyText.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label shapeText = new Label(galaxyUpdatePage, SWT.FILL);
		updateGD.horizontalSpan = 4;
		shapeText.setText("Shape:");
		shapeText.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, false, false);
		Combo galaxyShape = new Combo(galaxyUpdatePage, SWT.DROP_DOWN
				| SWT.READ_ONLY);
		updateGD.horizontalSpan = 2;
		galaxyShape.setLayoutData(updateGD);
		
		galaxyShape.add("Spiral");
		galaxyShape.add("Elliptical");
		galaxyShape.add("Lenticular");
		galaxyShape.add("Irregular");

		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label errorText = new Label(galaxyUpdatePage, SWT.FILL);
		updateGD.horizontalSpan = 4;
		errorText.setText("");
		errorText.setLayoutData(updateGD);
		errorText.setVisible(false);
		errorText.setForeground(Display.getCurrent().getSystemColor(
				SWT.COLOR_RED));

		updateGD = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		Button submit = new Button(galaxyUpdatePage, SWT.PUSH);
		updateGD.horizontalSpan = 1;
		submit.setText("Submit");
		submit.pack();
		submit.setLayoutData(updateGD);

		updateGD = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		Button refresh = new Button(galaxyUpdatePage, SWT.PUSH);
		updateGD.horizontalSpan = 1;
		refresh.setText("Refresh");
		refresh.pack();
		refresh.setLayoutData(updateGD);

		// Here is where we query the DB for the planet's info
		// It's important to pass in all of the editable boxes and drop-downs
		// because we set their text to be the queried data
		// For other object types we need to do the same, just tailor it for the
		// data fields for that object
		getGalaxyValues(conn, galaxyName, nameBox, diameterBox, galaxyShape);
//////////////////From here on down it gets a little messy//////////////////////////////		
		submit.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				boolean error = false;
				if (nameBox.getText().equals("")) {
					errorText.setText("Name must not be empty.");
					errorText.setVisible(true);
					error = true;
				}
				if (!NumberUtils.isNumber(diameterBox.getText())) {
					errorText.setText("Diameter must be a number.");
					errorText.setVisible(true);
					error = true;
				}
				if (NumberUtils.isNumber(diameterBox.getText())) {
					if (Integer.parseInt(diameterBox.getText()) <= 0) {
						errorText.setText("Diameter must be > 0.");
						errorText.setVisible(true);
						error = true;
					}

				}
				if(galaxyShape.getText().equals("")){
					errorText.setText("You must select a shape.");
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
						PreparedStatement updateGalaxy = conn
								.prepareStatement("UPDATE galaxy SET name=\""
										+ WordUtils.capitalize(nameBox.getText()) + "\", diameter="
										+ diameterBox.getText() + ", shape=\""
										+ galaxyShape.getText() + "\" WHERE "
										+ "name=\"" + galaxyName + "\";");
						updateGalaxy.execute();
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

			}
		});

		refresh.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
					try {
						PreparedStatement getTableNames = conn
								.prepareStatement("SELECT * FROM galaxy WHERE name='"
										+ galaxyName + "';");
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
								if (columnName.equalsIgnoreCase("Diameter")) {
									diameterBox.setText(columnValue);
								}
								if (columnName.equalsIgnoreCase("Shape")) {
									galaxyShape.select(galaxyShape
											.indexOf(columnValue));

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
					updateGalaxySelect.removeAll();
					PreparedStatement getTableNames = conn
							.prepareStatement("SELECT name FROM galaxy;");
					getTableNames.execute();
					ResultSet rs = getTableNames.getResultSet();
					ResultSetMetaData rsmd = rs.getMetaData();

					while (rs.next()) {
						for (int i = 1; i <= rsmd.getColumnCount(); i++) {
							String columnValue = rs.getString(i);
							updateGalaxySelect.add(WordUtils
									.capitalize(columnValue));
						}
					}
					updateGalaxySelect.select(updateGalaxySelect.indexOf(name));
					getGalaxyValues(conn, name, nameBox, diameterBox, galaxyShape);

				} catch (SQLException e) {
					System.out.println("SQL Error");
					e.printStackTrace();
				}

			}
		});

		galaxyUpdatePage.setLayout(updateGL);
		return galaxyUpdatePage;
	}

	public static void getGalaxyValues(Connection conn, String galaxyName,
			Text nameBox, Text diameterBox, Combo shapeSelect) {
		// Query DB for the selected planet's data
		try {
			PreparedStatement getTableNames = conn
					.prepareStatement("SELECT * FROM galaxy WHERE name='"
							+ galaxyName + "';");
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
					if (columnName.equalsIgnoreCase("Diameter")) {
						diameterBox.setText(columnValue);
					}
					if (columnName.equalsIgnoreCase("Shape")) {
						shapeSelect.select(shapeSelect.indexOf(columnValue));
					}
				}
			}

		} catch (SQLException e) {
			System.out.println("SQL Error");
			e.printStackTrace();
		}
	}
}
