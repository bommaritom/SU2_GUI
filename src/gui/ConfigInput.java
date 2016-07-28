package gui;
 

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;

public class ConfigInput extends JPanel{
    

	/**
	 * 
	 */
	private static final long serialVersionUID = -1777708078645686766L;

	private Map<String, String> params;
    
    private ArrayList<JTextField> fields;
    private ArrayList<JLabel> labels;
    
    public void updateParams(Map<String,String> params){
    	removeAll();
    	
    	this.params = params;
    	createFields();
    	loadUser();
    	repaint();
    	revalidate();
    }
    
    public ConfigInput(Map<String, String> params){
    	setSize(400,400);
    	setBackground(Color.decode("#ccebff"));
    	//setBackground(Color.decode("#b3e0ff"));
        updateParams(params);
        
    }
        
    public void createFields(){
    	
        /**Creates JLabels based on the method arguments in params.  This
         * does not change the values in params. */
        labels = new ArrayList<JLabel>();
        for ( Map.Entry<String, String> entry: params.entrySet() ) {
            JLabel temp = new JLabel();
            temp.setText(entry.getValue());
            
            labels.add(temp);
        }
        
        /**We need an array of blank text fields so that GroupLayout has
         * a different component for each iteration of its code.**/
        fields = new ArrayList<JTextField>();
        for ( @SuppressWarnings("unused") Map.Entry<String, String> entry: params.entrySet() ) {
            JTextField temp = new JTextField();
            temp.setColumns(10);
            fields.add(temp);
        }
        /**------------------------------------**/
        
        
        
                    JPanel j = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    //j.setBackground(Color.decode("#99d6ff"));
                    
                    GroupLayout inputLayout = new GroupLayout(j);
                    inputLayout.setAutoCreateGaps(true);
                    inputLayout.setAutoCreateContainerGaps(true);
                    j.setLayout(inputLayout);
                
                
                    /**We use GroupLayout to arrange the desired inputs*/
                    
                    /**This code implements a for
                       loop to adjust for any amount of parameters.*/
                    
                    /**First, create the horizontal group.  This is basically two columns
                       of JComponents.  The first column has the labels and the second column has the text input
                       boxes.*/
                    GroupLayout.SequentialGroup hGroup = inputLayout.createSequentialGroup();
                    GroupLayout.ParallelGroup tempGroup = inputLayout
                            .createParallelGroup(GroupLayout.Alignment.LEADING);
                    for ( int i = 0; i < labels.size(); i++ ) {
                        tempGroup.addComponent(labels.get(i));
                    }
                    hGroup.addGroup(tempGroup);
                    
                    tempGroup = inputLayout
                            .createParallelGroup(GroupLayout.Alignment.LEADING);
                    for ( int i = 0; i < fields.size(); i++ ) {
                        tempGroup.addComponent(fields.get(i));
                    }
                    hGroup.addGroup(tempGroup);
                    /**/inputLayout.setHorizontalGroup(hGroup);
                    
                    /**Now, the code creates the vertical group.  This is a series of rows
                       which each contain a label and its respective text input box.*/
                    GroupLayout.SequentialGroup vGroup = inputLayout.createSequentialGroup();
                    for ( int i = 0; i < labels.size(); i++ ) {
                        tempGroup = inputLayout
                                .createParallelGroup(GroupLayout.Alignment.BASELINE);
                        tempGroup
                                .addComponent(labels.get(i))
                                .addComponent(fields.get(i));
                        vGroup.addGroup(tempGroup);
                    } 
                    /**/inputLayout.setVerticalGroup(vGroup);
        
                        
                     
        JScrollPane s = new JScrollPane(j);
        s.setPreferredSize(new Dimension(320, 200));
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(s, c);
    }
    
    public void loadUser(){
        
        //System.out.println("Searching for user data...");
        File file = new File("su2/user_conf.cfg");
        if ( !file.exists() || file.isDirectory() ){
            this.loadDefaults();
            return;
        }
        //System.out.println("User data found!");
        
        
        /**Read config_user.cfg and identify variable values; put them
         * in the text boxes.
         */
        String fileName = "su2/user_conf.cfg";
        
        BufferedReader br = null;
        
        try{
            br = new BufferedReader(new FileReader(fileName));
            
            String line;
            
            while ( (line = br.readLine()) != null) {
                int i = 0;
                outerloop:
                for ( Map.Entry<String, String> entry: params.entrySet() ) {
                    if (line.contains(entry.getKey() + "=")){
                        String recent = line.substring( (2 + line.indexOf("=")) );
                        fields.get(i).setText(recent);
                        i++;
                        continue outerloop;
                    }
                    i++;
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
                //
            }
        }
    }
    
    public void loadDefaults(){
        
        /**Same as loadConfigs, but using the unchanging file.*/
        String fileName = "su2/default_conf.cfg";
        BufferedReader br = null;
        
        try{
            br = new BufferedReader(new FileReader(fileName));
            
            String line;
            
            while ( (line = br.readLine()) != null) {
                int i = 0;
                outerloop:
                for ( Map.Entry<String, String> entry: params.entrySet() ) {
                    if (line.contains(entry.getKey() + "=")){
                        String recent = line.substring( (2 + line.indexOf("=")) );
                        fields.get(i).setText(recent);
                        i++;
                        continue outerloop;
                    }
                    i++;
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
                //
            }
        }
    }
    
    public ArrayList<String> getData(){
    ArrayList<String> p1data = new ArrayList<String>();
        for ( int i = 0; i < fields.size(); i++ ) {
            String temp = fields.get(i).getText();
            p1data.add(temp);
        }
        return p1data;
    }
}
