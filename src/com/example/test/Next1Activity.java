package com.example.test;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class Next1Activity extends Activity {
   public static String message="null";
   public static int n;
   public static boolean s[];
   public static String subject;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next1);
        Intent intent = getIntent();
        message = intent.getStringExtra(NextActivity.subject);
        final Database db=new Database(this);
        db.createTable(message);
        n=db.getRoll(message);
         s=new boolean[n];
        final GridView gridView = (GridView) findViewById(R.id.gridView1);
 		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, db.Roll_No);
 		gridView.setAdapter(adapter);
 		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
				int position, long id) {
			String w;
			
				if(s[position]==false)
				{
					w=db.Roll_No.get(position).replaceAll("A","P");
					db.Roll_No.set(position, w);
					((TextView)v).setText(w);
					
			     s[position]=true;
				}
				else if(s[position]==true)
				{
					w=db.Roll_No.get(position).replaceAll("P","A");
					db.Roll_No.set(position, w);
					((TextView)v).setText(w);
				s[position]=false;
				}
			}
		});
 		db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_next1, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
                 Database db=new Database(this);
                
                 switch(item.getItemId())
                 {
                 case R.id.submit:
                	 if(!db.checkToday(message)){
                		 Toast.makeText(this, "Students Present="+db.insertCol(s,n,message), Toast.LENGTH_LONG).show(); 
                	 }
                	  else
                		  Toast.makeText(this, "Attendancs already taken!!CAN'T OVERWRITE", Toast.LENGTH_LONG).show();
                	  break;
                 case R.id.Import:
                	 if(db.getNoOfRows(message)==0)
                		 fileExplorer();
                   	 else
                   		 Toast.makeText(this, "Data Already Exists!!CAN'T OVERWRITE", Toast.LENGTH_LONG).show();
                        break;
                	 
				  
                 case R.id.Export:
                	 int e=db.export(message);
                	 if(e==0)
                	 Toast.makeText(this, ""+message+" Table Successfully Exported", Toast.LENGTH_LONG).show();
                	 else
                		 Toast.makeText(this, "External Storage either not present or accessible", Toast.LENGTH_LONG).show(); 
                		 
                	 break;
                 case R.id.View:
                	 checkAttend();
                     break;
                 case R.id.Delete:
                	 db.deleteSub(message);
                	 Intent i2=new Intent(this,Next1Activity.class);
                 	i2.putExtra(subject, message);
                 	startActivity(i2);
                     break;
                 }
        return true;
    }
    public void fileExplorer(){
    	Intent i1 = new Intent(this,Next3Activity.class);
		i1.putExtra(subject, message);
		 startActivity(i1);
    }
    public void checkAttend(){
    	Intent i2=new Intent(this,Next2Activity.class);
    	i2.putExtra(subject, message);
    	startActivity(i2);
    }
}
