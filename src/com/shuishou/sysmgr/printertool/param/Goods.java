package com.shuishou.sysmgr.printertool.param;

/**
 * 商品属性配置参数
 * @author zhulinfeng
 * @时间 2016年9月23日下午1:00:59
 *
 */
public class Goods {

    // 属性名称
    private String name;
    // 对齐方式 居左、居中、居右
    private int format;
    // 占半角字符宽度 58mm 每行32 80mm 每行48
    private int width;
    // 占位符 格式${time}
    private String variable;
    
    private boolean bold;
    
    private int size;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFormat() {
        return format;
    }

    public void setFormat(int format) {
        this.format = format;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

	public boolean isBold() {
		return bold;
	}

	public void setBold(boolean bold) {
		this.bold = bold;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
    
    
}