package io;

import gui.Log;

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
        
        writeBash();
        runBash();
        
        
        File shell = new File("run_bash_script.txt");
        if ( !shell.delete() ) shell.deleteOnExit();
        
    }
    
    public void writeBash(){
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
    
    public void runBash(){
    	try {
            
            // run the SU2 executable command
                
            /** "--login" is necessary because some environment variables that 
               SU2_CFD needs in order to execute properly are stored in
               .bash_profile when the user correctly installs SU2.*/
        	
            String [] cmd = {"bash", "--login", "run_bash_script.txt"};
            
            Process p = Runtime.getRuntime().exec(cmd);
            BufferedReader stdInput = new BufferedReader(new 
                 InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new 
                 InputStreamReader(p.getErrorStream()));
            //String line = null;
            log = new Log(stdInput, stdError);
            
            //while ((line = stdInput.readLine()) != null) System.out.println(line);
            //while ((line = stdError.readLine()) != null) System.out.println(line);
            
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
