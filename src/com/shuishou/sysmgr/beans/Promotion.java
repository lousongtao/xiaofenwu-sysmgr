package com.shuishou.sysmgr.beans;

public class Promotion {

	private int id;
	
	private boolean forbidMemberDiscount = true;
	
	/**
	 * 商品的类型:
	 * 1: 商品
	 * 2: 二级目录
	 * 3: 一级目录
	 */
	private int objectAType;
	
	private int objectAId;
	
	private int objectAQuantity;
	
	private int objectBType;
	
	private int objectBId;
	
	private int objectBQuantity;
	
	/**
	 * 奖励方式
	 * 1: 买n个A直接折扣x元
	 * 2: 买n个A后, 第n+1个A折扣x元
	 * 3: 买n个A后, 第n+1个A折扣x%
	 * 4: 买n个A, 给B一个折扣, 此时B的内容为非空, 直接金额折扣
	 * 5: 买n个A, 给B一个百分比折扣
	 */
	private int rewardType;
	
	/**
	 * 奖励金额
	 * 1.	当reward_type为直接折扣时, 该值为特定的价格金额
	 * 2.	当reward_type为折扣金额时, 这是个百分比值
	 */
	private double rewardValue;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isForbidMemberDiscount() {
		return forbidMemberDiscount;
	}

	public void setForbidMemberDiscount(boolean forbidMemberDiscount) {
		this.forbidMemberDiscount = forbidMemberDiscount;
	}

	public int getObjectAType() {
		return objectAType;
	}

	public void setObjectAType(int objectAType) {
		this.objectAType = objectAType;
	}

	public int getObjectAId() {
		return objectAId;
	}

	public void setObjectAId(int objectAId) {
		this.objectAId = objectAId;
	}

	public int getObjectAQuantity() {
		return objectAQuantity;
	}

	public void setObjectAQuantity(int objectAQuantity) {
		this.objectAQuantity = objectAQuantity;
	}

	public int getObjectBType() {
		return objectBType;
	}

	public void setObjectBType(int objectBType) {
		this.objectBType = objectBType;
	}

	public int getObjectBId() {
		return objectBId;
	}

	public void setObjectBId(int objectBId) {
		this.objectBId = objectBId;
	}

	public int getObjectBQuantity() {
		return objectBQuantity;
	}

	public void setObjectBQuantity(int objectBQuantity) {
		this.objectBQuantity = objectBQuantity;
	}

	public int getRewardType() {
		return rewardType;
	}

	public void setRewardType(int rewardType) {
		this.rewardType = rewardType;
	}

	public double getRewardValue() {
		return rewardValue;
	}

	public void setRewardValue(double rewardValue) {
		this.rewardValue = rewardValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Promotion other = (Promotion) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Promotion [forbidMemberDiscount=" + forbidMemberDiscount + ", objectAType=" + objectAType
				+ ", objectAId=" + objectAId + ", objectAQuantity=" + objectAQuantity + ", objectBType=" + objectBType
				+ ", objectBId=" + objectBId + ", objectBQuantity=" + objectBQuantity + ", rewardType=" + rewardType
				+ ", rewardValue=" + rewardValue + "]";
	}
	
	
}
