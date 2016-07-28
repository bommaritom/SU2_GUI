package gui;
 

import javax.swing.*;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;

public class Log extends JPanel{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -2183987749225839245L;
	private JTextArea t;
     
    public Log(BufferedReader stdInput, BufferedReader stdError){
    	
        t = new JTextArea(20,40);
        t.setEditable(false);
        JScrollPane s = new JScrollPane(t);
        
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        
        add(s, c);
        
        String line = null;
        
        try{
	        while ((line = stdInput.readLine()) != null) {
	        	this.addLine(line);
	        }
	        
	        while((line = stdError.readLine()) != null) {
	        	this.addLine(line);
	        }
	        
        } catch(IOException e) {
        	e.printStackTrace();
        	
        } finally {
        	try{
                if ( stdInput != null ){
                     stdInput.close();
                }
                if ( stdError != null ){
                	 stdError.close();
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        setVisible(true);
    }
    
    
    public void addLine(String line){
        t.append(line + "\n");
        t.setCaretPosition(t.getDocument().getLength());
    }
    
    
}
