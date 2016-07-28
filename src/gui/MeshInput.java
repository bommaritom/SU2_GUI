package gui;
 
import javax.swing.*;

import io.Mesh;

import java.awt.*;

public class MeshInput extends JPanel{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 2702996304976510859L;
	
	private JTextArea m;
    
    public MeshInput(){
        
        m = new JTextArea(20, 50);
        m.setLineWrap(true);
        m.setWrapStyleWord(true);
        
        
        GridBagConstraints c = new GridBagConstraints();
        
        JScrollPane s = new JScrollPane(m);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        
        add(s, c);
    }
        
    public void loadUser(){
        m.setText(Mesh.load());
    }
    
    public String getData(){
        return m.getText();
    }
    
}
