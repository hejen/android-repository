package com.kqhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DbManager {
	
	private SQLiteDatabase db;
	
	public DbManager(Context context){
		DbOpenHelper doh = new DbOpenHelper(context);
	}

}
