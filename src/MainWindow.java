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
	
	public MainWindow(Connection conn){
		
		Display display = new Display();
		Shell shell = new Shell(display);
		GridLayout gl = new GridLayout(4, true);
		GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label action = new Label(shell, SWT.FILL);
		gd.horizontalSpan = 4;
		action.setText("Action:");
		action.setLayoutData(gd);
		
		gd = new GridData(SWT.FILL, SWT.CENTER, false, false);
		Combo actionSelect = new Combo(shell, SWT.DROP_DOWN);
		gd.horizontalSpan = 2;
		actionSelect.setLayoutData(gd);
		actionSelect.add("Insert");
		actionSelect.add("Update");
		actionSelect.add("Delete");
		actionSelect.add("Query");
		
		Composite sharedComposite = new Composite(shell, SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.horizontalSpan = 4;
		sharedComposite.setLayoutData(gd);
		StackLayout sl = new StackLayout();
		sharedComposite.setLayout(sl);
		
		Composite insertPage = new Composite (sharedComposite, SWT.BORDER);
		GridLayout insertGL = new GridLayout(4, true);
		
		//insertPage.setBackground(display.getSystemColor(SWT.COLOR_RED));
		
		GridData insertGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label tableInsertName = new Label(insertPage, SWT.NONE);
		insertGD.horizontalSpan = 4;
		tableInsertName.setText("Select a table to insert into:");
		tableInsertName.setLayoutData(insertGD);
		
		insertGD = new GridData(SWT.FILL, SWT.CENTER, false, false);
		Combo insertTableSelect = new Combo(insertPage, SWT.DROP_DOWN);
		insertGD.horizontalSpan = 2;
		insertTableSelect.setLayoutData(insertGD);
		try {
			PreparedStatement getTableNames = conn.prepareStatement("SELECT table_name FROM information_schema.tables WHERE table_schema='datastellar';");
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

		insertPage.setLayout(insertGL);

		shell.setLayout(gl);

		shell.setSize(shell.getSize().x / 2, shell.getSize().y);
		
		actionSelect.addListener (SWT.Selection, new Listener () {
			@Override
			public void handleEvent (Event e) {
				//System.out.println (e.widget + " - Default Selection");
				if(actionSelect.getText().equalsIgnoreCase("Insert")){
					System.out.println(actionSelect.getText());
					//insertPage.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
					sl.topControl = insertPage;
					sharedComposite.layout();
				}
			}
		});
		
		shell.open();
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		System.exit(0);
	}

}
