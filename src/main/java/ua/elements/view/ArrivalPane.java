package ua.elements.view;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;

public class ArrivalPane extends Composite {
    private Color background = new Color(null, 255, 255, 255);
    private Color foreground = new Color(null, 0, 175, 240);
    private Text name;
    private Text count;
    private Text price;
    
    public ArrivalPane(Composite parent, int style) {
	super(parent, style);
	setBackground(background);
	GridLayout layout = new GridLayout();
	layout.numColumns = 2;
	setLayout(layout);

	Label nameLabel = new Label(this, SWT.NONE);
	nameLabel.setText("Наименование");
	nameLabel.setBackground(background);
	nameLabel.setForeground(foreground);

	name = new Text(this, SWT.BORDER);
	GridData nameData = new GridData();
	nameData.widthHint = 200;
	name.setLayoutData(nameData);

	Label countLabel = new Label(this, SWT.NONE);
	countLabel.setText("Количество");
	countLabel.setBackground(background);
	countLabel.setForeground(foreground);

	count = new Text(this, SWT.BORDER);
	GridData countData = new GridData();
	countData.widthHint = 200;
	count.setLayoutData(countData);

	Label priceLabel = new Label(this, SWT.NONE);
	priceLabel.setText("Стоимость");
	priceLabel.setBackground(background);
	priceLabel.setForeground(foreground);

	price = new Text(this, SWT.BORDER);
	GridData priceData = new GridData();
	priceData.widthHint = 200;
	price.setLayoutData(priceData);
	
	ButtonsPane buttonsPane = new ButtonsPane(this, SWT.NONE);
	GridData data = new GridData();
	data.horizontalSpan = 2;
	buttonsPane.setLayoutData(data);
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
	    cancelButton = new Button(this, SWT.PUSH);
	    cancelButton.setText("Отмена");
	}
    }


    /* TESTING */
    public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	ArrivalPane arrivalPane = new ArrivalPane(shell, SWT.NONE);
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
    
