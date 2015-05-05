import java.sql.*;

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

		//Only one display is needed ever, you will get an exception if you make another
		Display display = new Display();
		//Shell is the actual window that pops up, when adding a widget to a shell or composite, specify that as its parent
		Shell shell = new Shell(display);
		//Layouts determine how widgets are positioned
		GridLayout gl = new GridLayout(4, true); //This make it a grid with 4 columns and of equal width (true value specifies this)
		//For thing to be positioned, sized, and scaled correctly each widget NEEDS to have a GridData
		GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		Label action = new Label(shell, SWT.FILL); //Labels are just text, this one has shell as its parent
		gd.horizontalSpan = 4; //This tells the grid data that I want the label to take up all 4 columns (1 complete row)
		action.setText("Action:"); //Sets the text of the label
		action.setLayoutData(gd); //Tells the label to use this GridData object

		gd = new GridData(SWT.FILL, SWT.CENTER, false, false); //We can reuse the same name for GridData for multiple objects
		// A copy of the GridData is saved in the widget
		//Combos are drop-down menus, read only means users can't enter their own data
		Combo actionSelect = new Combo(shell, SWT.DROP_DOWN | SWT.READ_ONLY); 
		gd.horizontalSpan = 2; //We want this to take up half of a row
		actionSelect.setLayoutData(gd);
		actionSelect.add("Insert"); //These add our options to the drop-down menu
		actionSelect.add("Update");
		actionSelect.add("Delete");
		actionSelect.add("Query");
		
		// Create shared composite, this one is what gets swapped out
		//Composites are groupings of widgets, everything inside of it is its child
		Composite sharedComposite = new Composite(shell, SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		//We want this to take up the rest of the space so it is set to all 4 columns and to grab all excess vertical space
		gd.horizontalSpan = 4;
		sharedComposite.setLayoutData(gd);
		//This stack layout is used inside the composite to "swap pages"
		StackLayout sl = new StackLayout();
		sharedComposite.setLayout(sl);

		// Selected composite
		// These composites are the "pages" where our database interactions take place
		// We'll also need one for deletion and querying
		Composite insertPage = InsertPage.createInsertPage(sharedComposite, conn);
		Composite updatePage = UpdatePage.createUpdatePage(sharedComposite, conn);
		Composite deletePage = DeletePage.createDeletePage(sharedComposite, conn);
		Composite queryPage = QueryPage.createQueryPage(sharedComposite, conn);
		
		// This tells the shell that we want it to use everything in the passed in layout, in this case the grid with all children
		shell.setLayout(gl);

	

		//For widgets that we want to interact with you have to add a listener for the event, in this case the drop down menu
		actionSelect.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				//Here we get the selected option in the drop-down menu by name
				if (actionSelect.getText().equalsIgnoreCase("Insert")) {
					// Setting the top control tells the stack layout that we want a specific composite to be "on top" or visible
					// For comments on insertion see InsertPage.java
					sl.topControl = insertPage;
					// This tells the composite used for swapping pages in and out we want it to refresh
					sharedComposite.layout();
				}
				if (actionSelect.getText().equalsIgnoreCase("Update")) {
					//Does the same as above but for the update page
					// For update comments see Update.java
					sl.topControl = updatePage;
					sharedComposite.layout();
				}
				if (actionSelect.getText().equalsIgnoreCase("Delete")) {
					//Does the same as above but for the update page
					// For update comments see Update.java
					sl.topControl = deletePage;
					sharedComposite.layout();
				}
				if (actionSelect.getText().equalsIgnoreCase("Query")) {
					//Does the same as above but for the update page
					// For update comments see Update.java
					sl.topControl = queryPage;
					sharedComposite.layout();
				}
				//TODO Add actions for Delete and Query
			}
		});
		
		//Packing guesses at the minimum size the object can be to display all its contents
		shell.pack();
		//This sets the size of the shell to slightly bigger than the packed size, numbers are in pixels
		shell.setSize(shell.getSize().x + 25, shell.getSize().y + 75);
		//This actually opens the window
		shell.open();

		//This while loop is needed so that a.) The window doesn't get closed immediately and b.) While open and not doing anything
		// the window will sleep
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		//Exits just to skip the CLI stuff used for Stage 5
		System.exit(0);
	}

}
