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
	 * ���ļ��߳�
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
	 * �����߳�
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
		//						��ȡ��ϣ�����Ϊ����������
								if(isStop) {
									System.err.println("Analyse finished");
									JOptionPane.showMessageDialog(null, "��Ϣ��ȡ��ϣ�");
									out.close();
									return;
								}
								try {
		//							�ȴ���ȡ�̼߳�����ȡ
									linkedList.wait();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							perline=linkedList.removeLast();	
						}
		//				��������	
						
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
		 * ����һ�����ݵķ���
		 * @param perline һ������
		 */
		private String analyse(String perline) {
			Plane plane = new Plane();
			String result = "";
			String binaryString = DataSolver.bytes2BinaryStr(perline);			
			if(DataSolver.is_ADS_B(binaryString)) {					
				String AAString = DataSolver.getAA(perline);
				String MEString = DataSolver.getME(binaryString);
				System.out.println(binaryString+"  AA�������Ϊ��"+AAString+" ME������Ϊ��"+MEString);
				result+="���������ݣ�"+binaryString+"  AA�������Ϊ��"+AAString+" ME������Ϊ��"+MEString;
				plane.setId(DataSolver.getAA(perline));
//				System.out.println("ME���ͣ�"+DataSolver.getMEType(MEString));
				switch (DataSolver.getMEType(MEString)) {
				case 1:
					uiUpdate.updateFlightNumbr(perline,plane);
					FlightNumber flightNumber = new FlightNumber();
					System.out.println("����ţ�"+flightNumber.getPlaneNumber(MEString));
					result+=" ����ţ�"+flightNumber.getPlaneNumber(MEString);
					break;
				case 2:
					uiUpdate.updateGroundPosition(perline,plane);
					GroundPosition groundPosition = new GroundPosition();
					System.out.println("�����ٶȣ�"+groundPosition.getGroundSpeed(MEString));
					System.out.println("����Ϊ��"+groundPosition.getCourse(MEString));						
					System.out.println("γ��Ϊ��"+groundPosition.getLatitude(MEString));
					System.out.println("����Ϊ��"+groundPosition.getLongitude(MEString));
					result+=" �����ٶȣ�"+groundPosition.getGroundSpeed(MEString)+" ����Ϊ��"+groundPosition.getCourse(MEString)+" γ��Ϊ��"+groundPosition.getLatitude(MEString)+"����Ϊ��"+groundPosition.getLongitude(MEString);
					break;
				case 3:
					uiUpdate.updateAirPosition(perline,plane);
					AirPosition airPosition = new AirPosition();
					System.out.println("�߶�Ϊ��"+airPosition.getHeight(MEString));
					System.out.println("γ��Ϊ:"+airPosition.getLatitude(MEString));
					System.out.println("����Ϊ��"+airPosition.getLongitude(MEString));
					result+=" �߶�Ϊ��"+airPosition.getHeight(MEString)+" γ��Ϊ:"+airPosition.getLatitude(MEString)+" ����Ϊ��"+airPosition.getLongitude(MEString);
					break;
				case 4:
					uiUpdate.updateAirSpeed(perline,plane);
					AirSpeed airSpeed = new AirSpeed();
					System.out.println("ˮƽ�ٶ�Ϊ��"+airSpeed.getAirSpeedHorizontal(MEString).get("ˮƽ�ٶ�"));
					System.out.println("ˮƽ����Ϊ��"+airSpeed.getAirSpeedHorizontal(MEString).get("ˮƽ����"));
					System.out.println("��ֱ�ٶ�Ϊ��"+airSpeed.getAirSpeedVertical(MEString));
					result+=" ˮƽ�ٶ�Ϊ��"+airSpeed.getAirSpeedHorizontal(MEString).get("ˮƽ�ٶ�")+" ˮƽ����Ϊ��"+airSpeed.getAirSpeedHorizontal(MEString).get("ˮƽ����")+" ��ֱ�ٶ�Ϊ��"+airSpeed.getAirSpeedVertical(MEString);
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
