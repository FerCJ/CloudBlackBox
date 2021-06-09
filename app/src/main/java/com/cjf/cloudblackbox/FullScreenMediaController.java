package com.cjf.cloudblackbox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.cjf.cloudblackbox.viewmodel.FirebaseViewModel;

public class FullScreenMediaController extends MediaController {

    private ImageButton fullScreen;
    private String isFullScreen;
    private FirebaseViewModel firebaseViewModel;
    private String userID;

    public FullScreenMediaController(Context context, String id) {
        super(context);
        this.userID = id;
    }

    @Override
    public void setAnchorView(View view) {

        super.setAnchorView(view);

        //image button for full screen to be added to media controller
        fullScreen = new ImageButton (super.getContext());

        FrameLayout.LayoutParams params =
                new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.RIGHT;
        params.rightMargin = 80;
        addView(fullScreen, params);

        //fullscreen indicator from intent
        isFullScreen =  ((Activity)getContext()).getIntent().
                getStringExtra("fullScreenInd");

        if("y".equals(isFullScreen)){
            fullScreen.setImageResource(R.drawable.iconapp2);
        }else{
            fullScreen.setImageResource(R.drawable.iconapp2);
        }

        //add listener to image button to handle full screen and exit full screen events
        fullScreen.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(),VerVideos.class);


                if("y".equals(isFullScreen)){
                    intent.putExtra("fullScreenInd", "");
                    intent.putExtra("ID", userID);
                }else{
                    intent.putExtra("fullScreenInd", "y");
                    intent.putExtra("ID", userID);
                }
                ((Activity)getContext()).startActivity(intent);
            }
        });
    }
}
