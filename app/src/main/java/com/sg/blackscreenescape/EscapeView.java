package com.sg.blackscreenescape;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class EscapeView extends View {
    private float px,py;
    private float[] stx={1,0,-1,0};
    private float[] sty={0,1,0,-1};
    private int direction;
    private Paint paint=new Paint();
    private long createTime;
    ArrayList list=new ArrayList();
    public class TimePoint{
        public TimePoint(float x, float y,long t){
            this.x=x;this.y=y;this.t=t;
        }
        public String toString(){
           return String.valueOf(x)+","+String.valueOf(y)+","+String.valueOf(t)+'\n';
        }
        public float x,y;
        public long t;
    }
    public EscapeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setColor(Color.WHITE);
        paint.setTextSize(100);
        createTime=System.currentTimeMillis();
    }

    public EscapeView(Context context) {
        super(context);
        paint.setColor(Color.WHITE);
        paint.setTextSize(100);
        createTime=System.currentTimeMillis();
    }
    private int getDirection(float dx,float dy){
        int dir=0;
        float min=-1;
        for(int i=0;i<4;++i){
            float t=stx[i]*dx+sty[i]*dy;
            if(t>min){
                min=t;
                dir=i;
            }
        }
        return dir;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        canvas.drawText(String.valueOf(direction)+" x:"+String.valueOf(px)+" y:"+String.valueOf(py),100,100,paint);
        super.onDraw(canvas);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float cx=event.getX(),cy=event.getY();
        list.add(new TimePoint(cx, cy, System.currentTimeMillis()-createTime));
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                direction=getDirection(cx-px,cy-py);
                break;
            default:break;
        }
        px=cx;py=cy;
        invalidate();
        return true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        writeFileToSD();
        list.clear();
    }
    private void writeFileToSD() {
        String sdStatus = Environment.getExternalStorageState();
        if(!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            return;
        }
        try {
            String pathName="/sdcard/test/";
            String fileName="file.txt";
            File path = new File(pathName);
            File file = new File(pathName + fileName);
            if( !path.exists()) {
                path.mkdir();
            }
            if( !file.exists()) {
                file.createNewFile();
            }
            FileOutputStream stream = new FileOutputStream(file);
            String s="x,y,t\n";
            for(int i=0;i<list.size();++i){
                s+=list.get(i).toString();
            }
            byte[] buf = s.getBytes();
            stream.write(buf);
            stream.close();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
