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

public class DeleteMoonPage {
	public static Composite createMoonPage(Composite parent, Connection conn) {
		Composite moonDeletePage = new Composite(parent, SWT.NONE);
		// planetInsertPage.setBackground(new Color(Display.getCurrent(), 255,
		// 0, 0));
		GridLayout deleteGL = new GridLayout(4, true);

		GridData deleteGD = new GridData(SWT.FILL, SWT.CENTER, true, false);

		Label name = new Label(moonDeletePage, SWT.FILL);
		deleteGD.horizontalSpan = 4;
		name.setText("Name:");
		name.setLayoutData(deleteGD);

		deleteGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		deleteGD.horizontalSpan = 2;
		Combo deleteMoonNames = new Combo(moonDeletePage, SWT.READ_ONLY);
		deleteMoonNames.setLayoutData(deleteGD);

		deleteGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label errorText = new Label(moonDeletePage, SWT.FILL);
		deleteGD.horizontalSpan = 4;
		errorText.setText("");
		errorText.setLayoutData(deleteGD);
		errorText.setVisible(false);

		deleteGD = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		Button submit = new Button(moonDeletePage, SWT.PUSH);
		deleteGD.horizontalSpan = 1;
		submit.setText("Submit");
		submit.pack();
		submit.setLayoutData(deleteGD);

		deleteGD = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		Button refresh = new Button(moonDeletePage, SWT.PUSH);
		deleteGD.horizontalSpan = 1;
		refresh.setText("Refresh");
		refresh.pack();
		refresh.setLayoutData(deleteGD);

		refresh(deleteMoonNames, conn);

		refresh.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				refresh(deleteMoonNames, conn);
				errorText.setVisible(false);
			}
		});

		submit.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				try {
					PreparedStatement deleteEntry = conn
							.prepareStatement("DELETE FROM moon WHERE name='"
									+ deleteMoonNames.getText() + "';");
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
				errorText.setText("Entry " + deleteMoonNames.getText()
						+ " successfully deleted.");
				errorText.setVisible(true);
			}
		});

		moonDeletePage.setLayout(deleteGL);
		return moonDeletePage;
	}

	public static void refresh(Combo deleteMoonNames, Connection conn) {

		try {
			deleteMoonNames.removeAll();
			PreparedStatement getTableNames = conn
					.prepareStatement("SELECT name FROM moon ORDER BY name;");
			getTableNames.execute();
			ResultSet rs = getTableNames.getResultSet();
			ResultSetMetaData rsmd = rs.getMetaData();

			while (rs.next()) {
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					String columnValue = rs.getString(i);
					// This inserts the names of the tables into the
					// drop-down menu
					deleteMoonNames.add(WordUtils.capitalize(columnValue));
				}
			}

		} catch (SQLException e) {
			System.out.println("Moon refresh SQL error.");
		}
	}
}