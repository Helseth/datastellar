import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.commons.lang3.text.WordUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;


public class DeletePage {
	public static Composite createDeletePage(Composite parent, Connection conn) {

		Composite deletePage = new Composite(parent, SWT.NONE);
		GridLayout deleteGL = new GridLayout(4, true);

		GridData deleteGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label tableInsertName = new Label(deletePage, SWT.NONE);
		deleteGD.horizontalSpan = 4;
		tableInsertName.setText("Select a table to delete from:");
		tableInsertName.setLayoutData(deleteGD);

		deleteGD = new GridData(SWT.FILL, SWT.CENTER, false, false);
		Combo deleteTableSelect = new Combo(deletePage, SWT.DROP_DOWN
				| SWT.READ_ONLY);
		deleteGD.horizontalSpan = 2;
		deleteTableSelect.setLayoutData(deleteGD);

		Composite sharedComposite = new Composite(deletePage, SWT.NONE);
		deleteGD = new GridData(SWT.FILL, SWT.FILL, true, true);
		deleteGD.horizontalSpan = 4;
		sharedComposite.setLayoutData(deleteGD);
		StackLayout sl = new StackLayout();
		sharedComposite.setLayout(sl);

		//Here we are querying the DB for the names of all the tables
		try {
			PreparedStatement getTableNames = conn
					.prepareStatement("SELECT table_name FROM information_schema.tables WHERE table_schema='datastellar';");
			getTableNames.execute();
			ResultSet rs = getTableNames.getResultSet();
			ResultSetMetaData rsmd = rs.getMetaData();

			while (rs.next()) {
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					String columnValue = rs.getString(i);
					// This inserts the names of the tables into the drop-down menu
					deleteTableSelect.add(WordUtils.capitalize(columnValue));
				}
			}

		} catch (SQLException e) {
			System.out.println("SQL Error");
			e.printStackTrace();
		}

		//These are composites specifically made for each data type, when a different table name is
		// selected in the drop-down menu these are what will get swapped out in the "Shared Composite"
		// For creating the composites we want to pass the server connection (conn) into the page
		// The .java files don't actually store any class info, just used to separate code cleanly
		// Make sure when making new ones to make them static so we can access them without having to make
		// new objects.
		// TODO Still needs one for Species and Inhabits
		Composite planetPage = DeletePlanetPage.createPlanetPage(sharedComposite, conn);
		Composite starPage = DeleteStarPage.createStarPage(sharedComposite, conn);
		Composite galaxyPage = DeleteGalaxyPage.createGalaxyPage(sharedComposite, conn);
		Composite moonPage = DeleteMoonPage.createMoonPage(sharedComposite, conn);
		Composite speciesPage = DeleteSpeciesPage.createSpeciesPage(sharedComposite, conn);
		Composite inhabitsPage = DeleteInhabitsPage.createInhabitsPage(sharedComposite, conn);


		deleteTableSelect.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				if (deleteTableSelect.getText().equalsIgnoreCase("Planet")) {
					// Swaps in the planet page
					// For more comments see the InsertPlanetPage.java
					sl.topControl = planetPage;
					sharedComposite.layout();
				}
				if (deleteTableSelect.getText().equalsIgnoreCase("Star")) {
					sl.topControl = starPage;
					sharedComposite.layout();
				}
				if (deleteTableSelect.getText().equalsIgnoreCase("Galaxy")) {
					sl.topControl = galaxyPage;
					sharedComposite.layout();
				}
				if (deleteTableSelect.getText().equalsIgnoreCase("Moon")) {
					sl.topControl = moonPage;
					sharedComposite.layout();
				}
				if (deleteTableSelect.getText().equalsIgnoreCase("Species")) {
					sl.topControl = speciesPage;
					sharedComposite.layout();
				}
				if (deleteTableSelect.getText().equalsIgnoreCase("Inhabits")) {
					sl.topControl = inhabitsPage;
					sharedComposite.layout();
				}
			}
		});
		deletePage.setLayout(deleteGL);
		return deletePage;
	}
}
