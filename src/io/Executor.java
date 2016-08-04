package io;

import gui.Log;
import utils.OSScanner;

import java.io.*;

/**
 * SU2 relies on command-line prompts. This class helps interface between
 * the GUI and the OS-native command-line console.  Currently only works
 * on Unix-based systems.
 * @author marcobom
 *
 */
public class Executor{
	
	public Log log;
	
    public void run(){
        
    	deleteOldFiles();
    	
    	if (OSScanner.isMac() || OSScanner.isUnix()){
	        runOnUnix();
    	} else if (OSScanner.isWindows()){
    		runOnWindows();
    	}
        
        
        File shell = new File("run_bash_script.txt");
        if ( !shell.delete() ) shell.deleteOnExit();
        
    }
    
    private void runOnUnix(){
    	writeBash();
        runBash();
        deleteBash();
    }
    
    private void runOnWindows(){
    	runDirectly();
    }
    
    private void deleteOldFiles(){
    	deleteFile("su2/flow.dat");
    	deleteFile("su2/forces_breakdown.dat");
    	deleteFile("su2/history.dat");
    	deleteFile("su2/restart_flow.dat");
    	deleteFile("su2/surface_flow.csv");
    	deleteFile("su2/surface_flow.dat");
    }
    
    private void deleteFile(String fileName){
    	File file = new File(fileName);
    	file.delete();
    }
    
    private void writeBash(){
		String shellFileName = "run_bash_script.txt";
		        
        BufferedWriter bw = null;
        
        try{
            bw = new BufferedWriter(new FileWriter(shellFileName));
            bw.write("#!bin/bash");
            bw.write(System.getProperty("line.separator"));
            bw.write("cd su2");
            bw.write(System.getProperty("line.separator"));
            bw.write("SU2_CFD user_conf.cfg");
            bw.write(System.getProperty("line.separator"));
            bw.close();
        
        } catch (IOException e) {
            System.out.println("Something happened while writing the bash.");
        }
	}
    
    private void runBash(){
    	try {
            
            /** 
             * "--login" is necessary because the environment variables that 
             *  SU2_CFD needs in order to execute properly are stored in
             *  .bash_profile when the user correctly installs SU2.
             */
        	
            String [] cmd = {"bash", "--login", "run_bash_script.txt"};
            
            Process p = Runtime.getRuntime().exec(cmd);
            BufferedReader stdInput = new BufferedReader(new 
                 InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new 
                 InputStreamReader(p.getErrorStream()));
            
            log = new Log(stdInput, stdError);
            
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    
    private void deleteBash(){
    	deleteFile("run_bash_script.txt");
    }
    
    private void runDirectly(){
    	try{
    		
    		/**
    		 * On Windows, the SU2 environment variables have system-wide
    		 * scope, so there is no need for a bash.
    		 */
    		String[] cmd = {"%SU2_RUN%SU2_CFD", "user_config.cfg"};
    		Process p = Runtime.getRuntime().exec(cmd);
            BufferedReader stdInput = new BufferedReader(new 
                 InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new 
                 InputStreamReader(p.getErrorStream()));
            
            log = new Log(stdInput, stdError);
            
        }catch (IOException e){
            e.printStackTrace();
    	}
    }
    
}
