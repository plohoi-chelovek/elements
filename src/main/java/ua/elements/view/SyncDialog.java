package ua.elements.view;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

public class SyncDialog {
    public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	shell.open();
	FileDialog dialog = new FileDialog(shell, SWT.OPEN);

	String [] filterNames = new String [] {"DB Files"};
	String [] filterExtensions = new String [] {"*.db"};
	dialog.setFilterNames(filterNames);
	dialog.setFilterExtensions(filterExtensions);
	dialog.setFilterPath(System.getProperty("user.home"));
	String result = dialog.open();
	String result2 = dialog.open();
	System.out.println(result2);
	while (!shell.isDisposed()) {
	    if (!display.readAndDispatch()) 
		display.sleep();
	}
	display.dispose();
    }
}
