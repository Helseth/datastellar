import java.sql.*;

import org.apache.commons.lang3.text.WordUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

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
		Composite insertPage = InsertPage.createInsertPage(sharedComposite, conn);
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

	public Composite createUpdatePage(Composite parent, Connection conn) {

		Composite updatePage = new Composite(parent, SWT.NONE);
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
}
