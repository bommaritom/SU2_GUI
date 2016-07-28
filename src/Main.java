import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import controller.MainController;

public class Main {

	public static void main(String[] args){
		setLF();
		new MainController();
	}
	
	public static void setLF(){
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
