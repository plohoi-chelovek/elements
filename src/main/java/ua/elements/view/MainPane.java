package ua.elements.view;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;

public class MainPane extends Composite {
    private Color background = new Color(null, 255, 255, 255);
    private OptionsPanel options;
    private ArrivalPane arrival;
    private ChargePane charge;
    private ServicePane service;
    private RegisterPane register;
    private GraphView graph;
    private ServiceGraphView serviceGraph;
    private Composite current;

    public MainPane(Composite parent, int style) {
	super(parent, style);
	((Shell)parent).setLayout(new FillLayout());
	setMenuBar((Shell)parent);
	setBackground(background);
	setLayout(new MainPaneLayout());

	options = new OptionsPanel(this, SWT.NONE);
	options.addOptionsListener(new OptionsHandler());
	options.pack();

	arrival = new ArrivalPane(this, SWT.NONE);
	arrival.addChoiceListener(new ChoiceListener() {
		public void choiceSelected(ChoiceEvent event) {
		    current.setVisible(false);
		    current = options;
		    current.setVisible(true);
		    layout();
		}
	    });
	arrival.pack();
	arrival.setVisible(false);

	charge = new ChargePane(this, SWT.NONE);
	charge.addChoiceListener(new ChoiceListener() {
		public void choiceSelected(ChoiceEvent event) {
		    current.setVisible(false);
		    current = options;
		    current.setVisible(true);
		    layout();
		}
	    });
	charge.pack();
	charge.setVisible(false);

	service = new ServicePane(this, SWT.NONE);
	service.addChoiceListener(new ChoiceListener() {
		public void choiceSelected(ChoiceEvent event) {
		    current.setVisible(false);
		    current = options;
		    current.setVisible(true);
		    layout();
		}
	    });
	service.pack();
	service.setVisible(false);

	register = new RegisterPane(this, SWT.NONE);
	register.addChoiceListener(new ChoiceListener() {
		public void choiceSelected(ChoiceEvent event) {
		    current.setVisible(false);
		    current = options;
		    current.setVisible(true);
		    layout();
		}
	    });
	register.pack();
	register.setVisible(false);

	graph = new GraphView(this, SWT.NONE);
	graph.addChoiceListener(new ChoiceListener() {
		public void choiceSelected(ChoiceEvent event) {
		    current.setVisible(false);
		    current = options;
		    current.setVisible(true);
		    layout();
		}
	    });
	graph.pack();
	graph.setVisible(false);

	serviceGraph = new ServiceGraphView(this, SWT.NONE);
	serviceGraph.addChoiceListener(new ChoiceListener() {
		public void choiceSelected(ChoiceEvent event) {
		    current.setVisible(false);
		    current = options;
		    current.setVisible(true);
		    layout();
		}
	    });
	serviceGraph.pack();
	serviceGraph.setVisible(false);

	current = options;
    }

    private void setMenuBar(Shell shell) {
	Menu menuBar = new Menu(shell, SWT.BAR);

	MenuItem fileMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
	fileMenuHeader.setText("Управление");

	Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
	fileMenuHeader.setMenu(fileMenu);

	MenuItem fileExitItem = new MenuItem(fileMenu, SWT.PUSH);
	fileExitItem.setText("Выход из программы");
	fileExitItem.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
		    System.exit(0);
		}
	    });
	shell.setMenuBar(menuBar);
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
	    else if (event.getOption().equals("graphOption"))
		current = graph;
	    else if (event.getOption().equals("serviceGraphOption"))
		current = serviceGraph;
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
