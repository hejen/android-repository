package com.kqhelper.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DbManager {

	private DbHelper dbhelper;
	
	private SQLiteDatabase db;
	
	public DbManager(Context context){
		dbhelper = new DbHelper(context);
		db = dbhelper.getWritableDatabase();
	}
	
	public void update(String sql, String... bindArgs){
		db.execSQL(sql, bindArgs);
	}
	
	public List<Map> query(String sql, String... selectionArgs){
		List<Map> result = new ArrayList<Map>();
		Cursor c = db.rawQuery(sql, selectionArgs);
		String[] colNames = c.getColumnNames();
		while (c.moveToNext()){
			Map line = new HashMap();
			for (String colName: colNames){
				if (c.getType(c.getColumnIndex(colName))==Cursor.FIELD_TYPE_BLOB){
					line.put(colName, c.getBlob(c.getColumnIndex(colName)));
				}else if (c.getType(c.getColumnIndex(colName))==Cursor.FIELD_TYPE_FLOAT){
					line.put(colName, c.getFloat(c.getColumnIndex(colName)));
				}else if (c.getType(c.getColumnIndex(colName))==Cursor.FIELD_TYPE_INTEGER){
					line.put(colName, c.getInt(c.getColumnIndex(colName)));
				}else if (c.getType(c.getColumnIndex(colName))==Cursor.FIELD_TYPE_STRING){
					line.put(colName, c.getString(c.getColumnIndex(colName)));
				}
			}
			result.add(line);
		}
		return result;
	}
	
	public Map queryForMap(String sql, String... selectionArgs){
		List<Map> result = this.query(sql, selectionArgs);
		if (result==null || result.size()==0){
			return new HashMap();
		}
		return result.get(0);
	}
	
	public void batchUpdate(String sql, List<String[]> params){
		db.beginTransaction();
		for (String[] param: params){
			db.execSQL(sql, param);
		}
		db.setTransactionSuccessful();
		db.endTransaction();
	}
	
	public void close(){
		db.close();
	}
	
	public String queryForString(String sql, String... selectionArgs){
		List<Map> listResult = query(sql, selectionArgs);
		if (listResult==null || listResult.size()==0){
			return "";
		}
		for (Map map: listResult){
			for (Iterator iter=map.entrySet().iterator();iter.hasNext();){
				Map.Entry entry = (Map.Entry)iter.next();
				return entry.getValue().toString();
			}
		}
		return "";
	}
}
