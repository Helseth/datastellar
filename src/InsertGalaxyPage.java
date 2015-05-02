import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.commons.lang3.math.NumberUtils;
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
import org.eclipse.swt.widgets.Text;


public class InsertGalaxyPage {
	public static Composite createGalaxyPage(Composite parent, Connection conn) {
		Composite galaxyInsertPage = new Composite(parent, SWT.NONE);
		// planetInsertPage.setBackground(new Color(Display.getCurrent(), 255,
		// 0, 0));
		GridLayout insertGL = new GridLayout(4, true);

		GridData insertGD = new GridData(SWT.FILL, SWT.CENTER, true, false);

		Label name = new Label(galaxyInsertPage, SWT.FILL);
		insertGD.horizontalSpan = 4;
		name.setText("Name:");
		name.setLayoutData(insertGD);

		insertGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		insertGD.horizontalSpan = 1;
		Text nameBox = new Text(galaxyInsertPage, SWT.NONE);
		nameBox.setLayoutData(insertGD);

		insertGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label diameter = new Label(galaxyInsertPage, SWT.FILL);
		insertGD.horizontalSpan = 4;
		diameter.setText("Diameter:");
		diameter.setLayoutData(insertGD);

		insertGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		insertGD.horizontalSpan = 1;
		Text diameterBox = new Text(galaxyInsertPage, SWT.NONE);
		diameterBox.setLayoutData(insertGD);
		
		insertGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label klyText = new Label(galaxyInsertPage, SWT.FILL);
		insertGD.horizontalSpan = 1;
		klyText.setText("kly");
		klyText.setLayoutData(insertGD);

		insertGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label shapeText = new Label(galaxyInsertPage, SWT.FILL);
		insertGD.horizontalSpan = 4;
		shapeText.setText("Shape:");
		shapeText.setLayoutData(insertGD);

		insertGD = new GridData(SWT.FILL, SWT.CENTER, false, false);
		Combo galaxyShape = new Combo(galaxyInsertPage, SWT.DROP_DOWN
				| SWT.READ_ONLY);
		insertGD.horizontalSpan = 2;
		galaxyShape.setLayoutData(insertGD);
		
		galaxyShape.add("Spiral");
		galaxyShape.add("Elliptical");
		galaxyShape.add("Lenticular");
		galaxyShape.add("Irregular");

		insertGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label errorText = new Label(galaxyInsertPage, SWT.FILL);
		insertGD.horizontalSpan = 4;
		errorText.setText("");
		errorText.setLayoutData(insertGD);
		errorText.setVisible(false);
		errorText.setForeground(Display.getCurrent().getSystemColor(
				SWT.COLOR_RED));

		insertGD = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		Button submit = new Button(galaxyInsertPage, SWT.PUSH);
		insertGD.horizontalSpan = 1;
		submit.setText("Submit");
		submit.pack();
		submit.setLayoutData(insertGD);
		
		submit.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				boolean error = false;
				if (nameBox.getText().equals("")) {
					errorText.setText("Name must not be empty.");
					errorText.setVisible(true);
					error = true;
				}
				if (!NumberUtils.isNumber(diameterBox.getText())) {
					errorText.setText("Diameter must be a number.");
					errorText.setVisible(true);
					error = true;
				}
				if (NumberUtils.isNumber(diameterBox.getText())) {
					if (Integer.parseInt(diameterBox.getText()) <= 0) {
						errorText.setText("Diameter must be > 0.");
						errorText.setVisible(true);
						error = true;
					}

				}
				if(galaxyShape.getText().equals("")){
					errorText.setText("You must select a shape.");
					errorText.setVisible(true);
					error = true;
				}
				if (!error) {
					try {
						PreparedStatement insertNewPlanet = conn
								.prepareStatement("INSERT INTO Galaxy VALUE(\""
							+ WordUtils.capitalize(nameBox.getText()) + "\",\"" +
							galaxyShape.getText() + "\",\""
							+ diameterBox.getText() + "\");");
						insertNewPlanet.execute();
						System.out.println("Inserting " + nameBox.getText());

					} catch (SQLException e) {
						System.out.println("SQL Error");
						if(e.getMessage().contains("Duplicate")){
							errorText.setText("Entry already exists in database.");
							errorText.setVisible(true);
						}
					}
				}

			}
		});
		
		galaxyInsertPage.setLayout(insertGL);
		return galaxyInsertPage;
	}
}
