package io;
 
import java.io.*;

public class Mesh{
	
    public static void write(String data){
    
        String fileName = "su2/user_mesh.su2";

        BufferedWriter bw = null;
        
        try{
            bw = new BufferedWriter(new FileWriter(fileName));
            bw.write(data);
        }catch (Exception e){
            System.out.println("Something went wrong while writing the mesh file.");
        }finally{
            try{
                if ( bw != null ){
                    bw.close();
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static String load(){
        
        File meshFile = new File("su2/user_mesh.su2"); /**As referred to in default_conf.cfg*/
        if ( !meshFile.exists() || meshFile.isDirectory() ) return null;
        
        StringBuilder mesh = new StringBuilder();
        String meshFileName = "su2/user_mesh.su2";
        
        BufferedReader br = null;
        
        try{
            br = new BufferedReader(new FileReader(meshFileName));
            String line;
            while ( (line = br.readLine() ) != null) {
                mesh.append(line + System.getProperty("line.separator"));
            }
        }catch (Exception e){
            System.out.println();
            e.printStackTrace();
        }finally{
            try {
                if ( br != null){
                    br.close();
                }
            } catch (IOException e) {
                //
            }
        }
        
        String m = mesh.toString();
        return m;
        
    }
    
}
