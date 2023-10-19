package com.example.clara.contask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

public  class ActivityFullScreenPhoto extends AppCompatActivity {


    private SubsamplingScaleImageView imageFullScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_photo);

        imageFullScreen = findViewById(R.id.imageFullScreen);
        imageFullScreen.setZoomEnabled(true);
        String photoUrl = getIntent().getExtras().getString("photoUrl", null);

        if (photoUrl != null) {

             Target mTarget;
            mTarget = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    int height = displayMetrics.heightPixels;
                    int width = displayMetrics.widthPixels;
                    System.out.println(" W: "+width+" H:"+height);
                    if (bitmap != null) {
                        System.out.println("bitmap W: "+bitmap.getWidth());
                        if(bitmap.getWidth()<width){
                            Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                                    bitmap, width, height, false);
                            imageFullScreen.setImage(ImageSource.bitmap(resizedBitmap));

                        }
                        else {
                            imageFullScreen.setImage(ImageSource.bitmap(bitmap));
                        }
                        } else {
                    }
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }


            };
            Picasso.get().load(photoUrl).into(mTarget);

        }

    }


}