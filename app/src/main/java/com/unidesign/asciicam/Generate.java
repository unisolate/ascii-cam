package com.unidesign.asciicam;

import java.io.File;
import java.io.IOException;
import android.os.Bundle;
import android.widget.SeekBar;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class Generate extends Activity implements OnClickListener{

	public static ImageView imageView;
	private SeekBar seekBar;
	TextView qnum;
	int quality=3;
	String picPath,name,tname;
	Bitmap bmp,ascii;
	Uni uni;
	Boolean outTxt=true,outJpg=false;
	private final static String ALBUM_PATH=Environment.getExternalStorageDirectory()+File.separator+"ASCII Cam"+File.separator;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_generate);

		TextView gen=(TextView) findViewById(R.id.button1);
		gen.setOnClickListener(this);
		
		qnum=(TextView) findViewById(R.id.qnum);
		qnum.setText(" 3");
		seekBar = (SeekBar) this.findViewById(R.id.seekBar1);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar,int progress,boolean fromUser){
		        qnum.setText(" "+(progress+1));
		        quality=progress+1;
		    }
		    @Override  
		    public void onStartTrackingTouch(SeekBar seekBar) {
		    }
		    @Override
		    public void onStopTrackingTouch(SeekBar seekBar) {
		    }
		});
		
		CheckBox txt=(CheckBox) findViewById(R.id.checkBox1);
		txt.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if(arg1) outTxt=true;
				else outTxt=false;
			}
        });
		
		CheckBox jpg=(CheckBox) findViewById(R.id.checkBox2);
		jpg.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if(arg1) outJpg=true;
				else outJpg=false;
			}
        });
		
		Intent intent=getIntent();
		Bundle bundle = intent.getExtras();
		picPath = bundle.getString("string");
		String[] s1 = picPath.split(File.separator);
		name=s1[s1.length-1];
		String[] s2 = name.split("\\.");
		tname=s2[0];
		
		File file = new File(picPath);
		if (!file.exists()) {
			byebye();
		}
		
		bmp=BitmapFactory.decodeFile(picPath);
		imageView=(ImageView) findViewById(R.id.bitmap);
        imageView.setImageBitmap(bmp);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.generate, menu);
		return true;
	}
	
	public void onClick(View v){
		boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);  
        if(!sdCardExist)
        {
        	Toast.makeText(Generate.this, "No SD Card Found", Toast.LENGTH_SHORT).show();
        }else{
            File dirFirstFile=new File(ALBUM_PATH);
            if(!dirFirstFile.exists()){   
                 dirFirstFile.mkdir();
            }
        }
        if(outTxt || outJpg)
        {
        	Toast.makeText(Generate.this, "Generating...", Toast.LENGTH_SHORT).show();
        	uni=new Uni();
        	if(outTxt)
        	{
        		Toast.makeText(Generate.this, "Generating Text...", Toast.LENGTH_LONG).show();
        		File file=new File(ALBUM_PATH+tname+".txt");
        		try {
    				uni.textConvert(bmp, file, quality);
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
            }
            if(outJpg){
        		Toast.makeText(Generate.this, "Generating Picture...", Toast.LENGTH_LONG).show();
            	File file=new File(ALBUM_PATH+"ASCII_"+tname+".png");
            	try {
					uni.toPic(bmp, file, quality);
				} catch (IOException e) {
					e.printStackTrace();
				}
//            	ascii=Bitmap.createBitmap(512, 512, Bitmap.Config.ARGB_8888);
//            	Canvas canvasTemp = new Canvas(ascii);
//            	canvasTemp.drawColor(Color.WHITE);
//            	Paint p = new Paint();
//            	
//            	p.setColor(Color.BLACK);
//            	p.setTypeface(Typeface.MONOSPACE);
//            	p.setTextSize(40);
//            	canvasTemp.drawText("77777777777777777777", 0, 40, p);
//            	canvasTemp.drawText("NNNNNNNNNNNNNNNNNNNN", 30, 80, p);
//        		imageView.setImageBitmap(ascii);
            }
    		Toast.makeText(Generate.this, "Generated Successfully", Toast.LENGTH_SHORT).show();
        }
		byebye();
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK){
        	byebye();
        }
        return super.onKeyDown(keyCode, event);
    }
	
	public void byebye(){
		Intent intent=new Intent(Generate.this, MainActivity.class);
        startActivity(intent);
    	overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); 
        Generate.this.finish();
	}
}
