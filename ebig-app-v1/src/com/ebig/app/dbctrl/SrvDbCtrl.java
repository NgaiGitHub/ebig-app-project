package com.ebig.app.dbctrl;

import java.util.Map;

import com.ebig.app.App;
import com.ebig.app.NetInfo;
import com.ebig.app.R;
import com.ebig.app.utils.LogUtil;
import com.ebig.net.client.HttpPacket;

/**
 * û��,���Ƿ���AppSrv�����ݿ�����,�����˷�����������������,�������ݷ��ʿ�����,���ü̳С�
 * @author HungLau.Ngai
 *
 */
public class SrvDbCtrl {
	
	static private String setLogTag() {
		return SrvDbCtrl.class.getName();
	}

	static protected HttpPacket getPacket(String srvtype) {
		HttpPacket mPacket = new HttpPacket();
//		mPacket.setSrvtype(srvtype+"_"+App.getAccount().getEntryId()); 
		mPacket.setSrvtype(srvtype); 
		mPacket.setParameter(null);
		return mPacket;
	}
	/**
	 * ���ư�,��������������Ǿ���Ȩ�޹��˵�,��������������,ȡ�������
	 * @param srvtype
	 * @return
	 */
	static protected HttpPacket getPacketLimit(String srvtype) {
		HttpPacket mPacket = new HttpPacket();
		mPacket.setSrvtype(srvtype+"_"+App.getAccount().getEntryId()); 
//		mPacket.setSrvtype(srvtype); 
		return mPacket;
	}
	
	static protected Object requestSrv(HttpPacket packet) throws Exception {
		if (!NetInfo.isEnable())// ���粻����,����!!
			throw new RuntimeException(App.getContext().getString(
					R.string.app_cannot_connect_srv));
		
		LogUtil.Log(setLogTag(), "����:"+packet.getInparams().toString());
		
		packet = NetInfo.getHttpClient().send(packet);
		Object rsObj = packet.getOutresult();
		
		if(rsObj.toString().equals("{}"))// ����������Ϊ��ʱ,����,������Ʊ���,���Բ������ϡ�
			throw new RuntimeException(App.getContext().getString(R.string.srv_response_null));
		   // rsObj = null;

		LogUtil.Log(setLogTag(), "���:"+rsObj.toString());
		
		return rsObj;
	}
	
	static protected Object genVersionInfo() throws Exception {
		return NetInfo.getHttpClient().genVersionInfo();
	}
	
	static protected Object registerAccount(Map<String, String> httpParams) throws Exception {
		return NetInfo.getHttpClient().register(httpParams);
	}
	
	static protected Object genProjectList() throws Exception {
		return NetInfo.getHttpClient().genPojects();
	}
//	static protected Object limitRequestSrv(HttpPacket packet) throws Exception {
//		if (!NetInfo.isEnable())// ���粻����,����!!
//			throw new RuntimeException(App.getContext().getString(
//					R.string.curr_netinfo_isnuable));
//		
//		LogUtils.Log(setLogTag(), "����:"+packet.getInparams().toString());
//		
//		packet = NetInfo.getHttpClient().send(packet);
//		Object rsObj = packet.getOutresult();
//		
//		if(rsObj.toString().equals("{}"))// ����������Ϊ��ʱ,����,������Ʊ���,���Բ������ϡ�
//			throw new RuntimeException(App.getContext().getString(R.string.srv_response_null));
//		   // rsObj = null;
//
//		LogUtils.Log(setLogTag(), "���:"+rsObj.toString());
//		
//		return rsObj;
//	}
	
}
