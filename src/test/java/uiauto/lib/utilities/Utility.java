package uiauto.lib.utilities;

import java.io.File;
import java.net.ServerSocket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {
	
	public static String join(String delimiter, String... inArray){
		StringBuilder sb = new StringBuilder();
		for (int i=0; i < inArray.length; i++){
			sb.append(inArray[i]);
			if (i < inArray.length - 1){
				sb.append(delimiter);
			}
		}
		return sb.toString();
	}
	
	public static String joinPaths(String... inArray){
		return join(File.separator, inArray);
	}
	
	public static String getDateTimeString(String format){
		DateFormat dateFormat = new SimpleDateFormat(format);
		Date d = new Date();
		return dateFormat.format(d);	
	}
	
	public static String getJsonFilePathFromMapDir(String packageName) {
		String[] split = packageName.split("\\.");
		String value = "";
		if(split.length > 2) {
			value = split[2];
		}
		
		for(int i =3; i<split.length; i++) {
			value = Utility.joinPaths(value, split[i]);
		}
		return value;
	}
	
	@SuppressWarnings("resource")
	public static int getUnusedPort() throws Exception {
 		ServerSocket socket = new ServerSocket(0);
 		return socket.getLocalPort();
 	}
}
