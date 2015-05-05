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

public class QueryPlanetPage {
	public static Composite createPlanetPage(Composite parent, Connection conn) {
		Composite planetQueryPage = new Composite(parent, SWT.NONE);
		// planetInsertPage.setBackground(new Color(Display.getCurrent(), 255,
		// 0, 0));
		GridLayout queryGL = new GridLayout(4, true);

		GridData queryGD = new GridData(SWT.FILL, SWT.CENTER, true, false);

		Label name = new Label(planetQueryPage, SWT.FILL);
		queryGD.horizontalSpan = 4;
		name.setText("Name:");
		name.setLayoutData(queryGD);

		queryGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		queryGD.horizontalSpan = 2;
		Combo queryPlanetNames = new Combo(planetQueryPage, SWT.READ_ONLY);
		queryPlanetNames.setLayoutData(queryGD);

		queryGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label errorText = new Label(planetQueryPage, SWT.FILL);
		queryGD.horizontalSpan = 4;
		errorText.setText("");
		errorText.setLayoutData(queryGD);
		errorText.setVisible(false);
		errorText.setForeground(Display.getCurrent().getSystemColor(
				SWT.COLOR_RED));

		queryGD = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		Button submit = new Button(planetQueryPage, SWT.PUSH);
		queryGD.horizontalSpan = 1;
		submit.setText("Submit");
		submit.pack();
		submit.setLayoutData(queryGD);

		queryGD = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		Button refresh = new Button(planetQueryPage, SWT.PUSH);
		queryGD.horizontalSpan = 1;
		refresh.setText("Refresh");
		refresh.pack();
		refresh.setLayoutData(queryGD);

		refresh(queryPlanetNames, conn);

		refresh.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				refresh(queryPlanetNames, conn);
			}
		});

		submit.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				createPlanetPage(conn, queryPlanetNames.getText());
			}
		});

		planetQueryPage.setLayout(queryGL);
		return planetQueryPage;
	}

	public static void refresh(Combo queryPlanetNames, Connection conn) {

		try {
			queryPlanetNames.removeAll();
			PreparedStatement getTableNames = conn
					.prepareStatement("SELECT name FROM planet ORDER BY name;");
			getTableNames.execute();

			ResultSet rs = getTableNames.getResultSet();
			ResultSetMetaData rsmd = rs.getMetaData();

			while (rs.next()) {
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					String columnValue = rs.getString(i);
					// This inserts the names of the tables into the
					// drop-down menu
					queryPlanetNames.add(WordUtils.capitalize(columnValue));

				}
			}

		} catch (SQLException e) {
			System.out.println("SQL Error");
		}
	}

	public static void createPlanetPage(Connection conn, String planetName) {
		Shell shell = new Shell(Display.getCurrent());
		// Composite c = new Composite(shell, SWT.BORDER);
		GridLayout gl = new GridLayout(1, true);

		GridData queryGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		try {
			PreparedStatement queryEntry = conn
					.prepareStatement("SELECT * FROM planet WHERE name='"
							+ planetName + "';");
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

					String columnName = rsmd.getColumnName(i);
					System.out.println(columnName);
					if (rsmd.getColumnName(i).equalsIgnoreCase("Mass"))
						
						resultData.setText(WordUtils.capitalize(columnValue)
								+ " kg");
					
					else if (rsmd.getColumnName(i).equalsIgnoreCase("orbitalPeriod"))
						resultData.setText(WordUtils.capitalize(columnValue)
								+ " Earth Days");
					else
						resultData.setText(WordUtils.capitalize(columnValue));

					resultData.setLayoutData(queryGD);
				}
			}
		} catch (SQLException e) {

		}

		queryGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label moonNames = new Label(shell, SWT.FILL);
		moonNames.setText("Moons orbiting " + planetName);
		moonNames.setLayoutData(queryGD);

		queryGD = new GridData(SWT.FILL, SWT.FILL, true, true);
		List moons = new List(shell, SWT.V_SCROLL);
		moons.setLayoutData(queryGD);
		try {
			PreparedStatement queryMoons = conn
					.prepareStatement("SELECT name FROM moon WHERE orbitsPlanet='"
							+ planetName + "';");
			queryMoons.execute();
			ResultSet rs = queryMoons.getResultSet();
			ResultSetMetaData rsmd = rs.getMetaData();

			while (rs.next()) {
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					String columnValue = rs.getString(i);
					moons.add(columnValue);
				}
			}
		} catch (SQLException e) {

		}
		
		queryGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label speciesNames = new Label(shell, SWT.FILL);
		speciesNames.setText("Species Inhabiting " + planetName);
		speciesNames.setLayoutData(queryGD);

		queryGD = new GridData(SWT.FILL, SWT.FILL, true, true);
		List species = new List(shell, SWT.V_SCROLL);
		species.setLayoutData(queryGD);
		try {
			PreparedStatement querySpecies = conn
					.prepareStatement("SELECT speciesName FROM inhabits WHERE planetName='"
							+ planetName + "';");
			querySpecies.execute();
			ResultSet rs = querySpecies.getResultSet();
			ResultSetMetaData rsmd = rs.getMetaData();

			while (rs.next()) {
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					String columnValue = rs.getString(i);
					species.add(columnValue);
				}
			}
		} catch (SQLException e) {

		}
		
		moons.addSelectionListener( new SelectionAdapter() { 
			public void widgetDefaultSelected( SelectionEvent e ) { 
				try{
				QueryMoonPage.createMoonPage(conn, moons.getSelection()[0]);
				}catch(Exception e2){
					
				}
			}
		});
		
		species.addSelectionListener( new SelectionAdapter() { 
			public void widgetDefaultSelected( SelectionEvent e ) { 
				try{
				QuerySpeciesPage.createSpeciestPage(conn, species.getSelection()[0]);
				}catch(Exception e2){
					
				}
			}
		});

		shell.setLayout(gl);
		shell.setSize(400, 600);
		shell.open();
		while (!shell.isDisposed()) {
			if (!Display.getCurrent().readAndDispatch()) {
				Display.getCurrent().sleep();
			}
		}
	}
}
