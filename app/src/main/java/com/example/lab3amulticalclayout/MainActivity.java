package com.example.lab3amulticalclayout;

import static androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior.getTag;

import androidx.annotation.ContentView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab3amulticalclayout.databinding.ActivityMainBinding;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements AbstractView{

    private ActivityMainBinding binding;
    private final int KEY_WIDTH = 5;
    private final int KEY_HEIGHT = 4;
    private final int KEYS = KEY_WIDTH * KEY_HEIGHT;
    private static final String TAG = "MainActivity";
    private DefaultController controller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        controller = new DefaultController();
        DefaultModel model = new DefaultModel();

        controller.addView(this);
        controller.addModel(model);

        model.initDefault();

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
        tv.setEms(12);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        layout.addView(tv);

        params = tv.getLayoutParams();
        params.width = 0;
        params.height = LayoutParams.WRAP_CONTENT;
        tv.setLayoutParams(params);

        //Buttons
        CalculatorClickHandler click = new CalculatorClickHandler();

        int[] buttonIds = new int[KEYS]; // array of TextView IDs
        String[] bText = getResources().getStringArray(R.array.buttons);
        for (int i = 0; i < KEYS; ++i) {
            id = View.generateViewId(); // generate new ID
            Button but = new Button(this); // create new TextView
            but.setId(id); // assign ID
            but.setTag("b" + i); // assign tag (for acquiring references later)
            but.setText(bText[i]); // set text (using a string resource)
            but.setTextSize(24); // set size
            but.setOnClickListener(click); //add click handler
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

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        String propertyValue = evt.getNewValue().toString();

        if ( propertyName.equals(DefaultController.ELEMENT_BUTTONPRESS_PROPERTY) ) {


            TextView text = binding.getRoot().findViewWithTag("tv1");
            String oldPropertyValue = text.getText().toString();

            if ( !oldPropertyValue.equals(propertyValue) ) {
                text.setText(propertyValue);
            }

        }
    }

    class CalculatorClickHandler implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            String tag = view.getTag().toString();
            Toast toast = Toast.makeText(binding.getRoot().getContext(), tag, Toast.LENGTH_SHORT);
            toast.show();


            if (tag.equals("b0")) {
                String newText = "7";
                controller.digitPress(newText);
            }
            else if (tag.equals("b1")) {
                String newText = "8";
                controller.digitPress(newText);
            }
            else if (tag.equals("b2")) {
                String newText = "9";
                controller.digitPress(newText);
            }
            else if (tag.equals("b3")) {
                String newText = "√";
                controller.b3Press(newText);
            }
            else if (tag.equals("b4")) {
                String newText = "C";
                controller.b4Press(newText);
            }
            else if (tag.equals("b5")) {
                String newText = "4";
                controller.digitPress(newText);
            }
            else if (tag.equals("b6")) {
                String newText = "5";
                controller.digitPress(newText);
            }
            else if (tag.equals("b7")) {
                String newText = "6";
                controller.digitPress(newText);
            }
            else if (tag.equals("b8")) {
                String newText = "÷";
                controller.operPress(newText);
            }
            else if (tag.equals("b9")) {
                String newText = "%";
                controller.b9Press(newText);
            }
            else if (tag.equals("b10")) {
                String newText = "1";
                controller.digitPress(newText);
            }
            else if (tag.equals("b11")) {
                String newText = "2";
                controller.digitPress(newText);
            }
            else if (tag.equals("b12")) {
                String newText = "3";
                controller.digitPress(newText);
            }
            else if (tag.equals("b13")) {
                String newText = "*";
                controller.operPress(newText);
            }
            else if (tag.equals("b14")) {
                String newText = "-";
                controller.operPress(newText);
            }
            else if (tag.equals("b15")) {
                String newText = "Negate";
                controller.b15Press(newText);
            }
            else if (tag.equals("b16")) {
                String newText = "0";
                controller.digitPress(newText);
            }
            else if (tag.equals("b17")) {
                String newText = ".";
                controller.b17Press(newText);
            }
            else if (tag.equals("b18")) {
                String newText = "+";
                controller.operPress(newText);
            }
            else if (tag.equals("b19")) {
                String newText = "=";
                controller.b19Press(newText);
            }
        }

    }

}

