package io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;



public class SurfaceFlow{
	
	private String csvFileName;
	
	public ArrayList<String> Global_Index      		= new ArrayList<String>();
	public ArrayList<String> x_coord 				= new ArrayList<String>();
	public ArrayList<String> y_coord 				= new ArrayList<String>();
	public ArrayList<String> Pressure 				= new ArrayList<String>();
	public ArrayList<String> Pressure_Coefficient	= new ArrayList<String>();
	
	public SurfaceFlow(String csvFileName) throws FileNotFoundException{
		
		this.csvFileName = csvFileName;
		
		BufferedReader br = null;
		String line;
		
		try{
			br = new BufferedReader(new FileReader(csvFileName));
			while ((line = br.readLine()) != null){
				
				String[] point = line.split(", ");
				Global_Index			.add(point[0]);
				x_coord     			.add(point[1]);
				y_coord     			.add(point[2]);
				Pressure    			.add(point[3]);
				Pressure_Coefficient	.add(point[4]);
			}
			
			Global_Index			.remove(0);
			x_coord					.remove(0);
			y_coord					.remove(0);
			Pressure				.remove(0);
			Pressure_Coefficient	.remove(0);
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public void sfError(){
		JFrame frame = new JFrame("File Not Found");
		JOptionPane.showMessageDialog(frame,
				"Could not find " + csvFileName + "!\n"
				+ "SU2 may not have executed properly.",
				"Could not find " + csvFileName,
				JOptionPane.ERROR_MESSAGE);
	}

}
