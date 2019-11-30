package controller;

import java.text.DecimalFormat;

public class GroundPosition {
	
//	获得地面速度
	public String getGroundSpeed(String string) {
//		6-12表示速度
		String groundSpeedString = string.substring(5,12);
		int res = Integer.parseInt(groundSpeedString,2);
		String result = "";
		if(res==0)
			result = "无地面运动信息";
		else if(res==1)
			result = "飞机无地面速度";
		else if(res==2)
			result = "0~0.125 节";
		else if(res>=3&&res<=8) 
			result = String.valueOf(((res-3)*0.125+0.125))+"节";
		else if(res>=9&&res<=12)
			result = String.valueOf(((res-9)*0.25+1))+"节";
		else if(res>=13&&res<=38)
			result = String.valueOf(((res-13)*0.5+2))+"节";
		else if(res>=39&&res<=93)
			result = String.valueOf(((res-39)*1+15))+"节";
		else if(res>=94&&res<=108)
			result = String.valueOf(((res-94)*2+70))+"节";
		else if(res>=109&&res<=123)
			result = String.valueOf(((res-109)*5+100))+"节";
		else 
			result = "大于175节";
		return result;
	}
	
//	获得航向
	public String getCourse(String string) {
//		当第13比特值为1 时，表示航向信息可用，可以对数据进行下一步解算。14~20 比特为航向信息域，含有航向信息。
		String thirteenString = string.substring(12,13);
		String course = "无航向信息";
		if(thirteenString.equals("1")){
			int N = Integer.parseInt(string.substring(13, 20),2);
			course = String.valueOf(2.8125*N)+"度";
		}
		return course;
	}
//	获得经度
	public String getLongitude(String string) {
//		40~56 CPR编码经度
		String longitudeString = string.substring(39, 56);
		double Loncpr = (double)Integer.parseInt(longitudeString,2)/131072;
//		System.out.println("loncpr:"+Loncpr);
		int ni = 58;
		double d_lon = (double)360/ni;
//		System.out.println("d_lon:"+d_lon);
		double m,longitude;
		m =(int)(117.35002/d_lon)+(int)(0.5+(117.35002%d_lon)/ d_lon-Loncpr);
//		System.out.println("m:"+m);
		longitude = d_lon *(m+Loncpr);
		DecimalFormat df=new DecimalFormat("0.00000");//设置保留位数
		return df.format(longitude);
	}
//	获得纬度
	public String getLatitude(String string) {
//		23~39 CPR编码纬度
		String CPRType = string.substring(21, 22);
		String latitudeString = string.substring(22,39);
		double latcpr = (double)Integer.parseInt(latitudeString,2)/131072;
		double d_lat,latitude;
		int j;
		if(CPRType.equals("1"))
			d_lat = (double)360/59;
		else 
			d_lat = (double)360/60;
//		System.out.println("d_lat:"+d_lat);
	    j =(int)(39.11198/d_lat)+(int)(0.5+(39.11198%d_lat)/ d_lat-latcpr);
//	    System.out.println("j:"+ j);
	    latitude = d_lat *(j +latcpr);
//	    System.out.println("latcpr:"+latcpr+" d_lat:"+d_lat);
	    DecimalFormat df=new DecimalFormat("0.00000");//设置保留位数
		return df.format(latitude);
	}
	
	
	
}
