package controller;

import java.text.DecimalFormat;
import java.util.HashMap;

public class AirSpeed {
	public HashMap<String, String> getAirSpeedHorizontal(String string) {
//		15~24 ���������ٶ�
//		26~35 �ϱ������ٶ�
		HashMap<String, String> map = new HashMap<>();
		int Vew = Integer.parseInt(string.substring(14,24),2)-1;
		int Vns = Integer.parseInt(string.substring(25,35),2)-1;
		double V = Math.sqrt(Math.pow(Vew, 2)+Math.pow(Vns, 2));
//		System.out.println("Vew Vns:"+Vew+" "+Vns);
		double H = Math.atan((double)Vew/Vns)*180/Math.PI;
//		System.out.println("H:"+H);
	    DecimalFormat df=new DecimalFormat("0.00");//���ñ���λ��
	    String v = df.format(V);
	    String h = df.format(H);
		map.put("ˮƽ�ٶ�", v);
		map.put("ˮƽ����", h);
		return map;
	}
	public String getAirSpeedVertical(String string){
//		38~46 ��ֱ�ٶ�  37Ϊ������־λ
		int v_r = Integer.parseInt(string.substring(37,46),2);
		String sign = string.substring(36,37);
		int Vr_sign;
		if(sign.equals("0"))
			Vr_sign=1;
		else 
			Vr_sign=-1;
		double V = Vr_sign*(v_r -1)*64;
		return String.valueOf(V);
		
	}

	
}
