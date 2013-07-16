package com.kqhelper.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
	
	private static final String dbName = "KQhelperDb.db";
	
	private static final int version = 1;

	public DbHelper(Context context) {
		super(context, dbName, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS CO_CardSuit(cThemeid varchar primary key, iLevel integer, cName varchar)");
		db.execSQL("CREATE TABLE IF NOT EXISTS CO_CardInfo(cThemeid varchar, cCardID varchar, cName varchar, cSubs varchar, iPrice integer, iIsBottom integer)");
		db.execSQL("CREATE TABLE IF NOT EXISTS CO_WorkType(cTypeid varchar primary key, cName varchar)");
		db.execSQL("CREATE TABLE IF NOT EXISTS CO_WorkList(cWorkid varchar primary key, cWorkType varchar, csid varchar, cName varchar)");
		db.execSQL("CREATE TABLE IF NOT EXISTS CO_WorkPref(cGUID varchar primary key, cWorkid varchar, cName varchar, cValue varchar)");
		db.execSQL("insert into CO_WorkType(cTypeid,cName) values(?,?)", new String[]{"1","Ä§·¨¿¨Æ¬"});
		//²âÊÔÊý¾Ý
		db.execSQL("insert into CO_WorkList(cWorkid,cWorkType,csid,cName) values(?,?,?,?)", new String[]{"1","1","AYASVZlbvJNzrPaLoUB6bKpb","3448"});
		db.execSQL("insert into CO_WorkList(cWorkid,cWorkType,csid,cName) values(?,?,?,?)", new String[]{"2","1","AdIgWDifiRcX8tASMSKYVb1Z","860"});
		db.execSQL("insert into CO_WorkList(cWorkid,cWorkType,csid,cName) values(?,?,?,?)", new String[]{"3","1","ARgcN0nyTguSGHnYO2ZcJ0hc","680"});
		db.execSQL("insert into CO_WorkList(cWorkid,cWorkType,csid,cName) values(?,?,?,?)", new String[]{"4","1","ARXRV85sJGOw5eCjMthxuIta","620"});
		db.execSQL("insert into CO_WorkList(cWorkid,cWorkType,csid,cName) values(?,?,?,?)", new String[]{"5","1","Af_WGcIROJ71bPEuK0x3XA1a","682"});
		
		db.execSQL("insert into CO_WorkPref(cGUID,cWorkid,cName,cValue) values(?,?,?,?)", new String[]{"1","1","smeltCard","52"});
		db.execSQL("insert into CO_WorkPref(cGUID,cWorkid,cName,cValue) values(?,?,?,?)", new String[]{"2","2","smeltCard","0"});
		db.execSQL("insert into CO_WorkPref(cGUID,cWorkid,cName,cValue) values(?,?,?,?)", new String[]{"3","3","smeltCard","52"});
		db.execSQL("insert into CO_WorkPref(cGUID,cWorkid,cName,cValue) values(?,?,?,?)", new String[]{"4","4","smeltCard","52"});
		db.execSQL("insert into CO_WorkPref(cGUID,cWorkid,cName,cValue) values(?,?,?,?)", new String[]{"5","5","smeltCard","45"});
		db.execSQL("insert into CO_WorkPref(cGUID,cWorkid,cName,cValue) values(?,?,?,?)", new String[]{"6","1","isSteal","1"});
		db.execSQL("insert into CO_WorkPref(cGUID,cWorkid,cName,cValue) values(?,?,?,?)", new String[]{"7","2","isSteal","0"});
		db.execSQL("insert into CO_WorkPref(cGUID,cWorkid,cName,cValue) values(?,?,?,?)", new String[]{"8","3","isSteal","0"});
		db.execSQL("insert into CO_WorkPref(cGUID,cWorkid,cName,cValue) values(?,?,?,?)", new String[]{"9","4","isSteal","1"});
		db.execSQL("insert into CO_WorkPref(cGUID,cWorkid,cName,cValue) values(?,?,?,?)", new String[]{"10","5","isSteal","1"});
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
