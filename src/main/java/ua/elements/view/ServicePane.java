package ua.elements.view;

import ua.elements.*;
import ua.elements.model.*;

import java.util.*;
import java.text.*;

import javax.swing.event.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.events.*;

public class ServicePane extends Composite {
    private Color background = new Color(null, 255, 255, 255);
    private Color foreground = new Color(null, 0, 175, 240);
    private Text name;
    private Text price;
    private Text time;
    
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy.MM.dd в HH:mm:ss");

    private EventListenerList listeners = new EventListenerList();
    
    public ServicePane(Composite parent, int style) {
	super(parent, style);
	setBackground(background);
	GridLayout layout = new GridLayout();
	layout.numColumns = 2;
	setLayout(layout);

	Label nameLabel = new Label(this, SWT.NONE);
	nameLabel.setText("Наименование услуги");
	nameLabel.setBackground(background);
	nameLabel.setForeground(foreground);

	name = new Text(this, SWT.BORDER);
	GridData nameData = new GridData();
	nameData.widthHint = 200;
	name.setLayoutData(nameData);

	Label priceLabel = new Label(this, SWT.NONE);
	priceLabel.setText("Стоимость");
	priceLabel.setBackground(background);
	priceLabel.setForeground(foreground);

	price = new Text(this, SWT.BORDER);
	GridData priceData = new GridData();
	priceData.widthHint = 200;
	price.setLayoutData(priceData);

	Label timeLabel = new Label(this, SWT.NONE);
	timeLabel.setText("Время");
	timeLabel.setBackground(background);
	timeLabel.setForeground(foreground);

	time = new Text(this, SWT.BORDER);
	time.setText(dateFormatter.format(new Date()));
	GridData timeData = new GridData();
	timeData.widthHint = 200;
	time.setLayoutData(timeData);

	ButtonsPane buttonsPane = new ButtonsPane(this, SWT.NONE);
	GridData data = new GridData();
	data.horizontalSpan = 2;
	buttonsPane.setLayoutData(data);
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
    
    private class ButtonsPane extends Canvas {
	private Button okButton;
	private Button cancelButton;

	public ButtonsPane(Composite parent, int style) {
	    super(parent, style);
	    setLayout(new RowLayout());
	    setBackground(background);
	    okButton = new Button(this, SWT.PUSH);
	    okButton.setText("Добавить");
	    okButton.addSelectionListener(new SelectionAdapter() {
		    public void widgetSelected(SelectionEvent e) {
			try {
			    Service service = new Service(name.getText(),
							  Double.parseDouble(price.getText()),
							  dateFormatter.parse(time.getText()));
			    App.getDataManagement().getServiceManagement().insert(service);
			    fireChoiceSelected("add");
			} catch (Exception ex) { }
		    }
		});
	    cancelButton = new Button(this, SWT.PUSH);
	    cancelButton.setText("Отмена");
	    cancelButton.addSelectionListener(new SelectionAdapter() {
		    public void widgetSelected(SelectionEvent e) {
			fireChoiceSelected("cancel");
		    }
		});
	}
    }


    /* TESTING */
    public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	ServicePane servicePane = new ServicePane(shell, SWT.NONE);
	shell.setLayout(new FillLayout());
	shell.open();
	shell.pack();
	while (!shell.isDisposed()) {
	    if (!display.readAndDispatch()) 
		display.sleep();
	}
	display.dispose();    
    }
}
    
