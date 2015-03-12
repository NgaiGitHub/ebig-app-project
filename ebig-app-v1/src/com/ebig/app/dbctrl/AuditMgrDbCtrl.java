package com.ebig.app.dbctrl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ebig.net.client.HttpPacket;
import com.ebig.pub.DateUtil;
import com.ebig.utils.JSONUtils;

public class AuditMgrDbCtrl extends SrvDbCtrl {
	
	private static List auditGroups;

	/**
	 * 清除内存数据,所有DbCtrl必须包此方法
	 */
	static public void clear(){
		if(auditGroups != null)
			auditGroups.clear();
	}

	/**
	 * 当网络访问正常返回List=[{pintypename:"",pintype:""},{}.....] <br />
	 * 否则返回Exception <br />
	 * @return
	 */
	static public Object getAuditGroups() {
		if(auditGroups != null && auditGroups.size()>0)
			return auditGroups;
		
		HttpPacket mHttpPacket = getPacketLimit(SrvType.AUDITING_PINTYPE_SRV);
		Object obj = null;
		try {
			obj = requestSrv(mHttpPacket);
		} catch (Exception e) {
			return e;
		}
		if(obj == null)
			return null;
		
		if(auditGroups == null)
			auditGroups = new ArrayList();
		
		auditGroups = JSONUtils.json2List(obj.toString());
		Map tMap = new HashMap();
		tMap.put("pintypename", "全部");
		tMap.put("pintype", "");
		auditGroups.add(tMap);
		
		// 补全空位
		int size = auditGroups.size() % 3;
		if (size != 0) {
			size = 3 - size;
			for (int i = 0; i < size; i++) {
				auditGroups.add(null);
			}
		}
		return auditGroups;
	}

	/**
	 * 
	 * @param page
	 * @param pintype
	 * @param begindate
	 * @param enddate
	 * @param flag  标志 待审批信息附 1 , 历史信息附 2
	 * @return
	 */
	public static Object getAuditRecord(int page, String pintype,
			Date begindate, Date enddate, String flag){
		HttpPacket mHttpPacket = getPacketLimit(SrvType.AUDITING_QUERY_SRV);
		mHttpPacket.setParameter(page);
		mHttpPacket.setParameter(pintype);
		mHttpPacket.setParameter(DateUtil.format(begindate));
		mHttpPacket.setParameter(DateUtil.format(enddate));
		mHttpPacket.setParameter(flag);
		Object obj = null;
		try {
			obj = requestSrv(mHttpPacket);
		} catch (Exception e) { 
			return e;
		}
		List list = null;
		if(obj != null){
			list  = JSONUtils.json2List(obj.toString());
		}
		return list;
	}
 
	public static  Object auditOperSrv(String attrid, String pintype, String checktext,
			boolean checkresult) {
		HttpPacket mHttpPacket = getPacketLimit(SrvType.AUDITING_OPER_SRV);
		mHttpPacket.setParameter(attrid);
		mHttpPacket.setParameter(pintype);
		mHttpPacket.setParameter(checktext);
		mHttpPacket.setParameter(checkresult); 
		try {
			return requestSrv(mHttpPacket);
		} catch (Exception e) {
			return e;
		}
	}

	public static  Object getAuditDtlInfoSrv(int i, String attrid, String sourceid,
			String pintype) {
		HttpPacket mHttpPacket = getPacketLimit(SrvType.AUDITING_DTLQUERY_SRV);
		if(attrid == null) attrid = "";
		mHttpPacket.setParameter(attrid);
		mHttpPacket.setParameter(pintype);
		mHttpPacket.setParameter(sourceid);
		try {
			return requestSrv(mHttpPacket);
		} catch (Exception e) {
			return e;
		}
	}
	
	public static  Object getCusInfoSrv(String customno) {
		HttpPacket mHttpPacket = getPacketLimit(SrvType.CUS_INFO_SRV);
		mHttpPacket.setParameter(customno);
		try {
			return requestSrv(mHttpPacket);
		} catch (Exception e) {
			return e;
		}
	}
}
