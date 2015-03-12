package com.ebig.app.dbctrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ebig.app.App;
import com.ebig.app.utils.JSONUtil;
import com.ebig.net.client.HttpPacket;

public class SalBackDbCtrl extends SrvDbCtrl {

	private List mSelGoodsList;// ��ѡ��Ʒ�б�
	private Map tSelGoodsMaps;// ��ʱ�����Ʒ��Ϣ,��ȷ��ʱ�������ɶ�����Ϣ��put("saldtlid",map)
	
	private Map mDisptypeMap;
	
	public Object querySalBackGoods(int page,String warid,String companyno,String goodsno){
		HttpPacket mPacket = getPacketLimit(SrvType.SALBACK_GOODS_QUERY_SRV);
		mPacket.setParameter(String.valueOf(page));
		mPacket.setParameter(warid);
		mPacket.setParameter(companyno);
		mPacket.setParameter(goodsno);
		Object object;
		try {
			object = requestSrv(mPacket);
		} catch (Exception e) {
			return e;
		}
		return object;
	}
	
	public Map getGoodsForSalBack(){
		if(tSelGoodsMaps == null)
			tSelGoodsMaps = new HashMap();
		return tSelGoodsMaps;
	}
	
	/**
	 * ���ɴ��˻�������Ʒ���ύ������Ϣ
	 * @return
	 */
	public List creatGoodsForSalBack(){
		List tList = new ArrayList();
		
		Map tHashMap = new HashMap(getGoodsForSalBack());
		Iterator iter = tHashMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Object val = entry.getValue();
			if(val instanceof Map){				
				((Map)val).remove("html");
				tList.add(val);
			}
		}
		return tList;
	}
	
	/**
	 * ��ȡ�û���ѡ����Ʒ��Ϣ�б�
	 * @return
	 */
	public List getGoodsForSabBack2List() {
		List tList = new ArrayList();
		Iterator iter = getGoodsForSalBack().entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			//Object key = entry.getKey();
			Object val = entry.getValue();
			tList.add(val);
		}
		return tList;
	}
	
	/**
	 * ����Ʒ��ӵ��˻��������
	 */
	public void addGoodsForSalBack(Map map){
		Map tmMap = new HashMap();
		tmMap.putAll(map);
		//tmMap.remove("html"); ����Ҫ��ʾ,��ʱ��ɾ��
		tmMap.put("checkbox", true);
		String saldtlid = (String) tmMap.get("saldtlid");
		
		getGoodsForSalBack().put(saldtlid, tmMap);
		//getGoodsForSalBack()
	}

	public List getDisptypeItem(String upperCase){
		return (List) mDisptypeMap.get(upperCase);
	}
	public void queryDisptypeItem(String upperCase) {
		HttpPacket mPacket = getPacketLimit(SrvType.DISPTYPE_ITEM_QUERY_SRV);
		mPacket.setParameter(App.getAccount().getAccountId());
		mPacket.setParameter(upperCase);
		 if(mDisptypeMap == null)
			 mDisptypeMap = new HashMap();
		try {
			Object object = requestSrv(mPacket);
			mDisptypeMap.put(upperCase, JSONUtil.json2List(object.toString())); 
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} 
	}

	/**
	 * �ύ��������,���������˻����뵥
	 * @param creatGoodsForSalBack
	 */
	public boolean commitGoodsForSalBack(List creatGoodsForSalBack) {
		HttpPacket mPacket = getPacketLimit(SrvType.SALBACK_SRV);
		mPacket.setParameter(JSONUtil.toJSONString(creatGoodsForSalBack));
		try {
			Object object = requestSrv(mPacket);
			if(!"true".equals(object.toString())){
				// [{"android.errormsg":"ԭ��(214),��Ʒ(�㹽����ע��Һ)���˻������������Գ�����������"}]
				List rsList = JSONUtil.json2List(object.toString());
				Map rsMap = (Map) rsList.get(0);
				throw new RuntimeException(rsMap.get("android.errormsg").toString());
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return true;
	}
	
}
