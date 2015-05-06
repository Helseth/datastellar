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


public class InsertMoonPage {
	public static Composite createMoonPage(Composite parent, Connection conn) {
		Composite moonInsertPage = new Composite(parent, SWT.NONE);
		// planetInsertPage.setBackground(new Color(Display.getCurrent(), 255,
		// 0, 0));
		GridLayout insertGL = new GridLayout(4, true);

		GridData insertGD = new GridData(SWT.FILL, SWT.CENTER, true, false);

		Label name = new Label(moonInsertPage, SWT.FILL);
		insertGD.horizontalSpan = 4;
		name.setText("Name:");
		name.setLayoutData(insertGD);

		insertGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		insertGD.horizontalSpan = 1;
		Text nameBox = new Text(moonInsertPage, SWT.NONE);
		nameBox.setLayoutData(insertGD);

		insertGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label mass = new Label(moonInsertPage, SWT.FILL);
		insertGD.horizontalSpan = 4;
		mass.setText("Mass:");
		mass.setLayoutData(insertGD);

		insertGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		insertGD.horizontalSpan = 1;
		Text massBox = new Text(moonInsertPage, SWT.NONE);
		massBox.setLayoutData(insertGD);
		
		Label massUnit = new Label(moonInsertPage, SWT.FILL);
		insertGD.horizontalSpan = 1;
		massUnit.setText("kg");
		massUnit.setLayoutData(insertGD);

		insertGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label planetOrbit = new Label(moonInsertPage, SWT.FILL);
		insertGD.horizontalSpan = 4;
		planetOrbit.setText("Orbits Planet:");
		planetOrbit.setLayoutData(insertGD);

		insertGD = new GridData(SWT.FILL, SWT.CENTER, false, false);
		Combo planetSelect = new Combo(moonInsertPage, SWT.DROP_DOWN
				| SWT.READ_ONLY);
		insertGD.horizontalSpan = 2;
		planetSelect.setLayoutData(insertGD);

		insertGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label orbitalPeriod = new Label(moonInsertPage, SWT.FILL);
		insertGD.horizontalSpan = 4;
		orbitalPeriod.setText("Orbital Period:");
		orbitalPeriod.setLayoutData(insertGD);

		insertGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		insertGD.horizontalSpan = 1;
		Text periodBox = new Text(moonInsertPage, SWT.NONE);
		periodBox.setLayoutData(insertGD);
		
		insertGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label orbitalDays = new Label(moonInsertPage, SWT.FILL);
		insertGD.horizontalSpan = 1;
		orbitalDays.setText("Earth days");
		orbitalDays.setLayoutData(insertGD);

		insertGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label inGalaxy = new Label(moonInsertPage, SWT.FILL);
		insertGD.horizontalSpan = 4;
		inGalaxy.setText("In Galaxy:");
		inGalaxy.setLayoutData(insertGD);

		insertGD = new GridData(SWT.FILL, SWT.CENTER, false, false);
		Text galaxySelect = new Text(moonInsertPage, SWT.READ_ONLY);
		insertGD.horizontalSpan = 2;
		galaxySelect.setLayoutData(insertGD);

		insertGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label errorText = new Label(moonInsertPage, SWT.FILL);
		insertGD.horizontalSpan = 4;
		errorText.setText("");
		errorText.setLayoutData(insertGD);
		errorText.setVisible(false);
		

		insertGD = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		Button submit = new Button(moonInsertPage, SWT.PUSH);
		insertGD.horizontalSpan = 1;
		submit.setText("Submit");
		submit.pack();
		submit.setLayoutData(insertGD);
		
		insertGD = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		Button refresh = new Button(moonInsertPage, SWT.PUSH);
		insertGD.horizontalSpan = 1;
		refresh.setText("Refresh");
		refresh.pack();
		refresh.setLayoutData(insertGD);

		try {
			PreparedStatement getPlanetNames = conn
					.prepareStatement("SELECT DISTINCT name FROM planet ORDER BY name;");
			getPlanetNames.execute();
			ResultSet rs = getPlanetNames.getResultSet();
			ResultSetMetaData rsmd = rs.getMetaData();

			PreparedStatement getGalaxyNames = conn
					.prepareStatement("SELECT DISTINCT name FROM galaxy ORDER BY name;");
			getGalaxyNames.execute();

			while (rs.next()) {
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					String columnValue = rs.getString(i);
					planetSelect.add(WordUtils.capitalize(columnValue));
				}
			}

			planetSelect.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					try {
						PreparedStatement getGalaxyName = conn
								.prepareStatement("SELECT inGalaxy FROM planet WHERE name='" + planetSelect.getText() + "';");
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
						System.out.println("Moon insert inGalaxy SQL error");
						//e.printStackTrace();
					}
					
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub
					
				}
			});

		} catch (SQLException e) {
			System.out.println("Moon insert refresh SQL error.");
			//e.printStackTrace();
		}

		submit.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				boolean error = false;
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
				if (planetSelect.getText().equals("")) {
					errorText.setForeground(Display.getCurrent().getSystemColor(
							SWT.COLOR_RED));
					errorText.setText("You must select a Planet to orbit.");
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
					try {
						PreparedStatement insertNewPlanet = conn
								.prepareStatement("INSERT INTO moon VALUE(\""
							+ WordUtils.capitalize(nameBox.getText()) + "\"," + massBox.getText()
							+ ",\"" + planetSelect.getText() + "\","
							+ periodBox.getText() + ",\"" + galaxySelect.getText() + "\");");
						insertNewPlanet.execute();
						System.out.println("Inserting " + nameBox.getText());

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
							.setText(nameBox.getText() + " successfully inserted.");
					errorText.setVisible(true);
					return;
				}

			}
		});
		
		refresh.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				errorText.setVisible(false);
				try {
					PreparedStatement getPlanetNames = conn
							.prepareStatement("SELECT DISTINCT name FROM planet ORDER BY name;");
					getPlanetNames.execute();
					ResultSet rs = getPlanetNames.getResultSet();
					ResultSetMetaData rsmd = rs.getMetaData();
					
					planetSelect.removeAll();
					while (rs.next()) {
						for (int i = 1; i <= rsmd.getColumnCount(); i++) {
							String columnValue = rs.getString(i);
							planetSelect.add(WordUtils.capitalize(columnValue));
						}
					}

					galaxySelect.setText("");

				} catch (SQLException e) {
					System.out.println("Moon insert refresh error.");
					e.printStackTrace();
				}
			}
		});

		moonInsertPage.setLayout(insertGL);
		return moonInsertPage;
	}
}
