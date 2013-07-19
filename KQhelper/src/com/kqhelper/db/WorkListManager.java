package com.kqhelper.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

public class WorkListManager {

	private DbManager dbManager;
	
	public WorkListManager(Context context){
		this.dbManager = new DbManager(context);
	}
	
	public List<Map> getAllWorkList(){
		return dbManager.query("select wl.*, wt.cName cWorkTypeName from CO_WorkList wl join CO_WorkType wt on wt.cTypeid=wl.cWorkType");
	}
	
	public List<Map> getAllValidWorkList(){
		return dbManager.query("select wl.*, wt.cName cWorkTypeName from CO_WorkList wl join CO_WorkType wt on wt.cTypeid=wl.cWorkType where iStatus=1");
	}
	
	public List<Map> getWorkListByIds(String... ids){
		StringBuffer sql = new StringBuffer("select wl.*,wt.cName cWorkTypeName from CO_WorkList wl join CO_WorkType wt on wt.cTypeid=wl.cWorkType where wl.cWorkid in (");
		for (int i=0;i<ids.length;i++){
			sql.append("?,");
		}
		sql.deleteCharAt(sql.length()-1).append(")");
		return dbManager.query(sql.toString(), ids);
	}
	
	public List<Map> getAllWorkType(){
		return dbManager.query("select * from CO_WorkType");
	}
	
	public void saveQQCard(Map qqcard){
		String workTypeid = dbManager.queryForString("select cTypeid from CO_WorkType where cName=?", qqcard.get("cWorkTypeName").toString());
		if (qqcard.get("cWorkid")==null || qqcard.get("cWorkid").toString().equals("")){
			dbManager.update("insert into CO_WorkList(cWorkid,cWorkType,csid,cName,iStatus) values(?,?,?,?,?)", String.valueOf(System.nanoTime()), workTypeid, qqcard.get("sid").toString(), qqcard.get("cName").toString(), qqcard.get("iStatus").toString());
		}else{
			dbManager.update("update CO_WorkList set cWorkType=?,csid=?,cName=?,iStatus=? where cWorkid=?", workTypeid, qqcard.get("csid").toString(), qqcard.get("cName").toString(), qqcard.get("iStatus").toString(), qqcard.get("cWorkid").toString());
		}
	}
	
	public Map getWorkList(String type, String sid){
		List<Map> workList = dbManager.query("select * from CO_WorkList where cWorkType=? and csid=?", type, sid);
		if (workList==null || workList.size()==0){
			return new HashMap();
		}
		return workList.get(0);
	}
	
	public String getWorkPrefer(String workType, String sid, String prefName){
		List<Map> preferLine = dbManager.query("select wp.* from CO_WorkPref wp join CO_WorkList wl on wl.cWorkid=wp.cWorkid where wl.cWorkType=? and wl.csid=? and wp.cName=?", workType, sid, prefName);
		if (preferLine==null || preferLine.size()==0){
			return null;
		}
		return preferLine.get(0).get("cValue")==null?null:preferLine.get(0).get("cValue").toString();
	}
}
