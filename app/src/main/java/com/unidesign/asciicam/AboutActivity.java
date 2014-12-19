package com.unidesign.asciicam;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class AboutActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		TextView about=(TextView) findViewById(R.id.abouttext);
		about.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.about, menu);
		return true;
	}

	public void onClick(View v){
		Intent intent=new Intent(AboutActivity.this, MainActivity.class);
        startActivity(intent);
    	overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); 
        AboutActivity.this.finish();
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK){
        	Intent intent=new Intent(AboutActivity.this, MainActivity.class);
            startActivity(intent);
        	overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); 
            AboutActivity.this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
