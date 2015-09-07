package com.example.test;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class NextActivity extends Activity {
	public static String subject;
	Intent i1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.teacher);
        final Database db1=new Database(this);
        db1.getSub(message);
        int i=0,n=db1.Subject.size();
        ListView listview =(ListView) findViewById(R.id.list1);
        while(i<n){
        	if (db1.Subject.get(i)==null)
        			break;
        	i=i+1;
        }
        ArrayList<String> Subject1=new ArrayList<String>(i);
        for(int j=0;j<i;j++){
        	Subject1.add(db1.Subject.get(j));
        }
        
	    ArrayAdapter<String> adap=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,Subject1);
	    listview.setAdapter(adap);
	    listview.setOnItemClickListener(new OnItemClickListener(){
	    	public void onItemClick(AdapterView<?> arg0, View view, int arg2,long itemID) {
	    		 i1=new Intent(getApplicationContext(),Next1Activity.class);
	    		 i1.putExtra(subject, ((TextView)view).getText().toString());
	    		 startActivity(i1);
	    	}
	    });
	    db1.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_next, menu);
        return true;
    }
}
