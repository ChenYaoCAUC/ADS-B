package controller;

import java.text.DecimalFormat;
import java.util.HashMap;

public class AirSpeed {
	public HashMap<String, String> getAirSpeedHorizontal(String string) {
//		15~24 东西方向速度
//		26~35 南北方向速度
		HashMap<String, String> map = new HashMap<>();
		int Vew = Integer.parseInt(string.substring(14,24),2)-1;
		int Vns = Integer.parseInt(string.substring(25,35),2)-1;
		double V = Math.sqrt(Math.pow(Vew, 2)+Math.pow(Vns, 2));
//		System.out.println("Vew Vns:"+Vew+" "+Vns);
		double H = Math.atan((double)Vew/Vns)*180/Math.PI;
//		System.out.println("H:"+H);
	    DecimalFormat df=new DecimalFormat("0.00");//设置保留位数
	    String v = df.format(V);
	    String h = df.format(H);
		map.put("水平速度", v);
		map.put("水平方向", h);
		return map;
	}
	public String getAirSpeedVertical(String string){
//		38~46 垂直速度  37为俯仰标志位
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
