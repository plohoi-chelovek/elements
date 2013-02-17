package ua.elements.view;

import ua.elements.*;
import ua.elements.model.*;

import java.util.*;

import javax.swing.event.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.events.*;

public class BalancePane extends Composite {
    private Color background = new Color(null, 255, 255, 255);
    private Color foreground = new Color(null, 0, 175, 240);
    private Text debit;

    private EventListenerList listeners = new EventListenerList();
    
    public BalancePane(Composite parent, int style) {
	super(parent, style);
	setBackground(background);
	GridLayout layout = new GridLayout();
	layout.numColumns = 2;
	setLayout(layout);

	Label debitLabel = new Label(this, SWT.NONE);
	debitLabel.setText("Прибыль");
	debitLabel.setBackground(background);
	debitLabel.setForeground(foreground);

	debit = new Text(this, SWT.BORDER);
	GridData debitData = new GridData();
	debitData.widthHint = 200;
	debit.setLayoutData(debitData);

	ButtonsPane buttonsPane = new ButtonsPane(this, SWT.NONE);
	GridData data = new GridData();
	data.horizontalSpan = 2;
	buttonsPane.setLayoutData(data);

	double d = 0;
	for (Product product : App.getDataManagement().getProductManagement().selectAll()) {
	    d += product.getPrice();
	}
	debit.setText("" + d);
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
	    okButton.setText("Вернуться");
	    okButton.addSelectionListener(new SelectionAdapter() {
		    public void widgetSelected(SelectionEvent e) {
			fireChoiceSelected("cancel");
		    }
		});
	}
    }

}
    
