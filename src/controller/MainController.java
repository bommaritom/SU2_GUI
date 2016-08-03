package controller;
 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import gui.GraphPanels;
import gui.Log;
import gui.MainView;
import io.Config;
import io.Executor;
import io.Mesh;
import io.SU2Input;

public class MainController{
    
	public MainView mainView = new MainView();
	
    /**This method is invoked by a button on the user input (options) window.  It is fed
     * 1: the window from which to gather data, and 2: The original map of parameters.
     */
	
	public MainController(){
		initViewActionListeners();
	}
	
	public void initViewActionListeners(){
		mainView.initButtons(new DefListener(), new SaveListener(), new RunListener());
		
	}
	
	public class DefListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			mainView.configInput.loadDefaults();
		}
	}
	
	public class SaveListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			saveData();
		}
	}
	
	public class RunListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			saveData();
			run();
		}
	}
	
    public void saveData(){
        
    	SU2Input rawData = mainView.getData();
    	
    	//formatConfigs wraps data in a format that Config can 
    	//easily write to the user_config.cfg file
        Map<String, String> configs = formatConfigs(rawData.p1data, mainView.params);
        
        
        String problemType = (String)mainView.problemBox.getSelectedItem();
        String turbModel = (String)mainView.turbBox.getSelectedItem();
        
        String mesh = rawData.p2data;
        
        //all inputs are then passed to the config and mesh file writers
        Config.write(configs, problemType, turbModel);
        Mesh.write(mesh);
        
    }
    
    public void run(){
    	Executor executor = new Executor();
    	executor.run();
    	
    	JPanel sfGraph;
    	try{
	    	GraphPanels graphPanels     = new GraphPanels();
	    	sfGraph = graphPanels.sfGraph;
    	}catch (FileNotFoundException e){
    		//e.printStackTrace();
    		
			JFrame frame = new JFrame("File Not Found");
			JOptionPane.showMessageDialog(frame,
					"Could not find the necessary files!\n"
					+ "SU2 may not have executed properly.",
					"Something went wrong! :(",
					JOptionPane.ERROR_MESSAGE);
    		
    		sfGraph = null;
    	}
    	
        //executor spits out a Jpanel (log) with console feedback on it
    	Log log = executor.log;
    	
    	//Add new graph functionality by:
    	// 1) Make graphPanels write the graph (in the constructor).
    	// 2) Handle possible errors (the graph should == null).
    	// 3) Change Tabs.update() so you can pass the graph to it.
    	mainView.tabs.update(log, sfGraph);
    }
    
    public static Map<String, String> formatConfigs(ArrayList<String> p1data, Map<String, String> configs){
    	int cur = 0;
        for ( Map.Entry<String, String> entry: configs.entrySet() ) {
            configs.put(entry.getKey(), p1data.get(cur));
            cur++;
        }
        return configs;
    }
    
    
}