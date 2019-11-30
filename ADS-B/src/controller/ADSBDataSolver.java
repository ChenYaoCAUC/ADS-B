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
	 * 读文件线程
	 * @author ysmilec
	 *
	 */
	public class ReadThread extends Thread {
		@Override
	
		public void run() {
			super.run();
			try {
				if (connect()) {
					System.out.println("客户端连接成功...");
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
							System.out.println("打开串口成功！");
							while ((Line = reader.readLine())!=null) {
								System.out.println("从服务端接收的数据为："+Line);	
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
							System.out.println("打开串口失败！");
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
	 * 分析线程
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
								} catch (final Exception e) {
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
		private String analyse(final String perline) {
			final Plane plane = new Plane();
			String result = "";
			final String binaryString = DataSolver.bytes2BinaryStr(perline);			
			if(DataSolver.is_ADS_B(binaryString)) {					
				final String AAString = DataSolver.getAA(perline);
				final String MEString = DataSolver.getME(binaryString);
				System.out.println(binaryString+"  AA域的数据为："+AAString+" ME的数据为："+MEString);
				result+="二进制数据："+binaryString+"  AA域的数据为："+AAString+" ME的数据为："+MEString;
				plane.setId(DataSolver.getAA(perline));
//				System.out.println("ME类型："+DataSolver.getMEType(MEString));
				switch (DataSolver.getMEType(MEString)) {
				case 1:
					uiUpdate.updateFlightNumbr(perline,plane);
					final FlightNumber flightNumber = new FlightNumber();
					System.out.println("航班号："+flightNumber.getPlaneNumber(MEString));
					result+=" 航班号："+flightNumber.getPlaneNumber(MEString);
					break;
				case 2:
					uiUpdate.updateGroundPosition(perline,plane);
					final GroundPosition groundPosition = new GroundPosition();
					System.out.println("地面速度："+groundPosition.getGroundSpeed(MEString));
					System.out.println("航向为："+groundPosition.getCourse(MEString));						
					System.out.println("纬度为："+groundPosition.getLatitude(MEString));
					System.out.println("经度为："+groundPosition.getLongitude(MEString));
					result+=" 地面速度："+groundPosition.getGroundSpeed(MEString)+" 航向为："+groundPosition.getCourse(MEString)+" 纬度为："+groundPosition.getLatitude(MEString)+"经度为："+groundPosition.getLongitude(MEString);
					break;
				case 3:
					uiUpdate.updateAirPosition(perline,plane);
					final AirPosition airPosition = new AirPosition();
					System.out.println("高度为："+airPosition.getHeight(MEString));
					System.out.println("纬度为:"+airPosition.getLatitude(MEString));
					System.out.println("经度为："+airPosition.getLongitude(MEString));
					result+=" 高度为："+airPosition.getHeight(MEString)+" 纬度为:"+airPosition.getLatitude(MEString)+" 经度为："+airPosition.getLongitude(MEString);
					break;
				case 4:
					uiUpdate.updateAirSpeed(perline,plane);
					final AirSpeed airSpeed = new AirSpeed();
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
