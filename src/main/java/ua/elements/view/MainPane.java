package ua.elements.view;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;

public class MainPane extends Composite {
    private Color background = new Color(null, 255, 255, 255);
    private OptionsPanel options;
    private ArrivalPane arrival;
    private ChargePane charge;
    private ServicePane service;
    private RegisterPane register;
    private Composite current;

    public MainPane(Composite parent, int style) {
	super(parent, style);
	((Shell)parent).setLayout(new FillLayout());

	setBackground(background);
	setLayout(new MainPaneLayout());

	options = new OptionsPanel(this, SWT.NONE);
	options.addOptionsListener(new OptionsHandler());
	options.pack();

	arrival = new ArrivalPane(this, SWT.NONE);
	arrival.pack();
	arrival.setVisible(false);

	charge = new ChargePane(this, SWT.NONE);
	charge.pack();
	charge.setVisible(false);

	service = new ServicePane(this, SWT.NONE);
	service.pack();
	service.setVisible(false);

	register = new RegisterPane(this, SWT.NONE);
	register.pack();
	register.setVisible(false);

	current = options;
    }

    /* INNER CLASS PART */
    private class MainPaneLayout extends Layout {
	protected Point computeSize(Composite composite, int wHint, int hHint,
				    boolean changed) {
	    return current.computeSize(wHint, hHint, false);
	}
 
	protected void layout(Composite composite, boolean changed) {
	    // System.out.println("layout");
	    // int x = (getSize().x - current.computeSize(SWT.DEFAULT, SWT.DEFAULT, false).x) / 2;
	    // int y = (getSize().y - current.computeSize(SWT.DEFAULT, SWT.DEFAULT, false).y) / 2;
	    // System.out.printf("%d %d", getSize().x, current.computeSize(SWT.DEFAULT, SWT.DEFAULT, false).x);
	    current.setBounds(0, 0, getSize().x, getSize().y);
	}
    }

    private class OptionsHandler implements OptionsListener {
	public void optionSelected(OptionEvent event) {
	    current.setVisible(false);
	    if (event.getOption().equals("arrivalOption"))
		current = arrival;
	    else if (event.getOption().equals("chargeOption"))
		current = charge;
	    else if (event.getOption().equals("serviceOption"))
		current = service;
	    else if (event.getOption().equals("registerOption"))
		current = register;
	    current.setVisible(true);
	    layout(true);
	}
    }

    /* TESTING */
    public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	shell.setLayout(new FillLayout());
	MainPane mainPane = new MainPane(shell, SWT.NONE);
	shell.pack();
	shell.open();
	while (!shell.isDisposed()) {
	    if (!display.readAndDispatch()) 
		display.sleep();
	}
	display.dispose();
    }    
}
