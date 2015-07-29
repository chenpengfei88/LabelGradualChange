package com.chenpengfei.labelgradualchange;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Frank on 2015/7/29.
 */
public class TextActivityu extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(TextActivityu.this, MainActivity.class));
    }
}
