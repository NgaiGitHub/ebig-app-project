package com.ebig.app.model;

import java.util.Date;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Account")
public class Account extends Model{

	@Column(name = "accountId")
	private String accountId; // µ«¬Ω’À∫≈ID
	
	@Column(name = "accountName")
	private String accountName;// ’ ∫≈√˚≥∆
	
	@Column(name = "accountPassword")
	private String accountPassword;// √‹¬Î
	 
	@Column(name = "accountStatus") 
	private Long accountStatus;
	 
	@Column(name = "personId")
	private Long personId;
	
	@Column(name = "personname")
	private String personname;
	
	@Column(name = "lastLoginDate")
	private Date lastLoginDate;
	
	@Column(name = "lastLoginIp")
	private String lastLoginIp;
	
	@Column(name = "lastLoginArea")
	private String lastLoginArea;
 
	@Column(name = "accountInvalidDate")
	private Date accountInvalidDate; // Ωÿ÷π»’∆⁄

	@Column(name = "entryId")
	private String entryId;
	@Column(name = "accountPic")
	private String accountPic;
	@Column(name = "gesturePassword")
	private String gesturePassword;//  ÷ ∆√‹¬Î

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountPassword() {
		return accountPassword;
	}

	public void setAccountPassword(String accountPassword) {
		this.accountPassword = accountPassword;
	}

	public Long getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(Long accountStatus) {
		this.accountStatus = accountStatus;
	}

	public Long getPersonId() {
		return personId;
	}

	public void setPersonId(Long personId) {
		this.personId = personId;
	}

	public String getPersonname() {
		return personname;
	}

	public void setPersonname(String personname) {
		this.personname = personname;
	}

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	public String getLastLoginArea() {
		return lastLoginArea;
	}

	public void setLastLoginArea(String lastLoginArea) {
		this.lastLoginArea = lastLoginArea;
	}

	public Date getAccountInvalidDate() {
		return accountInvalidDate;
	}

	public void setAccountInvalidDate(Date accountInvalidDate) {
		this.accountInvalidDate = accountInvalidDate;
	}

	public String getEntryId() {
		return entryId;
	}

	public void setEntryId(String entryId) {
		this.entryId = entryId;
	}

	public String getAccountPic() {
		return accountPic;
	}

	public void setAccountPic(String accountPic) {
		this.accountPic = accountPic;
	}

	public String getGesturePassword() {
		return gesturePassword;
	}

	public void setGesturePassword(String gesturePassword) {
		this.gesturePassword = gesturePassword;
	}

	@Override
	public String toString() {
		return "Account [accountId=" + accountId + ", accountName="
				+ accountName + ", accountPassword=" + accountPassword
				+ ", accountStatus=" + accountStatus + ", personId=" + personId
				+ ", personname=" + personname + ", lastLoginDate="
				+ lastLoginDate + ", lastLoginIp=" + lastLoginIp
				+ ", lastLoginArea=" + lastLoginArea + ", accountInvalidDate="
				+ accountInvalidDate + ", entryId=" + entryId + ", accountPic="
				+ accountPic + ", gesturePassword=" + gesturePassword + "]";
	}

	
	
	
	
}
