package com.ebig.app.dbctrl;

import java.util.Map;

import com.ebig.app.App;
import com.ebig.app.NetInfo;
import com.ebig.app.R;
import com.ebig.app.utils.LogUtil;
import com.ebig.net.client.HttpPacket;

/**
 * 没错,就是访问AppSrv的数据控制器,构成了访问网络的最基本方法,所有数据访问控制器,都得继承。
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
	 * 限制包,当这个功能请求是经过权限过滤的,必须调用这个方法,取请求包。
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
		if (!NetInfo.isEnable())// 网络不可用,报错!!
			throw new RuntimeException(App.getContext().getString(
					R.string.app_cannot_connect_srv));
		
		LogUtil.Log(setLogTag(), "参数:"+packet.getInparams().toString());
		
		packet = NetInfo.getHttpClient().send(packet);
		Object rsObj = packet.getOutresult();
		
		if(rsObj.toString().equals("{}"))// 服务器返回为空时,报错,这个机制保留,可以不会用上。
			throw new RuntimeException(App.getContext().getString(R.string.srv_response_null));
		   // rsObj = null;

		LogUtil.Log(setLogTag(), "结果:"+rsObj.toString());
		
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
//		if (!NetInfo.isEnable())// 网络不可用,报错!!
//			throw new RuntimeException(App.getContext().getString(
//					R.string.curr_netinfo_isnuable));
//		
//		LogUtils.Log(setLogTag(), "参数:"+packet.getInparams().toString());
//		
//		packet = NetInfo.getHttpClient().send(packet);
//		Object rsObj = packet.getOutresult();
//		
//		if(rsObj.toString().equals("{}"))// 服务器返回为空时,报错,这个机制保留,可以不会用上。
//			throw new RuntimeException(App.getContext().getString(R.string.srv_response_null));
//		   // rsObj = null;
//
//		LogUtils.Log(setLogTag(), "结果:"+rsObj.toString());
//		
//		return rsObj;
//	}
	
}
