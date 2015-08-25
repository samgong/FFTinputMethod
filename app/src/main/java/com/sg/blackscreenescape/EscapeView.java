package com.sg.blackscreenescape;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class EscapeView extends View {
    private final int GO_FORWARD = 0;
    private final int TURN_RIGHT = 1;
    private final int TURN_LEFT = 2;
    private final int GO_BACK = 3;
    private final float TAP_DIS = 3000;
    private final float ACTION_MIN_DURATION = 1000;

    private float startX, startY;
    private float[] stx = {4, -4, 0};
    private float[] sty = {-3, -3, 5};
    private boolean haveSentAction;
    private long lastTurnTime = 0;
    private Paint paint=new Paint();

//    private long createTime;
//    ArrayList list=new ArrayList();
//    public class TimePoint{
//        public TimePoint(float x, float y,long t){
//            this.x=x;this.y=y;this.t=t;
//        }
//        public String toString(){
//           return String.valueOf(x)+","+String.valueOf(y)+","+String.valueOf(t)+'\n';
//        }
//        public float x,y;
//        public long t;
//    }

    public EscapeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setColor(Color.WHITE);
        paint.setTextSize(100);
//        createTime=System.currentTimeMillis();
    }

    public EscapeView(Context context) {
        super(context);
        paint.setColor(Color.WHITE);
        paint.setTextSize(100);
//        createTime=System.currentTimeMillis();
    }

    private void actionTurn(int dir) {
        haveSentAction = true;
        lastTurnTime = System.currentTimeMillis();
        String toastStr = "";
        switch (dir) {
            case GO_FORWARD:
                toastStr = "Go Forward";
                break;
            case TURN_RIGHT:
                toastStr = "Turn Right";
                break;
            case TURN_LEFT:
                toastStr = "Turn Left";
                break;
            case GO_BACK:
                toastStr = "Go Back";
                break;
            default:
                break;
        }
        Toast.makeText(getContext(), toastStr, Toast.LENGTH_SHORT).show();
    }

    private void switchDirectionAction(float dx, float dy, boolean up) {
        if (haveSentAction) return;
        if (System.currentTimeMillis() - lastTurnTime < ACTION_MIN_DURATION) return;
        float dis = dx * dx + dy * dy;
        if (up && dis < TAP_DIS) {
            actionTurn(GO_FORWARD);
            return;
        }
        if (dis < TAP_DIS) return;
        int dir = 1;
        float min=-1;
        for (int i = 0; i < 3; ++i) {
            float t = stx[i] * dx + sty[i] * dy;
            if (t > min) {
                min = t;
                dir = i + 1;
            }
        }
        actionTurn(dir);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        // canvas.drawText(String.valueOf(direction)+" x:"+String.valueOf(startX)+" y:"+String.valueOf(startY),100,100,paint);
        super.onDraw(canvas);
    }
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        float cx=event.getX(),cy=event.getY();
//        list.add(new TimePoint(cx, cy, System.currentTimeMillis()-createTime));
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = cx;
                startY = cy;
                haveSentAction = false;
                break;
            case MotionEvent.ACTION_MOVE:
                switchDirectionAction(cx - startX, cy - startY, false);
                break;
            case MotionEvent.ACTION_UP:
                switchDirectionAction(cx - startX, cy - startY, true);
                break;
            default:
                break;
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
//        writeFileToSD();
//        list.clear();
    }
//    private void writeFileToSD() {
//        String sdStatus = Environment.getExternalStorageState();
//        if(!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
//            return;
//        }
//        try {
//            String pathName="/sdcard/test/";
//            String fileName="file.txt";
//            File path = new File(pathName);
//            File file = new File(pathName + fileName);
//            if( !path.exists()) {
//                path.mkdir();
//            }
//            if( !file.exists()) {
//                file.createNewFile();
//            }
//            FileOutputStream stream = new FileOutputStream(file);
//            String s="x,y,t\n";
//            for(int i=0;i<list.size();++i){
//                s+=list.get(i).toString();
//            }
//            byte[] buf = s.getBytes();
//            stream.write(buf);
//            stream.close();
//
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//    }
}
