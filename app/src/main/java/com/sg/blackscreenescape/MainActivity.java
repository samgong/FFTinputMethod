package com.sg.blackscreenescape;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;


public class MainActivity extends ActionBarActivity {
    private EscapeView escapeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        escapeView=new EscapeView(this);
        setContentView(escapeView);
    }
}
