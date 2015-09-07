package com.example.test;

import java.io.File;
import java.io.IOException;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {
	public static String teacher,subject;
	DBHelper ourHelper;
	Intent i;
   
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ourHelper=new DBHelper(this);

        try {
	
	ourHelper.createDataBase();

			Log.i("minav copieddddddddddddddddddddddddddd", "msg");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try{
        ourHelper.openDataBase();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }

        Database db=new Database(this);
        int n=db.getRow();
        if(n==0){
        	Toast.makeText(this, "Welcome Please import TEACHER-SUBJECT list", Toast.LENGTH_LONG).show();
        	i=new Intent(getApplicationContext(),Next3Activity.class);
   		 i.putExtra(subject,"cse_teacher");
   		 Log.i("ma", "hgdhgdhsgdhsghsgh");
   		 startActivity(i);
        }
        ListView listview =(ListView) findViewById(R.id.list);
	    ArrayAdapter<String> adap=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,db.TName);
	    listview.setAdapter(adap);
	    listview.setItemsCanFocus(true);
	    listview.setOnItemClickListener(new OnItemClickListener(){
	    	public void onItemClick(AdapterView<?> arg0, View view, int arg2,long itemID) {
	    		 i=new Intent(getApplicationContext(),NextActivity.class);
	    		 i.putExtra(teacher, ((TextView)view).getText().toString());
	    		 Log.i("ma", "hgdhgdhsgdhsghsgh");
	    		 startActivity(i);
	    	}
	    });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
                 Database db=new Database(this);
                
                 switch(item.getItemId())
                 {
                 case R.id.reset:
                	 File file = new File("/data/data/com.example.test/databases/cse_nitrr");
                	  if(file.delete()){
                		  Toast.makeText(this, "DATABASE DELETED", Toast.LENGTH_LONG).show(); 
                		  Intent i2=new Intent(this,MainActivity.class);
                       //	i2.putExtra(subject, message);
                       	startActivity(i2);
                	  }
                	  break;
                 
                 }
        return true;
    }
}
