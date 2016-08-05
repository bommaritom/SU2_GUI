package gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import io.History;

public class Stats extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4900395608912664519L;
	private JPanel p;
	private JTextArea m;
	
	private String Mach;
	private String AoA;
	private String CLift;
	private String CDrag;
	private String CMz;
	
	public Stats(){
		p = new JPanel();
		
		m = new JTextArea(20, 60);
        m.setLineWrap(true);
        m.setWrapStyleWord(true);
        m.setEditable(false);
        
        m.setText("Mach Number|Angle of Attack|       CLift       |      CDrag      |      Cmz\n");
        
        GridBagConstraints c = new GridBagConstraints();
        
        JScrollPane s = new JScrollPane(m);
        s.setBackground(Color.decode("#ccebff"));
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        
        p.add(s, c);
        add(p);
		pack();
		setResizable(false);
		setAlwaysOnTop(true);
		setLocationByPlatform(true);
		setVisible(true);
		
	};
	
	public void updateStats(){
		fetchData();
		cleanUpData();
		m.append(Mach + " | " + AoA + " | " + CLift + " | " + CDrag + " | " + CMz + "\n");
		setVisible(true); //just in case
	}
	
	private void fetchData(){
		History history;
		try{
			history = new History("su2/history.dat");
		}catch (FileNotFoundException e){
			e.printStackTrace();
			return;
		}
		CLift = history.CLift.get(history.CLift.size()-1);
		CDrag = history.CDrag.get(history.CDrag.size()-1);
		CMz = history.CMz.get(history.CMz.size()-1);
		
		
		BufferedReader br = null;
        String fileName = "su2/user_conf.cfg";
        try{
            br = new BufferedReader(new FileReader(fileName));
            
            String line;
            
            while ( (line = br.readLine()) != null) {
                
                if (line.contains("MACH_NUMBER=")){
                    Mach = line.substring( (2 + line.indexOf("=")) );
                }
                if (line.contains("AoA=")){
                	AoA = line.substring( (2 + line.indexOf("=")) );
                }
            }
        }catch(IOException e){
            return;
        }finally{
            try {
                if( br != null){
                    br.close();
                }      
            } catch (IOException e){
                e.printStackTrace();
            }
        }
	}
	
	private void cleanUpData(){
		//length of number (10) + two spaces (2) = 12 characters
        if (Mach.length() > 10) Mach = Mach.substring(0, 10);
        if (Mach.length() < 10){
        	if (!Mach.contains(".")) Mach += ".";
        	for (int i = Mach.length(); i < 10; i++){
        		Mach += "0";
        	}
        }
        if (AoA.length() > 10) AoA = AoA.substring(0, 10);
        if (AoA.length() < 10){
        	if (!AoA.contains(".")) AoA += ".";
        	for (int i = AoA.length(); i < 10; i++){
        		AoA += "0";
        	}
        }
        if (CLift.length() > 10) CLift = CLift.substring(0, 10);
        if (CDrag.length() > 10) CDrag = CDrag.substring(0, 10);
        if (CMz  .length() > 10) CMz   = CMz  .substring(0, 10);
	}
	
}
