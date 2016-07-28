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
	public JPanel graph;
	
	
	public Tabs(MeshInput m){
		setPreferredSize(new Dimension(700,400));
		meshInput = m;
		addTab("Mesh", meshInput);
	}
	
	public void update(Log l, JPanel g){
		log    = l;
		graph = g;
		
		int numOfTabs = this.getTabCount();
		//remove all tabs except the user input mesh
		//need to remove the tab at index 1 each time because
		//the tabs "slide" over each time you remove one
		for ( int i = 1; i < numOfTabs; i++) this.remove(1);
		
		if ( log != null ) addTab("Log", log);
		if ( graph != null) addTab("Surface Pressure", g);
		
		
		setSelectedIndex(1);
	}
}
