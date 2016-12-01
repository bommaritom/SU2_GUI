package controller;
 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.jfree.chart.ChartPanel;

import gui.GraphPanels;
import gui.Log;
import gui.MainView;
import gui.Stats;
import io.Config;
import io.Executor;
import io.Mesh;
import io.SU2Input;

// class MainController is designed to react to button presses and to call upon other classes.
public class MainController{
    
	public MainView mainView;
	public Stats stats;
	public GraphPanels graphPanels;
	
	
	
    /**This method is invoked by a button on the user input (options) window.  It is fed
     * 1: the window from which to gather data, and 2: The original map of parameters.
     */
	
	public MainController()
	{
		mainView = new MainView();
		initViewActionListeners();
	}
	
	private void initViewActionListeners()
	{
		mainView.initButtons(new DefListener(), 
				     new SaveListener(), 
			             new RunListener(),
				     new NewListener());
		
	}
	
	//when the "defaults" button is pressed, tells @mainView to load default parameters
	public class DefListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			mainView.configInput.loadDefaults();
		}
	}
	
	//when the "save" button is pressed, saves the data
	public class SaveListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			saveData();
		}
	}
	
	//when the "run SU2" button is pressed, saves the data and then runs, with parameter @overwrite set to false
	public class RunListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			saveData();
			run(false);
		}
	}
	
	public class NewListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e){
			int n;
			if (mainView.tabs.getTabCount() > 1){
				n = JOptionPane.showOptionDialog(
					new JFrame("Confirm"),
					"Do you really want to start a new graph?",
					"Confirm",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,
					new Object[]{"Cancel", "Yes"},
					"Yes");
			}else{
				n = 1;
			}
			if (n == 1){
				saveData();
				run(true);
			}
		}
	}
	
    private void saveData()
    {
        
    	SU2Input rawData = mainView.getData();
    	
    	/**formatConfigs wraps data in a format that Config can 
    	 * easily write to the user_config.cfg file */
        Map<String, String> configs = formatConfigs(rawData.p1data, mainView.params);
        
        //retrieves the options that the user has selected from the dropdown menus
        String problemType = (String)mainView.problemBox.getSelectedItem();
        String turbModel = (String)mainView.turbBox.getSelectedItem();
        
	//retrieves the raw "mesh" data as one long string
        String mesh = rawData.p2data;
        
        //all inputs are then passed to the config and mesh file writers
        Config.write(configs, problemType, turbModel);
        Mesh.write(mesh);
        
    }
    
    //@overwrite determines whether or not the graph will be overwritten, or if a new line will simply be drawn
    private void run(Boolean overwrite)
    {
    	Executor executor = new Executor();
    	executor.run();
    	
    	Boolean update = true;
    	
	
    	ChartPanel sfGraph = null;
	    
	
    	if (overwrite || graphPanels == null){
	    	try{
		    	graphPanels = new GraphPanels();
		    	sfGraph = graphPanels.sfGraph;
	    	}catch (FileNotFoundException e){
	    		//e.printStackTrace();
	    		showRunErrorMessage();
	    		update = false;
	    	}
	    	
    	} else if (!overwrite) {
    		try{
    			graphPanels.addNewSurfacePressureData();
    			sfGraph = graphPanels.sfGraph;
    		}catch (FileNotFoundException e){
    			//e.printStackTrace();
    			showRunErrorMessage();
    			update = false;
    		}
    	}
    	//executor spits out a Jpanel (log) with console feedback on it
    	Log log = executor.log;
    	mainView.tabs.update(log, sfGraph);
    	
    	if (update){
	    	if (overwrite || stats == null){
	    		if (stats == null){
	    			stats = new Stats();
	    		}else{
	    			stats.clear();
	    		}
	    		stats.updateStats();
	    	}else if (!overwrite && stats != null){
	    		stats.updateStats();
	    	}
    	}
    	mainView.toFront();
    	
    }
    
    private static void showRunErrorMessage()
    {
    	JFrame frame = new JFrame("File Not Found");
		JOptionPane.showMessageDialog(frame,
				"Could not find the necessary files!\n"
				+ "SU2 may not have executed properly.",
				"Something went wrong! :(",
				JOptionPane.ERROR_MESSAGE);
    }
    
    private static Map<String, String> formatConfigs(ArrayList<String> p1data, Map<String, String> configs)
    {
    	int cur = 0;
        for ( Map.Entry<String, String> entry: configs.entrySet() ) {
            configs.put(entry.getKey(), p1data.get(cur));
            cur++;
        }
        return configs;
    }
    
    
}
