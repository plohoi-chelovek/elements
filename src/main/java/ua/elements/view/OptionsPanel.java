package ua.elements.view;

import ua.elements.*;

import java.io.*;

import javax.swing.event.EventListenerList;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;

public class OptionsPanel extends Composite {
    private Color background = new Color(null, 255, 255, 255);
    private Color foreground = new Color(null, 0, 175, 240);

    private FileDialog syncDialog;
    private ImageLabel syncLabel;
    private MessageBox syncMessageDialog;

    private FileDialog backupDialog;
    private ImageLabel backupLabel;
    private MessageBox backupMessageDialog;

    private ImageLabel arrivalLabel;
    private ImageLabel chargeLabel;
    private ImageLabel servicesLabel;
    private ImageLabel registerLabel;

    private EventListenerList listeners = new EventListenerList();

    public OptionsPanel(Composite parent, int style) {
	super(parent, style);
	setLayout(new OptionsPanelLayout());
	// setBackground(background);
	setBackgroundImage(new Image(null, getClass().getResourceAsStream("/background.png")));

	syncMessageDialog = new MessageBox(getShell(), SWT.ICON_INFORMATION | SWT.OK);
	syncMessageDialog.setMessage("Для дальнейшей работы программы необходима перезагрузка");

	syncDialog = new FileDialog(getShell(), SWT.OPEN);
	syncDialog.setFilterNames(new String [] {"DB Files"});
	syncDialog.setFilterExtensions(new String [] {"*.db"});
	syncDialog.setFilterPath(System.getProperty("user.home"));

	syncLabel = new ImageLabel(this, SWT.NONE, 20, 5, 20, 0);
	syncLabel.setText("");
	syncLabel.setImage(new Image(null, getClass().getResourceAsStream("/sync.png")));
	syncLabel.addMouseListener(new MouseAdapter() {
		public void mouseUp(MouseEvent e) {
		    String file = syncDialog.open();
		    if (file != null)
			if (App.getDataManagement().sync(new File(file)))
			    syncMessageDialog.open();
		}
	    });

	backupMessageDialog = new MessageBox(getShell(), SWT.ERROR | SWT.OK);
	backupMessageDialog.setMessage("Невозможно сохранить дамп. Попробуйте указать другой файл");

	backupDialog = new FileDialog(getShell(), SWT.SAVE);
	backupDialog.setFilterNames(new String [] {"DB Files"});
	backupDialog.setFilterExtensions(new String [] {"*.db"});
	backupDialog.setFileName("data.h2.db");
	backupDialog.setFilterPath(System.getProperty("user.home"));

	backupLabel = new ImageLabel(this, SWT.NONE, 20, 5, 20, 0);
	backupLabel.setText("");
	backupLabel.setImage(new Image(null, getClass().getResourceAsStream("/backup.png")));
	backupLabel.addMouseListener(new MouseAdapter() {
		public void mouseUp(MouseEvent e) {
		    String file = backupDialog.open();
		    if (file != null) 
			if (!App.getDataManagement().dump(new File(file))) {
			    backupMessageDialog.open();
			}
		}
	    });

	arrivalLabel = new ImageLabel(this, SWT.NONE);
	arrivalLabel.setText("Приход", "Товара");
	arrivalLabel.addMouseListener(new MouseAdapter() {
		public void mouseUp(MouseEvent e) {
		    fireOptionSelected("arrivalOption");
		}
	    });
	arrivalLabel.setImage(new Image(null, getClass().getResourceAsStream("/add.png")));

	chargeLabel = new ImageLabel(this, SWT.NONE);
	chargeLabel.setText("Расход", "Товара");
	chargeLabel.addMouseListener(new MouseAdapter() {
		public void mouseUp(MouseEvent e) {
		    fireOptionSelected("chargeOption");
		}
	    });
	chargeLabel.setImage(new Image(null, getClass().getResourceAsStream("/charge.png")));
	servicesLabel = new ImageLabel(this, SWT.NONE);
	servicesLabel.setText("Услуги");
	servicesLabel.addMouseListener(new MouseAdapter() {
		public void mouseUp(MouseEvent e) {
		    fireOptionSelected("serviceOption");
		}
	    });
	servicesLabel.setImage(new Image(null, getClass().getResourceAsStream("/service.png")));
	registerLabel = new ImageLabel(this, SWT.NONE);
	registerLabel.setText("Журнал");
	registerLabel.addMouseListener(new MouseAdapter() {
		public void mouseUp(MouseEvent e) {
		    fireOptionSelected("registerOption");
		}
	    });
	registerLabel.setImage(new Image(null, getClass().getResourceAsStream("/register.png")));
	addPaintListener(new PaintListener() {
		public void paintControl(PaintEvent e) {
		    OptionsPanel.this.paintControl(e);
		}
	    });
    }

    public void addOptionsListener(OptionsListener l) {
	listeners.add(OptionsListener.class, l);
    }

    public void removeOptionsListener(OptionsListener l) {
	listeners.remove(OptionsListener.class, l);
    }

    private void fireOptionSelected(String option) {
	OptionEvent event = new OptionEvent(this, option);
	Object[] l = listeners.getListenerList();
	for (int i = l.length-2; i>=0; i-=2) {
	    if (l[i]==OptionsListener.class) {
		((OptionsListener)l[i+1]).optionSelected(event);
	    }
	}
    }

    private void paintControl(PaintEvent e) {
	System.out.println("paintControl");
	e.gc.setBackground(foreground);
	e.gc.fillRectangle(0, 30, getSize().x, 2); 
    }

    /* INNER CLASS PART */
    private class OptionsPanelLayout extends Layout {
	protected Point computeSize(Composite composite, int wHint, int hHint,
				    boolean changed) {
	    int x = Math.max(arrivalLabel.computeSize(SWT.NONE, SWT.NONE, false).x +
			     chargeLabel.computeSize(SWT.NONE, SWT.NONE, false).x,
			     servicesLabel.computeSize(SWT.NONE, SWT.NONE, false).x +
			     registerLabel.computeSize(SWT.NONE, SWT.NONE, false).x);
	    int y = Math.max(arrivalLabel.computeSize(SWT.NONE, SWT.NONE, false).y +
			     servicesLabel.computeSize(SWT.NONE, SWT.NONE, false).y,
			     chargeLabel.computeSize(SWT.NONE, SWT.NONE, false).y +
			     registerLabel.computeSize(SWT.NONE, SWT.NONE, false).y);
	    return new Point(x, y + 55);
	}
 
	protected void layout(Composite composite, boolean changed) {
	    arrivalLabel.setBounds(0, 0 + 55, arrivalLabel.computeSize(SWT.NONE, SWT.NONE, false).x,
				   arrivalLabel.computeSize(SWT.NONE, SWT.NONE, false).y);
	    chargeLabel.setBounds(arrivalLabel.computeSize(SWT.NONE, SWT.NONE, false).x, 0 + 55,
				  chargeLabel.computeSize(SWT.NONE, SWT.NONE, false).x,
				  chargeLabel.computeSize(SWT.NONE, SWT.NONE, false).y);
	    servicesLabel.setBounds(0, arrivalLabel.computeSize(SWT.NONE, SWT.NONE, false).y + 55,
				    servicesLabel.computeSize(SWT.NONE, SWT.NONE, false).x,
				    servicesLabel.computeSize(SWT.NONE, SWT.NONE, false).y);
	    registerLabel.setBounds(servicesLabel.computeSize(SWT.NONE, SWT.NONE, false).x,
				    chargeLabel.computeSize(SWT.NONE, SWT.NONE, false).y + 55,
				    registerLabel.computeSize(SWT.NONE, SWT.NONE, false).x,
				    registerLabel.computeSize(SWT.NONE, SWT.NONE, false).y);
	    syncLabel.setLocation(getSize().x - syncLabel.getSize().x, 0);
	    backupLabel.setLocation(syncLabel.getLocation().x - backupLabel.getSize().x, 0);
	}
    }

    /* TESTING */
    public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell(display, SWT.NONE);
	shell.setLayout(new FillLayout());
	OptionsPanel optionsPanel = new OptionsPanel(shell, SWT.NONE);
	shell.pack();
	shell.open();
	while (!shell.isDisposed()) {
	    if (!display.readAndDispatch()) 
		display.sleep();
	}
	display.dispose();
    }
}
	
