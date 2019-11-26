package controller;
import java.util.HashMap;


public abstract class DataSolver {
	/*
	 * public Plane getReadableLineString(String perline) { Plane air = new Plane();
	 * 
	 * return air; }
	 */
	
//	将十六进制数据转为二进制
	public static String bytes2BinaryStr(String string) {
		 HashMap<String, String> map = new HashMap<String, String>() {
			    {
			        put("0", "0000");  
			        put("1", "0001"); 
			        put("2", "0010");
			        put("3", "0011");
			        put("4", "0100");
			        put("5", "0101");
			        put("6", "0110");
			        put("7", "0111");
			        put("8", "1000");
			        put("9", "1001");
			        put("A", "1010");
			        put("B", "1011");
			        put("C", "1100");
			        put("D", "1101");
			        put("E", "1110");
			        put("F", "1111");
			    }
			};
		String hexString = string;
		int length = hexString.length();
		String binaryString = null;
		for(int i=0;i<length;i++){
			char hex = hexString.charAt(i);
			String s = String.valueOf(hex);
			binaryString+=map.get(s);
		}
//		前四个字符是null,我也不知道为啥，只能截取一下
        return binaryString.substring(4);  
    }  
	
//	判断DF值  DF=17 表示S模式应答机的ADS-B 故只取DF=17的值
	public static boolean is_ADS_B(String string) {
		String DFString = string.substring(0,5);
		if(DFString.equals("10001"))
			return true;
		else 
			return false;
		
	}
	
//	获取AA域的值，将9~32二进制的数值转为十六进制，即取最初得到的数据取3~5的值
	public static String getAA(String string) {
		return string.substring(2, 8);
	}
	
//	获取ME（消息域）33~88
	public static String getME(String string) {
		return string.substring(32, 88);	
	}
	
//	判断ME消息类型
	public static int getMEType(String string) {
		String MEtypeString = string.substring(0, 5);
//		将二进制转为十进制
		int res = Integer.parseInt(MEtypeString,2);
//		0~4为飞机身份信息解码，5~8为地面位置报文，9~18为空中位置报文，19为空中速度报文信息解码,大于19不进行任何操作
		if(res>=0&&res<=4)
			return 1;
		else if(res>=5&&res<=8)
			return 2;
		else if(res>=9&&res<=18)
			return 3;
		else if (res==19)
			return 4;
		else
			return 5;
	}
	
}
	

	
