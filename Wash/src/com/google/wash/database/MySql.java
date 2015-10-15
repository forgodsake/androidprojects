package com.google.wash.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MySql extends SQLiteOpenHelper {

	private final String TABLE_NAME = "clothes";

	public MySql(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create TABLE " + TABLE_NAME
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT," 
				+"image INT NOT NULL,"
				+"num INT NOT NULL ,"
				+ "name VARCHAR(20) NOT NULL,"
				+"price VARCHER(20) NOT NULL)");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
