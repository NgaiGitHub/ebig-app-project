package com.ebig.app.dbctrl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ebig.app.App;
import com.ebig.net.client.HttpPacket;
import com.ebig.pub.DateUtil;
import com.ebig.utils.JSONUtils;

public class StockQueryDbCtrl extends SrvDbCtrl {
	private final String GOODS_SEARCH_SRV = "GoodsQuerySrv_";
	private final String GOODSDTL_SEARCH_SRV = "GoodsDtlQuerySrv_";
	private final String STOCKQUERY_LEVEL_ONE_LIST_SRV = "GoodsQueryGroupOneListSrv_";
	private final String STOCKQUERY_LEVEL_TWO_LIST_SRV = "GoodsQueryGroupTwoListSrv_";
	
	public static List<Object> goodsItems;
	public static List<Object> goodsDtlItems;
	public static List<Object> mGoodsPriceSalRecordItems;
	private static List<Object> sortLevelOneList;
	private static List<Object> sortLevelTwoList;
	
	/**
	 * 清除内存数据,所有DbCtrl必须包此方法
	 */
	static public void clear(){
		if(goodsItems != null){			
			goodsItems.clear();
		}
		if(goodsDtlItems != null){
			goodsDtlItems.clear();
		}
		if(mGoodsPriceSalRecordItems != null){
			mGoodsPriceSalRecordItems.clear();
		}
		if(sortLevelOneList != null){
			sortLevelOneList.clear();
		}
		if(sortLevelTwoList != null){
			sortLevelTwoList.clear();
		}
	}
	
	/**
	 * 查询商品信息
	 * @param page
	 * @param groupid
	 * @param salbuy
	 * @param newfalg
	 * @param searchtext
	 * @return [{"goodsid":"36088","goodsname":"格列齐特片","goodspic":"-","html":"<font size=1>…</font>","mobile":""},…]
	 */
	@SuppressWarnings("unchecked")
	public List<Object> goodsQuerySrv(int page, String groupid, String salbuy,
			String newfalg, String searchtext) {
		HttpPacket mHttpPacket = new HttpPacket();
		mHttpPacket.setSrvtype(GOODS_SEARCH_SRV + App.getAccount().getEntryId());
		mHttpPacket.setParameter(page);
		mHttpPacket.setParameter(groupid);
		mHttpPacket.setParameter(salbuy);
		mHttpPacket.setParameter(newfalg);
		mHttpPacket.setParameter(searchtext);
		try {
			Object obj = requestSrv(mHttpPacket);
			if(obj == null)
				return null;
			if(goodsItems == null){
				goodsItems = new ArrayList<Object>();
			}
			goodsItems = JSONUtils.json2List(obj.toString());
			return goodsItems;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/**
	 * 查询商品详细信息
	 * @param goodsid
	 * @return [{"ditchno":"1796","html":"<font color='#999999'>…<br/>"},…]
	 */
	@SuppressWarnings("unchecked")
	public List<Object> goodsDtlQuerySrv(String goodsid) {
		HttpPacket mHttpPacket = new HttpPacket();
		mHttpPacket.setSrvtype(GOODSDTL_SEARCH_SRV + App.getAccount().getEntryId());
		mHttpPacket.setParameter(goodsid);
		try {
			Object obj = requestSrv(mHttpPacket);
			if(obj == null)
				return null;
			if(goodsDtlItems == null){
				goodsDtlItems = new ArrayList<Object>();
			}
			goodsDtlItems = JSONUtils.json2List(obj.toString());
			return goodsDtlItems;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/**
	 * 查询商品流向
	 * @param page
	 * @param goodsid
	 * @param channelno
	 * @param customer
	 * @param begindate
	 * @param enddate
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Object goodsPriceSalRecordSrv(int page,
			String goodsid, String channelno,String customer, Date begindate, Date enddate) {
		HttpPacket mHttpPacket = new HttpPacket();
		mHttpPacket.setSrvtype("GoodsSalQuerySrv_"+App.getAccount().getEntryId());
		mHttpPacket.setParameter(page);// 获取全局上下文,一般请求带上
		mHttpPacket.setParameter(goodsid);
		mHttpPacket.setParameter(channelno);
		mHttpPacket.setParameter(customer);
		
		mHttpPacket.setParameter(DateUtil.format(begindate));
		mHttpPacket.setParameter(DateUtil.format(enddate));
		try {
			Object obj = requestSrv(mHttpPacket);
			if(obj == null)
				return null;
			if(mGoodsPriceSalRecordItems == null){
				mGoodsPriceSalRecordItems = new ArrayList<Object>();
			}
			mGoodsPriceSalRecordItems = JSONUtils.json2List(obj.toString());
			return mGoodsPriceSalRecordItems;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/**
	 * 查询一级商品分类
	 * @return [{"groupid":"6409","groupname":"中药材","grouppic":"","haschildflag":"0","rownum_":"1"},…]
	 */
	public List<Object> stockQueryFirstLevelListSrv() {
		HttpPacket mHttpPacket = new HttpPacket();
		mHttpPacket.setSrvtype(STOCKQUERY_LEVEL_ONE_LIST_SRV + App.getAccount().getEntryId());
		//return connServer(mHttpPacket, "stockQueryLevelOneListSrv");
		try {
			Object obj = requestSrv(mHttpPacket);
			if(obj == null)
				return null;
			if(sortLevelOneList == null){
				sortLevelOneList = new ArrayList<Object>();
			}
			sortLevelOneList = JSONUtils.json2List(obj.toString());
			return sortLevelOneList;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/**
	 * 查询二级商品分类
	 * @param groupid
	 * @return
	 */
	public List<Object> stockQuerySecondLevelListSrv(String groupid) {
		if(groupid == null || "".equals(groupid)) return null;
		
		HttpPacket mHttpPacket = new HttpPacket();
		mHttpPacket.setSrvtype(STOCKQUERY_LEVEL_TWO_LIST_SRV + App.getAccount().getEntryId());
		mHttpPacket.setParameter(groupid);
		//return connServer(mHttpPacket, "stockQueryLevelTwoListSrvStatic");
		try {
			Object obj = requestSrv(mHttpPacket);
			if(obj == null)
				return null;
			if(sortLevelTwoList == null){
				sortLevelTwoList = new ArrayList<Object>();
			}
			sortLevelTwoList = JSONUtils.json2List(obj.toString());
			return sortLevelTwoList;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
