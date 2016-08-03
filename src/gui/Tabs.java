package gui;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class Tabs extends JTabbedPane{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7517928095048898349L;
	
	public MeshInput meshInput;
	public Log log;
	public JPanel sfGraph;
  //public JPanel exampleGraph;
	
	public Tabs(MeshInput m){
		setPreferredSize(new Dimension(700,400));
		meshInput = m;
		addTab("Mesh", meshInput);
	}
	
	
	public void update(Log l, JPanel sf /**JPanel ex*/){
		log    = l;
		sfGraph = sf;
	  //exampleGraph = ex;
		
		int numOfTabs = this.getTabCount();
		
		//remove all tabs except the user input mesh
		//need to remove the tab at index 1 each time because
		//the tabs "slide" over each time you remove one
		for ( int i = 1; i < numOfTabs; i++) this.remove(1);
		
		if ( log != null ) addTab("Console", log);
		if ( sfGraph != null) addTab("Surface Pressure", sfGraph);
	  //if ( exampleGraph != null) addTab("Example Graph", exampleGraph);
		
		
		setSelectedIndex(1);
	}
}
