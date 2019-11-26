package controller;

import java.text.DecimalFormat;

public class AirPosition {
//	获得高度
	public String getHeight(String string) {
//		9~20 高度信息
		String index = string.substring(8, 20);
		String eight =  index.substring(7,8);
		int m;
		if(eight.equals("1")) 
			m=25;	
		else 
			m=100;
		int D = Integer.parseInt(index.substring(0,7)+index.substring(8),2);
//		System.out.println("二进制："+index.substring(0,7)+index.substring(8));
//		System.out.println("D:"+D);
		String height = String.valueOf(D*m-1000);
		return height;		
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
