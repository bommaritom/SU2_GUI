package io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class SurfaceFlow {
	
	public ArrayList<String> Global_Index      		= new ArrayList<String>();
	public ArrayList<String> x_coord 				= new ArrayList<String>();
	public ArrayList<String> y_coord 				= new ArrayList<String>();
	public ArrayList<String> Pressure 				= new ArrayList<String>();
	public ArrayList<String> Pressure_Coefficient	= new ArrayList<String>();
	public ArrayList<String> Mach_Number 			= new ArrayList<String>();
	
	public SurfaceFlow(String csvFileName){
		
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
				Mach_Number				.add(point[5]);
			}
			
			Global_Index			.remove(0);
			x_coord					.remove(0);
			y_coord					.remove(0);
			Pressure				.remove(0);
			Pressure_Coefficient	.remove(0);
			Mach_Number				.remove(0);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
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

}
