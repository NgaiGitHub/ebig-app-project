package com.ebig.app.model;

/**
 * 权限类
 * 
 * @version 1.0
 */
public class Functions {

	private String accountId;

	private String moduleId;

	private String modulePic;

	private String moduleName;

	private String controlNO;

	private String moduleClass;

	private String isShortcuts;// 是否为快捷功能... isshortcuts

	public Functions() {
		super();
	}

	public Functions(String moduleId, String modulePic, String moduleName,
			String controlNO, String moduleClass, String isShortcuts) {
		super();
		this.moduleId = moduleId;
		this.modulePic = modulePic;
		this.moduleName = moduleName;
		this.controlNO = controlNO;
		this.moduleClass = moduleClass;
		this.isShortcuts = isShortcuts;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public String getModulePic() {
		return modulePic;
	}

	public void setModulePic(String modulePic) {
		this.modulePic = modulePic;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getControlNO() {
		return controlNO;
	}

	public void setControlNO(String controlNO) {
		this.controlNO = controlNO;
	}

	public String getModuleClass() {
		return moduleClass;
	}

	public void setModuleClass(String moduleClass) {
		this.moduleClass = moduleClass;
	}

	public String getIsShortcuts() {
		return isShortcuts;
	}

	public boolean isShortcuts() {
		return Boolean.valueOf(isShortcuts);
	}

	public void setIsShortcuts(String isShortcuts) {
		this.isShortcuts = isShortcuts;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	@Override
	public String toString() {
		return "ModuleView [accountId=" + accountId + ", moduleId=" + moduleId
				+ ", modulePic=" + modulePic + ", moduleName=" + moduleName
				+ ", controlNO=" + controlNO + ", moduleClass=" + moduleClass
				+ ", isShortcuts=" + isShortcuts + "]";
	}

}
