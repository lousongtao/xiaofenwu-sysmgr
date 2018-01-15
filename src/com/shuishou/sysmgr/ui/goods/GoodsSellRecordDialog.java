package com.shuishou.sysmgr.ui.goods;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import com.shuishou.sysmgr.beans.Goods;
import com.shuishou.sysmgr.beans.GoodsSellRecord;
import com.shuishou.sysmgr.ui.MainFrame;
import com.shuishou.sysmgr.ui.member.MemberQueryPanel;


public class GoodsSellRecordDialog extends JDialog{
	private final Logger logger = Logger.getLogger(MemberQueryPanel.class.getName());
	private JTable table = new JTable();
	private RecordTableModel model = new RecordTableModel();
	private MainFrame parent;
	
	public GoodsSellRecordDialog(MainFrame parent, String title, ArrayList<GoodsSellRecord> records, int width, int height){
		setTitle(title);
		setSize(width, height);
		this.setModal(true);
		this.parent = parent;
		initUI(records);
		
	}
	
	private void initUI(ArrayList<GoodsSellRecord> records){
		model.setData(records);
		table.setModel(model);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getColumnModel().getColumn(0).setPreferredWidth(250);
		table.getColumnModel().getColumn(1).setPreferredWidth(150);
		table.getColumnModel().getColumn(2).setPreferredWidth(150);
		table.getColumnModel().getColumn(3).setPreferredWidth(80);
		table.getColumnModel().getColumn(4).setPreferredWidth(150);
		JScrollPane jspTable = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		JButton btnCancel = new JButton("Close");
		JPanel pButton = new JPanel();
		pButton.add(btnCancel);
		Container c = this.getContentPane();
		c.setLayout(new BorderLayout());
		c.add(jspTable, BorderLayout.CENTER);
		c.add(pButton, BorderLayout.SOUTH);
		setLocation((int)(parent.getWidth() / 2 - this.getWidth() /2 + parent.getLocation().getX()), 
				(int)(parent.getHeight() / 2 - this.getHeight() / 2 + parent.getLocation().getY()));
		btnCancel.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				GoodsSellRecordDialog.this.setVisible(false);
			}});
	}
	
	class RecordTableModel extends DefaultTableModel{
		private ArrayList<GoodsSellRecord> items = new ArrayList<>();
		private String[] header = new String[]{"Order Time","Member","Pay Way","Amount","Sold Price"};

		public RecordTableModel(){
		}
		
		public RecordTableModel(ArrayList<GoodsSellRecord> items){
			this.items = items;
		}
		@Override
		public int getRowCount() {
			if (items == null)
				return 0;
			return items.size();
		}

		@Override
		public int getColumnCount() {
			return header.length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			GoodsSellRecord r = getObjectAt(rowIndex);
			switch(columnIndex){
			case 0:
				return r.getSoldTime();
			case 1:
				return r.getMember();
			case 2:
				return r.getPayWay();
			case 3:
				return r.getAmount();
			case 4: 
				return r.getSoldPrice();
			}
			return "";
		}
		
		@Override
		public String getColumnName(int column) {
			return header[column];
	    }
		
		public void setData(ArrayList<GoodsSellRecord> items){
			this.items = items;
		}
		
		public ArrayList<GoodsSellRecord> getData(){
			return items;
		}
		
		public GoodsSellRecord getObjectAt(int index){
			return items.get(index);
		}
		
		@Override
	    public boolean isCellEditable(int row, int column) { 
	        return false;
	    }
	}
}
