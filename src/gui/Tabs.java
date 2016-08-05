package gui;

import java.awt.Dimension;

import javax.swing.JTabbedPane;

import org.jfree.chart.ChartPanel;

public class Tabs extends JTabbedPane{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7517928095048898349L;
	
	public MeshInput meshInput;
	public Log log;
	public ChartPanel sfGraph;
	
	public Tabs(MeshInput m){
		setPreferredSize(new Dimension(700,430));
		meshInput = m;
		addTab("Mesh", meshInput);
	}
	
	
	public void update(Log l, ChartPanel sf){
		log    = l;
		sfGraph = sf;
		
		int numOfTabs = this.getTabCount();
		
		if ( log != null ){
			if (numOfTabs > 1) this.remove(1);
			this.insertTab("Console", null, log, "Console", 1);
		}
		if ( sfGraph != null){
			if (numOfTabs > 2) this.remove(2);
			this.insertTab("Surface Pressure", null, sfGraph, "Surface Pressure", 2);
		}
		
		setSelectedIndex(this.getTabCount()-1);
	}
}
