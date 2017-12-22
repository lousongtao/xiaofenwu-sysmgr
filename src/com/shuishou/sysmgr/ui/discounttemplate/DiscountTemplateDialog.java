package com.shuishou.sysmgr.ui.discounttemplate;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shuishou.sysmgr.Messages;
import com.shuishou.sysmgr.beans.Category1;
import com.shuishou.sysmgr.beans.DiscountTemplate;
import com.shuishou.sysmgr.beans.HttpResult;
import com.shuishou.sysmgr.beans.Permission;
import com.shuishou.sysmgr.beans.UserData;
import com.shuishou.sysmgr.http.HttpUtil;
import com.shuishou.sysmgr.ui.MainFrame;
import com.shuishou.sysmgr.ui.components.NumberTextField;


public class DiscountTemplateDialog extends JDialog implements ActionListener {

	private final Logger logger = Logger.getLogger(DiscountTemplateDialog.class.getName());
	
	private MainFrame mainFrame;
	private DiscountTemplateMgmtPanel parent;
	
	private JTextField tfName = new JTextField();
	private NumberTextField tfDiscountRate = new NumberTextField(true);
	private JButton btnSave = new JButton("Save");
	private JButton btnCancel = new JButton("Cancel");
	public DiscountTemplateDialog(MainFrame mainFrame, DiscountTemplateMgmtPanel parent,String title){
		super(mainFrame, title, true);
		this.mainFrame = mainFrame;
		this.parent = parent;
		initUI();
	}
	
	private void initUI(){
		JLabel lbName = new JLabel("Name");
		JLabel lbDiscountRate = new JLabel("Discount Rate");
		Container c = this.getContentPane();
		c.setLayout(new GridBagLayout());
		c.add(lbName, 			new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		c.add(tfName, 			new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 0), 0, 0));
		c.add(lbDiscountRate, 	new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		c.add(tfDiscountRate, 	new GridBagConstraints(1, 1, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 0), 0, 0));
		c.add(btnSave, 			new GridBagConstraints(0, 3, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		c.add(btnCancel, 		new GridBagConstraints(1, 3, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 0, 0));
		btnSave.addActionListener(this);
		btnCancel.addActionListener(this);
		
		this.setSize(250,150);
		this.setLocation((int)(mainFrame.getWidth() / 2 - this.getWidth() /2 + mainFrame.getLocation().getX()), 
				(int)(mainFrame.getHeight() / 2 - this.getHeight() / 2 + mainFrame.getLocation().getY()));
	}
	
	private void doSave(){
		if (tfName.getText() == null || tfName.getText().length() == 0){
			JOptionPane.showMessageDialog(this, "must input name");
			return;
		}
		if (tfDiscountRate.getText() == null || tfDiscountRate.getText().length() == 0){
			JOptionPane.showMessageDialog(this, "must input discount rate");
			return;
		}
		Map<String, String> params = new HashMap<>();
		params.put("userId", MainFrame.getLoginUser().getId() + "");
		params.put("name", tfName.getText());
		params.put("rate", tfDiscountRate.getText());
		
		String url = "common/adddiscounttemplate";
		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
		if (response == null){
			logger.error("get null from server for add discount template. URL = " + url + ", param = "+ params);
			JOptionPane.showMessageDialog(this, "get null from server for add discount template. URL = " + url);
			return;
		}
		Gson gson = new Gson();
		HttpResult<String> result = gson.fromJson(response, new TypeToken<HttpResult<String>>(){}.getType());
		if (!result.success){
			logger.error("return false while add discount template. URL = " + url + ", response = "+response);
			JOptionPane.showMessageDialog(this, "return false while add discount template. URL = " + url + ", response = "+response);
			return;
		}
		parent.refreshData();
		setVisible(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnCancel){
			setVisible(false);
		} else if (e.getSource() == btnSave){
			doSave();			
		}
	}
	
}
