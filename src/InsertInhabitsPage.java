import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

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


public class InsertInhabitsPage {
	public static Composite createInhabitsPage(Composite parent, Connection conn) {
		Composite inhabitsInsertPage = new Composite(parent, SWT.NONE);
		// planetInsertPage.setBackground(new Color(Display.getCurrent(), 255,
		// 0, 0));
		
		//Same stuff as before
		GridLayout insertGL = new GridLayout(4, true);

		GridData insertGD = new GridData(SWT.FILL, SWT.CENTER, true, false);


		insertGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label species = new Label(inhabitsInsertPage, SWT.FILL);
		insertGD.horizontalSpan = 4;
		species.setText("Species:");
		species.setLayoutData(insertGD);

		insertGD = new GridData(SWT.FILL, SWT.CENTER, false, false);
		Combo speciesSelect = new Combo(inhabitsInsertPage, SWT.DROP_DOWN
				| SWT.READ_ONLY);
		insertGD.horizontalSpan = 2;
		speciesSelect.setLayoutData(insertGD);


		insertGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label planet = new Label(inhabitsInsertPage, SWT.FILL);
		insertGD.horizontalSpan = 4;
		planet.setText("Planet:");
		planet.setLayoutData(insertGD);

		insertGD = new GridData(SWT.FILL, SWT.CENTER, false, false);
		Combo planetSelect = new Combo(inhabitsInsertPage, SWT.DROP_DOWN
				| SWT.READ_ONLY);
		insertGD.horizontalSpan = 2;
		planetSelect.setLayoutData(insertGD);

		insertGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label errorText = new Label(inhabitsInsertPage, SWT.FILL);
		insertGD.horizontalSpan = 4;
		errorText.setText("");
		errorText.setLayoutData(insertGD);
		errorText.setVisible(false);		

		insertGD = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		Button submit = new Button(inhabitsInsertPage, SWT.PUSH);
		insertGD.horizontalSpan = 1;
		submit.setText("Submit");
		submit.pack();
		submit.setLayoutData(insertGD);
		
		insertGD = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		Button refresh = new Button(inhabitsInsertPage, SWT.PUSH);
		insertGD.horizontalSpan = 1;
		refresh.setText("Refresh");
		refresh.pack();
		refresh.setLayoutData(insertGD);

		// Now that all the widgets are made, we want to populate the "Orbits Star" and "In Galaxy" menus with
		// values that are actually in the DB, easier to do this than check if a custom entered one is in the DB,
		// and if it wasn't we would have to create an INSERT statement for that.
		try {
			PreparedStatement getSpeciesNames = conn
					.prepareStatement("SELECT DISTINCT name FROM Species ORDER BY name;");
			getSpeciesNames.execute();
			ResultSet rs = getSpeciesNames.getResultSet();
			ResultSetMetaData rsmd = rs.getMetaData();

			PreparedStatement getPlanetNames = conn
					.prepareStatement("SELECT DISTINCT name FROM Planet ORDER BY name;");
			getPlanetNames.execute();

			while (rs.next()) {
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					String columnValue = rs.getString(i);
					speciesSelect.add(WordUtils.capitalize(columnValue));
				}
			}

			rs = getPlanetNames.getResultSet();
			rsmd = rs.getMetaData();

			while (rs.next()) {
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					String columnValue = rs.getString(i);
					planetSelect.add(WordUtils.capitalize(columnValue));
				}
			}

		} catch (SQLException e) {
			System.out.println("SQL Error");
			e.printStackTrace();
		}

		//Listener for the submit button, the event type is SWT.Selection, NOT SWT.PUSH
		submit.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				boolean error = false;
				// Just some basic error checking before we make the SQL statement
				
				if (speciesSelect.getText().equals("")) {
					errorText.setForeground(Display.getCurrent().getSystemColor(
							SWT.COLOR_RED));
					errorText.setText("You must select a Species.");
					errorText.setVisible(true);
					error = true;
				}
				if (planetSelect.getText().equals("")) {
					errorText.setForeground(Display.getCurrent().getSystemColor(
							SWT.COLOR_RED));
					errorText.setText("You must select a Planet.");
					errorText.setVisible(true);
					error = true;
				}
				if (!error) {
					errorText.setVisible(false);
					//Now for the actual SQL statement construction
					try {
						PreparedStatement insertNewInhabits = conn
								.prepareStatement("INSERT INTO Inhabits VALUE(\""
										+ speciesSelect.getText() + "\",\""
										+ planetSelect.getText() + "\");");
						//This sends it to the server
						insertNewInhabits.execute();
						//ystem.out.println("Inserting " + speciesSelect.getText() + " inhabits " + planetSelect.getText());

					} catch (SQLException e) {
						//System.out.println("SQL Error");
						if(e.getMessage().contains("Duplicate")){
							errorText.setForeground(Display.getCurrent().getSystemColor(
									SWT.COLOR_RED));
							errorText.setText("Entry already exists in database.");
							errorText.setVisible(true);
							return;
						}
					}
					errorText.setForeground(Display.getCurrent().getSystemColor(
							SWT.COLOR_BLACK));
					errorText
							.setText(speciesSelect.getText() + "-" + planetSelect.getText() + " successfully inserted.");
					errorText.setVisible(true);
					return;
				}

			}
		});
		
		// This is for the refresh button, when pushed we want it to re-query the server for any possible stars
		// or galaxies added. The composites DO NOT refresh on their own.
		refresh.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				errorText.setVisible(false);
				try {
					PreparedStatement getSpeciesNames = conn
							.prepareStatement("SELECT DISTINCT name FROM Species ORDER BY name;");
					getSpeciesNames.execute();
					ResultSet rs = getSpeciesNames.getResultSet();
					ResultSetMetaData rsmd = rs.getMetaData();

					PreparedStatement getPlanetNames = conn
							.prepareStatement("SELECT DISTINCT name FROM Planet ORDER BY name;");
					getPlanetNames.execute();
					
					// Removes all of the options from the starSelect Combo
					speciesSelect.removeAll();
					//Now we add a fresh list back in
					while (rs.next()) {
						for (int i = 1; i <= rsmd.getColumnCount(); i++) {
							String columnValue = rs.getString(i);
							speciesSelect.add(WordUtils.capitalize(columnValue));
						}
					}

					rs = getPlanetNames.getResultSet();
					rsmd = rs.getMetaData();
					
					// Do the same for the galaxy list
					planetSelect.removeAll();
					while (rs.next()) {
						for (int i = 1; i <= rsmd.getColumnCount(); i++) {
							String columnValue = rs.getString(i);
							planetSelect.add(WordUtils.capitalize(columnValue));
						}
					}

				} catch (SQLException e) {
					System.out.println("Inhabits insertion refresh SQL error.");
					e.printStackTrace();
				}
			}
		});

		//Tell the composite to use that layout and its widget children
		inhabitsInsertPage.setLayout(insertGL);
		return inhabitsInsertPage;
	}
}
