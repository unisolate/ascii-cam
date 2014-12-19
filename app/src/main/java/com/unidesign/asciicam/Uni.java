package com.unidesign.asciicam;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Typeface;

public class Uni {
	int width;
    int height;
    int r,g,b;
    FileWriter write;
    BufferedWriter bufWriter;

    public void textConvert(Bitmap img, File file, int quality) throws IOException {
        width = img.getWidth();
        height = img.getHeight();
        quality=(int) ((double)quality*(double)(((double)width+(double)height)/2000.0));
        if(quality==0) quality=1;
        
        write = null;
        try {
            write = new FileWriter(file, true);
        } catch (IOException e) {
        }
        bufWriter = new BufferedWriter(write);

        for (int y = 0; y < height; y += (2 * quality)) {
            for (int x = 0; x < width; x += quality) {
                int rgbValue = img.getPixel(x, y);
                r = (rgbValue & 0xff0000) >> 16;
                g = (rgbValue & 0xff00) >> 8;
                b = (rgbValue & 0xff);
                bufWriter.write(toText(0.299 * r + 0.578 * g + 0.114 * b));
            }
            bufWriter.newLine();
            bufWriter.flush();
        }
        bufWriter.flush();
        write.close();
    }
    
    public void toPic(Bitmap img,File file,int quality) throws IOException{
    	width = img.getWidth();
        height = img.getHeight();

    	Paint p = new Paint();
    	p.setTypeface(Typeface.MONOSPACE);
    	p.setColor(Color.BLACK);
    	p.setTextSize(6);
    	FontMetrics fm = p.getFontMetrics();
    	double textH=Math.ceil(fm.descent - fm.ascent);
    	float textW=p.measureText("7");
    	
    	Bitmap ascii=Bitmap.createBitmap((int)(width*textW/quality), (int)((double)height/(double)quality*textH*4/7), Bitmap.Config.ARGB_8888);
    	Canvas canvasTemp = new Canvas(ascii);
    	canvasTemp.drawColor(Color.WHITE);

        for (int y = 0, l = 1, k = 0; y < height; y += quality,l++) {
        	String line="";
            for (int x = 0; x < width; x += quality) {
                int rgbValue = img.getPixel(x, y);
                r = (rgbValue & 0xff0000) >> 16;
                g = (rgbValue & 0xff00) >> 8;
                b = (rgbValue & 0xff);
                line+=String.valueOf(toText(0.299 * r + 0.578 * g + 0.114 * b));
            }
            canvasTemp.drawText(line, 0, l*6, p);
            k++;
            if(k==2){k=0;y+=quality;}
        }
        
        file.createNewFile();
        FileOutputStream fOut = null;
        try {
                fOut = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
                e.printStackTrace();
        }
        ascii.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        try {
                fOut.flush();
        } catch (IOException e) {
                e.printStackTrace();
        }
        try {
                fOut.close();
        } catch (IOException e) {
                e.printStackTrace();
        }
    }

    public char toText(double g) {
        if (g <= 16)
            return 'M';
        else if (g > 16 && g <= 31)
            return 'N';
        else if (g > 31 && g <= 47)
            return 'H';
        else if (g > 47 && g <= 63)
            return 'Q';
        else if (g > 63 && g <= 79)
            return '$';
        else if (g > 79 && g <= 94)
            return 'O';
        else if (g > 94 && g <= 110)
            return 'C';
        else if (g > 110 && g <= 126)
            return '?';
        else if (g > 126 && g <= 142)
            return '7';
        else if (g > 142 && g <= 158)
            return '>';
        else if (g > 158 && g <= 173)
            return '!';
        else if (g > 173 && g <= 189)
            return ';';
        else if (g > 189 && g <= 205)
            return ':';
        else if (g > 205 && g <= 221)
            return '-';
        else if (g > 221 && g <= 235)
            return '.';
        else
            return ' ';
    }
}
