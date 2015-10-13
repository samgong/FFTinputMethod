package com.sg.FFTinputMethod;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

//import android.os.Environment;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.util.List;

public class EscapeView extends View {
    //    private final int LEFT = 0;
//    private final int RIGHT = 1;
//    private final int UP = 2;
//    private final int DOWN = 3;
    private static final int TAP = 0;
    private static final int ROUND_ANTI = 5;
    private static final int ROUND_WISE = 6;
    private static final float TAP_DIS = 50;
    private final String[] TOAST_STRING = {"T", "L", "R", "U", "D", "-", "+"};
//    private final float ACTION_MIN_DURATION = 200;

    private float[][] clusterCenter = {{207, 1395}, {167, 935}, {419, 750}, {750, 661}, {914, 743}};
    private double[][][] directionVector = {{{-0.83323, 0.55292}, {0.79807, -0.60256}, {-0.65123, -0.75888}, {0.73867, 0.67407}},
            {{-0.78909, 0.61428}, {0.80836, -0.58869}, {-0.31494, -0.94911}, {0.30286, 0.95303}},
            {{-0.88692, 0.46192}, {0.84657, -0.53228}, {-0.16551, -0.98621}, {0.20656, 0.97843}},
            {{-0.83503, 0.5502}, {0.8697, -0.49357}, {-0.08874, -0.99605}, {0.20033, 0.97973}},
            {{-0.89326, 0.44954}, {0.89282, -0.45042}, {0.04576, -0.99895}, {-0.12198, 0.99253}}};


    private char[] mapTable = {
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            ' ', '5', '1', 'a', '9', '+', '-', 0,
            '\n', 0, 0, 'A', 0, '*', '%', 0,
            ',', '6', '2', 'e', '0', '_', '=', 0,
            '.', 0, 0, 'E', '0', '^', '#', 0,
            0, 'b', 'c', 'd', 'f', 0, 0, 0,
            0, 'B', 'C', 'D', 'F', 0, 0, 0,
            '?', '7', '3', 'i', 'j', '/', '\\', 0,
            '!', 0, 0, 'I', 'J', '$', '@', 0,
            0, 'g', 'h', 'm', 'n', 0, 0, 0,
            0, 'G', 'H', 'M', 'N', 0, 0, 0,
            0, 'l', 'r', 's', 't', 0, 0, 0,
            0, 'L', 'R', 'S', 'T', 0, 0, 0,
            0, '(', ')', '<', '>', 0, 0, 0,
            0, '[', ']', '{', '}', 0, 0, 0,
            8, '8', '4', 'o', 'k', '|', '&', 0,
            127, 0, 0, 'O', 'K', '`', '~', 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 'p', 'q', 'x', 'y', 0, 0, 0,
            0, 'P', 'Q', 'X', 'Y', 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 'u', 'v', 'w', 'z', 0, 0, 0,
            0, 'U', 'V', 'W', 'Z', 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, ';', '\'', 0, 0, 0, 0, 0, 0, ':', '"', 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
    };
    //    private boolean haveSentAction;
    private long lastOutputTime = 0;
    private Paint paint = new Paint();
    private String displayString = "";
    private String displayString2 = "";
    private long createTime;
    private ArrayList<ArrayList<TimePoint>> list = new ArrayList<>();
    private int[] fingerState = new int[5];
    private OutPutListener outPutListener = new OutPutListener() {
        @Override
        public void onOutPut(int a) {
            char c = mapTable[a];
            if (c != 0) {
                if (c == 8) {
                    if (displayString.length() > 0)
                        displayString = displayString.substring(0, displayString.length() - 1);
                } else if (c == 127) displayString = "";
                else displayString += String.valueOf(c);
            }
            invalidate();
        }
    };

//    public void setOutPutListener(OutPutListener outPutListener) {
//        this.outPutListener = outPutListener;
//    }


    public class TimePoint {
        public TimePoint(float x, float y, long t, int property) {
            this.x = x;
            this.y = y;
            this.t = t;
            this.property = property;
        }

        public String toString() {
            return String.valueOf(x) + "," + String.valueOf(y) + "," + String.valueOf(t) + "," + String.valueOf(property) + '\n';
        }

        public float x, y;
        public long t;
        public int property;
    }

    public EscapeView(Context context) {
        super(context);
        paint.setColor(Color.WHITE);
        paint.setTextSize(40);
        createTime = System.currentTimeMillis();
        for (int i = 0; i < 5; ++i) list.add(new ArrayList<TimePoint>());
    }

    private void getCenter(int n) {
        for (int k = 0; k < n; ++k) {
            int res = -1;
            float x = 0, y = 0;
            for (int i = 0; i < list.get(k).size(); i++) {
                x += list.get(k).get(i).x;
                y += list.get(k).get(i).y;
            }
            x /= list.get(k).size();
            y /= list.get(k).size();
            float minDis = 1000000000;
            for (int i = 0; i < 5; ++i) {
                float tDis = (clusterCenter[i][0] - x) * (clusterCenter[i][0] - x)
                        + (clusterCenter[i][1] - y) * (clusterCenter[i][1] - y);
                if (tDis < minDis) {
                    res = i;
                    minDis = tDis;
                }
            }
            fingerResult[k] = res;
            traceCenter[k][0] = x;
            traceCenter[k][1] = y;
        }
    }

    private float getTurnAngle(int k) {
        int sign;
        float angleSum = 0;
        float x1, y1, x2 = list.get(k).get(1).x - list.get(k).get(0).x, y2 = list.get(k).get(1).y - list.get(k).get(0).y;
        for (int i = 2; i < list.get(k).size() - 1; i++) {
            x1 = x2;
            y1 = y2;
            x2 = list.get(k).get(i).x - list.get(k).get(i - 1).x;
            y2 = list.get(k).get(i).y - list.get(k).get(i - 1).y;
            sign = (x1 * y2 - x2 * y1 > 0) ? -1 : 1;
            float t = (float) ((x1 * x2 + y1 * y2) / ((Math.sqrt(x1 * x1 + y1 * y1)) * Math.sqrt(x2 * x2 + y2 * y2)));
            if (!(t == t)) continue;
            angleSum += sign * Math.acos(t);
        }
        return angleSum;
    }

    private void actionTurnShow(int n) {
//        haveSentAction = true;
//        lastTurnTime = System.currentTimeMillis();
//        CustomToast.showToast(getContext(), toastStr, Toast.LENGTH_SHORT);
        displayString2 = "";
        for (int i = 0; i < 5; ++i)
            if (fingerState[i] != -1)
                displayString2 += String.valueOf(i + 1) + " " + TOAST_STRING[fingerState[i]] + "\n";
        if (n == 5) {
            boolean allTap = true;
            for (int i = 0; i < 5; ++i) if (fingerState[i] != 0) allTap = false;
            if (allTap) {
                Arrays.sort(traceCenter, new Comparator<float[]>() {
                    @Override
                    public int compare(float[] lhs, float[] rhs) {
                        if (lhs[0] < rhs[0]) return -1;
                        return 1;
                    }
                });
                int finger0 = -1;
                float maxy = -1;
                for (int i = 0; i < 5; ++i) {
                    if (traceCenter[i][1] > maxy) {
                        maxy = traceCenter[i][1];
                        finger0 = i;
                    }
                }
                for (int i = 0, j = 0, k; i < 5; ++i) {
                    if (i == finger0) k = 0;
                    else k = ++j;
                    clusterCenter[k][0] = traceCenter[i][0];
                    clusterCenter[k][1] = traceCenter[i][1];
                }
            }
        }
        int outputInt = 0, state = 0;
        for (int i = 0; i < 5; ++i) {
            if (fingerState[i] != -1) {
                outputInt += (1 << (3 + i));
                if (i != 0 && fingerState[i] > state) state = fingerState[i];
            }
        }
        outputInt += state;
        outPutListener.onOutPut(outputInt);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
//      canvas.drawText(" x:"+String.valueOf(startX)+" y:"+String.valueOf(startY),100,100,paint);
        canvas.drawText(displayString, 50, 100, paint);
        canvas.drawText(displayString2, 50, 300, paint);
        for (int i = 0; i < 5; ++i)
            canvas.drawCircle(clusterCenter[i][0], clusterCenter[i][1], 20, paint);
        super.onDraw(canvas);
    }

    float[][] traceCenter = new float[5][2];
    int[] fingerResult = new int[5];

    private void recognizeInput(int n) {
        for (int i = 0; i < 5; ++i) fingerState[i] = -1;
        getCenter(n);
        for (int k = 0; k < n; ++k) {
            if (list.get(k).isEmpty()) continue;
            int finger = fingerResult[k];
            float dx = list.get(k).get(list.get(k).size() - 1).x - list.get(k).get(0).x, dy = list.get(k).get(list.get(k).size() - 1).y - list.get(k).get(0).y;
            float dis = dx * dx + dy * dy;
            if (dis < TAP_DIS) {
                fingerState[finger] = TAP;
                continue;
            }
            float turnAngle = getTurnAngle(k);
            if (Math.abs(turnAngle) > Math.PI) {
                if (turnAngle > 0) fingerState[finger] = ROUND_ANTI;
                else fingerState[finger] = ROUND_WISE;
                continue;
            }
            int dir = -1;
            float min = -1;
            for (int i = 0; i < 4; ++i) {
                float t = (float) directionVector[finger][i][0] * dx + (float) directionVector[finger][i][1] * dy;
                if (t > min) {
                    min = t;
                    dir = i;
                }
            }
            fingerState[finger] = dir + 1;
        }
        actionTurnShow(n);
    }

    int[] validPointerId = new int[5];

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        int pointerCount = Math.min(event.getPointerCount(), 5);
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_DOWN:
                for (int i = 0; i < 5; ++i) list.get(i).clear();
                for (int i = 0; i < pointerCount; ++i)
                    validPointerId[i] = event.getPointerId(i);
//                haveSentAction = false;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                long curT = System.currentTimeMillis();
                if (curT - lastOutputTime >= 100) {
                    lastOutputTime = curT;
                    recognizeInput(event.getPointerCount());
                }
                //         for (int i=0;i<5;++i) list.get(i).clear();
                break;
            default:
                break;
        }
        for (int i = 0; i < pointerCount; ++i) {
            int id = event.getPointerId(i);
            for (int j = 0; j < 5; ++j) {
                if (validPointerId[j] == id)
                    list.get(j).add(new TimePoint(event.getX(i), event.getY(i), System.currentTimeMillis() - createTime, event.getAction()));
            }
        }
        return true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //     writeFileToSD();
    }
//    private void writeFileToSD() {
//        String sdStatus = Environment.getExternalStorageState();
//        if(!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
//            return;
//        }
//        try {
//            String pathName="/sdcard/test/";
//            String fileName="file.csv";
//            File path = new File(pathName);
//            File file = new File(pathName + fileName);
//            if( !path.exists()) {
//                path.mkdir();
//            }
//            if( !file.exists()) {
//                file.createNewFile();
//            }
//            FileOutputStream stream = new FileOutputStream(file);
//            String s="x,y,t,p\n";
//            for(int i=0;i<list.get(0).size();++i){
//                s+=list.get(0).get(i).toString();
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
