package ua.elements;

import ua.elements.model.*;
import ua.elements.view.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

public class App {
    private static DataManagement dataManagement;

    private static void init() {
	dataManagement = new DataManagement();
    }
    
    private static void start() {
	Display display = new Display();
	Shell shell = new Shell(display);
	MainPane mainPane = new MainPane(shell, SWT.NONE);
	shell.pack();
	shell.open();
	while (!shell.isDisposed()) {
	    if (!display.readAndDispatch()) 
		display.sleep();
	}
	display.dispose();
    }


    public static DataManagement getDataManagement() {
	return dataManagement;
    }

    public static void main(String[] args) {
	init();
	start();
    }
}
