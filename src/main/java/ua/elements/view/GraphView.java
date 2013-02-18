package ua.elements.view;

import ua.elements.*;
import ua.elements.model.*;

import java.util.*;

import javax.swing.event.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;

public class GraphView extends Composite {
    private GraphPane graphPane;

    private Combo year;
    private String[] yearTitles;
    private Combo month;
    private String[] monthTitles = {"Январь", "Февраль", "Март", "Апрель",
				    "Май", "Июнь", "Июль", "Август",
				    "Сентябрь", "Октябрь",  "Ноябрь" , "Декабрь"};

    private Button show;
    private Button back;

    private EventListenerList listeners = new EventListenerList();

    public GraphView(Composite parent, int style) {
	super(parent, style);

	GridLayout layout = new GridLayout();
	layout.numColumns = 3;
	setLayout(layout);

	graphPane = new GraphPane(this, SWT.NONE);
	GridData graphData = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
	graphData.widthHint = 400;
	graphPane.setLayoutData(graphData);
	
	Calendar cal = new GregorianCalendar();
	cal.setTime(new Date());
	yearTitles = new String[20];
	for (int i = 0; i < yearTitles.length; i++)
	    yearTitles[i] = String.format("%d", cal.get(Calendar.YEAR) - i);

	year = new Combo(this, SWT.NONE);
	year.setItems(yearTitles);
	year.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
	year.select(0);
	year.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
		    //nonthing
		}
	    });

	month = new Combo(this, SWT.NONE);
	month.setItems(monthTitles);
	month.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
	month.select(cal.get(Calendar.MONTH));
	month.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
		    //nothing
		}
	    });

	show = new Button(this, SWT.NONE);
	show.setText("Показать");
	show.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
	show.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
		    setValues(App.getDataManagement().getProductManagement().
			      getValues(Integer.parseInt(year.getText()),
					month.getSelectionIndex() + 1));
		}
	    });
	
	back = new Button(this, SWT.NONE);
	back.setText("Вернуться");
	back.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
	back.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
		    fireChoiceSelected("cancel");
		}
	    });

	App.getDataManagement().getProductManagement().
	    addProductManagementListener(new ProductManagementListener() {
		    public void productInserted(ProductManagementEvent event) {
			setValues(App.getDataManagement().getProductManagement().
				  getValues(Integer.parseInt(year.getText()),
					    month.getSelectionIndex() + 1));
		    }

		    public void productChargeInserted(ProductManagementEvent event) {
			//nothing
		    }
		});

	setValues(App.getDataManagement().getProductManagement().
		  getValues(Integer.parseInt(year.getText()),
			    month.getSelectionIndex() + 1));

	
    }

    public void setValues(double[] values) {
	graphPane.setValues(values);
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

    private class GraphPane extends Composite {
	private double[] values = {0.0};

	private int leftPadding = 10;
	private int rightPadding = 20;
	private int topPadding = 10;
	private int bottomPadding = 40;

	private double maxValue = -1;

	private Color background = new Color(null, 0, 175, 240);
	private Color background2 = new Color(null, 255, 255, 255);

	private Font font = new Font(null, "Courier", 12, SWT.BOLD);

	public GraphPane(Composite parent, int style) {
	    super(parent, style);
	    setBackground(background2);
	    setFont(font);
	    setLayout(new GraphPaneLayout());
	    addPaintListener(new PaintListener() {
		    public void paintControl(PaintEvent e) {
			GraphPane.this.paintControl(e);
		    }
		});
	}

	public void paintControl(PaintEvent e) {
	    if (getSize().x > 50 && getSize().y > 50) {
		if (leftPadding == -1)
		    leftPadding = e.gc.textExtent(getMaxValue() + "").x;
		e.gc.drawLine(leftPadding, topPadding, leftPadding, getSize().y - bottomPadding);
		e.gc.drawLine(leftPadding, getSize().y - bottomPadding, getSize().x - rightPadding,
			      getSize().y - bottomPadding);
		drawAbscissaNumbers(e);
		drawOrdinateNumbers(e);
		drawContent(e);
	    }
	}

	private void drawAbscissaNumbers(PaintEvent e) {
	    for (int i = 1, x = 0; i <= values.length; i++) {
		x = (int)(getPoint(i, 0).x + leftPadding - getXStep() / 2 - 
			  e.gc.textExtent(i + "").x / 2);
		e.gc.drawText(i + "", x, getSize().y - bottomPadding - getPoint(i, 0).y);
	    }
	}

	private void drawOrdinateNumbers(PaintEvent e) {
	    int step = (int)(getMaxValue() / countNumbersOnOrdinate(e));
	    if (step > 10)
		step -= step % 10;
	    if (step > 0) {
		for (int i = 0, value = step, y = 0; value <= getMaxValue() ; i++, value += step) {
		    y = getSize().y - bottomPadding - getPoint(0, value).y;
		    e.gc.drawLine(leftPadding - 2, y + e.gc.textExtent(value + "").y / 2, 
				  leftPadding + 2, y + e.gc.textExtent(value + "").y / 2);
		    e.gc.drawText(value + "", 0, y);
		}
	    }
	}

	private void drawContent(PaintEvent e) {
	    e.gc.setBackground(background);
	    for (int i = 0, x = 0, y = 0; i < values.length; i++) {
		x = getPoint(i, values[i]).x;
		y = getPoint(i, values[i]).y;
		e.gc.fillRectangle(leftPadding + x, getSize().y - y - bottomPadding,
				   (int)getXStep(), y);
		e.gc.drawRectangle(leftPadding + x, getSize().y - y - bottomPadding,
				   (int)getXStep(), y);
	    }
	}
    
	private int countNumbersOnOrdinate(PaintEvent e) {
	    return (getSize().y - bottomPadding - topPadding) / e.gc.textExtent("").y;
	}

	public void setValues(double[] values) {
	    this.values = values;
	    maxValue = -1;
	    leftPadding = -1;
	    redraw();
	}

	public double[] getValues() {
	    return values;
	}

	public int getMaxDay() {
	    return values.length;
	}

	public double getMaxValue() {
	    if (maxValue < 0) {
		double max = values[0];
		for (int i = 1; i < values.length; i++)
		    if (max < values[i])
			max = values[i];
		maxValue = max;
	    }
	    return maxValue;
	}

	public double getXStep() {
	    return (double)(getSize().x - leftPadding - rightPadding) / getMaxDay();
	}

	public double getYStep() {
	    return (double)(getSize().y - topPadding - bottomPadding) / getMaxValue();
	}

	public Point getPoint(int day, double value) {
	    return new Point((int)(day * getXStep()), 
			     (int)(value * getYStep()));
	}

	private class GraphPaneLayout extends Layout {
	    protected Point computeSize(Composite composite, int wHint, int hHint,
					boolean changed) {
		return new Point(100, 100);
	    }
 
	    protected void layout(Composite composite, boolean changed) {
		//soon
	    }
	}
    }

    public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	shell.setLayout(new FillLayout());
	GraphView graphView = new GraphView(shell, SWT.NONE);
	// graphView.setValues(new double[]{15,34,54,65,76,8,787,998,90,90,9,1200});
	graphView.setValues(new double[]{15,34,54,65,76,8,787,998,90,90,94,1200, 1300,
					 15,34,54,65,76,8,787,998,90,90,94,1200, 1300, 43, 78,
					 756, 4320, 456});
	shell.pack();
	shell.open();
	while (!shell.isDisposed()) {
	    if (!display.readAndDispatch()) 
		display.sleep();
	}
	display.dispose();
    }
}

