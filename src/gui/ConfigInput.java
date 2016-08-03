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
    	//completely redoes the fields with the new inputs
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
                    
                    GroupLayout inputLayout = new GroupLayout(j);
                    inputLayout.setAutoCreateGaps(true);
                    inputLayout.setAutoCreateContainerGaps(true);
                    j.setLayout(inputLayout);
                
                
                    /**Use GroupLayout to arrange the desired inputs*/
                    
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
                    
                    /**Now, the code creates the vertical group – a series of rows
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
    
    /**
     * This is a more complex method; ConfigInput needs to
     * fetch data from the necessary .cfg file and display
     * it in the text fields. It is much simpler for ConfigInput
     * to handle it internally instead of calling some function
     * in MainController and relying on a returned array.
     */
    
    public void loadUser(){
    	loadCFG("su2/user_conf.cfg");
    }
    
    public void loadDefaults(){
    	loadCFG("su2/default_conf.cfg");
    }
    
    private void loadCFG(String fileName){
        
        File file = new File(fileName);
        if ( !file.exists() || file.isDirectory() ){
            fileName = "su2/default_conf.cfg";
        }
        
        
        /**Read config_user.cfg and identify variable values; put them
         * in the text boxes.
         */
        
        
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
    
   