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

public class QuerySpeciesPage {
	public static Composite createSpeciesPage(Composite parent, Connection conn) {
		Composite speciesQueryPage = new Composite(parent, SWT.NONE);
		// planetInsertPage.setBackground(new Color(Display.getCurrent(), 255,
		// 0, 0));
		GridLayout queryGL = new GridLayout(4, true);

		GridData queryGD = new GridData(SWT.FILL, SWT.CENTER, true, false);

		Label name = new Label(speciesQueryPage, SWT.FILL);
		queryGD.horizontalSpan = 4;
		name.setText("Name:");
		name.setLayoutData(queryGD);

		queryGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		queryGD.horizontalSpan = 2;
		Combo querySpeciesNames = new Combo(speciesQueryPage, SWT.READ_ONLY);
		querySpeciesNames.setLayoutData(queryGD);

		queryGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label errorText = new Label(speciesQueryPage, SWT.FILL);
		queryGD.horizontalSpan = 4;
		errorText.setText("");
		errorText.setLayoutData(queryGD);
		errorText.setVisible(false);
		errorText.setForeground(Display.getCurrent().getSystemColor(
				SWT.COLOR_RED));

		queryGD = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		Button submit = new Button(speciesQueryPage, SWT.PUSH);
		queryGD.horizontalSpan = 1;
		submit.setText("Submit");
		submit.pack();
		submit.setLayoutData(queryGD);

		queryGD = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		Button refresh = new Button(speciesQueryPage, SWT.PUSH);
		queryGD.horizontalSpan = 1;
		refresh.setText("Refresh");
		refresh.pack();
		refresh.setLayoutData(queryGD);

		refresh(querySpeciesNames, conn);

		refresh.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				refresh(querySpeciesNames, conn);
			}
		});

		submit.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				createSpeciestPage(conn, querySpeciesNames.getText());
			}
		});

		speciesQueryPage.setLayout(queryGL);
		return speciesQueryPage;
	}

	public static void refresh(Combo querySpeciesName, Connection conn) {

		try {
			querySpeciesName.removeAll();
			PreparedStatement getTableNames = conn
					.prepareStatement("SELECT name FROM species ORDER BY name;");
			getTableNames.execute();

			ResultSet rs = getTableNames.getResultSet();
			ResultSetMetaData rsmd = rs.getMetaData();

			while (rs.next()) {
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					String columnValue = rs.getString(i);
					// This inserts the names of the tables into the
					// drop-down menu
					querySpeciesName.add(WordUtils.capitalize(columnValue));

				}
			}

		} catch (SQLException e) {
			System.out.println("SQL Error");
		}
	}

	public static void createSpeciestPage(Connection conn, String speciesName) {
		Shell shell = new Shell(Display.getCurrent());
		// Composite c = new Composite(shell, SWT.BORDER);
		GridLayout gl = new GridLayout(1, true);

		GridData queryGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		try {
			PreparedStatement queryEntry = conn
					.prepareStatement("SELECT * FROM species WHERE name='"
							+ speciesName + "';");
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

					if (rsmd.getColumnName(i).equalsIgnoreCase("height"))
						resultData.setText(WordUtils.capitalize(columnValue)
								+ " ft");
					else
						resultData.setText(WordUtils.capitalize(columnValue));

					resultData.setLayoutData(queryGD);
				}
			}
		} catch (SQLException e) {

		}
		
		queryGD = new GridData(SWT.FILL, SWT.FILL, true, true);
		List planets = new List(shell, SWT.V_SCROLL);
		planets.setLayoutData(queryGD);
		try {
			PreparedStatement queryMoons = conn
					.prepareStatement("SELECT planetName FROM inhabits WHERE speciesName='"
							+ speciesName + "';");
			queryMoons.execute();
			ResultSet rs = queryMoons.getResultSet();
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
		shell.setSize(400, 400);
		shell.open();
		while (!shell.isDisposed()) {
			if (!Display.getCurrent().readAndDispatch()) {
				Display.getCurrent().sleep();
			}
		}
	}
}
