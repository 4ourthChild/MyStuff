package edu.byu.isys413.fbrooke;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class WinController {

	protected Shell shell;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			WinController window = new WinController();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			SaleWin window = new SaleWin();
			window.open();
			//window.isVisible(false);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			Display display = Display.getDefault();
			LoginShell shell = new LoginShell(display);
			shell.open();
			shell.layout();
			
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("SWT Application");

	}

}
