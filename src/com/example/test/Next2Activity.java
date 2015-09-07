package com.example.test;

import java.util.ArrayList;
import java.util.HashMap;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class Next2Activity extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		String message = intent.getStringExtra(NextActivity.subject);
		Database db=new Database(this);
		db.get_attendance(message);
		setContentView(R.layout.main);
		ListView list = (ListView) findViewById(R.id.mylist);
		ArrayList<HashMap<String, String>> mylistData =
		                   new ArrayList<HashMap<String, String>>();
		 
		String[] columnTags = new String[] {"rollno", "total"};
		 
		int[] columnIds = new int[] {R.id.rollno, R.id.total};
		int r = db.TotalA.size();
		HashMap<String,String> map = new HashMap<String, String>();
		   map.put(columnTags[0], "Total Classes");
		   map.put(columnTags[1], ""+(db.getColCount(message)-2));
		   mylistData.add(map);
		   HashMap<String,String> map2 = new HashMap<String, String>();
		   map2.put(columnTags[0], "RollNO");
		   map2.put(columnTags[1], "Present");
		   mylistData.add(map2);
		for(int i=0; i<r; i=i+2)
		{
		 HashMap<String,String> map1 = new HashMap<String, String>();
		 //initialize row data
		 for(int j=0; j<2; j++)
		 {
		    map1.put(columnTags[j], db.TotalA.get(i+j));
		 }
		 mylistData.add(map1);
		
	}// onCreate
		SimpleAdapter arrayAdapter =
	               new SimpleAdapter(this, mylistData, R.layout.mylistrow,
	                             columnTags , columnIds);
	list.setAdapter(arrayAdapter);
	}
}
