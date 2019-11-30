package controller;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class FlightNumber {
	protected static Properties flightNumber = new Properties();
	public FlightNumber() {
		try {
			flightNumber.load(new FileReader("FlightNumber.txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
//	获取航班号
	public String getPlaneNumber(String string) {
//		9~56共48位表示航班号信息
		String MENumber = string.substring(8);
		String number = "";
		int j=0;
		for(int i=0;i<8;i++) {
			String indexString = MENumber.substring(j, j+6); 
			String res = String.valueOf(Integer.parseInt(indexString,2));
			String mString = flightNumber.getProperty(res);
			if(mString!=null)
				number+=mString;		
			j+=6;
		}
		return number;
	}
}
