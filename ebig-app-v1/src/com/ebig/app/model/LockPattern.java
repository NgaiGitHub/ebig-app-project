package com.ebig.app.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "LockPattern")
public class LockPattern extends Model {
	
	@Column(name = "accountId")
	private String accountId;
	
	@Column(name = "accountPassword")
	private String accountPassword;
	
	@Column(name = "islock")
	private boolean islock;
	
	@Column(name = "encryptionStr")
	private String encryptionStr;

	public String getAccountId() {
		return accountId;
	}

	public String getAccountPassword() {
		return accountPassword;
	}

	public void setAccountPassword(String accountPassword) {
		this.accountPassword = accountPassword;
	}

	public boolean isIslock() {
		return islock;
	}

	public void setIslock(boolean islock) {
		this.islock = islock;
	}

	public String getEncryptionStr() {
		return encryptionStr;
	}

	public void setEncryptionStr(String encryptionStr) {
		this.encryptionStr = encryptionStr;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	} 
	
	@Override
	public String toString() {
		return "LockPattern [accountId=" + accountId + ", accountPassword="
				+ accountPassword + ", islock=" + islock + ", encryptionStr="
				+ encryptionStr + "]";
	}
	
	
}
