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


public class InsertPage {
	public static Composite createInsertPage(Composite parent, Connection conn) {

		Composite insertPage = new Composite(parent, SWT.NONE);
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

		Composite sharedComposite = new Composite(insertPage, SWT.NONE);
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

		Composite planetPage = InsertPlanetPage.createPlanetPage(sharedComposite, conn);
		Composite starPage = InsertStarPage.createStarPage(sharedComposite, conn);

		insertTableSelect.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				if (insertTableSelect.getText().equalsIgnoreCase("Planet")) {
					sl.topControl = planetPage;
					sharedComposite.layout();
				}
				if (insertTableSelect.getText().equalsIgnoreCase("Star")) {
					sl.topControl = starPage;
					sharedComposite.layout();
				}
			}
		});

		insertPage.setLayout(insertGL);
		return insertPage;
	}
}
