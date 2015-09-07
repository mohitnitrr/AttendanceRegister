package com.example.test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
public class Next3Activity extends ListActivity {

	   public static String message="null";
	   public static String subject;
	   private List<String> item = null;
	   private List<String> path = null;
	   private String root=Environment.getExternalStorageDirectory()+"/attendance/import/";
	   private TextView myPath;
    /** Called when the activity is first created. */

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next3);
        myPath = (TextView)findViewById(R.id.path);
        Intent intent = getIntent();
        message = intent.getStringExtra(NextActivity.subject);
        getDir(root);
    }
    private void getDir(String dirPath)
    {
     myPath.setText("Location: " + dirPath);
     item = new ArrayList<String>();
     path = new ArrayList<String>();
     File f = new File(dirPath);
     f.mkdirs();
     File[] files = f.listFiles();
      item.add("../");
      path.add(f.getParent());
     for(int i=0; i < files.length; i++)
     {
       File file = files[i];
       path.add(file.getPath());
       if(file.isDirectory())
        item.add(file.getName() + "/");
       else
        item.add(file.getName());
    }
     ArrayAdapter<String> fileList =
      new ArrayAdapter<String>(this, R.layout.row, item);
     setListAdapter(fileList);
    }
 @Override
 protected void onListItemClick(ListView l, View v, int position, long id) {
  final File file = new File(path.get(position));
  if (file.isDirectory())
  {
   if(file.canRead())
    getDir(path.get(position));
   else
   {
    new AlertDialog.Builder(this)
    .setIcon(R.drawable.i)
    .setTitle("[" + file.getName() + "] Sorry Folder is not accessible!")
    .setPositiveButton("OK", 
      new DialogInterface.OnClickListener() {
       public void onClick(DialogInterface dialog, int which) {
       }
      }).show();
   }
  }
  else
  {
	  
	  if(file.getName().endsWith("csv")){
		  new AlertDialog.Builder(this)
		    .setIcon(R.drawable.r)
		    .setTitle("[" + file.getName() + "]")
		    .setPositiveButton("OK", 
		      new DialogInterface.OnClickListener() {
		       public void onClick(DialogInterface dialog, int which) {
		            impo(file);
		       }
		      }).show();
		  }
		  else{
			  new AlertDialog.Builder(this)
			    .setIcon(R.drawable.e)
			    .setTitle("[" + file.getName() + "] INVALID FORMAT Select another file")
			    .setPositiveButton("OK", 
			      new DialogInterface.OnClickListener() {
			       public void onClick(DialogInterface dialog, int which) {
			            
			       }
			      }).show();
		  }
	  
   
  }
 }
 public void impo(File file){
		String str="";
    	Database db=new Database(this);
    	String buf = new String();			
    	InputStream is=null;
		try {
			is = new BufferedInputStream(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    	if (is!=null) {	
    		int i = 0;
    		try {
				while ((str = reader.readLine()) != null) {	
					buf=buf+"-"+str;
					 Log.i("dsds",str);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		if(message.equalsIgnoreCase("cse_teacher"))
    		  db.importTeacher(buf,message);
    		else
    			db.importRollList(buf,message);
    	}		
    	try {
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
    	if(message.equalsIgnoreCase("cse_teacher")){
    		Intent i1 = new Intent(this,MainActivity.class);
   		 startActivity(i1);
    	}
  		else{
  			Intent i1 = new Intent(this,Next1Activity.class);
   			i1.putExtra(subject, message);
  			 startActivity(i1);
  			 }
    	
		
 }

}
