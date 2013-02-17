package ua.elements.view;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;

public class GraphPane extends Composite {
    private double[] values = {0.0};

    private int leftPadding = 10;
    private int rightPadding = 20;
    private int topPadding = 10;
    private int bottomPadding = 20;

    private double maxValue = -1;

    public GraphPane(Composite parent, int style) {
	super(parent, style);
	setLayout(new GraphPaneLayout());
	addPaintListener(new PaintListener() {
		public void paintControl(PaintEvent e) {
		    GraphPane.this.paintControl(e);
		}
	    });
    }

    public void paintControl(PaintEvent e) {
	if (leftPadding == -1)
	    leftPadding = e.gc.textExtent(getMaxValue() + "").x;
	e.gc.drawLine(leftPadding, topPadding, leftPadding, getSize().y - bottomPadding);
	e.gc.drawLine(leftPadding, getSize().y - bottomPadding, getSize().x - rightPadding,
		      getSize().y - bottomPadding);
	drawAbscissaNumbers(e);
	drawOrdinateNumbers(e);
    }

    public void drawAbscissaNumbers(PaintEvent e) {
	for (int i = 1; i <= values.length; i++) {
	    e.gc.drawText(i + "", getPoint(i, 0).x + leftPadding,
			  getSize().y - bottomPadding - getPoint(i, 0).y - 10);
		
	}
    }

    public void drawContent(PaintEvent e) {
	//soon
    }

    public void drawOrdinateNumbers(PaintEvent e) {
	int step = (int)(getMaxValue() / countNumbersOnOrdinate(e));
	step -= step % 10;
	for (int i = 0, value = step; value <= getMaxValue() ; i++, value += step) {
	    e.gc.drawText(value + "", 0, getSize().y - bottomPadding - getPoint(0, value).y - 10);
	}
    }
    
    private int countNumbersOnOrdinate(PaintEvent e) {
	return (getSize().y - bottomPadding - topPadding) / e.gc.textExtent("").y;
    }

    public void setValues(double[] values) {
	this.values = values;
	maxValue = -1;
	leftPadding = -1;
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

    public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	shell.setLayout(new FillLayout());
	GraphPane graphPane = new GraphPane(shell, SWT.NONE);
	graphPane.setValues(new double[]{15,34,54,65,76,8,787,998,90,90,9,1200});
	graphPane.setValues(new double[]{15,34,54,65,76,8,787,998,90,90,9,1200, 1300});
	shell.pack();
	shell.open();
	while (!shell.isDisposed()) {
	    if (!display.readAndDispatch()) 
		display.sleep();
	}
	display.dispose();
    }
}
