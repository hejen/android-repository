package com.kqhelper.db;

import java.util.List;
import java.util.Map;

import android.content.Context;

public class WorkListManager {

	private DbManager dbManager;
	
	public WorkListManager(Context context){
		this.dbManager = new DbManager(context);
	}
	
	public List<Map> getAllWorkList(){
		return dbManager.query("select * from CO_WorkList");
	}
	
	public List<Map> getWorkListByIds(String... ids){
		StringBuffer sql = new StringBuffer("select * from CO_WorkList where 1=1 cWorkid in (");
		for (int i=0;i<ids.length;i++){
			sql.append("?,");
		}
		sql.deleteCharAt(sql.length()-1).append(")");
		return dbManager.query(sql.toString(), ids);
	}
}
