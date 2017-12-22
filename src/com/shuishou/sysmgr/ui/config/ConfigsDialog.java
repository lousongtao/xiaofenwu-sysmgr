package com.shuishou.sysmgr.ui.config;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shuishou.sysmgr.ConstantValue;
import com.shuishou.sysmgr.Messages;
import com.shuishou.sysmgr.beans.HttpResult;
import com.shuishou.sysmgr.http.HttpUtil;
import com.shuishou.sysmgr.ui.MainFrame;
import com.shuishou.sysmgr.ui.components.NumberTextField;

public class ConfigsDialog extends JDialog implements ActionListener{
	private final Logger logger = Logger.getLogger(ConfigsDialog.class.getName());
	private JButton btnSaveOpenCashdrawerCode = new JButton("Save");
	private JTextField tfOldOpenCashdrawerCode;
	private NumberTextField tfNewOpenCashdrawerCode;
	private MainFrame mainFrame;
	public ConfigsDialog(MainFrame mainFrame){
		super(mainFrame, Messages.getString("ConfigsDialog.Config"), true);
		this.mainFrame = mainFrame;
		initUI();
		initData();
	}
	
	private void initUI(){
		JLabel lbOldOpenCashdrawerCode = new JLabel("old code");
		JLabel lbNewOpenCashdrawerCode = new JLabel("new code");
		tfOldOpenCashdrawerCode = new JTextField();
		tfNewOpenCashdrawerCode = new NumberTextField(false);
		JPanel pOpenCashdrawerCode = new JPanel(new GridBagLayout());
		pOpenCashdrawerCode.add(lbOldOpenCashdrawerCode, new GridBagConstraints(0, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));;
		pOpenCashdrawerCode.add(tfOldOpenCashdrawerCode, new GridBagConstraints(1, 0, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,10,0,0), 0, 0));;
		pOpenCashdrawerCode.add(lbNewOpenCashdrawerCode, new GridBagConstraints(2, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));;
		pOpenCashdrawerCode.add(tfNewOpenCashdrawerCode, new GridBagConstraints(3, 0, 1, 1,1,0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0,10,0,0), 0, 0));;
		pOpenCashdrawerCode.add(btnSaveOpenCashdrawerCode, new GridBagConstraints(4, 0, 1, 1,0,0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,10,0,0), 0, 0));;
		pOpenCashdrawerCode.setBorder(BorderFactory.createTitledBorder("Open Cashdrawer Code"));
		
		
		JPanel pBasic = new JPanel(new GridBagLayout());
		pBasic.add(pOpenCashdrawerCode, new GridBagConstraints(0, 0, 1, 1,1,1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10,0,0,0), 0, 0));;
		JTabbedPane tabPane = new JTabbedPane();
		tabPane.add("Basic Setting", pBasic);
		
		Container c = this.getContentPane();
		c.setLayout(new BorderLayout());
		c.add(tabPane,  BorderLayout.CENTER);
		
		btnSaveOpenCashdrawerCode.addActionListener(this);
		
		setSize(500, 400);
		this.setLocation((int)(mainFrame.getWidth() / 2 - this.getWidth() /2 + mainFrame.getLocation().getX()), 
				(int)(mainFrame.getHeight() / 2 - this.getHeight() / 2 + mainFrame.getLocation().getY()));
	}

	private void initData(){
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnSaveOpenCashdrawerCode){
			doSaveOpenCashdrawerCode();
		} 
	}
	
	private void doSaveOpenCashdrawerCode(){
		if (tfNewOpenCashdrawerCode.getText() == null || tfNewOpenCashdrawerCode.getText().length() == 0){
			JOptionPane.showMessageDialog(this, "No input new code");
			return;
		}
		String url = "common/saveopencashdrawercode";
		HashMap<String, String> params = new HashMap<>();
		params.put("userId", MainFrame.getLoginUser().getId()+"");
		params.put("code", tfNewOpenCashdrawerCode.getText());
		params.put("oldCode", tfOldOpenCashdrawerCode.getText());
		String response = HttpUtil.getJSONObjectByPost(MainFrame.SERVER_URL + url, params);
		if (response == null){
			logger.error("get null from server for save open cashdrawer code. URL = " + url + ", param = "+ params);
			JOptionPane.showMessageDialog(this, "get null from server for save open cashdrawer code. URL = " + url);
			return;
		}
		
		HttpResult<String> result = new Gson().fromJson(response, new TypeToken<HttpResult<String>>(){}.getType());
		if (!result.success){
			logger.error("return false while save open cashdrawer code. URL = " + url + ", response = "+response);
			JOptionPane.showMessageDialog(this, "return false while save open cashdrawer code. URL = " + url + ", response = "+response);
			return;
		}
		mainFrame.getConfigsMap().put(ConstantValue.CONFIGS_OPENCASHDRAWERCODE, tfNewOpenCashdrawerCode.getText());
		this.setVisible(false);
	}
}
