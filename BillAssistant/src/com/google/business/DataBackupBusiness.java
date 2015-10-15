package com.google.business;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.business.base.BusinessBase;
import com.google.utility.FileUtil;

public class DataBackupBusiness extends BusinessBase {

	public DataBackupBusiness(Context p_Context) {
		super(p_Context);
	}

	public boolean DatabaseBackup(Date p_Backup) {
		boolean result = false;
		try {
			File sourceFile = new File("/data/data/" + getContext().getPackageName() + "/databases/BillDatabase");
			
			if(sourceFile.exists())
			{
				File fileDir = new File("/sdcard/GOD/DataBaseBak/");
				if (!fileDir.exists()) {
					fileDir.mkdirs();
				}
				
				FileUtil.cp("/data/data/" + getContext().getPackageName() + "/databases/BillDatabase", "/sdcard/GOD/DataBaseBak/BillDatabase");
				
			}
			
			SaveDatabaseBackupDate(p_Backup.getTime());
			
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public boolean DatabaseRestore() {
		boolean result = false;
		try {
			long databaseBackupDate = LoadDatabaseBackupDate();
			
			if(databaseBackupDate != 0)
			{
				FileUtil.cp("/sdcard/GOD/DataBaseBak/BillDatabase", "/data/data/com.google/databases/BillDatabase");
			}
			
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public void SaveDatabaseBackupDate(long millise)
	{		
		
		SharedPreferences sp = getContext().getSharedPreferences("DatabaseBackupDate",Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putLong("DatabaseBackupDate", millise);
		editor.commit();
	}
	
	public long LoadDatabaseBackupDate()
	{
		long databaseBackupDate = 0;
		SharedPreferences sp = getContext().getSharedPreferences("DatabaseBackupDate",Context.MODE_PRIVATE);
		if (sp != null) {
			databaseBackupDate = sp.getLong("DatabaseBackupDate", 0);
		}
		
		return databaseBackupDate;
	}
}
