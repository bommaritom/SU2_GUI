package gui;
 

import javax.swing.*;

import io.SU2Input;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class MainView extends JFrame{
    
    

	/**
	 * 
	 */
	private static final long serialVersionUID = 1697756983663793966L;

	public JPanel p0;
    
	public JComboBox<String> problemBox;
    public ConfigInput configInput;
    public Tabs tabs;
    public MeshInput meshInput;
    
    public JButton d;
    public JButton s;
    public JButton b;
    
    
    public Map<String, String> params;
    
    public GridBagConstraints c;
    
    public class ProblemActionListener implements ActionListener{
    	@SuppressWarnings("unchecked")
		@Override
    	public void actionPerformed(ActionEvent e){
    		JComboBox<String> cb = (JComboBox<String>)e.getSource();
    		String choice = (String)cb.getSelectedItem();
    		updateParams(choice);
    	}
    }
    
    public void updateParams(String choice){
    	if (choice.equals("EULER")){
    		params = eulerParams();
    	}else if (choice.equals("NAVIER_STOKES")){
    		params = navierParams();
    	}
    	configInput.updateParams(params);
    }
    
    public MainView(){
    	
    	//this.setBackground(Color.decode("#1aa3ff"));
    	params = eulerParams();
    	//System.out.println(params);
        
        setTitle("SU2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setResizable(true);
        //setResizable(false);
        
        
        p0 = new JPanel(new GridBagLayout());
        p0.setBackground(Color.decode("#ccebff"));
        
        String[] problemTypes = {"EULER", "NAVIER_STOKES"};
        problemBox = new JComboBox<>(problemTypes);
        problemBox.addActionListener(new ProblemActionListener());
        JPanel boxPanel = new JPanel();
        boxPanel.add(new JLabel("Problem Type:"));
        boxPanel.add(problemBox);
        boxPanel.setBackground(Color.decode("#ccebff"));
        
        configInput = new ConfigInput(params);
        meshInput = new MeshInput();
        tabs = new Tabs(meshInput);
        
        GridBagConstraints c = new GridBagConstraints();
        
        
        //Insets(top, left, bottom, right)
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(5,5,5,3);
        p0.add(boxPanel, c);
        
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(5,5,5,5);
        p0.add(configInput, c);
        
        c.gridheight = 5;
        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(5,2,5,5);
        p0.add(tabs, c);
        
        
        
        add(p0);
        
        pack();
        p0.setPreferredSize(p0.getMinimumSize());
        setMinimumSize(this.getSize());
        setVisible(true);
        
        loadUser();
    }
    
    public Map<String,String> eulerParams(){
    	
    	Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("MACH_NUMBER",             "Mach Number"          	);
        params.put("AoA",                     "Angle of Attack"     	);
        params.put("SIDESLIP_ANGLE",          "Sideslip Angle"      	);
        params.put("FREESTREAM_PRESSURE",     "Free-stream press."   	);
        params.put("FREESTREAM_TEMPERATURE",  "Free-stream temp."   	);
        params.put("EXT_ITER",                "Max Iterations"       	);
        params.put("MARKER_EULER",            "Euler wall marker(s)"    );
        params.put("MARKER_FAR",              "Far-field marker(s)"     );
        params.put("MARKER_PLOTTING",         "MARKER_PLOTTING"         );
        params.put("MARKER_MONITORING",       "MARKER_MONITORING"       );
        params.put("MARKER_MOVING",           "MARKER_MOVING"           );
        return params;
        
    }
    
    public Map<String,String> navierParams(){
    	
    	Map<String, String> params = new LinkedHashMap<String, String>();
        params.put("MACH_NUMBER",             "Mach Number"       		);
        params.put("AoA",                     "Angle of Attack"   		);
        params.put("SIDESLIP_ANGLE",          "Sideslip Angle"   		);
        params.put("REYNOLDS_NUMBER",         "Reynolds Number"   		);
        params.put("FREESTREAM_TEMPERATURE",  "Free-stream temp." 		);
        params.put("EXT_ITER",                "Max Iterations"    		);
        params.put("MARKER_HEATFLUX",         "Heat flux marker(s)"     );
        params.put("MARKER_FAR",              "Far-field marker(s)"     );
        params.put("MARKER_PLOTTING",         "MARKER_PLOTTING"         );
        params.put("MARKER_MONITORING",       "MARKER_MONITORING"       );
        params.put("MARKER_MOVING",           "MARKER_MOVING"           );
        return params;
        
    }
    
    public void initButtons(ActionListener defListener, ActionListener saveListener, ActionListener runListener){
    	d = new JButton("Use Defaults");
        d.setPreferredSize(new Dimension(140, 30));
        d.addActionListener(defListener);
        
        s = new JButton("Save Data");
        s.setPreferredSize(new Dimension(140, 30));
        s.addActionListener(saveListener);
        
        b = new JButton("Run SU2!");
        b.setPreferredSize(new Dimension(140, 30));
        b.addActionListener(runListener);
        
        GridBagConstraints c = new GridBagConstraints();
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 2;
        c.anchor = GridBagConstraints.PAGE_START;
        c.insets = new Insets(0,0,0,0);
        
        p0.add(d, c);
        
        c.gridx = 0;
        c.gridy = 3;
        c.anchor = GridBagConstraints.PAGE_START;
        c.insets = new Insets(0,0,0,0);
        
        p0.add(s, c);
        
        c.gridx = 0;
        c.gridy = 4;
        c.anchor = GridBagConstraints.PAGE_START;
        c.insets = new Insets(0,0,0,0);
        
        p0.add(b, c);
        
        
        p0.repaint();
        p0.revalidate();
        
        pack();
    }
    
    public void loadUser(){
        configInput.loadUser();
        meshInput.loadUser();
    }
    
    public SU2Input getData(){
        SU2Input rawData = new SU2Input();
        rawData.p1data = configInput.getData(); /**Returns user input to p1 as an ArrayList<String>*/
        rawData.p2data = meshInput.getData(); /**Returns the mesh as a String */
        return rawData;
    }
    
}
