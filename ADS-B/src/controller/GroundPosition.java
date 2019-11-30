package controller;

import java.text.DecimalFormat;

public class GroundPosition {
	
//	��õ����ٶ�
	public String getGroundSpeed(String string) {
//		6-12��ʾ�ٶ�
		String groundSpeedString = string.substring(5,12);
		int res = Integer.parseInt(groundSpeedString,2);
		String result = "";
		if(res==0)
			result = "�޵����˶���Ϣ";
		else if(res==1)
			result = "�ɻ��޵����ٶ�";
		else if(res==2)
			result = "0~0.125 ��";
		else if(res>=3&&res<=8) 
			result = String.valueOf(((res-3)*0.125+0.125))+"��";
		else if(res>=9&&res<=12)
			result = String.valueOf(((res-9)*0.25+1))+"��";
		else if(res>=13&&res<=38)
			result = String.valueOf(((res-13)*0.5+2))+"��";
		else if(res>=39&&res<=93)
			result = String.valueOf(((res-39)*1+15))+"��";
		else if(res>=94&&res<=108)
			result = String.valueOf(((res-94)*2+70))+"��";
		else if(res>=109&&res<=123)
			result = String.valueOf(((res-109)*5+100))+"��";
		else 
			result = "����175��";
		return result;
	}
	
//	��ú���
	public String getCourse(String string) {
//		����13����ֵΪ1 ʱ����ʾ������Ϣ���ã����Զ����ݽ�����һ�����㡣14~20 ����Ϊ������Ϣ�򣬺��к�����Ϣ��
		String thirteenString = string.substring(12,13);
		String course = "�޺�����Ϣ";
		if(thirteenString.equals("1")){
			int N = Integer.parseInt(string.substring(13, 20),2);
			course = String.valueOf(2.8125*N)+"��";
		}
		return course;
	}
//	��þ���
	public String getLongitude(String string) {
//		40~56 CPR���뾭��
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
		DecimalFormat df=new DecimalFormat("0.00000");//���ñ���λ��
		return df.format(longitude);
	}
//	���γ��
	public String getLatitude(String string) {
//		23~39 CPR����γ��
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
	    DecimalFormat df=new DecimalFormat("0.00000");//���ñ���λ��
		return df.format(latitude);
	}
	
	
	
}
