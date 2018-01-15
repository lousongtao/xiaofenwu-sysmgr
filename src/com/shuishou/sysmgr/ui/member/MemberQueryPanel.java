package com.shuishou.sysmgr.ui.member;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.shuishou.sysmgr.ConstantValue;
import com.shuishou.sysmgr.Messages;
import com.shuishou.sysmgr.beans.HttpResult;
import com.shuishou.sysmgr.beans.Member;
import com.shuishou.sysmgr.http.HttpUtil;
import com.shuishou.sysmgr.ui.CommonDialog;
import com.shuishou.sysmgr.ui.MainFrame;

public class MemberQueryPanel extends JPanel implements ActionListener{
	private final Logger logger = Logger.getLogger(MemberQueryPanel.class.getName());

	private MainFrame mainFrame;
	private JTextField tfName = new JTextField();
	private JTextField tfMemberCard = new JTextField();
	private JTextField tfTelephone = new JTextField();
	private JTextField tfAddress = new JTextField();
	private JTextField tfPostcode = new JTextField();
	private JTable table = new JTable();
	private TableModel model = new TableModel();
	private JButton btnQuery = new JButton("Query");
	private JButton btnAdd = new JButton("Add");
	private JButton btnUpdate = new JButton("Update");
	private JButton btnUpdateScore = new JButton("Update Score");
	private JButton btnUpdateBalance = new JButton("Update Balance");
	
	private ArrayList<Member> members = new ArrayList<>();
	
	public MemberQueryPanel(MainFrame mainFrame){
		this.mainFrame = mainFrame;
		initUI();
	}
	
	private void initUI(){
		JLabel lbName = new JLabel("Name : ");
		JLabel lbMemberCard = new JLabel("Member Card : ");
		JLabel lbTelephone = new JLabel("Telephone : ");
		JLabel lbAddress = new JLabel("Address : ");
		JLabel lbPostcode = new JLabel("Postcode : ");
		tfName.setPreferredSize(new Dimension(120, 25));
		tfMemberCard.setPreferredSize(new Dimension(120, 25));
		tfTelephone.setPreferredSize(new Dimension(120, 25));
		tfAddress.setPreferredSize(new Dimension(120, 25));
		tfPostcode.setPreferredSize(new Dimension(120, 25));
		
		table.setModel(model);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		table.getColumnModel().getColumn(2).setPreferredWidth(80);
		table.getColumnModel().getColumn(3).setPreferredWidth(100);
		table.getColumnModel().getColumn(4).setPreferredWidth(100);
		table.getColumnModel().getColumn(5).setPreferredWidth(100);
		table.getColumnModel().getColumn(6).setPreferredWidth(100);
		table.getColumnModel().getColumn(7).setPreferredWidth(100);
		table.getColumnModel().getColumn(8).setPreferredWidth(100);
		table.getColumnModel().getColumn(9).setPreferredWidth(100);
		table.setAutoCreateRowSorter(true);
		JScrollPane jspTable = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		JPanel pCondition = new JPanel(new GridBagLayout());
		pCondition.add(lbName, 		new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		pCondition.add(tfName, 		new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 0), 0, 0));
		pCondition.add(lbMemberCard,new GridBagConstraints(2, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		pCondition.add(tfMemberCard,new GridBagConstraints(3, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 0), 0, 0));
		pCondition.add(lbTelephone, new GridBagConstraints(4, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		pCondition.add(tfTelephone, new GridBagConstraints(5, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 0), 0, 0));
		pCondition.add(lbAddress, 	new GridBagConstraints(0, 1, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		pCondition.add(tfAddress, 	new GridBagConstraints(1, 1, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 0), 0, 0));
		pCondition.add(lbPostcode, 	new GridBagConstraints(2, 1, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		pCondition.add(tfPostcode, 	new GridBagConstraints(3, 1, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 0), 0, 0));
		pCondition.add(btnQuery, 	new GridBagConstraints(4, 1, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		
		btnQuery.addActionListener(this);
		
		JPanel pButtons = new JPanel();
		pButtons.add(btnAdd);
		pButtons.add(btnUpdate);
		pButtons.add(btnUpdateScore);
		pButtons.add(btnUpdateBalance);
		btnAdd.addActionListener(this);
		btnUpdate.addActionListener(this);
		btnUpdateScore.addActionListener(this);
		btnUpdateBalance.addActionListener(this);
		
		setLayout(new BorderLayout());
		add(jspTable, BorderLayout.CENTER);
		add(pCondition, BorderLayout.NORTH);
		add(pButtons, BorderLayout.SOUTH);
	}
	
	private void doQuery(){
		members.clear();
		((TableModel)table.getModel()).setRowCount(0);
		String url = "member/querymember";
		Map<String, String> params = new HashMap<>();
		params.put("userId", MainFrame.getLoginUser().getId() + "");
		if (tfName.getText() != null && tfName.getText().length() > 0)
			params.put("name",tfName.getText());
		if (tfMemberCard.getText() != null && tfMemberCard.getText().length() > 0)
			params.put("memberCard", tfMemberCard.getText());
		if (tfAddress.getText() != null && tfAddress.getText().length() > 0)
			params.put("address", tfAddress.getText());
		if (tfPostcode.getText() != null && tfPostcode.getText().length() > 0)
			params.put("postCode", tfPostcode.getText());
		if (tfTelephone.getText() != null && tfTelephone.getText().length() > 0)
			params.put("telephone", tfTelephone.getText());
		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
		if (response == null){
			logger.error("get null from server for query member. URL = " + url + ", param = "+ params);
			JOptionPane.showMessageDialog(this, "get null from server for query member. URL = " + url);
			return;
		}
		Gson gson = new GsonBuilder().setDateFormat("yyyy/MM/dd HH:mm:ss").create();
		HttpResult<ArrayList<Member>> result = gson.fromJson(response, new TypeToken<HttpResult<ArrayList<Member>>>(){}.getType());
		if (!result.success){
			logger.error("return false while query member. URL = " + url + ", response = "+response);
			JOptionPane.showMessageDialog(this, "return false while query member. URL = " + url + ", response = "+response);
			return;
		}
		members = result.data;
		table.updateUI();
	}
	
	private void doAddMember(){
		MemberPanel p = new MemberPanel(this);
		CommonDialog dlg = new CommonDialog(mainFrame, p, "Add Member", 300, 300);
		dlg.setVisible(true);
	}

	private void doUpdateMember(){
		if (table.getSelectedRow() < 0)
			return;
		MemberPanel p = new MemberPanel(this);
		p.setObjectValue(model.getObjectAt(table.getSelectedRow()));
		CommonDialog dlg = new CommonDialog(mainFrame, p, "Update Member", 300, 300);
		dlg.setVisible(true);
	}
	
	private void doUpdateScore(){
	
	}
	
	private void doUpdateBalance(){
		
	}
	
	public void insertRow(Member m){
		members.add(0, m);
		model.fireTableDataChanged();
	}
	
	public void updateRow(Member m){
		for (int i = 0; i < members.size(); i++) {
			if (m.getId() == members.get(i).getId()){
				members.set(i, m);
				break;
			}
		}
		model.fireTableDataChanged();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnQuery){
			doQuery();
		} else if (e.getSource() == btnAdd){
			doAddMember();
		} else if (e.getSource() == btnUpdate){
			doUpdateMember();
		} else if (e.getSource() == btnUpdateScore){
			doUpdateScore();
		} else if (e.getSource() == btnUpdateBalance){
			doUpdateBalance();
		}
	}
	
	class TableModel extends DefaultTableModel{

		private String[] header = new String[]{"Name", "Member Card", "Telephone", "Discount Rate", "Create Date","Score", "Balance Money","Address", "Postcode",  "Birthday"};
		
		public TableModel(){

		}
		@Override
		public int getRowCount() {
			if (members == null) return 0;
			return members.size();
		}

		@Override
		public int getColumnCount() {
			return header.length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Member m = members.get(rowIndex);
			switch(columnIndex){
			case 0:
				return m.getName();
			case 1: 
				return m.getMemberCard();
			case 2:
				return m.getTelephone();
			case 3:
				return m.getDiscountRate();
			case 4:
				return ConstantValue.DFYMD.format(m.getCreateTime());
			case 5: 
				return m.getScore();
			case 6:
				return m.getBalanceMoney();
			case 7:
				return m.getAddress();
			case 8:
				return m.getPostCode();
			case 9:
				if (m.getBirth() == null) return "";
				return ConstantValue.DFYMD.format(m.getBirth());
			}
			return "";
		}
		
		@Override
		public String getColumnName(int col){
			return header[col];
		}
		
		public Member getObjectAt(int row){
			return members.get(row);
		}
		
		@Override
		public Class<?> getColumnClass(int col){
			switch(col){
			case 2:
				return Long.class;
			case 3:
				return Double.class;
			case 5: 
				return Double.class;
			case 6:
				return Double.class;
			}
			return String.class;
		}
		
		public boolean isCellEditable(int row, int column) {
        return false;
    }
	}
}
