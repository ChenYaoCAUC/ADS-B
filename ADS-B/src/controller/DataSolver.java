package controller;
import java.util.HashMap;


public abstract class DataSolver {
	/*
	 * public Plane getReadableLineString(String perline) { Plane air = new Plane();
	 * 
	 * return air; }
	 */
	
//	��ʮ����������תΪ������
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
//		ǰ�ĸ��ַ���null,��Ҳ��֪��Ϊɶ��ֻ�ܽ�ȡһ��
        return binaryString.substring(4);  
    }  
	
//	�ж�DFֵ  DF=17 ��ʾSģʽӦ�����ADS-B ��ֻȡDF=17��ֵ
	public static boolean is_ADS_B(String string) {
		String DFString = string.substring(0,5);
		if(DFString.equals("10001"))
			return true;
		else 
			return false;
		
	}
	
//	��ȡAA���ֵ����9~32�����Ƶ���ֵתΪʮ�����ƣ���ȡ����õ�������ȡ3~5��ֵ
	public static String getAA(String string) {
		return string.substring(2, 8);
	}
	
//	��ȡME����Ϣ��33~88
	public static String getME(String string) {
		return string.substring(32, 88);	
	}
	
//	�ж�ME��Ϣ����
	public static int getMEType(String string) {
		String MEtypeString = string.substring(0, 5);
//		��������תΪʮ����
		int res = Integer.parseInt(MEtypeString,2);
//		0~4Ϊ�ɻ������Ϣ���룬5~8Ϊ����λ�ñ��ģ�9~18Ϊ����λ�ñ��ģ�19Ϊ�����ٶȱ�����Ϣ����,����19�������κβ���
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
	

	
