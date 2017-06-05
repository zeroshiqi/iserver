package cn.ichazuo.model.app;

import java.io.Serializable;

public class UserInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private String mobile;
	private String weixin;
	private String sex;
	private String company;	
	private String weixinNickName;
	private String job;
	private String buyIntentions;
	private String joinReason;
	private String invoiceType;
	private String invoiceTitle;
	private String invoiceAddress;
	private String invoiceStatus;
	private String invoiceRemarks;
	private String invoiceName;
	private String invoiceMobile;
	private String email;
	private String isGift;
	private String ifPurchaser;
	private String unionId;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getInvoiceTitle() {
		return invoiceTitle;
	}

	public void setInvoiceTitle(String invoiceTitle) {
		this.invoiceTitle = invoiceTitle;
	}

	public String getInvoiceAddress() {
		return invoiceAddress;
	}

	public void setInvoiceAddress(String invoiceAddress) {
		this.invoiceAddress = invoiceAddress;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getBuyIntentions() {
		return buyIntentions;
	}

	public void setBuyIntentions(String buyIntentions) {
		this.buyIntentions = buyIntentions;
	}

	public String getJoinReason() {
		return joinReason;
	}

	public void setJoinReason(String joinReason) {
		this.joinReason = joinReason;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getWeixin() {
		return weixin;
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getWeixinNickName() {
		return weixinNickName;
	}

	public void setWeixinNickName(String weixinNickName) {
		this.weixinNickName = weixinNickName;
	}

	public String getIsGift() {
		return isGift;
	}

	public void setIsGift(String isGift) {
		this.isGift = isGift;
	}

	public String getIfPurchaser() {
		return ifPurchaser;
	}

	public void setIfPurchaser(String ifPurchaser) {
		this.ifPurchaser = ifPurchaser;
	}

	public String getUnionId() {
		return unionId;
	}

	public void setUnionId(String unionId) {
		this.unionId = unionId;
	}

	public String getInvoiceStatus() {
		return invoiceStatus;
	}

	public void setInvoiceStatus(String invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}

	public String getInvoiceRemarks() {
		return invoiceRemarks;
	}

	public void setInvoiceRemarks(String invoiceRemarks) {
		this.invoiceRemarks = invoiceRemarks;
	}

	public String getInvoiceName() {
		return invoiceName;
	}

	public void setInvoiceName(String invoiceName) {
		this.invoiceName = invoiceName;
	}

	public String getInvoiceMobile() {
		return invoiceMobile;
	}

	public void setInvoiceMobile(String invoiceMobile) {
		this.invoiceMobile = invoiceMobile;
	}
	
}
