package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import Bean.Plane;
import ui.UIUpdate;
import util.PythonIOException;


public class ADSBDataSolver {
	private final LinkedList<String> linkedList = new LinkedList<String>();
	private  boolean isStop = false;
	private static Socket socket;
	private static BufferedWriter writer;
	private static BufferedReader reader;

	public  boolean connect() throws PythonIOException {
		boolean isConnected = false;
		if (socket == null || !socket.isConnected()) {
			try {
				socket = new Socket("localhost", 20000);
				OutputStream os = socket.getOutputStream();
				InputStream is = socket.getInputStream();
				writer = new BufferedWriter(new OutputStreamWriter(os));
				reader = new BufferedReader(new InputStreamReader(is));
				isConnected = true;
//				((Object) log).debug("Connect to server "+socket.getInetAddress().getHostAddress()+":"+socket.getPort());
			} catch (final IOException e) {
				throw new PythonIOException("An error occured when connecting to python server.", e);
			}
		}
		return isConnected;
	}
	

	
	public static void close() throws IOException {
		if (socket != null && !socket.isClosed()) {
			socket.close();
		}
	}

	

	/**
	 * ���ļ��߳�
	 * @author ysmilec
	 *
	 */
	public class ReadThread extends Thread {
		@Override
	
		public void run() {
			super.run();
			try {
				if (connect()) {
					System.out.println("�ͻ������ӳɹ�...");
					Thread.sleep(2000);
					try {	
						writer.write("start");
						writer.flush();
						String dataString = reader.readLine();
						System.out.println(dataString);
						if (dataString.equals("OK")) {
							String Line = null;
							Thread.sleep(10000);
							writer.write("read");
							writer.flush();
							System.out.println("�򿪴��ڳɹ���");
							while ((Line = reader.readLine())!=null) {
								System.out.println("�ӷ���˽��յ�����Ϊ��"+Line);	
								String hexString = Line.substring(1,29);		
								synchronized (linkedList) {
								linkedList.addFirst(hexString);
								linkedList.notifyAll();
								}			
							}
							synchronized (linkedList) {
								isStop = true;
								linkedList.notifyAll();
								close();
							}
						}else {
							System.out.println("�򿪴���ʧ�ܣ�");
						}
					
					} catch (Exception e) {
					throw new PythonIOException("An error occured when sending command to python server.", e);
				}
				}
			} catch (PythonIOException | InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}
	/**
	 * �����߳�
	 * @author ysmilec
	 *
	 */
	
	public class AnalyseThread extends Thread{
		private final UIUpdate uiUpdate;	
		String resultString;
		public AnalyseThread(final UIUpdate uiUpdate) {
			this.uiUpdate = uiUpdate;
		}
		@Override
		public void run() {
			System.err.println("Start Analysing");
			try {
				final File file = new File("ADS-B-Data-Output.txt");
				final BufferedWriter out = new BufferedWriter(new FileWriter(file));  
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
								} catch (final Exception e) {
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
		private String analyse(final String perline) {
			final Plane plane = new Plane();
			String result = "";
			final String binaryString = DataSolver.bytes2BinaryStr(perline);			
			if(DataSolver.is_ADS_B(binaryString)) {					
				final String AAString = DataSolver.getAA(perline);
				final String MEString = DataSolver.getME(binaryString);
				System.out.println(binaryString+"  AA�������Ϊ��"+AAString+" ME������Ϊ��"+MEString);
				result+="���������ݣ�"+binaryString+"  AA�������Ϊ��"+AAString+" ME������Ϊ��"+MEString;
				plane.setId(DataSolver.getAA(perline));
//				System.out.println("ME���ͣ�"+DataSolver.getMEType(MEString));
				switch (DataSolver.getMEType(MEString)) {
				case 1:
					uiUpdate.updateFlightNumbr(perline,plane);
					final FlightNumber flightNumber = new FlightNumber();
					System.out.println("����ţ�"+flightNumber.getPlaneNumber(MEString));
					result+=" ����ţ�"+flightNumber.getPlaneNumber(MEString);
					break;
				case 2:
					uiUpdate.updateGroundPosition(perline,plane);
					final GroundPosition groundPosition = new GroundPosition();
					System.out.println("�����ٶȣ�"+groundPosition.getGroundSpeed(MEString));
					System.out.println("����Ϊ��"+groundPosition.getCourse(MEString));						
					System.out.println("γ��Ϊ��"+groundPosition.getLatitude(MEString));
					System.out.println("����Ϊ��"+groundPosition.getLongitude(MEString));
					result+=" �����ٶȣ�"+groundPosition.getGroundSpeed(MEString)+" ����Ϊ��"+groundPosition.getCourse(MEString)+" γ��Ϊ��"+groundPosition.getLatitude(MEString)+"����Ϊ��"+groundPosition.getLongitude(MEString);
					break;
				case 3:
					uiUpdate.updateAirPosition(perline,plane);
					final AirPosition airPosition = new AirPosition();
					System.out.println("�߶�Ϊ��"+airPosition.getHeight(MEString));
					System.out.println("γ��Ϊ:"+airPosition.getLatitude(MEString));
					System.out.println("����Ϊ��"+airPosition.getLongitude(MEString));
					result+=" �߶�Ϊ��"+airPosition.getHeight(MEString)+" γ��Ϊ:"+airPosition.getLatitude(MEString)+" ����Ϊ��"+airPosition.getLongitude(MEString);
					break;
				case 4:
					uiUpdate.updateAirSpeed(perline,plane);
					final AirSpeed airSpeed = new AirSpeed();
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
