/*
 * Copyright 2012 Thinkermobile, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.james.easydatabase;

import java.io.File;
import java.util.Date;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

/**
 * <br> SQLite 的控制，目前功能還蠻陽春的，支援資料庫的增加、刪除、修改、取得
 * <br> PS: No more support this class in the future. Please reference EasySQLiteDatabase.
 * @deprecated
 * @author JamesX
 * @since 2012/05/17
 */
public class SqliteDataBase {
	private static final String TAG = SqliteDataBase.class.getSimpleName();
	private String DATABASE_DIR = null;
	private String DATABASE_NAME = null;
	private int DATABASE_VERSION;
	
	private String DATABASE_TABLE = null;
	private String DATABASE_CREATE = null;
	
	private Context mContext = null;
	private DatabaseHelper dbHelper;
	private SQLiteDatabase database;
	
	public static final String KEY_ROWID = "_id";
	public static final String KEY_CREATED = "created";
	private String[] strCols;
	
	private class DatabaseHelper extends SQLiteOpenHelper{
	
		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				db.execSQL(DATABASE_CREATE);
			}catch (SQLException ex) {
	        }
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
			onCreate(db);
		}
	}
	
	/**
	 * dbName denotes database name; tableName denotes table name; columns denotes different key.
	 * dbVer denotes the version of this database
	 * Here "KEY_ROWID" and "KEY_CREATED" will be built automatically in columns. Note that this DB is a very simple
	 * tool to build SQLite for you, so there is only one type, "TEXT", so far.
	 * 
	 * @param context (Context)
	 * @param dbVer (int)
	 * @param dbName (String)
	 * @param tableName (String)
	 * @param columns (String[])
	 */
	public SqliteDataBase(Context context, int dbVer, String dbName, String tableName, String[] columns){
		initDataBase(context, null, dbVer, dbName, tableName, columns);
		
		Log.d(TAG, DATABASE_CREATE);
	}
	
	/**
	 * dbDir denotes the dir of database in SDcard, if dbDir is null, database would not be stored in SDcard.
	 * dbName denotes database name; tableName denotes table name; columns denotes different key.
	 * dbVer denotes the version of this database
	 * Here "KEY_ROWID" and "KEY_CREATED" will be built automatically in columns. Note that this DB is a very simple
	 * tool to build SQLite for you, so there is only one type, "TEXT", so far.
	 * 
	 * @param context (Context)
	 * @param dbDir (String) ex: /temp
	 * @param dbVer (int)
	 * @param dbName (String)
	 * @param tableName (String)
	 * @param columns (String[])
	 */
	public SqliteDataBase(Context context, String dbDir, int dbVer, String dbName, String tableName, String[] columns){
		initDataBase(context, dbDir, dbVer, dbName, tableName, columns);

		Log.d(TAG, DATABASE_CREATE);
	}
	
	//
	private void initDataBase(Context context, String dbDir, int dbVer, String dbName, String tableName, String[] columns){
		this.mContext = context;
		
		if(dbDir != null){
			if(dbDir.startsWith("/")){
				DATABASE_DIR = dbDir;
			}
			else{
				DATABASE_DIR = "/"+dbDir;
			}
			File file = new File(Environment.getExternalStorageDirectory()+DATABASE_DIR);
			if(!file.exists()){
				file.mkdir();
			}
		}
		
		DATABASE_VERSION = dbVer;
		
		if(dbName.endsWith(".db")){
			DATABASE_NAME = dbName;
		}
		else{
			DATABASE_NAME = dbName+".db";
		}
		
		DATABASE_TABLE = tableName;
		
		String sCreateColumn = "";
		strCols = new String[columns.length+2];
		strCols[0] = KEY_ROWID;
		strCols[1] = KEY_CREATED;
		for(int i=0; i<columns.length; i++){
			strCols[i+2] = columns[i];
			if(i<columns.length-1){
				sCreateColumn = sCreateColumn+columns[i]+" TEXT,";
			}
			else{
				sCreateColumn = sCreateColumn+columns[i]+" TEXT";
			}
		}
		
		DATABASE_CREATE = 
			"CREATE TABLE IF NOT EXISTS "
			+DATABASE_TABLE
			+"("
			+strCols[0]+" INTEGER PRIMARY KEY,"
			+strCols[1]+" TEXT,"
			+sCreateColumn
			+");";
		
		Log.d(TAG, DATABASE_CREATE);
	}
	
	/**
	 * 開啟 DB
	 * @return
	 * @throws SQLException
	 */
	public SqliteDataBase open()throws SQLException{
		if(DATABASE_DIR == null){
			dbHelper = new DatabaseHelper(mContext);
			database = dbHelper.getWritableDatabase();
		}
		else{
			if(!DATABASE_DIR.startsWith("/")) DATABASE_DIR = "/"+DATABASE_DIR;
			database = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory()+DATABASE_DIR+"/"+DATABASE_NAME, null);
			//建立table
			if(database.needUpgrade(DATABASE_VERSION)){
				database.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
				database.execSQL(DATABASE_CREATE);
				database.setVersion(DATABASE_VERSION);
			}
			else {
				database.execSQL(DATABASE_CREATE);
				database.setVersion(DATABASE_VERSION);
			}
		}
		
		Log.d(TAG, " db open");
		return this;
	}

	/**
	 * 回傳 db 是否開啟
	 * @return
	 */
	public boolean isOpen(){
		return database.isOpen();
	}
	
	/**
	 * 關閉 db
	 */
	public void close(){
		if(dbHelper != null)
			dbHelper.close();

		if(database != null)
			if(database.isOpen())
				database.close();
	}
	
	/**
	 *  Get all entries
	 * @return
	 */
	public Cursor getAll(){
		try {
			Cursor mCursor = database.rawQuery("SELECT * FROM "+DATABASE_TABLE, null);
			if (mCursor != null) {
				if (!mCursor.moveToFirst()){
					return null;
				}
			}
			return mCursor;
		}
		catch (Exception e) {
			return null;
		}
	}
	
	/**
	 *  Add an entry
	 * @param value (String[]) length of content is suggested to be same or less than columns
	 * @return
	 */
	public long create(String[] value){
		Date now = new Date();
		ContentValues args = new ContentValues();
		args.put(KEY_CREATED, String.valueOf(now.getTime()));
		Log.v(getClass().getSimpleName(), "strCols.length: "+strCols.length);
		Log.v(getClass().getSimpleName(), "value.length: "+value.length);
		for(int i=0; i<strCols.length-2; i++){
			if(i < value.length){
				args.put(strCols[i+2], value[i]);
			}
			else{
				args.put(strCols[i+2], "");
			}
		}
		Log.d(TAG, " db create");
		return database.insert(DATABASE_TABLE, null, args);
	}
	
	/**
	 * @deprecated
	 * @param rowId
	 * @return
	 * @throws SQLException
	 */
	public Cursor get(long rowId) throws SQLException {
		return queryRowId(rowId);
	}
	
	/**
	 *  Search an entry by id
	 * @param rowId
	 * @return
	 * @throws SQLException
	 */
	public Cursor queryRowId(long rowId) throws SQLException {
		Cursor mCursor = database.query(true,
				DATABASE_TABLE,
				strCols,
				KEY_ROWID + "=" + rowId,
				null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	/**
	 * @deprecated
	 * @param column
	 * @param value
	 * @return
	 * @throws SQLException
	 */
	public Cursor get(String column, String value) throws SQLException {
		return queryEqualsTo(column, value);
	}
	
	/**
	 * 
	 * @param column
	 * @param value
	 * @return
	 * @throws SQLException
	 */
	public Cursor queryEqualsTo(String column, String value) throws SQLException {
		Cursor mCursor = database.query(true,
				DATABASE_TABLE,
				strCols,
				column + " LIKE '" + value+"'",
				null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	/**
	 * 
	 * @param column
	 * @param value
	 * @return
	 * @throws SQLException
	 */
	public Cursor queryStartsWith(String column, String value) throws SQLException {
		Cursor mCursor = database.query(true,
				DATABASE_TABLE,
				strCols,
				column + " LIKE '" + value+"%'",
				null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	/**
	 * 
	 * @param column
	 * @param value
	 * @return
	 * @throws SQLException
	 */
	public Cursor queryEndsWith(String column, String value) throws SQLException {
		Cursor mCursor = database.query(true,
				DATABASE_TABLE,
				strCols,
				column + " LIKE '%" + value+"'",
				null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	/**
	 * 
	 * @param column
	 * @param value
	 * @return
	 * @throws SQLException
	 */
	public Cursor queryContainsWith(String column, String value) throws SQLException {
		Cursor mCursor = database.query(true,
				DATABASE_TABLE,
				strCols,
				column + " LIKE '%" + value+"%'",
				null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	/**
	 * Update
	 * @param rowId (long)
	 * @param value (String[])
	 * @return
	 */
	public boolean update(long rowId, String[] value) {
		ContentValues args = new ContentValues();
		for(int i=0; i<strCols.length-2; i++){
			if(i <= value.length){
				args.put(strCols[i+2], value[i]);
			}
			else{
				args.put(strCols[i+2], "");
			}
		}
		Log.d(TAG, " db update");
		return database.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null)>0;
	}
	
	/**
	 * 
	 * @param rowId
	 * @param column
	 * @param value
	 * @return
	 */
	public boolean update(long rowId, String column, String value) {
		String sql = "UPDATE "+DATABASE_TABLE+" "+
		"SET "+column+" = '"+value+"' "+
		"WHERE "+KEY_ROWID + "=" + rowId+";";
		
		return execSQL(sql);
	}
	

	/**
	 * Delete
	 * @param rowId (long) entry id
	 * @return
	 */
	public boolean delete(long rowId) {
		return database.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean deleteAll() {
		return execSQL("DELETE FROM "+DATABASE_TABLE);
	}
	
	/**
	 * 
	 * @param col
	 * @param defaultValue
	 * @return
	 */
	public boolean addCol(String col, String defaultValue){
		try {//DEFAULT 'baz'
            defaultValue = defaultValue == null ? "" : " DEFAULT " + defaultValue;
            return execSQL("ALTER TABLE " + DATABASE_TABLE + " ADD COLUMN " + col + " " + "TEXT" + defaultValue + ";");
        } catch (Exception ex) {
            Log.e("grandroid", "fail to add column " + col + " to " + DATABASE_TABLE);
            return false;
        }
	}
	
	/**
	 * 
	 * @param sql
	 */
	public boolean execSQL(String sql){
		try{
			database.execSQL(sql);
			return true;
		}
		catch (Exception e){
			return false;
		}
	}
}
