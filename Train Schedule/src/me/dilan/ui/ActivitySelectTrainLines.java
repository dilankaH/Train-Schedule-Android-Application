package me.dilan.ui;

import me.dilan.R;
import me.dilan.obj.TrainLines;
import me.dilan.util.Functions;
import me.dilan.webservice.RailwayWebServiceV2;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ActivitySelectTrainLines extends Activity {
	
	ListView mListViewLines;
	Handler mWebServiceToGetLinesHandler;
	ProgressDialog mProgressDialog;
	TrainLines mTrainLines;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_train_lines);  
        
        mListViewLines = (ListView) findViewById(R.id.select_train_lines_list_view_lines);
        
        mProgressDialog = Functions.getProgressDialog(ActivitySelectTrainLines.this, "Retriving Data");
        new WebServiceToTrainLines().execute(null,null,null);
        
        
        mWebServiceToGetLinesHandler = new Handler() { 
        	public void handleMessage(Message message) {        		
				mListViewLines.setAdapter(new ArrayAdapter<String>(ActivitySelectTrainLines.this,android.R.layout.simple_list_item_1 , mTrainLines.getNames()));
				mProgressDialog.dismiss();
 	        }
        };
        
        mListViewLines.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
				Intent nextScreen = new Intent(ActivitySelectTrainLines.this, ActivitySelectStations.class);
				nextScreen.putExtra("lineId", mTrainLines.getIds()[position]);
				ActivitySelectTrainLines.this.startActivity(nextScreen);
			}
        	
        });
        
    }
    
    
    class WebServiceToTrainLines extends AsyncTask<Object, Object, Object>{

		@Override
		protected Object doInBackground(Object... params) {
			try {					
					mTrainLines = RailwayWebServiceV2.getLines();
					Message message = mWebServiceToGetLinesHandler.obtainMessage();
					Bundle bundle = new Bundle();					
					message.setData(bundle);
					mWebServiceToGetLinesHandler.sendMessage(message);
			        return null;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			return null;
		}    	
    }   
    
}
