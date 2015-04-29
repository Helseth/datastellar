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


public class InsertStarPage {
	public static Composite createStarPage(Composite parent, Connection conn) {
		Composite starInsertPage = new Composite(parent, SWT.NONE);
		// planetInsertPage.setBackground(new Color(Display.getCurrent(), 255,
		// 0, 0));
		GridLayout updateGL = new GridLayout(4, true);

		GridData updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);

		Label name = new Label(starInsertPage, SWT.FILL);
		updateGD.horizontalSpan = 4;
		name.setText("Name:");
		name.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		updateGD.horizontalSpan = 1;
		Text nameBox = new Text(starInsertPage, SWT.NONE);
		nameBox.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label mass = new Label(starInsertPage, SWT.FILL);
		updateGD.horizontalSpan = 4;
		mass.setText("Mass:");
		mass.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		updateGD.horizontalSpan = 1;
		Text massBox = new Text(starInsertPage, SWT.NONE);
		massBox.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label classText = new Label(starInsertPage, SWT.FILL);
		updateGD.horizontalSpan = 4;
		classText.setText("Class:");
		classText.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		updateGD.horizontalSpan = 1;
		Text classBox = new Text(starInsertPage, SWT.NONE);
		classBox.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label inGalaxy = new Label(starInsertPage, SWT.FILL);
		updateGD.horizontalSpan = 4;
		inGalaxy.setText("In Galaxy:");
		inGalaxy.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, false, false);
		Combo galaxySelect = new Combo(starInsertPage, SWT.DROP_DOWN
				| SWT.READ_ONLY);
		updateGD.horizontalSpan = 2;
		galaxySelect.setLayoutData(updateGD);

		updateGD = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label errorText = new Label(starInsertPage, SWT.FILL);
		updateGD.horizontalSpan = 4;
		errorText.setText("");
		errorText.setLayoutData(updateGD);
		errorText.setVisible(false);
		errorText.setForeground(Display.getCurrent().getSystemColor(
				SWT.COLOR_RED));

		updateGD = new GridData(SWT.LEFT, SWT.CENTER, false, false);
		Button submit = new Button(starInsertPage, SWT.PUSH);
		updateGD.horizontalSpan = 1;
		submit.setText("Submit");
		submit.pack();
		submit.setLayoutData(updateGD);

		try {

			PreparedStatement getGalaxyNames = conn
					.prepareStatement("SELECT DISTINCT name FROM galaxy;");
			getGalaxyNames.execute();
			ResultSet rs = getGalaxyNames.getResultSet();
			ResultSetMetaData rsmd = rs.getMetaData();

			rs = getGalaxyNames.getResultSet();
			rsmd = rs.getMetaData();

			while (rs.next()) {
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					String columnValue = rs.getString(i);
					galaxySelect.add(WordUtils.capitalize(columnValue));
				}
			}

		} catch (SQLException e) {
			System.out.println("SQL Error");
			e.printStackTrace();
		}

		submit.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				boolean error = false;
				if (nameBox.getText().equals("")) {
					errorText.setText("Name must not be empty.");
					errorText.setVisible(true);
					error = true;
				}
				if (!NumberUtils.isNumber(massBox.getText())) {
					errorText.setText("Mass must be a number.");
					errorText.setVisible(true);
					error = true;
				}
				if (NumberUtils.isNumber(massBox.getText())) {
					if (Integer.parseInt(massBox.getText()) <= 0) {
						errorText.setText("Mass must be > 0.");
						errorText.setVisible(true);
						error = true;
					}

				}
				if (galaxySelect.getText().equals("")) {
					errorText.setText("You must select a Galaxy.");
					errorText.setVisible(true);
					error = true;
				}
				if (!error) {
					System.out.println("INSERT INTO Star VALUE(\""
							+ nameBox.getText() + "\","
							+ massBox.getText() + ",\""
							+ classBox.getText() + "\",\""
							+ galaxySelect.getText() + "\");");
					try {
						PreparedStatement insertNewPlanet = conn
								.prepareStatement("INSERT INTO Star VALUE(\""
										+ nameBox.getText() + "\","
										+ massBox.getText() + ",\""
										+ classBox.getText() + "\",\""
										+ galaxySelect.getText() + "\");");
						insertNewPlanet.execute();
						System.out.println("Inserting " + nameBox.getText());

					} catch (SQLException e) {
						System.out.println("SQL Error");
						e.printStackTrace();
					}
				}

			}
		});

		starInsertPage.setLayout(updateGL);
		return starInsertPage;
	}
}
