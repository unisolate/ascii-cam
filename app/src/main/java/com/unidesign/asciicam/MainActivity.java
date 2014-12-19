package com.unidesign.asciicam;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.unidesign.asciicam.R;

@SuppressLint("SimpleDateFormat")
public class MainActivity extends Activity implements OnClickListener{
	
	Bundle bundle=null;
	TextView about,gallery,camera;
	private Uri mOutPutFileUri;
	String picturePath="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);
		
		about=(TextView)findViewById(R.id.about);
		about.setOnClickListener(this);
		gallery=(TextView)findViewById(R.id.gallery);
		gallery.setOnClickListener(this);
		camera=(TextView)findViewById(R.id.camera);
		camera.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.about:
			Intent intent = new Intent(this,AboutActivity.class);
	    	startActivity(intent);
	    	overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	    	MainActivity.this.finish();
	    	break;
		case R.id.gallery:
			Intent picIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(picIntent, 1);
			break;
		case R.id.camera:
			Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			String path = Environment.getExternalStorageDirectory()+File.separator+"ASCII Cam"+File.separator;
			File path1 = new File(path);
			if(!path1.exists()){
				path1.mkdirs();
			}
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			File file = new File(path1,timeStamp+".jpg");
			picturePath=path+timeStamp+".jpg";
			mOutPutFileUri = Uri.fromFile(file);
			camIntent.putExtra(MediaStore.EXTRA_OUTPUT, mOutPutFileUri);
			startActivityForResult(camIntent, 2);
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
	    if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
	    	Uri selectedImage = data.getData();
	    	String[] filePathColumn = {
	    			MediaStore.Images.Media.DATA
	    	};
	    	
	    	Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
	    	cursor.moveToFirst();
	    	
	    	int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	    	picturePath = cursor.getString(columnIndex);
	    	cursor.close();
	    	
	    	Intent intent = new Intent(this,Generate.class);
	    	intent.putExtra("string",picturePath);
	    	startActivity(intent);
	    	overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	    }
	    
	    if(requestCode == 2){
	    	if(data != null){
	    		if(data.hasExtra("data")){
	    		}
	    	}else{
	    	}

    		Intent intent = new Intent(this,Generate.class);
	    	intent.putExtra("string",picturePath);
	    	startActivity(intent);
	    	overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	    }
	}
}