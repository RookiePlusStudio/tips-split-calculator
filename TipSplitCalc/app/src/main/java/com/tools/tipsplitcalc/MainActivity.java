package com.tools.tipsplitcalc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CalendarContract.Events;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Date;

public class MainActivity extends Activity implements OnSeekBarChangeListener {

	private SeekBar tipsPercent = null;
	private EditText tips;
	private EditText billAmount;
	private EditText tipsAmount;
	private EditText peopleCount;
	private EditText total;
	private EditText each;
	private EditText tax;
	private SeekBar seekBarEachAmount;
	private SeekBar seekBarTotalAmount;
	private TextView taxHint;
	private TextView min;
	private TextView max;
	private CheckBox checkBox;
	private AlertDialog alertDialog;
	private int eachValueAmount;
	private int totalValueAmount;
	private float taxValueAmount = 0f;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.setTitle("Tip Split Calculator");
        viewInitial();
	    registerListener();
	    reRegisterListener();
	    loadPreferences();
	    setTipsPosition();
	    tax.setOnFocusChangeListener(new OnFocusChangeListener() {
	        @Override
	        public void onFocusChange(View v, boolean hasFocus) {
	            if(hasFocus)
	                taxHint.setVisibility(View.VISIBLE);
	            else
	                taxHint.setVisibility(View.GONE);
	        }
	    });
	    alertDialog= new AlertDialog.Builder(this).create();
        alertDialog.setTitle("About Tips Helper");
        alertDialog.setMessage(aboutMessage());
		tipsPercent.setOnSeekBarChangeListener(this);
		seekBarTotalAmount.setOnSeekBarChangeListener(this);
		seekBarEachAmount.setOnSeekBarChangeListener(this);
	}
	
	void viewInitial(){
		total = (EditText) findViewById(R.id.editTextTotal);
		peopleCount = (EditText) findViewById(R.id.editTextPplCount);
		tips = (EditText) findViewById(R.id.editTextTipsPercent);
		each = (EditText) findViewById(R.id.editTextEach);
		billAmount = (EditText) findViewById(R.id.editTextBill);
	    tipsAmount = (EditText) findViewById(R.id.editTextTipsAmount);
	    tax = (EditText) findViewById(R.id.editTextTax);
	    taxHint = (TextView) findViewById(R.id.textViewTaxHint);
	    min = (TextView) findViewById(R.id.textViewMin);
	    max = (TextView) findViewById(R.id.textViewMax);
		tipsPercent = (SeekBar) findViewById(R.id.seekBar1);
		seekBarTotalAmount = (SeekBar) findViewById(R.id.seekBar2);
	    seekBarEachAmount = (SeekBar) findViewById(R.id.seekBar3);
	    checkBox = (CheckBox) findViewById(R.id.checkBoxTax);
	}
	
	void loadPreferences(){
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String getTips = sharedPreferences.getString("storedTips", "15");
        tips.setText(getTips);
        String getMin = sharedPreferences.getString("storedMin", "10");
        min.setText(getMin);
        String getMax = sharedPreferences.getString("storedMax", "30");
        max.setText(getMax);
	}
	
	void setTipsPosition(){
		tipsPercent.setMax(Integer.parseInt(max.getText().toString()) - Integer.parseInt(min.getText().toString()));
		tipsPercent.setProgress(((int) Float.parseFloat(tips.getText().toString()) - Integer.parseInt(min.getText().toString())));
	}
	
	String decimalFormat(Float f){
		DecimalFormat format = new DecimalFormat("##.##");
        return format.format(f);
	}
	
	void clearForm(){
		Intent intent = getIntent();
		finish();
		startActivity(intent);
	}
	
	String aboutMessage(){
		String result="Tip Split Calculator Version 1.0\n\n";
		result+="If you have any question, or the calculation result is wrong\n";
		result+="Please email rookieplusstudio@gmail.com";
		return result;
	}
	
    void registerListener(){
    	billAmount.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (billAmount.isFocused() && billAmount.getText().length() != 0)
                    calculate();
            }
        });
    	tips.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            	if(tips.isFocused() && billAmount.getText().length()!=0)
            	calculate();           
            }
            });
    }
    
    void reRegisterListener(){
    	total.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            	if(total.isFocused() && billAmount.getText().length()!=0)
            	reCalculate(1);
            }
            });
    	each.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            	if(each.isFocused() && billAmount.getText().length()!=0)
            	   reCalculate(2);
            }
            });
    }
    
    
    void calculate(){
    	if (billAmount.getText().length()!=0){
    	fontColorChange(0);
    	Float result;
    	Float getTips;
    	if(tips.getText().length()==0) getTips=0f;
    	else{
    	  getTips = Float.parseFloat(tips.getText().toString());
    	}
    	Float bill = Float.parseFloat(billAmount.getText().toString());
    	if(taxValueAmount <0) bill+= taxValueAmount;
    	Float tipsResult = (getTips/100) * (bill);
    	result = tipsResult +Float.parseFloat(billAmount.getText().toString()) + taxValueAmount;
    	tipsAmount.setText(decimalFormat(tipsResult));
    	total.setText(decimalFormat(result));  	
    	int ppl=Integer.parseInt(peopleCount.getText().toString());
    	Float eachResult = result/ppl;
    	each.setText(decimalFormat(eachResult));
    	
    	float setTips = eachResult;
    	int t = (int) setTips;
    	setTips = setTips - (float) t;
    	seekBarEachAmount.setProgress((int) (setTips * 100));
    	eachValueAmount =t;
    	
    	float setTotal = result;
    	int intTotal = (int) setTotal;
    	setTotal = setTotal - (float) intTotal;
    	seekBarTotalAmount.setProgress((int) (setTotal * 100));
    	totalValueAmount =intTotal;
    	}
    }
	
    void reCalculate(int type){
    	if(type==1){
    		//1
    		if(total.getText().length()!=0){
    			fontColorChange(1);
    			float getTotal = Float.parseFloat(total.getText().toString());
    			float newEach = getTotal /(Float.parseFloat(peopleCount.getText().toString()));
    			each.setText(decimalFormat(newEach));
    			float original = Float.parseFloat(billAmount.getText().toString());
    			float newTipsPercent = (((getTotal - taxValueAmount)/original) - 1f) * 100;
    			tips.setText(decimalFormat(newTipsPercent));
    			float newTipsAmount=(original)* (newTipsPercent/100);
    			tipsAmount.setText(decimalFormat(newTipsAmount));
    		}	
    	}
    	
    	if(type==2){
    		//2
    		if(each.getText().length()!=0){
    			fontColorChange(1);
    			float getEach = Float.parseFloat(each.getText().toString());
    			float newTotal = getEach *(Float.parseFloat(peopleCount.getText().toString()));
    			total.setText(decimalFormat(newTotal));
    			float original = Float.parseFloat(billAmount.getText().toString());
    			float newTipsPercent = ((newTotal - taxValueAmount)/original - 1f) * 100;
    			tips.setText(decimalFormat(newTipsPercent));
    			float newTipsAmount=(original)* (newTipsPercent/100);
    			tipsAmount.setText(decimalFormat(newTipsAmount));
    			eachValueAmount =(int)getEach;
    		}	
    	}
    	
    }
    
    void fontColorChange(int color){
    	if(color==1) {
    		total.setTextColor(Color.RED);
    		each.setTextColor(Color.RED);
    		tipsAmount.setTextColor(Color.RED);
    		tips.setTextColor(Color.RED);
    	}
    	if(color==0) {
    		total.setTextColor(Color.WHITE);
    		each.setTextColor(Color.WHITE);
    		tipsAmount.setTextColor(Color.WHITE);
    		tips.setTextColor(Color.WHITE);
    	}
    }
	
    String description(){
    	String result="";
    	result+="Bill Amount: "+ billAmount.getText();
    	result+="\nTips: "+ tipsAmount.getText();
    	result+="\nTotal: "+total.getText();
    	result+="\nPeople: "+ peopleCount.getText();
    	result+="\nEach people: "+each.getText();
    	return result;
    }
    
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
	   
       if(seekBar.getId()==R.id.seekBar1&& fromUser){
		  tips.setText((progress+(Integer.parseInt(min.getText().toString())))+"");
          calculate();
       }
       if(seekBar.getId()==R.id.seekBar2&& fromUser){
    	  if(total.getText().toString().length()!=0) {
     	  fontColorChange(1);
     	  total.clearFocus();
     	  float p = ((float)progress)/100;
     	  p+=(float) totalValueAmount;
     	  total.setText(""+p);
     	  float result = Float.parseFloat(total.getText().toString())/(Float.parseFloat(peopleCount.getText().toString()));
     	  each.setText(decimalFormat(result));
     	  float original = Float.parseFloat(billAmount.getText().toString());
     	  if(taxValueAmount <0) {
     		  original+= taxValueAmount;
     		  p+= taxValueAmount;
     	  }
     	  float newTips = (((p - taxValueAmount)/(original)-1)*100);
     	  tips.setText(decimalFormat(newTips));
     	  float newTipsAmount = (newTips/100) * (original);
     	  tipsAmount.setText(decimalFormat(newTipsAmount));
    	  }
       }
       if(seekBar.getId()==R.id.seekBar3&& fromUser){
 	      if(each.getText().toString().length()!=0) {
    	  fontColorChange(1);
    	  each.clearFocus();
    	  float p = ((float)progress)/100;
    	  p+=(float) eachValueAmount;
    	  each.setText(""+p);
    	 // float result = Float.parseFloat(total.getText().toString())-changed*(Float.parseFloat(peopleCount.getText().toString()));
    	  float result = Float.parseFloat(each.getText().toString())*(Float.parseFloat(peopleCount.getText().toString()));
    	  total.setText(decimalFormat(result));
    	  float original = Float.parseFloat(billAmount.getText().toString());
    	  if(taxValueAmount <0) {
    		  original+= taxValueAmount;
    		  result+= taxValueAmount;
    	  }
    	  float newTips = (((result - taxValueAmount)/(original)-1)*100);
    	  tips.setText(decimalFormat(newTips));
    	  float newTipsAmount = (newTips/100) * (original);
    	  tipsAmount.setText(decimalFormat(newTipsAmount));
 	      }
       }
    }
	 
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }
	
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
       
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onClick(View v) {
		switch(v.getId()){
		case R.id.buttonPplAdd:{
			peopleCount.setText("" + (Integer.parseInt(peopleCount.getText().toString()) + 1));
			calculate();
		}break;
		case R.id.buttonPplMinus:{
			String ppl= peopleCount.getText().toString();
			if(ppl.compareTo("1")==0) Toast.makeText(this, "Invalid Input", Toast.LENGTH_SHORT);
			else {
				peopleCount.setText("" + (Integer.parseInt(peopleCount.getText().toString()) - 1));
				calculate();
			}
		}break;
		case R.id.checkBoxTax:{
			final boolean isChecked = checkBox.isChecked();
			tax.clearFocus();
			if(isChecked){
				if(tax.getText().length()!=0){
						taxValueAmount = Float.parseFloat(tax.getText().toString());
					calculate();
				}
				else {
					Toast toast = Toast.makeText(this, "Input Tax value then check again.", Toast.LENGTH_SHORT);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
					checkBox.setChecked(false);
				}
			}
			else{
				taxValueAmount =0f;
				calculate();
			}
		}break;
		case R.id.buttonLog:{
			 Intent i = new Intent();
			 i.setType("vnd.android.cursor.item/event"); 
			 i.putExtra(Events.TITLE, "Payment Event  $ "+each.getText()); 
			 i.putExtra(Events.ALLOWED_REMINDERS, 0);
			 i.putExtra(Events.DESCRIPTION, description());			 
			 i.putExtra(Events.ALL_DAY, 0);
			 i.putExtra(Events.DTSTART, new Date().getTime()); 
			 i.putExtra(Events.DTEND, new Date().getTime());
			 i.setAction(Intent.ACTION_EDIT);
			 startActivity(i);
		}
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.clear) {
			clearForm();
			return true;
		}
		if (id == R.id.setting) {
			Intent intent = new Intent(this,FragmentActivity.class);
			this.startActivity(intent);
		}
		if (id == R.id.About) {
			 alertDialog.show();
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			return inflater.inflate(R.layout.fragment_main, container,
                    false);
		}
	}

}
