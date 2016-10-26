package com.tools.tipsplitcalc;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class FragmentActivity extends Activity{

	private EditText defaultTips;
	private EditText min;
	private EditText max;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        this.setContentView(R.layout.fragment_main);
        this.setTitle("Settings");
        ActionBar actionBar = getActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        defaultTips = (EditText) findViewById(R.id.editTextDefaultTax);
        min = (EditText) findViewById(R.id.editTextMin);
        max = (EditText) findViewById(R.id.editTextMax);
        defaultTips.setFocusable(false);
        min.setFocusable(false);
        max.setFocusable(false);
        loadPreferences();
    }
	
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}  
 
	
	public boolean onOptionsItemSelected(MenuItem item){
		Intent intent = new Intent(getApplicationContext(),MainActivity.class);
		this.startActivity(intent);
        return true;
	}
	
	void loadPreferences(){
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String getTips = sharedPreferences.getString("storedTips", "15.0");
        defaultTips.setText(getTips);
        String getMin = sharedPreferences.getString("storedMin", "10");
        min.setText(getMin);
        String getMax = sharedPreferences.getString("storedMax", "30");
        max.setText(getMax);
	}
	
	private void savePreferences(String key, String value) {
	    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.apply();
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.buttonDefaultAdd:{
				if((int)Float.parseFloat(defaultTips.getText().toString())<Integer.parseInt(max.getText().toString()))
				defaultTips.setText(""+(Float.parseFloat(defaultTips.getText().toString())+1));
			}break;
			case R.id.buttonDefaultMinus:{
				if((int)Float.parseFloat(defaultTips.getText().toString())>Integer.parseInt(min.getText().toString()))
				defaultTips.setText(""+(Float.parseFloat(defaultTips.getText().toString())-1));
			}break;
			//
			case R.id.buttonMinAdd:{
				if(Integer.parseInt(min.getText().toString())<Integer.parseInt(max.getText().toString())&&
						Integer.parseInt(min.getText().toString())<(int)Float.parseFloat(defaultTips.getText().toString()))
				min.setText(""+(Integer.parseInt(min.getText().toString())+1));
			}break;
			case R.id.buttonMinMinus:{
				if(Integer.parseInt(min.getText().toString())>1)
				min.setText(""+(Integer.parseInt(min.getText().toString())-1));
			}break;
			//
			case R.id.buttonMaxAdd:{
				max.setText(""+(Integer.parseInt(max.getText().toString())+1));
			}break;
			case R.id.buttonMaxMinus:{
				if(Integer.parseInt(max.getText().toString())>Integer.parseInt(min.getText().toString())&&
						Integer.parseInt(max.getText().toString())>(int)Float.parseFloat(defaultTips.getText().toString()))
				max.setText(""+(Integer.parseInt(max.getText().toString())-1));
			}break;
			//
			case R.id.buttonSave:{
				savePreferences("storedTips", defaultTips.getText().toString());
				savePreferences("storedMin", min.getText().toString());
				savePreferences("storedMax", max.getText().toString());
				Toast toast = Toast.makeText(this, "Saved", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				Intent intent = new Intent(getApplicationContext(),MainActivity.class);
				this.startActivity(intent);
				finish();
			}break;
			case R.id.buttonDefault:{
				defaultTips.setText("15");
				min.setText("10");
				max.setText("30");
			}break;
		}
	}

}


