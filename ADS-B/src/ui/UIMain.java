package ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import controller.ADSBDataSolver;


public class UIMain extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTable table;
	private DefaultTableModel defaultTableModel;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(()->{
			new UIMain().setVisible(true);
		});
	}
	
	public UIMain() {
		setSize(900, 500);
		setTitle("ADS-B���ݽ���");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		defaultTableModel = new DefaultTableModel(
				new String[]{"�ɻ�ʶ����","�����","����","γ��", "�߶�","��ֱ�ٶ�","ˮƽ�ٶȺͷ���","����","����"},0);
		table = new JTable(defaultTableModel);
		table.setEnabled(false);
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane,BorderLayout.CENTER);		
		JButton btnStart = new JButton("��ʼ");
		JButton btnStop = new JButton("����");
		btnStart.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				UIUpdate updater = new UIUpdate(defaultTableModel);
				ADSBDataSolver solver = new ADSBDataSolver();
				solver.new ReadThread().start();
				solver.new AnalyseThread(updater).start();
			}
		});
		btnStop.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				int option= JOptionPane.showConfirmDialog( 
						UIMain.this, "ȷ���˳�ϵͳ? ", "��ʾ ",JOptionPane.YES_NO_CANCEL_OPTION); 
				if(option==JOptionPane.YES_OPTION) 		         
					System.exit(0); 
		        else 
		        	return; 	         		
			}
		});
		JPanel southPanel = new JPanel();
		southPanel.add(btnStart);
		southPanel.add(btnStop);
		add(southPanel, BorderLayout.SOUTH);
	}
	
}
