import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import controller.MainController;
import utils.OSScanner;

public class Main {
	
	public static void main(String[] args){
		setLAndF();
		if (!OSScanner.isMac() && !OSScanner.isUnix() || true){
			OSWarning();
		}
		new MainController();
	}
	
	public static void OSWarning(){
		JFrame frame = new JFrame("Compatability Warning");
		JOptionPane.showMessageDialog(frame,
				"It appears you're running this software\n"
				+ "on a Windows OS. Please note that Windows\n"
				+ "functionality is not fully tested; there\n"
				+ "may be bugs when interacting with your computer.",
				"Windows Functionality",
				JOptionPane.WARNING_MESSAGE);
	}
	
	public static void setLAndF(){
		try{
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} 
	    catch (UnsupportedLookAndFeelException e) {
	       e.printStackTrace();
	    }
	    catch (ClassNotFoundException e) {
	    	e.printStackTrace();
	    }
	    catch (InstantiationException e) {
	    	e.printStackTrace();
	    }
	    catch (IllegalAccessException e) {
	    	e.printStackTrace();
	    }
	}
	
}
