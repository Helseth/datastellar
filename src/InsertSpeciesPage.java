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


public class InsertSpeciesPage {
	public static Composite createSpeciesPage(Composite parent, Connection conn) {
		Composite speciesInsertPage = new Composite(parent, SWT.NONE);
		//Same stuff as before
		GridLayout insertGL = new GridLayout(4, true);

		GridData insertGD = new GridData(SWT.FILL, SWT.CENTER, true, false);

		Label name = new Label(speciesInsertPage, SWT.FILL);
		insertGD.horizontalSpan = 4;
		name.setText("Name:");
		name.setLayoutData(insertGD);

		//Text is an editable textbox, you can set it to read-only by replacing the SWT.NONE in the contructor, but this is how
		// users will enter data to insert in the DB
		insertGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		insertGD.horizontalSpan = 1;
		Text nameBox = new Text(speciesInsertPage, SWT.NONE);
		nameBox.setLayoutData(insertGD);

		insertGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label height = new Label(speciesInsertPage, SWT.FILL);
		insertGD.horizontalSpan = 4;
		height.setText("Height:");
		height.setLayoutData(insertGD);

		insertGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		insertGD.horizontalSpan = 1;
		Text heightBox = new Text(speciesInsertPage, SWT.NONE);
		heightBox.setLayoutData(insertGD);
		
		Label heightUnit = new Label(speciesInsertPage, SWT.FILL);
		insertGD.horizontalSpan = 1;
		heightUnit.setText("ft");
		heightUnit.setLayoutData(insertGD);
        //
		insertGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label numberLiving = new Label(speciesInsertPage, SWT.FILL);
		insertGD.horizontalSpan = 4;
		numberLiving.setText("Number Living:");
		numberLiving.setLayoutData(insertGD);
		
		insertGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		insertGD.horizontalSpan = 1;
		Text numberLivingBox = new Text(speciesInsertPage, SWT.NONE);
		numberLivingBox.setLayoutData(insertGD);
		//
		
		insertGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label hostility = new Label(speciesInsertPage, SWT.FILL);
		insertGD.horizontalSpan = 4;
		hostility.setText("Hostility:");
		hostility.setLayoutData(insertGD);

		insertGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		insertGD.horizontalSpan = 1;
		Text hostilityBox = new Text(speciesInsertPage, SWT.NONE);
		hostilityBox.setLayoutData(insertGD);

		insertGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label errorText = new Label(speciesInsertPage, SWT.FILL);
		insertGD.horizontalSpan = 4;
		errorText.setText("");
		errorText.setLayoutData(insertGD);
		errorText.setVisible(false);
		//This sets the color of the error text label to red
		errorText.setForeground(Display.getCurrent().getSystemColor(
				SWT.COLOR_RED));

		insertGD = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		Button submit = new Button(speciesInsertPage, SWT.PUSH);
		insertGD.horizontalSpan = 1;
		submit.setText("Submit");
		submit.pack();
		submit.setLayoutData(insertGD);
		
		insertGD = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		Button refresh = new Button(speciesInsertPage, SWT.PUSH);
		insertGD.horizontalSpan = 1;
		refresh.setText("Refresh");
		refresh.pack();
		refresh.setLayoutData(insertGD);

		//Listener for the submit button, the event type is SWT.Selection, NOT SWT.PUSH
		submit.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				boolean error = false;
				// Just some basic error checking before we make the SQL statement
				if (nameBox.getText().equals("")) {
					errorText.setText("Name must not be empty.");
					errorText.setVisible(true);
					error = true;
				}
				if (!NumberUtils.isNumber(heightBox.getText())) {
					errorText.setText("Height must be a number.");
					errorText.setVisible(true);
					error = true;
				}
				if (NumberUtils.isNumber(heightBox.getText())) {
					if (Integer.parseInt(heightBox.getText()) <= 0) {
						errorText.setText("Height must be > 0.");
						errorText.setVisible(true);
						error = true;
					}

				}
				if (!NumberUtils.isNumber(numberLivingBox.getText())) {
					errorText.setText("Number Living must be a number.");
					errorText.setVisible(true);
					error = true;
				}
				if (NumberUtils.isNumber(numberLivingBox.getText())) {
					if (Integer.parseInt(numberLivingBox.getText()) < 0) {
						errorText.setText("Number Living must be >= 0.");
						errorText.setVisible(true);
						error = true;
					}

				}
				if (!NumberUtils.isNumber(hostilityBox.getText())) {
					errorText.setText("Hostility must be a number.");
					errorText.setVisible(true);
					error = true;
				}
				if (NumberUtils.isNumber(hostilityBox.getText())) {
					if (Long.parseLong(hostilityBox.getText()) < 1 || Long.parseLong(hostilityBox.getText()) > 10) {
						errorText.setText("Hostility must be a value in the range 1 to 10.");
						errorText.setVisible(true);
						error = true;
					}

				}
				if (!error) {
					//Now for the actual SQL statement construction
					try {
						PreparedStatement insertNewSpecies = conn
								.prepareStatement("INSERT INTO Species VALUE(\""
										+ WordUtils.capitalize(nameBox.getText()) + "\","
										+ heightBox.getText() + ","
										+ numberLivingBox.getText() + ","
										+ hostilityBox.getText() + ");");
						//This sends it to the server
						insertNewSpecies.execute();
						System.out.println("Inserting " + nameBox.getText());

					} catch (SQLException e) {
						System.out.println("SQL Error");
						e.printStackTrace();
					}
				}

			}
		});
		
		//Tell the composite to use that layout and its widget children
		speciesInsertPage.setLayout(insertGL);
		return speciesInsertPage;
	}
}
