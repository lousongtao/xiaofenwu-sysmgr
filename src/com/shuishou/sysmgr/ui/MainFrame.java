package com.shuishou.sysmgr.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.shuishou.sysmgr.ConstantValue;
import com.shuishou.sysmgr.Messages;
import com.shuishou.sysmgr.beans.Category1;
import com.shuishou.sysmgr.beans.Category2;
import com.shuishou.sysmgr.beans.DiscountTemplate;
import com.shuishou.sysmgr.beans.Goods;
import com.shuishou.sysmgr.beans.Member;
import com.shuishou.sysmgr.beans.PayWay;
import com.shuishou.sysmgr.beans.Permission;
import com.shuishou.sysmgr.beans.UserData;
import com.shuishou.sysmgr.http.HttpUtil;
import com.shuishou.sysmgr.printertool.PrintThread;
import com.shuishou.sysmgr.ui.account.AccountMgmtPanel;
import com.shuishou.sysmgr.ui.config.ConfigsDialog;
import com.shuishou.sysmgr.ui.discounttemplate.DiscountTemplateMgmtPanel;
import com.shuishou.sysmgr.ui.goods.GoodsMgmtPanel;
import com.shuishou.sysmgr.ui.member.MemberQueryPanel;
import com.shuishou.sysmgr.ui.payway.PayWayMgmtPanel;
import com.shuishou.sysmgr.ui.query.IndentQueryPanel;
import com.shuishou.sysmgr.ui.query.LogQueryPanel;
import com.shuishou.sysmgr.ui.query.SaleRecordQueryPanel;
import com.shuishou.sysmgr.ui.query.ShiftworkQueryPanel;
import com.shuishou.sysmgr.ui.statistics.StatisticsPanel;

public class MainFrame extends JFrame implements ActionListener{
	public static final Logger logger = Logger.getLogger(MainFrame.class.getName());
	public static int WINDOW_WIDTH;
	public static int WINDOW_HEIGHT;
	public static int WINDOW_LOCATIONX;
	public static int WINDOW_LOCATIONY;
	public static String language;
	public static String SERVER_URL;
	public static String printerName;
	public static String FONT_PRINTTICKET;
	private static final String CARDLAYOUT_GOODSMGMT= "goodsmgmt"; 
	private static final String CARDLAYOUT_MEMBERMGMT= "membermgmt"; 
	private static final String CARDLAYOUT_ACCOUNTMGMT= "accountmgmt"; 
	private static final String CARDLAYOUT_DISCOUNTTEMPLATEMGMT= "discounttemplatemgmt"; 
	private static final String CARDLAYOUT_PAYWAYMGMT= "paywaymgmt"; 
	private static final String CARDLAYOUT_LOGQUERY= "logquery"; 
	private static final String CARDLAYOUT_INDENTQUERY= "indentquery"; 
	private static final String CARDLAYOUT_SHIFTWORKQUERY= "shiftworkquery"; 
	private static final String CARDLAYOUT_STATISTICS= "statistics"; 
	private static final String CARDLAYOUT_SALERECORD = "salerecord";
	private JBlockedButton btnAccountMgr = new JBlockedButton(Messages.getString("MainFrame.ToolBar.AccountMgr"));
	private JBlockedButton btnGoodsMgr = new JBlockedButton(Messages.getString("MainFrame.ToolBar.GoodsMgr"));
	private JBlockedButton btnMemberMgr = new JBlockedButton(Messages.getString("MainFrame.ToolBar.MemberMgr"));
	private JBlockedButton btnPayWayMgr = new JBlockedButton(Messages.getString("MainFrame.ToolBar.PayWayMgr"));
	private JBlockedButton btnDiscountTempMgr = new JBlockedButton(Messages.getString("MainFrame.ToolBar.DiscountTempMgr"));
	private JBlockedButton btnConfig = new JBlockedButton(Messages.getString("MainFrame.ToolBar.Config"));
	private JBlockedButton btnQueryLog = new JBlockedButton(Messages.getString("MainFrame.ToolBar.QueryLog"));
	private JBlockedButton btnQueryIndent = new JBlockedButton(Messages.getString("MainFrame.ToolBar.QueryIndent"));
	private JBlockedButton btnQuerySwiftWork = new JBlockedButton(Messages.getString("MainFrame.ToolBar.QuerySwiftWork"));
	private JBlockedButton btnStatistic = new JBlockedButton(Messages.getString("MainFrame.ToolBar.QueryStatistic"));
	private JBlockedButton btnSaleRecord = new JBlockedButton(Messages.getString("MainFrame.ToolBar.SaleRecord"));
	private JPanel pContent = new JPanel(new CardLayout());
	
	private ArrayList<Category1> listCategory1s;
	private ArrayList<Member> listMember;
	
	private static UserData loginUser;
	private HashMap<String, String> configsMap;
	
	private MemberQueryPanel pMemberMgmt;
	private GoodsMgmtPanel pGoodsMgmt;
	private AccountMgmtPanel pAccount;
	private DiscountTemplateMgmtPanel pDiscountTemplate;
	private PayWayMgmtPanel pPayWay;
	private LogQueryPanel pQueryLog;
	private IndentQueryPanel pQueryIndent;
	private ShiftworkQueryPanel pQueryShiftwork;
	private StatisticsPanel pStatistics;
	private SaleRecordQueryPanel pSaleRecord;
	private Gson gson = new Gson();
	
	private MainFrame(){
		initUI();
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setLocation(WINDOW_LOCATIONX, WINDOW_LOCATIONY);
		setTitle(Messages.getString("MainFrame.FrameTitle")); //$NON-NLS-1$
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initData();
		//start printer thread
        new PrintThread().startThread();
	}
	
	private void initData(){
		reloadListCategory1s();
		loadConfigsMap();
	}
	
	private void initUI(){
		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);
		toolbar.setMargin(new Insets(0,20,20,20));
		toolbar.add(btnAccountMgr);
		toolbar.addSeparator();
		toolbar.add(btnConfig);
		toolbar.addSeparator();
		toolbar.add(btnGoodsMgr);
		toolbar.addSeparator();
		toolbar.add(btnMemberMgr);
		toolbar.addSeparator();
		toolbar.add(btnPayWayMgr);
		toolbar.addSeparator();
		toolbar.add(btnDiscountTempMgr);
		toolbar.addSeparator();
		toolbar.add(btnQueryLog);
		toolbar.addSeparator();
		toolbar.add(btnQueryIndent);
		toolbar.addSeparator();
		toolbar.add(btnQuerySwiftWork);
		toolbar.addSeparator();
		toolbar.add(btnStatistic);
		toolbar.addSeparator();
		toolbar.add(btnSaleRecord);
		btnAccountMgr.addActionListener(this);
		btnGoodsMgr.addActionListener(this);
		btnMemberMgr.addActionListener(this);
		btnPayWayMgr.addActionListener(this);
		btnDiscountTempMgr.addActionListener(this);
		btnConfig.addActionListener(this);
		btnQueryLog.addActionListener(this);
		btnQueryIndent.addActionListener(this);
		btnQuerySwiftWork.addActionListener(this);
		btnStatistic.addActionListener(this);
		btnSaleRecord.addActionListener(this);
		
		Container c = this.getContentPane();
		c.setLayout(new BorderLayout(0, 10));
		c.add(toolbar, BorderLayout.NORTH);
		c.add(pContent, BorderLayout.CENTER);
	}
	
	public void startLogin(String userName, String password){
		LoginDialog dlg = new LoginDialog(this);
		dlg.setValue(userName, password);
		dlg.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnAccountMgr){
			ArrayList<UserData> userList = loadUserList();
			ArrayList<Permission> permissionList = loadPermissionList();
			if (pAccount == null){
				pAccount = new AccountMgmtPanel(this, userList, permissionList);
				pContent.add(pAccount, CARDLAYOUT_ACCOUNTMGMT);
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_ACCOUNTMGMT);
				pContent.updateUI();
			} else {
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_ACCOUNTMGMT);
			}
			
			this.setTitle(Messages.getString("MainFrame.FrameTitle") + " - " + btnAccountMgr.getText());
		} else if (e.getSource() == btnGoodsMgr){
			if (pGoodsMgmt == null){
				pGoodsMgmt = new GoodsMgmtPanel(this, listCategory1s);
				pContent.add(pGoodsMgmt, CARDLAYOUT_GOODSMGMT);
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_GOODSMGMT);
				pContent.updateUI();
			} else {
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_GOODSMGMT);
			}
			this.setTitle(Messages.getString("MainFrame.FrameTitle") + " - " + btnGoodsMgr.getText());
		} else if (e.getSource() == btnMemberMgr){
			if (pMemberMgmt == null){
				pMemberMgmt = new MemberQueryPanel(this);
				pContent.add(pMemberMgmt, CARDLAYOUT_MEMBERMGMT);
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_MEMBERMGMT);
				pContent.updateUI();
			} else {
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_MEMBERMGMT);
			}
			this.setTitle(Messages.getString("MainFrame.FrameTitle") + " - " + btnMemberMgr.getText());
		} else if (e.getSource() == btnPayWayMgr){
			ArrayList<PayWay> paywayList = loadPayWayList();
			if (pPayWay == null){
				pPayWay = new PayWayMgmtPanel(this, paywayList);
				pContent.add(pPayWay, CARDLAYOUT_PAYWAYMGMT);
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_PAYWAYMGMT);
				pContent.updateUI();
			} else {
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_PAYWAYMGMT);
			}
			this.setTitle(Messages.getString("MainFrame.FrameTitle") + " - " + btnPayWayMgr.getText());
		} else if (e.getSource() == btnDiscountTempMgr){
			ArrayList<DiscountTemplate> discountTempList = loadDiscountTemplateList();
			if (pDiscountTemplate == null){
				pDiscountTemplate = new DiscountTemplateMgmtPanel(this, discountTempList);
				pContent.add(pDiscountTemplate, CARDLAYOUT_DISCOUNTTEMPLATEMGMT);
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_DISCOUNTTEMPLATEMGMT);
				pContent.updateUI();
			} else {
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_DISCOUNTTEMPLATEMGMT);
			}
			this.setTitle(Messages.getString("MainFrame.FrameTitle") + " - " + btnDiscountTempMgr.getText());
		} else if (e.getSource() == btnConfig){
			ConfigsDialog dlg = new ConfigsDialog(this);
			dlg.setVisible(true);
		} else if (e.getSource() == btnQueryLog){
			if (pQueryLog == null){
				ArrayList<String> listLogType = loadLogType();
				pQueryLog = new LogQueryPanel(this, listLogType);
				pContent.add(pQueryLog, CARDLAYOUT_LOGQUERY);
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_LOGQUERY);
				pContent.updateUI();
			} else {
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_LOGQUERY);
			}
			this.setTitle(Messages.getString("MainFrame.FrameTitle") + " - " + btnQueryLog.getText());
		} else if (e.getSource() == btnQueryIndent){
			if (pQueryIndent == null){
				pQueryIndent = new IndentQueryPanel(this);
				pContent.add(pQueryIndent, CARDLAYOUT_INDENTQUERY);
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_INDENTQUERY);
				pContent.updateUI();
			} else {
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_INDENTQUERY);
			}
			this.setTitle(Messages.getString("MainFrame.FrameTitle") + " - " + btnQueryIndent.getText());
		} else if (e.getSource() == btnQuerySwiftWork){
			if (pQueryShiftwork == null){
				pQueryShiftwork = new ShiftworkQueryPanel(this);
				pContent.add(pQueryShiftwork, CARDLAYOUT_SHIFTWORKQUERY);
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_SHIFTWORKQUERY);
				pContent.updateUI();
			} else {
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_SHIFTWORKQUERY);
			}
			this.setTitle(Messages.getString("MainFrame.FrameTitle") + " - " + btnQuerySwiftWork.getText());
		} else if (e.getSource() == btnStatistic){
			if (pStatistics == null){
				pStatistics = new StatisticsPanel(this);
				pContent.add(pStatistics, CARDLAYOUT_STATISTICS);
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_STATISTICS);
				pContent.updateUI();
			} else {
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_STATISTICS);
			}
		} else if (e.getSource() == btnSaleRecord){
			if (pSaleRecord == null){
				pSaleRecord = new SaleRecordQueryPanel(this, loadPayWayList());
				pContent.add(pSaleRecord, CARDLAYOUT_SALERECORD);
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_SALERECORD);
				pContent.updateUI();
			} else {
				((CardLayout)pContent.getLayout()).show(pContent, CARDLAYOUT_SALERECORD);
			}
		}
	}
	
	public HashMap<String, String> getConfigsMap() {
		return configsMap;
	}

	public void setConfigMap(HashMap<String, String> configsMap) {
		this.configsMap = configsMap;
	}
	
	public Goods getGoodsByBarcode(String barcode){
		if (listCategory1s == null)
			return null;
		for(Category1 c1 : listCategory1s){
			if (c1.getCategory2s() != null){
				for(Category2 c2 : c1.getCategory2s()){
					if (c2.getGoods() != null){
						for(Goods goods : c2.getGoods()){
							if (barcode.equals(goods.getBarcode())){
								return goods;
							}
						}
					}
				}
			}
		}
		return null;
	}

	public static UserData getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(UserData loginUser) {
		this.loginUser = loginUser;
	}

	public ArrayList<UserData> loadUserList(){
		ArrayList<UserData> userList = HttpUtil.loadUser(this, SERVER_URL + "account/accounts?userId="+loginUser.getId());
		return userList;
	}
	
	public ArrayList<PayWay> loadPayWayList(){
		ArrayList<PayWay> payWayList = HttpUtil.loadPayWay(this, SERVER_URL + "common/getpayways");
		Collections.sort(payWayList, new Comparator<PayWay>(){

			@Override
			public int compare(PayWay o1, PayWay o2) {
				return o1.getSequence() - o2.getSequence();
			}});
		return payWayList;
	}
	
	public ArrayList<DiscountTemplate> loadDiscountTemplateList(){
		ArrayList<DiscountTemplate> discountTemplateList = HttpUtil.loadDiscountTemplate(this, SERVER_URL + "common/getdiscounttemplates");
		return discountTemplateList;
	}
	
	public ArrayList<String> loadLogType(){
		ArrayList<String> listLogType = HttpUtil.loadLogType(this, SERVER_URL + "log/log_types?userId="+loginUser.getId());
		return listLogType;
	}
	
	private ArrayList<Permission> loadPermissionList(){
		ArrayList<Permission> permissionList = HttpUtil.loadPermission(this, SERVER_URL + "account/querypermission");
		return permissionList;
	}
	
	public ArrayList<Category1> getListCategory1s() {
		return listCategory1s;
	}
	
	public void reloadListCategory1s() {
		listCategory1s = HttpUtil.loadGoods(this, SERVER_URL + "goods/querygoods");
		Comparator comp = new Comparator() {

			@Override
			public int compare(Object o1, Object o2) {
				if (o1 instanceof Category1 && o2 instanceof Category1)
					return ((Category1) o1).getSequence() - ((Category1) o2).getSequence();
				else if (o1 instanceof Category2 && o2 instanceof Category2)
					return ((Category2) o1).getSequence() - ((Category2) o2).getSequence();
				else if (o1 instanceof Goods && o2 instanceof Goods)
					return ((Goods) o1).getSequence() - ((Goods) o2).getSequence();
				return 0;
			}
		};
		if (listCategory1s != null && !listCategory1s.isEmpty()){
			Collections.sort(listCategory1s, comp);
			for (Category1 c1 : listCategory1s) {
				Collections.sort(c1.getCategory2s(), comp);
				for (Category2 c2 : c1.getCategory2s()) {
					Collections.sort(c2.getGoods(), comp);
				}
			}
		}
	}
	
	public ArrayList<Member> getListMember(){
		if (listMember == null){
			loadMember();
		}
		return listMember;
	}
	
	public void loadMember(){
		listMember = HttpUtil.loadAllMember(this, loginUser);
	}
	
	public void loadConfigsMap(){
		this.configsMap = HttpUtil.loadConfigMap(this, SERVER_URL + "common/queryconfigmap");
	}

	public static void main(String[] args){
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				MainFrame.logger.error(ConstantValue.DFYMDHMS.format(new Date()));
				MainFrame.logger.error("", e);
				e.printStackTrace();
			}
		});
		StartingWaitDialog waitDlg = new StartingWaitDialog();
		waitDlg.setVisible(true);
		//load properties
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = MainFrame.class.getClassLoader().getResourceAsStream("config.properties");
			// load a properties file
			prop.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		Messages.initResourceBundle(prop.getProperty("language"));
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());//windows 格式
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		MainFrame.SERVER_URL = prop.getProperty("SERVER_URL");
		MainFrame.WINDOW_WIDTH = Integer.parseInt(prop.getProperty("mainframe.width"));
		MainFrame.WINDOW_HEIGHT = Integer.parseInt(prop.getProperty("mainframe.height"));
		MainFrame.WINDOW_LOCATIONX = Integer.parseInt(prop.getProperty("mainframe.locationx"));
		MainFrame.WINDOW_LOCATIONY = Integer.parseInt(prop.getProperty("mainframe.locationy"));
		MainFrame.language = prop.getProperty("language");
		MainFrame.printerName = prop.getProperty("printerName");
		MainFrame.FONT_PRINTTICKET = prop.getProperty("printFont");
		MainFrame f = new MainFrame();
		waitDlg.setVisible(false);
		f.setVisible(true);
		f.startLogin(prop.getProperty("defaultuser.name"), prop.getProperty("defaultuser.password"));
	}
}
