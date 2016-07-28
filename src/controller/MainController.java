package controller;
 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

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
    	
        Map<String, String> configs = formatConfigs(rawData.p1data, mainView.params);
        String problemType = (String)mainView.problemBox.getSelectedItem();
        String mesh = rawData.p2data;
        
        Config.write(configs, problemType);
        Mesh.write(mesh);
        
    }
    
    public void run(){
    	Executor executor = new Executor();
    	executor.run();
    	JPanel graph;
    	try{
	    	GraphPanels graphPanels     = new GraphPanels();
	    	graph = graphPanels.sfGraph;
    	}catch (Exception e){
    		graph = null;
    	}
    	Log log                      = executor.log;
    	
    	mainView.tabs.update(log, graph);
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