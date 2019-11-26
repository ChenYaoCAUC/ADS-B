package ui;

import java.awt.EventQueue;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import Bean.Plane;
import controller.AirPosition;
import controller.AirSpeed;
import controller.DataSolver;
import controller.FlightNumber;
import controller.GroundPosition;


public class UIUpdate {
	private DefaultTableModel model;
	private HashMap<String, Integer> numMap = new HashMap<>();
	private HashMap<String, Plane> infoMap = new HashMap<>();
	public UIUpdate(DefaultTableModel model) {
		this.model=model;
	}
//	�ڽ�����������Ϣ
	private void addAirlineInfoToModal(Plane info) {
		EventQueue.invokeLater(()->{
			numMap.put(info.getId(), model.getRowCount());
			infoMap.put(info.getId(), info);
			model.addRow(getAirlineInfoModal(info));
		});
	}
//	��ԭ�н����ϸ�����Ϣ
	private void updateAirlineInfoToModal(Plane info, int i) {
		EventQueue.invokeLater(()->{
			model.removeRow(i);
			infoMap.put(info.getId(), info);
			model.insertRow(i, getAirlineInfoModal(info));
		});
	}
	
//	�Ӷ����л�ȡ��Ӧ�����ݷ���
	private Vector<String> getAirlineInfoModal(Plane info) {
		Vector<String> vector = new Vector<>();
		vector.add(info.getId());
		if(info.getNumber()!=null) {
			vector.add(info.getNumber());
		}
		else {
			vector.add("���޺����");
		}
		if(info.getLongitude()!=null) {
			vector.add(info.getLongitude());
		}
		else {
			vector.add("���޾���");
		}
		if(info.getLatitude()!=null) {
			vector.add(info.getLatitude());
		}
		else {
			vector.add("����γ��");
		}
		if(info.getHeight()!=null) {
			vector.add(info.getHeight());
		}
		else {
			vector.add("���޷ɻ��߶�");
		}
		if(info.getAirSpeedVertical()!=null) {
			vector.add(info.getAirSpeedVertical());
		}
		else {
			vector.add("���޿��д�ֱ�ٶ�");
		}
		if(info.getAirSpeedHorizontal()!=null) {
			vector.add(info.getAirSpeedHorizontal());
		}
		else {
			vector.add("���޿���ˮƽ�ٶȺͷ���");
		}
		
		if(info.getGroundspeed()!=null) {
			vector.add(info.getGroundspeed());
		}
		else {
			vector.add("���޵����ٶ�");
		}
		
		if(info.getCourse()!=null) {
			vector.add(info.getCourse());
		}
		else {
			vector.add("���޺���");
		}
		return vector;
	}
//	���·ɻ��������Ϣ
	public void updateFlightNumbr(String string,Plane info) {
		Integer index = null;
//		info.setId(DataSolver.getAA(string));
		FlightNumber flightNumber = new FlightNumber();
		String binaryString = DataSolver.bytes2BinaryStr(string);	
		String MEString = DataSolver.getME(binaryString);	
		String numberString = flightNumber.getPlaneNumber(MEString);
		if ((index = numMap.get(info.getId())) != null) {			
			Plane infoPlane = infoMap.get(info.getId());		
			infoPlane.setNumber(numberString);
			updateAirlineInfoToModal(infoPlane,index.intValue());
		}else {	
			info.setNumber(numberString);
			addAirlineInfoToModal(info);	
		}
	}
//	���µ���λ����Ϣ
	public void updateGroundPosition(String string,Plane info) {
		Integer index = null;
//		info.setId(DataSolver.getAA(string));
		GroundPosition groundPosition = new GroundPosition();
		String binaryString = DataSolver.bytes2BinaryStr(string);	
		String MEString = DataSolver.getME(binaryString);				
		if ((index = numMap.get(info.getId())) != null) {	
			Plane infoPlane = infoMap.get(info.getId());		
			infoPlane.setGroundspeed(groundPosition.getGroundSpeed(MEString));
			infoPlane.setCourse(groundPosition.getCourse(MEString));
			infoPlane.setLatitude(groundPosition.getLatitude(MEString));
			infoPlane.setLongitude(groundPosition.getLongitude(MEString));
			updateAirlineInfoToModal(infoPlane, index.intValue());
		}else {		
			info.setGroundspeed(groundPosition.getGroundSpeed(MEString));
			info.setCourse(groundPosition.getCourse(MEString));
			info.setLatitude(groundPosition.getLatitude(MEString));
			info.setLongitude(groundPosition.getLongitude(MEString));
			addAirlineInfoToModal(info);
		}
	}
//	���¿���λ����Ϣ
	public void updateAirPosition(String string,Plane info) {
		Integer index = null;	
//		info.setId(DataSolver.getAA(string));
		AirPosition airPosition = new AirPosition();
		String binaryString = DataSolver.bytes2BinaryStr(string);	
		String MEString = DataSolver.getME(binaryString);		
		if ((index = numMap.get(info.getId())) != null) {
			Plane infoPlane = infoMap.get(info.getId());	
			infoPlane.setHeight(airPosition.getHeight(MEString));
			infoPlane.setLatitude(airPosition.getLatitude(MEString));
			infoPlane.setLongitude(airPosition.getLongitude(MEString));
			updateAirlineInfoToModal(infoPlane, index.intValue());
		}else {		
			info.setHeight(airPosition.getHeight(MEString));
			info.setLatitude(airPosition.getLatitude(MEString));
			info.setLongitude(airPosition.getLongitude(MEString));
			addAirlineInfoToModal(info);
		}
	}
//	���¿����ٶ���Ϣ
	public void updateAirSpeed(String string,Plane info) {
		Integer index = null;
//		info.setId(DataSolver.getAA(string));
		AirSpeed airSpeed = new AirSpeed();
		String binaryString = DataSolver.bytes2BinaryStr(string);	
		String MEString = DataSolver.getME(binaryString);		
		if ((index = numMap.get(info.getId())) != null) {	
			Plane infoPlane = infoMap.get(info.getId());	
			infoPlane.setAirSpeedHorizontal(airSpeed.getAirSpeedHorizontal(MEString).get("ˮƽ�ٶ�")+"����Ϊ��"+airSpeed.getAirSpeedHorizontal(MEString).get("ˮƽ����"));
			infoPlane.setAirSpeedVertical(airSpeed.getAirSpeedVertical(MEString));
			updateAirlineInfoToModal(infoPlane, index.intValue());
			
		}else {		
			info.setAirSpeedHorizontal(airSpeed.getAirSpeedHorizontal(MEString).get("ˮƽ�ٶ�")+"����Ϊ��"+airSpeed.getAirSpeedHorizontal(MEString).get("ˮƽ����"));
			info.setAirSpeedVertical(airSpeed.getAirSpeedVertical(MEString));
			addAirlineInfoToModal(info);
		}
	}
	

}
