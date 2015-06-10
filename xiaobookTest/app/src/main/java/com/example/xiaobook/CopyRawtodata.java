package com.example.xiaobook;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

final public class CopyRawtodata {

	public static boolean CopyRawtodata(String path,String DbName,Context context,int id,boolean IsRawData)
	{
		boolean dbExist=checkDataBase(path,DbName);
		if(!dbExist)
		{
        	try{
        		copyDataBase(path,DbName,context,id,IsRawData);
        	}catch(IOException e){
        		throw new Error("Error copying database");
        	}
		}
		return true;
	}
	private static boolean checkDataBase(String path,String DbName)
	{
    	SQLiteDatabase checkDB = null;
    	try{
    		String databaseFilename = path+"/"+DbName;
    		checkDB =SQLiteDatabase.openDatabase(databaseFilename, null,
    				SQLiteDatabase.OPEN_READONLY);
    	}catch(SQLiteException e){
    		
    	}
    	if(checkDB!=null){
    		checkDB.close();
    	}
    	return checkDB !=null?true:false;
	}
	private static  void copyDataBase(String path,String DbName,Context context,int ResId,boolean IsRawData) throws IOException{
	    	String databaseFilenames =path+"/"+DbName;
	    	InputStream is;
	    	File dir = new File(path+"/");
	    	if(!dir.exists())//�ж��ļ����Ƿ���ڣ������ھ��½�һ��
	    		dir.mkdir();
	    	FileOutputStream os = null;
	    	try{
	    		os = new FileOutputStream(databaseFilenames);//�õ����ݿ��ļ���д����
	    	}catch(FileNotFoundException e){
	    		e.printStackTrace();
	    	}
	    	if(IsRawData)
	    	{
	    		 is = context.getResources().openRawResource(ResId);//�õ����ݿ��ļ���������
	    	}
	    	else
	    	{
	    		is = context.getResources().getAssets().open(DbName);//�õ����ݿ��ļ���������
	    	}
	        byte[] buffer = new byte[8192];
	        int count = 0;
	        try{
	        	
	        	while((count=is.read(buffer))>0){
	        		os.write(buffer, 0, count);
	        		os.flush();
	        	}
	        }catch(IOException e){
	        	
	        }
	        try{
	        	is.close();
	        	os.close();
	        }catch(IOException e){
	        	e.printStackTrace();
	        }
	    }
}
