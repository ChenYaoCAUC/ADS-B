package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.Scanner;

import javax.swing.JOptionPane;

import Bean.Plane;
import ui.UIUpdate;


public class ADSBDataSolver {
	private LinkedList<String> linkedList = new LinkedList<String>();
	private boolean isStop = false;
	
	/**
	 * 读文件线程
	 * @author ysmilec
	 *
	 */
	public class ReadThread extends Thread {
		File file = new File("ADS-B-Data.txt");
		@Override
		public void run() {
			super.run();
			try(Scanner scanner = new Scanner(file)) {
				while(scanner.hasNextLine()) {
					String hexString = scanner.nextLine().substring(1,29);		
					synchronized (linkedList) {
						linkedList.addFirst(hexString);
						linkedList.notifyAll();
						}	
//					Thread.sleep(8000);
					}
				synchronized (linkedList) {
					isStop = true;
					linkedList.notifyAll();
					scanner.close();
					}
				}catch (Exception e) {
					e.printStackTrace();
			}
		}
	}
	/**
	 * 分析线程
	 * @author ysmilec
	 *
	 */
	
	public class AnalyseThread extends Thread{
		private UIUpdate uiUpdate;	
		String resultString;
		public AnalyseThread(UIUpdate uiUpdate) {
			this.uiUpdate = uiUpdate;
		}
		@Override
		public void run() {
			System.err.println("Start Analysing");
			
			try {
				File file = new File("ADS-B-Data-Output.txt");
				BufferedWriter out = new BufferedWriter(new FileWriter(file));  
				super.run();
					while(true) {
						String perline = null;
						synchronized (linkedList) {
							while(linkedList.isEmpty()) {
		//						读取完毕，链表为空则分析完成
								if(isStop) {
									System.err.println("Analyse finished");
									JOptionPane.showMessageDialog(null, "消息读取完毕！");
									out.close();
									return;
								}
								try {
		//							等待读取线程继续读取
									linkedList.wait();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							perline=linkedList.removeLast();	
						}
		//				分析数据	
						
							resultString = analyse(perline);
							Thread.sleep(2000);				
							out.write(resultString);
							out.flush();
					} 
				}catch (Exception e) {  
		            e.printStackTrace();  
		        } 
			

		}
		/**
		 * 分析一行数据的方法
		 * @param perline 一行数据
		 */
		private String analyse(String perline) {
			Plane plane = new Plane();
			String result = "";
			String binaryString = DataSolver.bytes2BinaryStr(perline);			
			if(DataSolver.is_ADS_B(binaryString)) {					
				String AAString = DataSolver.getAA(perline);
				String MEString = DataSolver.getME(binaryString);
				System.out.println(binaryString+"  AA域的数据为："+AAString+" ME的数据为："+MEString);
				result+="二进制数据："+binaryString+"  AA域的数据为："+AAString+" ME的数据为："+MEString;
				plane.setId(DataSolver.getAA(perline));
//				System.out.println("ME类型："+DataSolver.getMEType(MEString));
				switch (DataSolver.getMEType(MEString)) {
				case 1:
					uiUpdate.updateFlightNumbr(perline,plane);
					FlightNumber flightNumber = new FlightNumber();
					System.out.println("航班号："+flightNumber.getPlaneNumber(MEString));
					result+=" 航班号："+flightNumber.getPlaneNumber(MEString);
					break;
				case 2:
					uiUpdate.updateGroundPosition(perline,plane);
					GroundPosition groundPosition = new GroundPosition();
					System.out.println("地面速度："+groundPosition.getGroundSpeed(MEString));
					System.out.println("航向为："+groundPosition.getCourse(MEString));						
					System.out.println("纬度为："+groundPosition.getLatitude(MEString));
					System.out.println("经度为："+groundPosition.getLongitude(MEString));
					result+=" 地面速度："+groundPosition.getGroundSpeed(MEString)+" 航向为："+groundPosition.getCourse(MEString)+" 纬度为："+groundPosition.getLatitude(MEString)+"经度为："+groundPosition.getLongitude(MEString);
					break;
				case 3:
					uiUpdate.updateAirPosition(perline,plane);
					AirPosition airPosition = new AirPosition();
					System.out.println("高度为："+airPosition.getHeight(MEString));
					System.out.println("纬度为:"+airPosition.getLatitude(MEString));
					System.out.println("经度为："+airPosition.getLongitude(MEString));
					result+=" 高度为："+airPosition.getHeight(MEString)+" 纬度为:"+airPosition.getLatitude(MEString)+" 经度为："+airPosition.getLongitude(MEString);
					break;
				case 4:
					uiUpdate.updateAirSpeed(perline,plane);
					AirSpeed airSpeed = new AirSpeed();
					System.out.println("水平速度为："+airSpeed.getAirSpeedHorizontal(MEString).get("水平速度"));
					System.out.println("水平方向为："+airSpeed.getAirSpeedHorizontal(MEString).get("水平方向"));
					System.out.println("垂直速度为："+airSpeed.getAirSpeedVertical(MEString));
					result+=" 水平速度为："+airSpeed.getAirSpeedHorizontal(MEString).get("水平速度")+" 水平方向为："+airSpeed.getAirSpeedHorizontal(MEString).get("水平方向")+" 垂直速度为："+airSpeed.getAirSpeedVertical(MEString);
					break;			
				default:
					break;
				}
			}
			result +="\r\n";
			return result;			
		}
	}	
}
