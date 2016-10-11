package com.caocong.image;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.caocong.image.widget.RoundImageView;

/**
 * Created by caocong on 10/10/16.
 */
public class ResultActivity extends Activity {
    RoundImageView roundImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        roundImageView = (RoundImageView) findViewById(R.id.round_view);
        Bitmap bitmap=getIntent().getParcelableExtra("photo");
        roundImageView.setImageBitmap(bitmap);
    }
}
