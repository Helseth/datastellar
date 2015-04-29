import java.sql.*;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class MainWindow {

	public MainWindow(Connection conn) {

		Display display = new Display();
		Shell shell = new Shell(display);
		GridLayout gl = new GridLayout(4, true);
		GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label action = new Label(shell, SWT.FILL);
		gd.horizontalSpan = 4;
		action.setText("Action:");
		action.setLayoutData(gd);

		gd = new GridData(SWT.FILL, SWT.CENTER, false, false);
		Combo actionSelect = new Combo(shell, SWT.DROP_DOWN | SWT.READ_ONLY);
		gd.horizontalSpan = 2;
		actionSelect.setLayoutData(gd);
		actionSelect.add("Insert");
		actionSelect.add("Update");
		actionSelect.add("Delete");
		actionSelect.add("Query");

		// Create shared composite, this one is what gets swapped out
		Composite sharedComposite = new Composite(shell, SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.horizontalSpan = 4;
		sharedComposite.setLayoutData(gd);
		StackLayout sl = new StackLayout();
		sharedComposite.setLayout(sl);

		// Selected composite
		Composite insertPage = createInsertPage(sharedComposite, conn);
		Composite updatePage = createUpdatePage(sharedComposite, conn);

		shell.setLayout(gl);

		shell.setSize(shell.getSize().x / 2, shell.getSize().y);

		actionSelect.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				if (actionSelect.getText().equalsIgnoreCase("Insert")) {
					sl.topControl = insertPage;
					sharedComposite.layout();
				}
				if (actionSelect.getText().equalsIgnoreCase("Update")) {
					sl.topControl = updatePage;
					sharedComposite.layout();
				}
			}
		});
		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		System.exit(0);
	}

	public Composite createInsertPage(Composite parent, Connection conn) {

		Composite insertPage = new Composite(parent, SWT.BORDER);
		GridLayout insertGL = new GridLayout(4, true);

		GridData insertGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label tableInsertName = new Label(insertPage, SWT.NONE);
		insertGD.horizontalSpan = 4;
		tableInsertName.setText("Select a table to insert into:");
		tableInsertName.setLayoutData(insertGD);

		insertGD = new GridData(SWT.FILL, SWT.CENTER, false, false);
		Combo insertTableSelect = new Combo(insertPage, SWT.DROP_DOWN
				| SWT.READ_ONLY);
		insertGD.horizontalSpan = 2;
		insertTableSelect.setLayoutData(insertGD);

		Composite sharedComposite = new Composite(insertPage, SWT.BORDER);
		insertGD = new GridData(SWT.FILL, SWT.FILL, true, true);
		insertGD.horizontalSpan = 4;
		sharedComposite.setLayoutData(insertGD);
		StackLayout sl = new StackLayout();
		sharedComposite.setLayout(sl);

		try {
			PreparedStatement getTableNames = conn
					.prepareStatement("SELECT table_name FROM information_schema.tables WHERE table_schema='datastellar';");
			getTableNames.execute();
			ResultSet rs = getTableNames.getResultSet();
			ResultSetMetaData rsmd = rs.getMetaData();

			while (rs.next()) {
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					String columnValue = rs.getString(i);
					insertTableSelect.add(WordUtils.capitalize(columnValue));
				}
			}

		} catch (SQLException e) {
			System.out.println("SQL Error");
			e.printStackTrace();
		}

		Composite planetPage = createPlanetPage(sharedComposite, conn);

		insertTableSelect.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				if (insertTableSelect.getText().equalsIgnoreCase("Planet")) {
					sl.topControl = planetPage;
					sharedComposite.layout();
				}
				if (insertTableSelect.getText().equalsIgnoreCase("Update")) {
					// sl.topControl = updatePage;
					sharedComposite.layout();
				}
			}
		});

		insertPage.setLayout(insertGL);
		return insertPage;
	}

	public Composite createUpdatePage(Composite parent, Connection conn) {

		Composite updatePage = new Composite(parent, SWT.BORDER);
		GridLayout updateGL = new GridLayout(4, true);

		GridData updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label tableUpdateName = new Label(updatePage, SWT.NONE);
		updateGD.horizontalSpan = 4;
		tableUpdateName.setText("Select a table to update:");
		tableUpdateName.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, false, false);
		Combo insertTableSelect = new Combo(updatePage, SWT.DROP_DOWN
				| SWT.READ_ONLY);
		updateGD.horizontalSpan = 2;
		insertTableSelect.setLayoutData(updateGD);

		try {
			PreparedStatement getTableNames = conn
					.prepareStatement("SELECT table_name FROM information_schema.tables WHERE table_schema='datastellar';");
			getTableNames.execute();
			ResultSet rs = getTableNames.getResultSet();
			ResultSetMetaData rsmd = rs.getMetaData();

			while (rs.next()) {
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					String columnValue = rs.getString(i);
					insertTableSelect.add(WordUtils.capitalize(columnValue));
				}
			}

		} catch (SQLException e) {
			System.out.println("SQL Error");
			e.printStackTrace();
		}

		updatePage.setLayout(updateGL);
		return updatePage;
	}

	public Composite createPlanetPage(Composite parent, Connection conn) {
		Composite planetInsertPage = new Composite(parent, SWT.BORDER);
		// planetInsertPage.setBackground(new Color(Display.getCurrent(), 255,
		// 0, 0));
		GridLayout updateGL = new GridLayout(4, true);

		GridData updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);

		Label name = new Label(planetInsertPage, SWT.FILL);
		updateGD.horizontalSpan = 4;
		name.setText("Name:");
		name.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		updateGD.horizontalSpan = 1;
		Text nameBox = new Text(planetInsertPage, SWT.NONE);
		nameBox.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label mass = new Label(planetInsertPage, SWT.FILL);
		updateGD.horizontalSpan = 4;
		mass.setText("Mass:");
		mass.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		updateGD.horizontalSpan = 1;
		Text massBox = new Text(planetInsertPage, SWT.NONE);
		massBox.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label starOrbit = new Label(planetInsertPage, SWT.FILL);
		updateGD.horizontalSpan = 4;
		starOrbit.setText("Orbits Star:");
		starOrbit.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, false, false);
		Combo starSelect = new Combo(planetInsertPage, SWT.DROP_DOWN
				| SWT.READ_ONLY);
		updateGD.horizontalSpan = 2;
		starSelect.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label orbitalPeriod = new Label(planetInsertPage, SWT.FILL);
		updateGD.horizontalSpan = 4;
		orbitalPeriod.setText("Orbital Period:");
		orbitalPeriod.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		updateGD.horizontalSpan = 1;
		Text periodBox = new Text(planetInsertPage, SWT.NONE);
		periodBox.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label population = new Label(planetInsertPage, SWT.FILL);
		updateGD.horizontalSpan = 4;
		population.setText("Population:");
		population.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		updateGD.horizontalSpan = 1;
		Text popBox = new Text(planetInsertPage, SWT.NONE);
		popBox.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label inGalaxy = new Label(planetInsertPage, SWT.FILL);
		updateGD.horizontalSpan = 4;
		inGalaxy.setText("In Galaxy:");
		inGalaxy.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, false, false);
		Combo galaxySelect = new Combo(planetInsertPage, SWT.DROP_DOWN
				| SWT.READ_ONLY);
		updateGD.horizontalSpan = 2;
		galaxySelect.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label errorText = new Label(planetInsertPage, SWT.FILL);
		updateGD.horizontalSpan = 4;
		errorText.setText("");
		errorText.setLayoutData(updateGD);
		errorText.setVisible(false);
		errorText.setForeground(Display.getCurrent().getSystemColor(
				SWT.COLOR_RED));

		updateGD = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		Button submit = new Button(planetInsertPage, SWT.PUSH);
		updateGD.horizontalSpan = 1;
		submit.setText("Submit");
		submit.pack();
		submit.setLayoutData(updateGD);

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

		submit.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				boolean error = false;
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
					if (Integer.parseInt(popBox.getText()) < 0) {
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
					System.out.println("INSERT INTO Planet VALUE(\""
							+ nameBox.getText() + "," + massBox.getText()
							+ ",\"" + starSelect.getText() + "\","
							+ periodBox.getText() + "," + popBox.getText()
							+ ",\"" + galaxySelect.getText() + "\")");
					try {
						PreparedStatement insertNewPlanet = conn
								.prepareStatement("INSERT INTO Planet VALUE(\""
										+ nameBox.getText() + "\","
										+ massBox.getText() + ",\""
										+ starSelect.getText() + "\","
										+ periodBox.getText() + ","
										+ popBox.getText() + ",\""
										+ galaxySelect.getText() + "\");");
						insertNewPlanet.execute();
						System.out.println("Inserting " + nameBox.getText());

					} catch (SQLException e) {
						System.out.println("SQL Error");
						e.printStackTrace();
					}
				}

			}
		});

		planetInsertPage.setLayout(updateGL);
		return planetInsertPage;
	}

}
