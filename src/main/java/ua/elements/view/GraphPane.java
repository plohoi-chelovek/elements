package ua.elements.view;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;

public class GraphPane extends Composite {
    private double[] values = {0.0};

    private int leftPadding = 10;
    private int rightPadding = 10;
    private int topPadding = 10;
    private int bottomPadding = 10;

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
	System.out.println(getSize());
	System.out.println(getPoint(getMaxDay(), getMaxValue()));
    }

    public void setValues(double[] values) {
	this.values = values;
    }

    public double[] getValues() {
	return values;
    }

    public int getMaxDay() {
	return values.length;
    }

    public double getMaxValue() {
	double max = values[0];
	for (int i = 1; i < values.length; i++)
	    if (max < values[i])
		max = values[i];
	return max;
    }

    public double getXStep() {
	return (double)getSize().x / getMaxDay();
    }

    public double getYStep() {
	return (double)getSize().y / getMaxValue();
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
	shell.pack();
	shell.open();
	while (!shell.isDisposed()) {
	    if (!display.readAndDispatch()) 
		display.sleep();
	}
	display.dispose();
    }
}
