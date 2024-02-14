package com.example.lab3amulticalclayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.lab3amulticalclayout.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final int KEY_WIDTH = 5;
    private final int KEY_HEIGHT = 4;
    private final int KEYS = KEY_WIDTH * KEY_HEIGHT;
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Log.i(TAG, String.valueOf(KEYS));
        initLayout();
    }

    private void initLayout(){

        ConstraintLayout layout = binding.getRoot();
        LayoutParams params;
        MarginLayoutParams margParams;

        //TextView
        int id = View.generateViewId();
        TextView tv = new TextView(this);
        tv.setId(id);
        tv.setTag("tv1");
        tv.setText("0123");
        tv.setTextSize(48);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        tv.setEms(10);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        layout.addView(tv);

        params = tv.getLayoutParams();
        params.width = 0;
        params.height = LayoutParams.WRAP_CONTENT;
        tv.setLayoutParams(params);

        //Buttons
        int[] buttonIds = new int[KEYS]; // array of TextView IDs
        String[] bText = getResources().getStringArray(R.array.buttons);
        for (int i = 0; i < KEYS; ++i) {
            id = View.generateViewId(); // generate new ID
            Button but = new Button(this); // create new TextView
            but.setId(id); // assign ID
            but.setTag("b" + i); // assign tag (for acquiring references later)
            but.setText(bText[i]); // set text (using a string resource)
            but.setTextSize(24); // set size
            layout.addView(but); // add to layout

            params = but.getLayoutParams();
            margParams = (MarginLayoutParams) params;
            margParams.width = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT;
            margParams.height = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT;
            margParams.setMargins(4,4,4,4);
            but.setLayoutParams(margParams);

            buttonIds[i] = id; // store ID to collection
        }

        ConstraintSet set = new ConstraintSet();
        set.clone(layout);

        //TextView Constraints
        set.connect(tv.getId(), ConstraintSet.RIGHT, binding.rightGuide.getId(), ConstraintSet.RIGHT, 0);
        set.connect(tv.getId(), ConstraintSet.LEFT, binding.leftGuide.getId(), ConstraintSet.LEFT, 0);
        set.connect(tv.getId(), ConstraintSet.TOP, binding.topGuide.getId(), ConstraintSet.TOP, 0);

        //Button Constraints

        int[] tempChain = new int[KEY_WIDTH];
        //Rows
        for(int i = 0; i < KEY_HEIGHT; i++){
            for(int j = 0; j < KEY_WIDTH; j++){
                tempChain[j] = buttonIds[j + (i * KEY_WIDTH)];
            }
            set.createHorizontalChain(binding.leftGuide.getId(), ConstraintSet.RIGHT,binding.rightGuide.getId(), ConstraintSet.LEFT,tempChain,null,ConstraintSet.CHAIN_SPREAD_INSIDE);
            Log.i(TAG, Arrays.toString(tempChain));
            tempChain = new int[KEY_WIDTH];
        }

        int[] tempChain2 = new int[KEY_HEIGHT];
        //Columns
        for(int i = 0; i < KEY_WIDTH; i++){
            for(int j = 0; j < KEY_HEIGHT; j++){
                tempChain2[j] = buttonIds[(i) + (KEY_WIDTH * j)];
            }
            set.createVerticalChain(tv.getId(), ConstraintSet.BOTTOM,binding.bottomGuide.getId(), ConstraintSet.TOP,tempChain2,null,ConstraintSet.CHAIN_SPREAD_INSIDE);
            Log.i(TAG, Arrays.toString(tempChain2));
            tempChain2 = new int[KEY_HEIGHT];
        }

        set.applyTo(layout);

    }

}