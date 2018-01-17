package com.shuishou.sysmgr.printertool;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.JsonKit;
import com.shuishou.sysmgr.printertool.param.Constant;
import com.shuishou.sysmgr.printertool.param.Goods;
import com.shuishou.sysmgr.printertool.param.PagerImages;
import com.shuishou.sysmgr.printertool.param.PosParam;
import com.shuishou.sysmgr.printertool.param.PosTpl;
import com.shuishou.sysmgr.printertool.param.PrintPager;
import com.shuishou.sysmgr.printertool.param.Printer;
import com.shuishou.sysmgr.printertool.param.Text;
import com.shuishou.sysmgr.printertool.param._PagerBody;
import com.shuishou.sysmgr.ui.MainFrame;

/**

 * 

 * @author zhulinfeng

 * @时间 2016年9月23日下午12:59:03

 *

 */
public class DriverPos {
	private static Logger logger = Logger.getLogger(DriverPos.class);
	
	/**

	 * 打印

	 * @param template		打印模板

	 * @param param			打印参数

	 * @param printerName	打印机名称

	 * @throws IOException

	 */
	public void print(String template, String param, String printerName) throws IOException {
        PosParam posParam = JSON.parseObject(param, PosParam.class);

        Map<String, Object> keyMap = posParam.getKeys();
        List<Map<String, Object>> goodsParam = posParam.getGoods();

        //替换占位符的正则表达式

        Pattern pattern = Pattern.compile(Constant.REPLACE_PATTERN);

        Matcher matcher = pattern.matcher(template);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String key = matcher.group(1);
            if(keyMap.containsKey(key)){
            	matcher.appendReplacement(sb, keyMap.get(key)+"");
            }else{
            	System.out.println("keyMap has not found : " + key);
            }
        }

        matcher.appendTail(sb);

        template = sb.toString();

        PosTpl posTpl = JSON.parseObject(template, PosTpl.class);
        PrintPager pager = new PrintPager();
		if (MainFrame.FONT_PRINTTICKET != null && MainFrame.FONT_PRINTTICKET.length() > 0)
			pager.setFontFamily(MainFrame.FONT_PRINTTICKET);
		
        List<_PagerBody> bodyList = new ArrayList<_PagerBody>();

        // print header
        for (JSONObject jsonObject : posTpl.getHeader()) {
            bodyList.add(print(jsonObject));
        }

        if(posTpl.getGoods()!=null){
        	// print goods title
        	for (Goods goods : posTpl.getGoods()) {
        		//跳过requirement项
        		if (!"备注".equals(goods.getName()))
        			bodyList.add(printTitle(goods));
        	}
        	//换行
        	bodyList.add(new _PagerBody().setFeeLine(true));
        	
        	// print detail
        	for (Map<String, Object> goods : goodsParam) {
        		for(Goods goodTitle : posTpl.getGoods()){
        			//跳过requirement项
            		if (!"备注".equals(goodTitle.getName()))
            			bodyList.add(printGoods(goods, goodTitle));
        		}
        		//换行
        		bodyList.add(new _PagerBody().setFeeLine(true));
        		//单独一行打印需求
        		if(goods.get("requirement") != null && goods.get("requirement").toString().length() > 0){
        			bodyList.add(printRemark(goods.get("requirement")+""));
        		}
        		//换行
        		bodyList.add(new _PagerBody().setFeeLine(true));
        		//打印菜品条形码
        		if(goods.containsKey("qrcode") ){
        			bodyList.add(printGoodsQrcode(goods.get("qrcode")+""));
        			bodyList.add(printRemark(goods.get("remark")+""));
        		}
        	}
        }
        
        if(posTpl.getWarn()!=null){
        	//换行
            bodyList.add(new _PagerBody().setFeeLine(true));
        	for(JSONObject jsonObject : posTpl.getWarn()){
        		bodyList.add(print(jsonObject));
        	}
        }
        if(posTpl.getMsg()!=null){
        	for(JSONObject jsonObject : posTpl.getMsg()){
        		bodyList.add(print(jsonObject));
        	}
        }

        System.out.println(JsonKit.toJson(bodyList));
        
        //填充打印对象列表
        pager.setList(bodyList);
        new Printer().printJob(pager, printerName); 
    }

	/**

	 * 菜品注释

	 * @param remark

	 * @return

	 */
	private _PagerBody printRemark(String remark) {
		if(remark.equals("null")){
			return new _PagerBody().setFeeLine(true);
		}
		
		return new _PagerBody().setContent(remark).setAlign(0).setFeeLine(true);
	}

	/**

	 * 菜品条形码

	 * @param qrcode	条形码文件绝对路径

	 * @return

	 */
	private _PagerBody printGoodsQrcode(String qrcode) {
		
		return new _PagerBody().setImg(new PagerImages(qrcode, 50, 15)).setAlign(0).setFeeLine(false);
	}

	/**

     * 打印任何对象

     *

     * @param jsonObject  需要输出对象

     * @throws IOException

     */
    private static _PagerBody print(JSONObject jsonObject) throws IOException {
    	
        int type = jsonObject.getInteger("type");
        
        if(type==0){
        	Text text = JSON.toJavaObject(jsonObject, Text.class);
        	return printText(text);
        }else if(type==1){
        	PagerImages image = JSON.toJavaObject(jsonObject, PagerImages.class);
        	return printImage(image);
        }
        
        return new _PagerBody();
    }

    /**

     * 打印图片

     * @param image		PagerImages对象

     * @return

     */
	private static _PagerBody printImage(PagerImages image) {

		return new _PagerBody().setImg(image).setAlign(image.getAlign()).setFeeLine(image.isLine());
	}

	/**

	 * 打印文本

	 * @param text

	 * @return

	 */
	private static _PagerBody printText(Text text) {
		_PagerBody pagerBody = new _PagerBody();
		pagerBody.setContent(text.getText())
					.isBold(text.isBold())
					.setAlign(text.getAlign())
					.setFontSize(text.getSize())
					.setFeeLine(text.isLine());
		
		return pagerBody;
	}
	
	/**

	 * 循环打印菜品

	 * @param goodMap

	 * @param goodTitle

	 * @return

	 */
	private _PagerBody printGoods(Map<String, Object> goodMap, Goods goodTitle) {
		
		_PagerBody pagerBody = new _PagerBody();
		//根据variable替换字段

		pagerBody.setContent(addBlank(goodMap.get(goodTitle.getVariable())+"", goodTitle.getWidth()))
					.setFeeLine(false)
					.setFontSize(goodTitle.getSize())
					.isBold(goodTitle.isBold());
		
		return pagerBody;
	}

	/**

	 * 打印列表标题

	 * @param goods

	 * @return

	 */
	private _PagerBody printTitle(Goods goods) {
		_PagerBody pagerBody = new _PagerBody();
		pagerBody.setContent(addBlank(goods.getName(), goods.getWidth()))
					.setFeeLine(false)
					.isBold(false);
		return pagerBody;
	}

	/**  编码类型：GBK  */
	private static String encoding = "GBK";
	
	/**

	 * 填充文本

	 * @param str		文本

	 * @param length	字节长度

	 * @return

	 */
	private static String addBlank(String str, int length) {
		try {
			int len = str.getBytes(encoding).length;
			if (len > length) {
				return str.substring(0, length > str.length() ? str.length() : length);
			}
			for (int i = 0; i < length - len; i++) {
				str += " ";
			}
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return str;
	}

}