package utils;

public class OSScanner {

	private static String os = System.getProperty("os.name").toLowerCase();
	
	public static String detectOs(){
		if (isWindows()) return "Windows";
		else if (isMac()) return "Mac";
		else return null;
	}
	public static boolean isWindows(){
		return (os.indexOf("win") >= 0);
	}
	
	public static boolean isMac(){
		return (os.indexOf("mac") >= 0);
	}
	
}
