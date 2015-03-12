package com.ebig.app.custom.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class EbigSpinner extends Spinner {
	
	private List key_value_list;
	private List value_list;

	public EbigSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * eibg setAdapter
	 * @param list
	 */
	public void setAdapter(Context context,List list) {
		if(list == null) return ;
		key_value_list = list;
		if(key_value_list != null){
			value_list = new ArrayList();
			for (int i = 0; i < key_value_list.size(); i++) {
				Map map = (Map) key_value_list.get(i);
				value_list.add(map.get("value"));
			}
		}
		ArrayAdapter<String> adapterQuality = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, value_list);  //初始化Spinner样式适配器
		adapterQuality.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // 使用系统内置下拉菜单样式
		
		super.setAdapter(adapterQuality);
	}
	
	public String getItemKey() {
		int p = getSelectedItemPosition();
		Map map = (Map) key_value_list.get(p);
		String value = (String) map.get("key");
		return value;
	}

	public String getItemValue() {
		int p = getSelectedItemPosition();
		Map map = (Map) key_value_list.get(p);
		String value = (String) map.get("value");
		return value;
	}
}
