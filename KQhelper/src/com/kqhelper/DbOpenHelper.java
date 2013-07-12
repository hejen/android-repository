package com.kqhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpenHelper extends SQLiteOpenHelper {
	
	private static final String dbName = "KQhelperDb.db";
	
	private static final int version = 1;

	public DbOpenHelper(Context context) {
		super(context, dbName, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS CO_CardSuit(cThemeid varchar primary key, iLevel integer, cName varchar)");
		db.execSQL("CREATE TABLE IF NOT EXISTS CO_CardInfo(cThemeid varchar, cCardID varchar, cName varchar, cSubs varchar, iPrice integer, iIsBottom integer)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
