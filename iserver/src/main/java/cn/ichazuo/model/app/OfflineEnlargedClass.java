package cn.ichazuo.model.app;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.ichazuo.commons.base.BaseResult;
import cn.ichazuo.commons.util.DateUtils;

/**
 * @ClassName: OfflineEnlargedClass
 * @Description: (线下大课返回信息)
 * @author ZhaoXu
 * @date 2015年7月19日 下午12:19:39
 * @version V1.0
 */
public class OfflineEnlargedClass extends BaseResult {
	private static final long serialVersionUID = 1L;
	private String vipId; // 区域VIP对应课程Id
	private String AId; // 区域A对应课程Id
	private String BId; //  区域B对应课程Id
	private String CId; //  区域C对应课程Id
	private String DId; //  区域D对应课程Id
	private String AIsOver; // A区是否名额已满
	private String vipIsOver; // vip区是否名额已满
	private String BIsOver; //B区是否名额已满
	private String CIsOver; // C区是否名额已满
	private String DIsOver; // D区是否名额已满
	private String AIsOpen; // A区是否开放购买
	private String vipIsOpen; // vip区是否开放购买
	private String BIsOpen; //B区是否开放购买
	private String CIsOpen; // C区是否开放购买
	private String DIsOpen; // D区是否开放购买
	
	private float vipPrice; // 区域VIP对应课程价格
	private float APrice; // 区域A对应课程价格
	private float BPrice; //  区域B对应课程价格
	private float CPrice; //  区域C对应课程价格
	private float DPrice; //  区域D对应课程价格
	public String getVipId() {
		return vipId;
	}
	public void setVipId(String vipId) {
		this.vipId = vipId;
	}
	public String getAId() {
		return AId;
	}
	public void setAId(String aId) {
		AId = aId;
	}
	public String getBId() {
		return BId;
	}
	public void setBId(String bId) {
		BId = bId;
	}
	public String getCId() {
		return CId;
	}
	public void setCId(String cId) {
		CId = cId;
	}
	public String getDId() {
		return DId;
	}
	public void setDId(String dId) {
		DId = dId;
	}
	public String getAIsOver() {
		return AIsOver;
	}
	public void setAIsOver(String aIsOver) {
		AIsOver = aIsOver;
	}
	public String getVipIsOver() {
		return vipIsOver;
	}
	public void setVipIsOver(String vipIsOver) {
		this.vipIsOver = vipIsOver;
	}
	public String getBIsOver() {
		return BIsOver;
	}
	public void setBIsOver(String bIsOver) {
		BIsOver = bIsOver;
	}
	public String getCIsOver() {
		return CIsOver;
	}
	public void setCIsOver(String cIsOver) {
		CIsOver = cIsOver;
	}
	public String getDIsOver() {
		return DIsOver;
	}
	public void setDIsOver(String dIsOver) {
		DIsOver = dIsOver;
	}
	public String getAIsOpen() {
		return AIsOpen;
	}
	public void setAIsOpen(String aIsOpen) {
		AIsOpen = aIsOpen;
	}
	public String getVipIsOpen() {
		return vipIsOpen;
	}
	public void setVipIsOpen(String vipIsOpen) {
		this.vipIsOpen = vipIsOpen;
	}
	public String getBIsOpen() {
		return BIsOpen;
	}
	public void setBIsOpen(String bIsOpen) {
		BIsOpen = bIsOpen;
	}
	public String getCIsOpen() {
		return CIsOpen;
	}
	public void setCIsOpen(String cIsOpen) {
		CIsOpen = cIsOpen;
	}
	public String getDIsOpen() {
		return DIsOpen;
	}
	public void setDIsOpen(String dIsOpen) {
		DIsOpen = dIsOpen;
	}
	public float getVipPrice() {
		return vipPrice;
	}
	public void setVipPrice(float vipPrice) {
		this.vipPrice = vipPrice;
	}
	public float getAPrice() {
		return APrice;
	}
	public void setAPrice(float aPrice) {
		APrice = aPrice;
	}
	public float getBPrice() {
		return BPrice;
	}
	public void setBPrice(float bPrice) {
		BPrice = bPrice;
	}
	public float getCPrice() {
		return CPrice;
	}
	public void setCPrice(float cPrice) {
		CPrice = cPrice;
	}
	public float getDPrice() {
		return DPrice;
	}
	public void setDPrice(float dPrice) {
		DPrice = dPrice;
	}
	
}
