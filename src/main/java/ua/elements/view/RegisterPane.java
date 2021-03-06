package ua.elements.view;

import ua.elements.*;
import ua.elements.model.*;

import java.util.*;
import java.text.*;

import javax.swing.event.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.graphics.*;

public class RegisterPane extends Composite {
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy.MM.dd в HH:mm:ss");

    private Combo selection;
    private String[] selectionTitles = {"Приход", "Расход", "Услуги"};

    private TablePanel tablePanel;
    private Button backButton;
    
    private EventListenerList listeners = new EventListenerList();

    public RegisterPane(Composite parent, int style) {
	super(parent, style);
	setLayout(new GridLayout());

	selection = new Combo(this, SWT.READ_ONLY);
	selection.setItems(selectionTitles);
	selection.select(0);
	selection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
	selection.addSelectionListener(new SelectionHandler());

	
	tablePanel = new TablePanel(this, SWT.NONE);
	GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
	data.heightHint = 20;
	tablePanel.setLayoutData(data);

	backButton = new Button(this, SWT.NONE);
	backButton.setText("Назад");
	backButton.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
		    fireChoiceSelected("cancel");
		}
	    });
	backButton.setLayoutData(new GridData(SWT.LEFT, SWT.LEFT, true, true));
    }

    public void addChoiceListener(ChoiceListener l) {
	listeners.add(ChoiceListener.class, l);
    }

    public void removeChoiceListener(ChoiceListener l) {
	listeners.remove(ChoiceListener.class, l);
    }

    private void fireChoiceSelected(String choice) {
	ChoiceEvent event = new ChoiceEvent(this, choice);
	Object[] l = listeners.getListenerList();
	for (int i = l.length-2; i>=0; i-=2) {
	    if (l[i]==ChoiceListener.class) {
		((ChoiceListener)l[i+1]).choiceSelected(event);
	    }
	}
    }


    /* INNER CLASS PART */
    private class TablePanel extends Composite {
	private Table arrival;
	private String[] arrivalTitles = {"Наименование", "Стоимость", "Количество", "Дата"};
	private Table charge;
	private String[] chargeTitles = {"Наименование", "Количество", "Дата"};
	private Table selected;
	private String[] serviceTitles = {"Наименование", "Стоимость", "Дата"};
	private Table service;
	
	public TablePanel(Composite parent, int style) {
	    super(parent, style);
	    setLayout(new TablePanelLayout());

	    /* configure arrival table */
	    arrival = new Table(this, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
	    arrival.setLinesVisible(true);
	    arrival.setHeaderVisible(true);
	    for (int i=0; i<arrivalTitles.length; i++) {
		TableColumn column = new TableColumn(arrival, SWT.CENTER);
		column.setText(arrivalTitles[i]);
		column.pack();
	    }
	    App.getDataManagement().getProductManagement().
		addProductManagementListener(new ProductManagementListener() {
			public void productInserted(ProductManagementEvent event) {
			    TableItem arrivalItem = new TableItem (arrival, SWT.NONE);
			    Product product = event.getProduct();
			    arrivalItem.setText(0, product.getName());
			    arrivalItem.setText(1, "" + product.getPrice());
			    arrivalItem.setText(2, "" + product.getCount());
			    arrivalItem.setText(3, dateFormatter.format(product.getTime()));
			}

			public void productChargeInserted(ProductManagementEvent event) {
			    //nothing
			}
		    });
	    for (Product product : App.getDataManagement().getProductManagement().selectAll()) {
		TableItem arrivalItem = new TableItem (arrival, SWT.NONE);
		arrivalItem.setText(0, product.getName());
		arrivalItem.setText(1, "" + product.getPrice());
		arrivalItem.setText(2, "" + product.getCount());
		arrivalItem.setText(3, dateFormatter.format(product.getTime()));
	    }

	    /* configure charge table */
	    charge = new Table(this, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
	    charge.setVisible(false);
	    charge.setLinesVisible(true);
	    charge.setHeaderVisible(true);
	    for (int i=0; i<chargeTitles.length; i++) {
		TableColumn column = new TableColumn(charge, SWT.CENTER);
		column.setText(chargeTitles[i]);
		column.pack();
	    }
	    App.getDataManagement().getProductManagement().
		addProductManagementListener(new ProductManagementListener() {
			public void productInserted(ProductManagementEvent event) {
			    //nothing
			}
			public void productChargeInserted(ProductManagementEvent event) {
			    TableItem chargeItem = new TableItem(charge, SWT.NONE);
			    Product product = event.getProduct();
			    chargeItem.setText(0, product.getName());
			    chargeItem.setText(1, "" + product.getCount());
			    chargeItem.setText(2, dateFormatter.format(product.getTime()));
			}
		    });
	    
	    for (Product product : App.getDataManagement().getProductManagement().
		     selectAllCharge()) {
		TableItem chargeItem = new TableItem(charge, SWT.NONE);
		chargeItem.setText(0, product.getName());
		chargeItem.setText(1, "" + product.getCount());
		chargeItem.setText(2, dateFormatter.format(product.getTime()));
	    }

	    /* configure service table */
	    service = new Table(this, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
	    service.setVisible(false);
	    service.setLinesVisible(true);
	    service.setHeaderVisible(true);
	    for (int i=0; i<serviceTitles.length; i++) {
		TableColumn column = new TableColumn(service, SWT.CENTER);
		column.setText(serviceTitles[i]);
		column.pack();
	    }
	    App.getDataManagement().getServiceManagement().
		addServiceManagementListener(new ServiceManagementListener() {
			public void serviceInserted(ServiceManagementEvent event) {
			    TableItem serviceItem = new TableItem(service, SWT.NONE);
			    Service service = event.getService();
			    serviceItem.setText(0, service.getName());
			    serviceItem.setText(1, "" + service.getPrice());
			    serviceItem.setText(2, dateFormatter.format(service.getTime()));
			}
		    });
	    for (Service s : App.getDataManagement().getServiceManagement().selectAll()) {
		TableItem serviceItem = new TableItem(service, SWT.NONE);
		serviceItem.setText(0, s.getName());
		serviceItem.setText(1, "" + s.getPrice());
		serviceItem.setText(2, dateFormatter.format(s.getTime()));
	    }

	    selected = arrival;
	}

	public void showArrival() {
	    arrival.setVisible(true);
	    charge.setVisible(false);
	    service.setVisible(false);
	    selected = arrival;
	    layout(true);
	}

	public void showCharge() {
	    arrival.setVisible(false);
	    charge.setVisible(true);
	    service.setVisible(false);
	    selected = charge;
	    layout(true);
	}

	public void showService() {
	    arrival.setVisible(false);
	    charge.setVisible(false);
	    service.setVisible(true);
	    selected = service;
	    layout(true);
	}

	private class TablePanelLayout extends Layout {
	    protected Point computeSize(Composite composite, int wHint, int hHint,
					boolean changed) {
		return selected.computeSize(wHint, hHint, changed);
	    }
 
	    protected void layout(Composite composite, boolean changed) {
		selected.setBounds(0, 0, getSize().x, getSize().y);
	    }
	}
    }

    private class SelectionHandler extends SelectionAdapter {
	public void widgetSelected(SelectionEvent e) {
	    switch (selection.getSelectionIndex()) {
	    case 0:
		tablePanel.showArrival();
		break;
	    case 1:
		tablePanel.showCharge();
		break;
	    case 2:
		tablePanel.showService();
		break;
	    }
	}
    }

    /* TESTING */
    public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	shell.setLayout(new FillLayout());
	RegisterPane registerPane = new RegisterPane(shell, SWT.NONE);
	shell.pack();
	shell.open();
	while (!shell.isDisposed()) {
	    if (!display.readAndDispatch()) 
		display.sleep();
	}
	display.dispose();
    }
}
