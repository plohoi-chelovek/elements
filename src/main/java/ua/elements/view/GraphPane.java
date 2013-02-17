package ua.elements.view;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.graphics.*;

public class GraphPane extends Composite {
    private Button okButton;

    private double[] values = {0.0} ;
    private int leftPadding = 30;
    private int rightPadding = 15;
    private int topPadding = 5;
    private int bottomPadding = 30;

    public GraphPane(Composite parent, int style, double[] values) {
	this(parent, style);
	this.values = values;
    }
	
    public GraphPane(Composite parent, int style) {
	super(parent, style);
	okButton = new Button(this, SWT.PUSH);
	okButton.setText("Добавить");
	okButton.pack();
	addPaintListener(new PaintListener() {
		public void paintControl(PaintEvent e) {
		    GraphPane.this.paintControl(e);
		}
	    });
    }

    public void setValues(double[] values) {
	this.values = values;
	redraw();
    }

    public Point computeSize(int wHint, int hHint, boolean changed) {
	return new Point(600, 600);
    }

    private void paintControl(PaintEvent e) {
	System.out.println(getYStep());
	int x = getSize().x;
	int y = getSize().y;
	e.gc.drawLine(leftPadding, topPadding, leftPadding, y - bottomPadding);
	e.gc.drawLine(leftPadding, y - bottomPadding, x - rightPadding, y - bottomPadding);
	for (int i = 1; i <= values.length; i++) {
	    e.gc.drawText(i + "", leftPadding + (int)(getXStep() * i), y - 28); 
	    e.gc.drawText(values[i-1] + "", leftPadding - 20, (int)(y - bottomPadding -
								    values[i-1] * getYStep()));
	    e.gc.drawOval(leftPadding + (int)(getXStep() * i - 3), (int)(y - bottomPadding -
								  values[i-1] * getYStep() - 3),
			  6, 6);
	    if (i > 1) 
		e.gc.drawLine(leftPadding + (int)(getXStep() * i), (int)(y - bottomPadding -
								  values[i-1] * getYStep()),
			      leftPadding + (int)(getXStep() * (i - 1)),
			      (int)(y - bottomPadding - values[i-2] * getYStep()));
	}
						    
    }

    private double getXStep() {
	return (getSize().x - leftPadding - rightPadding) / values.length;
    }

    private double getYStep() {
	System.out.println(getSize().y);
	return  (int)((getSize().y - topPadding - bottomPadding) / getMax());
    }

    private double getMax() {
	double max = 0;
	for (double i : values)
	    if (i > max)
		max = i;
	return max;
    }

    private class GraphPanelLayout extends Layout {
	protected Point computeSize(Composite composite, int wHint, int hHint,
				    boolean changed) {
	    return new Point(600, 600);
	}
 
	protected void layout(Composite composite, boolean changed) {
	    okButton.setLocation(0, 0);
	}
    }

    /*
     * TEST METHOD 
     */
    public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	shell.setLayout(new FillLayout());
	GraphPane graphPane = new GraphPane(shell, SWT.NONE);
	graphPane.setValues(new double[]{0});
	shell.open();
	shell.pack();
	while (!shell.isDisposed()) {
	    if (!display.readAndDispatch()) 
		display.sleep();
	}
	display.dispose ();
    }
}
