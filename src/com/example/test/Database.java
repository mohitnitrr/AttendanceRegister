package com.example.test;




import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class Database {
	private static String DB_PATH = "/data/data/com.example.test/databases/";
	private static final String DATABASE_NAME="cse_nitrr";
	private static final int DATABASE_VERSION=1;
	private static final String DATABASE_TABLE1 = "cse_teachers";
    private static final String DATABASE_TABLE2 = "Message_send";
    private static final String DATABASE_TABLE3 = "Contacts";
    
  
    ArrayList<String> TotalA,Roll_No,TName,Subject,MobileList,NameList,ReceiverList,AttachmentList,MessageList,TimestampList,Exp_MessageList,Exp_TimestampList,Exp_dummyList,Exp_NameList,Exp_AttachmentList,ImageList;
    private Cursor mCursor1,mCursor2,mCursor3;
	
	private DbHelper ourHelper;
	

	private SQLiteDatabase ourDatabase;
	 Date date= new Date();
	private static class DbHelper extends SQLiteOpenHelper{

		private final Context myContext;

		
		public DbHelper(Context context, String name,
                CursorFactory factory, int version) {
			super(context,DATABASE_NAME, null, DATABASE_VERSION);
			this.myContext = context;
            // TODO Auto-generated constructor stub
        }

	

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}
		
		
	

		

		
	/*	public void openDataBase() throws SQLException {
			String myPath = DB_PATH+ DATABASE_NAME;
			ourDataBase = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);
		}*/
		
		
		
	}
	public Database(Context c)
	{
		ourHelper=new DbHelper(c ,DATABASE_NAME, null, DATABASE_VERSION);
		
		ourDatabase=ourHelper.getWritableDatabase();
	
		
	}
	
	public void close()
	{
		ourHelper.close();
		ourDatabase.close();
	}
	
//get roll list from specified subject
	public int getRoll(String m)
	{
		Cursor c;
		if(checkToday(m)){
			String col=getTodayCol();
			c=ourDatabase.rawQuery("select rollno,"+col+ " from "+ m +"",null);
			Roll_No=new ArrayList<String>(c.getCount());
			int N=c.getColumnIndex("rollno");
			int N1=c.getColumnIndex(col);
			while(c.moveToNext())
			{
				Roll_No.add(c.getString(N)+" ("+c.getString(N1)+")");
				
			}
		}
		else{
			c=ourDatabase.rawQuery("select rollno from "+ m +"",null);
			Roll_No=new ArrayList<String>(c.getCount());
			int N=c.getColumnIndex("rollno");
			while(c.moveToNext())
			{
				Roll_No.add(c.getString(N)+" (A)");
				
			}
		}
		
		return c.getCount();
	}
	public void deleteSub(String m){
		export(m);
		ourDatabase.execSQL("drop table "+m);
		
	}
	public String getTodayCol(){
		Calendar cal = Calendar.getInstance();
	       int d=cal.get(Calendar.DAY_OF_MONTH);
			SimpleDateFormat month_date = new SimpleDateFormat("MMM");
			 String m = month_date.format(cal.getTime());
			 
			 
		return ""+m+"_"+d+"";
	}
	public boolean checkToday(String tname){
		String col=getTodayCol();
	
		Cursor c1=ourDatabase.rawQuery("select * from "+tname,null);
		 if(c1.getColumnIndex(""+col+"")==-1){
			 return false;
			 }
		 else
		   return true;
	}
	//creates new column on attendance submit
	public int insertCol(boolean s[],int n,String tname)
	{
		char p='P';
			 String t,col=getTodayCol();
			 Log.i("dsds","alter table "+tname+" add column " +col+ " char(10) default 'A' NOT NULL");
			 ourDatabase.execSQL("alter table "+tname+" add column " +col+ " Text default 'A' NOT NULL");	
			 Cursor c=ourDatabase.rawQuery("select rollno,total from "+tname,null);
			 int N=c.getColumnIndex("rollno");
			 int total;
			 int updatecount=0;
			 for(int i=0;i<n;i++)
			 {
				 c.moveToNext();
				 if(s[i]==true){
					t=c.getString(N);
					total=Integer.parseInt(c.getString(c.getColumnIndex("total")))+1;
					ContentValues values=new ContentValues();
					values.put(""+col+"","P");
					values.put("total",total);
				

					int id=ourDatabase.update(tname,values,"rollno='"+t+"'",null);
					updatecount++;
				 }
			 }
			 return updatecount;
			 
	}

public int export(String tname){
	try {
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
		    // We can read and write the media
			Log.i("dsds","rw");
		    mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
		    // We can only read the media
			Log.i("dsds","r");
		    mExternalStorageAvailable = true;
		    mExternalStorageWriteable = false;
		} else {
		    // Something else is wrong. It may be one of many other states, but all we need
		    //  to know is we can neither read nor write
		    mExternalStorageAvailable = mExternalStorageWriteable = false;
		    Log.i("dsds","fil");
		}
		File myFile = new File(Environment.getExternalStorageDirectory()+"/attendance/export/");
		  myFile.mkdirs();
		  Calendar cal = Calendar.getInstance();
	       int d=cal.get(Calendar.DAY_OF_MONTH);
			SimpleDateFormat month_date = new SimpleDateFormat("MMM");
			 String m = month_date.format(cal.getTime());
		  File my=new File(myFile,tname+"_" +m+d+".csv");
		my.createNewFile();
     Log.i("dsds","filecreated");
	FileOutputStream fOut = new FileOutputStream(my);
		PrintWriter pw = new PrintWriter(fOut);
		String str;
		Cursor c = ourDatabase.rawQuery("select * from "+tname,null);
		 int n=c.getCount();
		 int k=c.getColumnCount();
		 String s="" +n+k;
		 Log.i("nk",s);
		 
			 str=""+c.getColumnName(0);
			 for(int i=1;i<k;i++){
				 	   str=str+ ","+c.getColumnName(i); 
			}
			 pw.println(str);
			 Log.i("s",str);
			 while(c.moveToNext()){
				 str=""+c.getString(0);
			      for(int j=1;j<k;j++){
			         str=str +","+ c.getString(j); 
			 }
			      pw.println(str);  
			      Log.i("s",str);
			 }    
	    pw.flush();
        pw.close();
        fOut.close();
        return 0;
		
	} catch (Exception e) {
		return -1;
	}
}
//create table for subject
	public void createTable(String tname){
	
	ourDatabase.execSQL("create table if not exists "+tname+"(rollno text,total Integer Default(0))");
	}
	//get number of columns in a table
	public int getColCount(String m){
		Cursor c=ourDatabase.rawQuery("select * from "+m,null);
		return c.getColumnCount();
	}
	//view 
	void get_attendance(String m)
	{
		Cursor c=ourDatabase.rawQuery("select rollno,total from "+m,null);
	    TotalA = new ArrayList<String>(c.getCount());
		int N=c.getColumnIndex("rollno");
		int N1=c.getColumnIndex("total");
		while(c.moveToNext())
		{
			TotalA.add(c.getString(N));
			TotalA.add(c.getString(N1));
			
		}
		
	}
	public int getNoOfRows(String m){
		Cursor c=ourDatabase.rawQuery("select * from "+m, null);
		return c.getCount();
		
	}
	public void importRollList(String s,String tname){
		String dstr[]=null; 
		 String dstr1[]=null;
		 dstr=s.split("-");
		 for(int i=2;i<dstr.length;i++){
			 dstr1=dstr[i].split(",");
			 
			
		// ourDatabase.insert(tname,null, dstr[i])
		 ourDatabase.execSQL("insert into "+tname+" values('"+dstr1[0]+"',0)");
		 }
	}
	//import database
	public void importTeacher(String s,String tname){
		String dstr[]=null; 
		 String dstr1[]=null;
		 String sub1=null;
		 String sub2=null;
		 String sub3=null;
		 String sub4=null;
		 String sub5=null;
		 Log.i("db","null");
		 dstr=s.split("-");
		 for(int i=2;i<dstr.length;i++){
			 dstr1=dstr[i].split(",");
			 ContentValues values=new ContentValues();
			 values.put("t_name", dstr1[0]);
			 for(int j=1;j<dstr1.length;j++){
				 dstr1[j]=dstr1[j].replaceAll(" ","_");
				values.put("sub"+j,dstr1[j]); 
			 }
			 
				
		ourDatabase.insert(tname,null,values);
		 }
		 
			 
		 
	}
	//get teacher
	public int getRow()
	{
		ourDatabase.execSQL("create table if not exists cse_teacher(t_name text,sub1 text,sub2 text,sub3 text,sub4 text,sub5 text)");
		Cursor c=ourDatabase.rawQuery("select t_name from cse_teacher",null);
		int n=c.getCount();		
		TName=new ArrayList<String>(c.getCount());
		int N=c.getColumnIndex("t_name");
		while(c.moveToNext())
		{
			TName.add(c.getString(N));
			
		}
		return n;
	}
	
	public void getSub(String m)
	{
		String [] Name={m};
		Cursor c=ourDatabase.rawQuery("select * from cse_teacher where t_name=?",Name);
		Subject=new ArrayList<String>();
		if (c.moveToFirst()){
			 
			Subject.add(c.getString(c.getColumnIndex("sub1")));
			Subject.add(c.getString(c.getColumnIndex("sub2")));
			Subject.add(c.getString(c.getColumnIndex("sub3")));
			Subject.add(c.getString(c.getColumnIndex("sub4")));
			Subject.add(c.getString(c.getColumnIndex("sub5")));
			      // do what ever you want here
			}
	}
}