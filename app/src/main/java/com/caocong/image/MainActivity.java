package com.caocong.image;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.caocong.image.widget.ClipPhotoLayout;

/**
 * Created by caocong on 10/9/16.
 */
public class MainActivity extends Activity {
    private ClipPhotoLayout mClipPhotoLayout;
    private int[] pictures = {R.drawable.mingren, R.drawable.cute, R.drawable.tuxi};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scale);
        setTitle("移动和缩放");
        mClipPhotoLayout = (ClipPhotoLayout) findViewById(R.id.clip_layout);
        mClipPhotoLayout.setImageDrawable(pictures[0]);
    }

    public void doClick(View view) {
        Bitmap bitmap = mClipPhotoLayout.clipBitmap();
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("photo", bitmap);
        startActivity(intent);
    }


}