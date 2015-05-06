import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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

public class InsertPlanetPage {
	public static Composite createPlanetPage(Composite parent, Connection conn) {
		Composite planetInsertPage = new Composite(parent, SWT.NONE);
		// planetInsertPage.setBackground(new Color(Display.getCurrent(), 255,
		// 0, 0));

		// Same stuff as before
		GridLayout insertGL = new GridLayout(4, true);

		GridData insertGD = new GridData(SWT.FILL, SWT.CENTER, true, false);

		Label name = new Label(planetInsertPage, SWT.FILL);
		insertGD.horizontalSpan = 4;
		name.setText("Name:");
		name.setLayoutData(insertGD);

		// Text is an editable textbox, you can set it to read-only by replacing
		// the SWT.NONE in the contructor, but this is how
		// users will enter data to insert in the DB
		insertGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		insertGD.horizontalSpan = 1;
		Text nameBox = new Text(planetInsertPage, SWT.NONE);
		nameBox.setLayoutData(insertGD);

		insertGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label mass = new Label(planetInsertPage, SWT.FILL);
		insertGD.horizontalSpan = 4;
		mass.setText("Mass:");
		mass.setLayoutData(insertGD);

		insertGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		insertGD.horizontalSpan = 1;
		Text massBox = new Text(planetInsertPage, SWT.NONE);
		massBox.setLayoutData(insertGD);

		Label massUnit = new Label(planetInsertPage, SWT.FILL);
		insertGD.horizontalSpan = 1;
		massUnit.setText("kg");
		massUnit.setLayoutData(insertGD);

		insertGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label starOrbit = new Label(planetInsertPage, SWT.FILL);
		insertGD.horizontalSpan = 4;
		starOrbit.setText("Orbits Star:");
		starOrbit.setLayoutData(insertGD);

		insertGD = new GridData(SWT.FILL, SWT.CENTER, false, false);
		Combo starSelect = new Combo(planetInsertPage, SWT.DROP_DOWN
				| SWT.READ_ONLY);
		insertGD.horizontalSpan = 2;
		starSelect.setLayoutData(insertGD);

		insertGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label orbitalPeriod = new Label(planetInsertPage, SWT.FILL);
		insertGD.horizontalSpan = 4;
		orbitalPeriod.setText("Orbital Period:");
		orbitalPeriod.setLayoutData(insertGD);

		insertGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		insertGD.horizontalSpan = 1;
		Text periodBox = new Text(planetInsertPage, SWT.NONE);
		periodBox.setLayoutData(insertGD);

		insertGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label orbitalDays = new Label(planetInsertPage, SWT.FILL);
		insertGD.horizontalSpan = 1;
		orbitalDays.setText("Earth days");
		orbitalDays.setLayoutData(insertGD);

		insertGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label population = new Label(planetInsertPage, SWT.FILL);
		insertGD.horizontalSpan = 4;
		population.setText("Population:");
		population.setLayoutData(insertGD);

		insertGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		insertGD.horizontalSpan = 1;
		Text popBox = new Text(planetInsertPage, SWT.NONE);
		popBox.setLayoutData(insertGD);

		insertGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label inGalaxy = new Label(planetInsertPage, SWT.FILL);
		insertGD.horizontalSpan = 4;
		inGalaxy.setText("In Galaxy:");
		inGalaxy.setLayoutData(insertGD);

		insertGD = new GridData(SWT.FILL, SWT.CENTER, false, false);
		Text galaxySelect = new Text(planetInsertPage, SWT.READ_ONLY);
		insertGD.horizontalSpan = 2;
		galaxySelect.setLayoutData(insertGD);

		insertGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label errorText = new Label(planetInsertPage, SWT.FILL);
		insertGD.horizontalSpan = 4;
		errorText.setText("");
		errorText.setLayoutData(insertGD);
		errorText.setVisible(false);
		// This sets the color of the error text label to red
		

		insertGD = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		Button submit = new Button(planetInsertPage, SWT.PUSH);
		insertGD.horizontalSpan = 1;
		submit.setText("Submit");
		submit.pack();
		submit.setLayoutData(insertGD);

		insertGD = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		Button refresh = new Button(planetInsertPage, SWT.PUSH);
		insertGD.horizontalSpan = 1;
		refresh.setText("Refresh");
		refresh.pack();
		refresh.setLayoutData(insertGD);

		// Now that all the widgets are made, we want to populate the
		// "Orbits Star" and "In Galaxy" menus with
		// values that are actually in the DB, easier to do this than check if a
		// custom entered one is in the DB,
		// and if it wasn't we would have to create an INSERT statement for
		// that.
		try {
			PreparedStatement getStarNames = conn
					.prepareStatement("SELECT DISTINCT name FROM star ORDER BY name;");
			getStarNames.execute();
			ResultSet rs = getStarNames.getResultSet();
			ResultSetMetaData rsmd = rs.getMetaData();

			while (rs.next()) {
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					String columnValue = rs.getString(i);
					starSelect.add(WordUtils.capitalize(columnValue));
				}
			}


		} catch (SQLException e) {
			System.out.println("Planet insert refresh SQL error");
			//e.printStackTrace();
		}

		
		starSelect.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					PreparedStatement getGalaxyName = conn
							.prepareStatement("SELECT inGalaxy FROM star WHERE name='" + starSelect.getText() + "';");
					getGalaxyName.execute();
					ResultSet rs = getGalaxyName.getResultSet();
					ResultSetMetaData rsmd = rs.getMetaData();

					while (rs.next()) {
						for (int i = 1; i <= rsmd.getColumnCount(); i++) {
							String columnValue = rs.getString(i);
							galaxySelect.setText(columnValue);
						}
					}


				} catch (SQLException e2) {
					System.out.println("Planet insert inGalaxy SQL error");
					//e.printStackTrace();
				}
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		// Listener for the submit button, the event type is SWT.Selection, NOT
		// SWT.PUSH
		submit.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				boolean error = false;
				// Just some basic error checking before we make the SQL
				// statement
				if (nameBox.getText().equals("")) {
					errorText.setForeground(Display.getCurrent().getSystemColor(
							SWT.COLOR_RED));
					errorText.setText("Name must not be empty.");
					errorText.setVisible(true);
					error = true;
					return;
				}
				if (!NumberUtils.isNumber(massBox.getText())) {
					errorText.setForeground(Display.getCurrent().getSystemColor(
							SWT.COLOR_RED));
					errorText.setText("Mass must be a number.");
					errorText.setVisible(true);
					error = true;
					return;
				}
				if (NumberUtils.isNumber(massBox.getText())) {
					errorText.setForeground(Display.getCurrent().getSystemColor(
							SWT.COLOR_RED));
					if (Integer.parseInt(massBox.getText()) <= 0) {
						errorText.setText("Mass must be > 0.");
						errorText.setVisible(true);
						error = true;
						return;
					}

				}
				if (starSelect.getText().equals("")) {
					errorText.setForeground(Display.getCurrent().getSystemColor(
							SWT.COLOR_RED));
					errorText.setText("You must select a Star to orbit.");
					errorText.setVisible(true);
					error = true;
					return;
				}
				if (NumberUtils.isNumber(periodBox.getText())) {
					if (Integer.parseInt(periodBox.getText()) <= 0) {
						errorText.setForeground(Display.getCurrent().getSystemColor(
								SWT.COLOR_RED));
						errorText.setText("Orbital Period must be > 0.");
						errorText.setVisible(true);
						error = true;
						return;
					}

				}
				if (!NumberUtils.isNumber(periodBox.getText())) {
					errorText.setForeground(Display.getCurrent().getSystemColor(
							SWT.COLOR_RED));
					errorText.setText("Orbital Period must be a number.");
					errorText.setVisible(true);
					error = true;
					return;
				}
				if (NumberUtils.isNumber(popBox.getText())) {
					if (Long.parseLong(popBox.getText()) < 0) {
						errorText.setForeground(Display.getCurrent().getSystemColor(
								SWT.COLOR_RED));
						errorText
								.setText("Population must be a non-negative value.");
						errorText.setVisible(true);
						error = true;
						return;
					}

				}
				if (!NumberUtils.isNumber(popBox.getText())) {
					errorText.setForeground(Display.getCurrent().getSystemColor(
							SWT.COLOR_RED));
					errorText.setText("Population must be a number.");
					errorText.setVisible(true);
					error = true;
					return;
				}
				if (galaxySelect.getText().equals("")) {
					errorText.setForeground(Display.getCurrent().getSystemColor(
							SWT.COLOR_RED));
					errorText.setText("You must select a Galaxy.");
					errorText.setVisible(true);
					error = true;
					return;
				}
				if (!error) {
					errorText.setVisible(false);
					// Now for the actual SQL statement construction
					try {
						PreparedStatement insertNewPlanet = conn
								.prepareStatement("INSERT INTO Planet VALUE(\""
										+ WordUtils.capitalize(nameBox
												.getText()) + "\","
										+ massBox.getText() + ",\""
										+ starSelect.getText() + "\","
										+ periodBox.getText() + ","
										+ popBox.getText() + ",\""
										+ galaxySelect.getText() + "\");");
						// This sends it to the server
						insertNewPlanet.execute();
						//System.out.println("Inserting " + nameBox.getText());

					} catch (SQLException e) {
						//System.out.println("SQL Error");
						if (e.getMessage().contains("Duplicate")) {
							errorText.setForeground(Display.getCurrent().getSystemColor(
									SWT.COLOR_RED));
							errorText
									.setText("Entry already exists in database.");
							errorText.setVisible(true);
						}
					}
					errorText.setForeground(Display.getCurrent().getSystemColor(
							SWT.COLOR_BLACK));
					errorText
							.setText(nameBox.getText() + " successfully inserted.");
					errorText.setVisible(true);
					return;
				}

			}
		});

		// This is for the refresh button, when pushed we want it to re-query
		// the server for any possible stars
		// or galaxies added. The composites DO NOT refresh on their own.
		refresh.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				errorText.setVisible(false);
				try {
					PreparedStatement getStarNames = conn
							.prepareStatement("SELECT DISTINCT name FROM star ORDER BY name;");
					getStarNames.execute();
					ResultSet rs = getStarNames.getResultSet();
					ResultSetMetaData rsmd = rs.getMetaData();
					
					// Removes all of the options from the starSelect Combo
					starSelect.removeAll();
					// Now we add a fresh list back in
					while (rs.next()) {
						for (int i = 1; i <= rsmd.getColumnCount(); i++) {
							String columnValue = rs.getString(i);
							starSelect.add(WordUtils.capitalize(columnValue));
						}
					}

					// Do the same for the galaxy list
					galaxySelect.setText("");
					

				} catch (SQLException e) {
					System.out.println("PLanet insert refresh SQL error");
					//e.printStackTrace();
				}
			}
		});

		// Tell the composite to use that layout and its widget children
		planetInsertPage.setLayout(insertGL);
		return planetInsertPage;
	}
}
