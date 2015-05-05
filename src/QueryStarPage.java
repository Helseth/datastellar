import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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

public class QueryStarPage {
	public static Composite createStarPage(Composite parent, Connection conn) {
		Composite starQueryPage = new Composite(parent, SWT.NONE);
		// planetInsertPage.setBackground(new Color(Display.getCurrent(), 255,
		// 0, 0));
		GridLayout queryGL = new GridLayout(4, true);

		GridData queryGD = new GridData(SWT.FILL, SWT.CENTER, true, false);

		Label name = new Label(starQueryPage, SWT.FILL);
		queryGD.horizontalSpan = 4;
		name.setText("Name:");
		name.setLayoutData(queryGD);

		queryGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		queryGD.horizontalSpan = 2;
		Combo queryStarNames = new Combo(starQueryPage, SWT.READ_ONLY);
		queryStarNames.setLayoutData(queryGD);

		queryGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label errorText = new Label(starQueryPage, SWT.FILL);
		queryGD.horizontalSpan = 4;
		errorText.setText("");
		errorText.setLayoutData(queryGD);
		errorText.setVisible(false);
		errorText.setForeground(Display.getCurrent().getSystemColor(
				SWT.COLOR_RED));

		queryGD = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		Button submit = new Button(starQueryPage, SWT.PUSH);
		queryGD.horizontalSpan = 1;
		submit.setText("Submit");
		submit.pack();
		submit.setLayoutData(queryGD);

		queryGD = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		Button refresh = new Button(starQueryPage, SWT.PUSH);
		queryGD.horizontalSpan = 1;
		refresh.setText("Refresh");
		refresh.pack();
		refresh.setLayoutData(queryGD);

		refresh(queryStarNames, conn);

		refresh.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				refresh(queryStarNames, conn);
			}
		});

		submit.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				createStarResults(conn, queryStarNames.getText());
			}
		});

		starQueryPage.setLayout(queryGL);
		return starQueryPage;
	}

	public static void refresh(Combo queryStarNames, Connection conn) {

		try {
			queryStarNames.removeAll();
			PreparedStatement getTableNames = conn
					.prepareStatement("SELECT name FROM star ORDER BY name;");
			getTableNames.execute();

			ResultSet rs = getTableNames.getResultSet();
			ResultSetMetaData rsmd = rs.getMetaData();

			while (rs.next()) {
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					String columnValue = rs.getString(i);
					// This inserts the names of the tables into the
					// drop-down menu
					queryStarNames.add(WordUtils.capitalize(columnValue));

				}
			}

		} catch (SQLException e) {
			System.out.println("SQL Error");
		}
	}

	public static void createStarResults(Connection conn, String starName) {
		Shell shell = new Shell(Display.getCurrent());
		// Composite c = new Composite(shell, SWT.BORDER);
		GridLayout gl = new GridLayout(1, true);

		GridData queryGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		try {
			PreparedStatement queryEntry = conn
					.prepareStatement("SELECT * FROM star WHERE name='"
							+ starName + "';");
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

					if (rsmd.getColumnName(i).equalsIgnoreCase("Mass"))
						resultData.setText(WordUtils.capitalize(columnValue)
								+ " kg");
					else
						resultData.setText(WordUtils.capitalize(columnValue));

					resultData.setLayoutData(queryGD);
				}
			}
		} catch (SQLException e) {

		}

		Label avgHostile = new Label(shell, SWT.FILL);
		avgHostile.setText("Average Hostility of Species in " + starName
				+ " System:");
		avgHostile.setLayoutData(queryGD);

		try {
			PreparedStatement queryEntry = conn
					.prepareStatement("SELECT AVG(hostility) from("
							+ "Select distinct Species.name, hostility"
							+ " FROM Star,Inhabits,Planet,Species"
							+ " WHERE Planet.orbitsStar = Star.name and"
							+ " Inhabits.speciesName = Species.name and"
							+ " Inhabits.planetName = Planet.name and"
							+ " Star.name ='" + starName + "')as subquery;");
			queryEntry.execute();
			ResultSet rs = queryEntry.getResultSet();
			ResultSetMetaData rsmd = rs.getMetaData();
			while (rs.next()) {
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					try {
						String columnValue = rs.getString(i);
						Label avgHostileVal = new Label(shell, SWT.FILL);
						avgHostileVal.setText(columnValue);
						avgHostileVal.setLayoutData(queryGD);
					} catch (IllegalArgumentException e) {

					}
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		Label starNames = new Label(shell, SWT.FILL);
		starNames.setText("Planets in " + starName + " System");
		starNames.setLayoutData(queryGD);

		queryGD = new GridData(SWT.FILL, SWT.FILL, true, true);
		List planets = new List(shell, SWT.V_SCROLL);
		planets.setLayoutData(queryGD);
		try {
			PreparedStatement queryStars = conn
					.prepareStatement("SELECT name FROM planet WHERE orbitsStar='"
							+ starName + "';");
			queryStars.execute();
			ResultSet rs = queryStars.getResultSet();
			ResultSetMetaData rsmd = rs.getMetaData();

			while (rs.next()) {
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					String columnValue = rs.getString(i);
					planets.add(columnValue);
				}
			}
		} catch (SQLException e) {

		}
		
		planets.addSelectionListener( new SelectionAdapter() { 
			public void widgetDefaultSelected( SelectionEvent e ) { 
				try{
				QueryPlanetPage.createPlanetPage(conn, planets.getSelection()[0]);
				}catch(Exception e2){
					
				}
			}
		});

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
