package ua.elements.view;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;


public class ImageLabel extends Canvas {
    private Color onMouseBackground = new Color(null, 0, 175, 240);
    private Color onMouseForeground = new Color(null, 255, 255, 255);
    private Color offMouseBackground = new Color(null, 255, 255, 255);
    private Color offMouseForeground = new Color(null, 0, 175, 240);
    private Color background = offMouseBackground;
    private Color foreground = offMouseForeground;
    
    private Font font = new Font(null, "Courier", 16, SWT.BOLD);

    private int leftPadding = 80;
    private int topPadding = 80;
    private int rightPadding = 80;
    private int bottomPadding = 25;

    private String[] text = {"DEFAULT"};
    private Image image;

    public ImageLabel(Composite parent, int style, int lp, int tp, int rp, int bp) {
	this(parent, style);
	leftPadding = lp;
	topPadding = tp;
	rightPadding = rp;
	bottomPadding = bp;
    }

    public ImageLabel(Composite parent, int style) {
	super(parent, style);
	setSize(computeSize(SWT.NONE, SWT.NONE, false));
	setFont(font);
	addMouseTrackListener(new MouseTrackListener() {
		public void mouseEnter(MouseEvent e) {
		    ImageLabel.this.mouseEnter(e);
		}
		public void mouseExit(MouseEvent e) {
		    ImageLabel.this.mouseExit(e);
		}
		public void mouseHover(MouseEvent e) {
		    ImageLabel.this.mouseHover(e);
		}
	    });
	addPaintListener(new PaintListener() {
		public void paintControl(PaintEvent e) {
		    ImageLabel.this.paintControl(e);
		}
	    });
    }

    public void setText(String... text) {
	this.text = text;
	setSize(computeSize(SWT.NONE, SWT.NONE, true));
	redraw();
    }

    public void setImage(Image image) {
	this.image = image;
	redraw();
    }

    public String[] getText() {
	return text;
    }

    public Point computeSize(int wHint, int hHint, boolean changed) {
	GC gc = new GC(this);
	int y = 0;
	int x = gc.stringExtent(text[0]).x;
	for (String s : text) {
	    Point extent = gc.stringExtent(s);
	    y += extent.y;
	    if (gc.stringExtent(s).x > x)
		x = gc.stringExtent(s).x;
	}
	gc.dispose();
	return new Point(leftPadding + x + rightPadding, topPadding + y + bottomPadding);
    }

    private void mouseEnter(MouseEvent e) {
	background = onMouseBackground;
	foreground = onMouseForeground;
	redraw();
    }

    private void mouseExit(MouseEvent e) {
	background = offMouseBackground;
	foreground = offMouseForeground;
	redraw();
    }

    private void mouseHover(MouseEvent e) {
	//nothin
    }

    private void paintControl(PaintEvent e) {
	setBackground(background);
	e.gc.setForeground(foreground);
	Point size = computeSize(SWT.NONE, SWT.NONE, false);
	int x = size.x;
	int y = topPadding;
	if (image != null)
	    e.gc.drawImage(image, (x - image.getBounds().width) / 2, 0);
	for (String s : text) {
	    e.gc.drawString(s, (x - e.gc.textExtent(s).x) / 2, y);
	    y += e.gc.textExtent(s).y;
	}
    }

    /*
     * TEST METHOD 
     */
    public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	ImageLabel arrivalLabel = new ImageLabel(shell, SWT.NONE);
	arrivalLabel.setText("Приход", "товара");
	shell.open ();
	shell.pack();
	while (!shell.isDisposed()) {
	    if (!display.readAndDispatch()) 
		display.sleep();
	}
	display.dispose ();
    }
}
    
