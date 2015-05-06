import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

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

public class DeleteInhabitsPage {
	public static Composite createInhabitsPage(Composite parent, Connection conn) {
		Composite inhabitsDeletePage = new Composite(parent, SWT.NONE);
		// planetInsertPage.setBackground(new Color(Display.getCurrent(), 255,
		// 0, 0));
		GridLayout deleteGL = new GridLayout(4, true);

		GridData deleteGD = new GridData(SWT.FILL, SWT.CENTER, true, false);

		Label species = new Label(inhabitsDeletePage, SWT.FILL);
		deleteGD.horizontalSpan = 4;
		species.setText("Species:");
		species.setLayoutData(deleteGD);
		
		deleteGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		deleteGD.horizontalSpan = 2;
		Combo deleteInhabitsSpecies = new Combo(inhabitsDeletePage, SWT.READ_ONLY);
		deleteInhabitsSpecies.setLayoutData(deleteGD);
		
		deleteGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label planet = new Label(inhabitsDeletePage, SWT.FILL);
		deleteGD.horizontalSpan = 4;
		planet.setText("Planet:");
		planet.setLayoutData(deleteGD);
		
		deleteGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		deleteGD.horizontalSpan = 2;
		Combo deleteInhabitsPlanet = new Combo(inhabitsDeletePage, SWT.READ_ONLY);
		deleteInhabitsPlanet.setLayoutData(deleteGD);

		deleteGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label errorText = new Label(inhabitsDeletePage, SWT.FILL);
		deleteGD.horizontalSpan = 4;
		errorText.setText("");
		errorText.setLayoutData(deleteGD);
		errorText.setVisible(false);
		errorText.setForeground(Display.getCurrent().getSystemColor(
				SWT.COLOR_RED));

		deleteGD = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		Button submit = new Button(inhabitsDeletePage, SWT.PUSH);
		deleteGD.horizontalSpan = 1;
		submit.setText("Submit");
		submit.pack();
		submit.setLayoutData(deleteGD);

		deleteGD = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		Button refresh = new Button(inhabitsDeletePage, SWT.PUSH);
		deleteGD.horizontalSpan = 1;
		refresh.setText("Refresh");
		refresh.pack();
		refresh.setLayoutData(deleteGD);

		refresh(deleteInhabitsSpecies, deleteInhabitsPlanet, conn);
		//refresh(deleteInhabitsPlanet, conn);

		refresh.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				refresh(deleteInhabitsSpecies, deleteInhabitsPlanet, conn);
				//refresh(deleteInhabitsPlanet, conn);
				errorText.setVisible(false);
			}
		});

		submit.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				if(deleteInhabitsPlanet.getText().equalsIgnoreCase("") || deleteInhabitsSpecies.getText().equalsIgnoreCase("")){
					errorText.setForeground(Display.getCurrent().getSystemColor(
							SWT.COLOR_RED));
					errorText
							.setText("You must select a species-planet inhabits relation.");
					errorText.setVisible(true);
					return;
				}
				try {
					PreparedStatement deleteEntry = conn
							.prepareStatement("DELETE FROM inhabits WHERE speciesName='"
									+ deleteInhabitsSpecies.getText() + 
									"' AND planetName='" + deleteInhabitsPlanet.getText() + "';");
					deleteEntry.execute();

				} catch (SQLException e) {
					//System.out.println("SQL Error");
					// e.printStackTrace();
					if (e.getMessage().contains("foreign key constraint fails")) {
						errorText.setForeground(Display.getCurrent().getSystemColor(
								SWT.COLOR_RED));
						errorText
								.setText("Cannot delete entry, removing violates foreign key constraints.");
						errorText.setVisible(true);
						return;
					}
				}
				errorText.setForeground(Display.getCurrent().getSystemColor(
						SWT.COLOR_BLACK));
				errorText.setText("Entry " + deleteInhabitsSpecies.getText() + "-" + deleteInhabitsPlanet.getText()
						+ " successfully deleted.");
				errorText.setVisible(true);
			}
		});
		
		deleteInhabitsSpecies.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				PreparedStatement getTablePlanet;
				deleteInhabitsPlanet.removeAll();
				if(!deleteInhabitsSpecies.getText().equalsIgnoreCase("")){
					try {
						getTablePlanet = conn.prepareStatement("SELECT planetName FROM inhabits WHERE speciesName='" + deleteInhabitsSpecies.getText() +"';");
						getTablePlanet.execute();
						ResultSet rs = getTablePlanet.getResultSet();
						ResultSetMetaData rsmd = rs.getMetaData();

						while (rs.next()) {
							for (int i = 1; i <= rsmd.getColumnCount(); i++) {
								String columnValue = rs.getString(i);
								// This inserts the names of the tables into the
								// drop-down menu
								deleteInhabitsPlanet.add(WordUtils.capitalize(columnValue));
							}
						}

					} catch (SQLException e1) {
						System.out.println("Planet selection for species " + deleteInhabitsSpecies.getText() + " SQL error.");
					}
				}
				else{
					try {
						deleteInhabitsPlanet.removeAll();
						getTablePlanet = conn.prepareStatement("SELECT planetName FROM inhabits;");
						getTablePlanet.execute();
						ResultSet rs = getTablePlanet.getResultSet();
						ResultSetMetaData rsmd = rs.getMetaData();

						while (rs.next()) {
							for (int i = 1; i <= rsmd.getColumnCount(); i++) {
								String columnValue = rs.getString(i);
								// This inserts the names of the tables into the
								// drop-down menu
								deleteInhabitsPlanet.add(WordUtils.capitalize(columnValue));
							}
						}

					} catch (SQLException e2) {
						System.out.println("Planet refresh SQL error.");
						//e2.printStackTrace();
					}
				}
				
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});

		inhabitsDeletePage.setLayout(deleteGL);
		return inhabitsDeletePage;
	}

	public static void refresh(Combo deleteInhabitsSpecies, Combo deleteInhabitsPlanet, Connection conn) {

		try {
			deleteInhabitsSpecies.removeAll();
			PreparedStatement getTableSpecies = conn
					.prepareStatement("SELECT DISTINCT speciesName FROM inhabits ORDER BY speciesName;");
			getTableSpecies.execute();
			ResultSet rs = getTableSpecies.getResultSet();
			ResultSetMetaData rsmd = rs.getMetaData();

			while (rs.next()) {
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					String columnValue = rs.getString(i);
					// This inserts the names of the tables into the
					// drop-down menu
					deleteInhabitsSpecies.add(WordUtils.capitalize(columnValue));
				}
			}

		} catch (SQLException e) {
			System.out.println("Species refresh SQL error.");
		}
		
		try {
			deleteInhabitsPlanet.removeAll();
			PreparedStatement getTablePlanet = conn.prepareStatement("SELECT DISTINCT planetName FROM inhabits;");
			getTablePlanet.execute();
			ResultSet rs = getTablePlanet.getResultSet();
			ResultSetMetaData rsmd = rs.getMetaData();

			while (rs.next()) {
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					String columnValue = rs.getString(i);
					// This inserts the names of the tables into the
					// drop-down menu
					deleteInhabitsPlanet.add(WordUtils.capitalize(columnValue));
				}
			}

		} catch (SQLException e) {
			System.out.println("Planet refresh SQL error.");
		}
	}
}