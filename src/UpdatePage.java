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


public class UpdatePage {
	public static Composite createUpdatePage(Composite parent, Connection conn) {

		// Extremely similar to the InsertPage
		Composite updatePage = new Composite(parent, SWT.NONE);
		GridLayout updateGL = new GridLayout(4, true);

		GridData updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label tableUpdateName = new Label(updatePage, SWT.NONE);
		updateGD.horizontalSpan = 4;
		tableUpdateName.setText("Select a table to update:");
		tableUpdateName.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, false, false);
		Combo updateTableSelect = new Combo(updatePage, SWT.DROP_DOWN
				| SWT.READ_ONLY);
		updateGD.horizontalSpan = 2;
		updateTableSelect.setLayoutData(updateGD);
		
		Composite sharedComposite = new Composite(updatePage, SWT.BORDER);
		updateGD = new GridData(SWT.FILL, SWT.FILL, true, true);
		updateGD.horizontalSpan = 4;
		sharedComposite.setLayoutData(updateGD);
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
					updateTableSelect.add(WordUtils.capitalize(columnValue));
				}
			}

		} catch (SQLException e) {
			System.out.println("SQL Error");
			e.printStackTrace();
		}
		
		// Only thing slightly different is that we have an additional layer of composites here, one for the object type, and one for the
		// actual object
		Composite planetPage = UpdateSelectPlanetPage.createSelectUpdatePage(sharedComposite, conn);
		Composite starPage = UpdateSelectStarPage.createSelectUpdatePage(sharedComposite, conn);
		Composite galaxyPage = UpdateSelectGalaxyPage.createSelectUpdatePage(sharedComposite, conn);
		Composite moonPage = UpdateSelectMoonPage.createSelectUpdatePage(sharedComposite, conn);
		Composite speciesPage = UpdateSelectSpeciesPage.createSelectUpdatePage(sharedComposite, conn);
		//Composite inhabitsPage = UpdateSelectInhabitsPage.createSelectUpdatePage(sharedComposite, conn);

		updateTableSelect.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				// We need one for all the other types,
				// See UpdateSelectPlanetPage.java for more info
				if (updateTableSelect.getText().equalsIgnoreCase("Planet")) {
					sl.topControl = planetPage;
					sharedComposite.layout();
				}
				if (updateTableSelect.getText().equalsIgnoreCase("Galaxy")) {
					sl.topControl = galaxyPage;
					sharedComposite.layout();
				}
				if (updateTableSelect.getText().equalsIgnoreCase("Star")) {
					sl.topControl = starPage;
					sharedComposite.layout();
				}
				if (updateTableSelect.getText().equalsIgnoreCase("Moon")) {
					sl.topControl = moonPage;
					sharedComposite.layout();
				}
				if (updateTableSelect.getText().equalsIgnoreCase("Species")) {
					sl.topControl = speciesPage;
					sharedComposite.layout();
				}
				/*
				if (updateTableSelect.getText().equalsIgnoreCase("Inhabits")) {
					sl.topControl = inhabitsPage;
					sharedComposite.layout();
				}
				*/
			}});

		updatePage.setLayout(updateGL);
		return updatePage;
	}
}
