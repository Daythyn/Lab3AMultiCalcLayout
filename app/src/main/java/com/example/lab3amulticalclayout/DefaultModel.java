package com.example.lab3amulticalclayout;

import static com.example.lab3amulticalclayout.CalcState.*;

import android.util.Log;
import android.widget.Switch;

import java.math.BigDecimal;
import java.math.MathContext;

public class DefaultModel extends AbstractModel {

    public static final String TAG = "DefaultModel";
    private static final int DIGIT_LIMIT = 10;
    private static final MathContext PRECISION = new MathContext(10);

    private String left;
    private String right;
    private String operator;

    private String oldOutput;
    private String newOutput;
    private StringBuilder number;
    private boolean hasDecimal;
    private boolean isNegative;
    private CalcState calcState;

    public void initDefault(){
        calcState = CLEAR;

        number = new StringBuilder();
        operator = "";

        left = "";
        right = "";

        hasDecimal = false;
        isNegative = false;
    }


    public void setDigit(String digit){

        Log.i("MainActivity", digit + "    Model");

        if(calcState == CLEAR){
            calcState = LEFTOPER;
            left = (appendDigit(left, digit, DIGIT_LIMIT));
        }

        else if(calcState == LEFTOPER){
            left = appendDigit(left, digit, DIGIT_LIMIT);
        }

        else if(calcState == SELOPER){
            calcState = RIGHTOPER;
            right = appendDigit(right, digit, DIGIT_LIMIT);
        }

        else if(calcState == RIGHTOPER){
            right = appendDigit(right, digit, DIGIT_LIMIT);
        }

        else if(calcState == RESULT){
            left = appendDigit(left, digit, DIGIT_LIMIT);
        }

        Log.i("MainActivity", "Womp Womp");
        fireChange(calcState);
    }

    public void setSquareRoot(String oper){

        if(calcState == LEFTOPER){

        }

        else if(calcState == RIGHTOPER){

        }

        else if(calcState == RESULT){

        }

        fireChange(calcState);
    }  //DO THIS

    public void setClear(String special){


        if(calcState == LEFTOPER){
        initDefault();
        calcState = CLEAR;
        }

        else if(calcState == SELOPER){
            initDefault();
            calcState = CLEAR;
        }

        else if(calcState == RIGHTOPER){
            initDefault();
            calcState = CLEAR;
        }

        else if(calcState == RESULT){
            initDefault();
            calcState = CLEAR;
        }

        else if(calcState == ERROR){
            initDefault();
            calcState = CLEAR;
        }

        fireChange(calcState);
    }

    public void setOper(String oper){

        if(calcState == LEFTOPER){
            operator = oper;
            calcState = SELOPER;
        }

        else if(calcState == SELOPER){
            operator = oper;
        }

        else if(calcState == RESULT){
            operator = oper;
            calcState = SELOPER;
        }

        fireChange(calcState);
    }

    public void setModPer(String special){

        if(calcState == LEFTOPER){
            operator = special;
            calcState = SELOPER;
        }

        else if(calcState == SELOPER){
            operator = special;
        }

        else if(calcState == RIGHTOPER){
            right = (new BigDecimal(left).multiply(new BigDecimal(right).divide(new BigDecimal(100))).round(PRECISION)).toString(); //get percent of left
        }

        else if(calcState == RESULT){
            operator = special;
            calcState = SELOPER;
        }

        fireChange(calcState);
    }

    public void setNegate(String special){

        if(calcState == LEFTOPER){
            left = (new BigDecimal(left)).negate().toString();
        }

        else if(calcState == RIGHTOPER){
            right = (new BigDecimal(right)).negate().toString();
        }

        else if(calcState == RESULT){
            left = (new BigDecimal(left)).negate().toString();
        }

        fireChange(calcState);
    }

    public void setDecimal(String special){
        Log.i("MainActivity", "Decimal Time!");

        if(calcState == CLEAR){
            left = appendDigit(left, "0" + special, DIGIT_LIMIT);
            calcState = LEFTOPER;
        }

        else if(calcState == LEFTOPER){
            if(!left.contains(special)) {
                left = appendDigit(left, special, DIGIT_LIMIT);
            }
        }

        else if(calcState == RIGHTOPER){
            right = appendDigit(right, special, DIGIT_LIMIT);
        }

        else if(calcState == RESULT){
            left = appendDigit(left, special, DIGIT_LIMIT);
        }

        fireChange(calcState);
    }

    public void setEquals(String special){


        if(calcState == LEFTOPER){

        }

        else if(calcState == SELOPER){
        }

        else if(calcState == RIGHTOPER){

        }

        else if(calcState == RESULT){

        }

        fireChange(calcState);
    }

    private String appendDigit(String bd, String digit, int dLimit){
        Log.i("MainActivity", "Digit Append");
        String value = null;
        if(bd.length() < dLimit) {
            StringBuilder bdString = new StringBuilder();
            bdString.append(bd).append(digit);
            value = bdString.toString();
        }
        Log.i("MainActivity", "Finished Append");
        return value;
    }

    private void fireChange(CalcState state){
        Log.i("MainActivity", "Firing change");
        oldOutput = newOutput;

        switch (state) {
            case CLEAR:
                newOutput = left;
                firePropertyChange(DefaultController.ELEMENT_BUTTONPRESS_PROPERTY, oldOutput,newOutput);
                break;
            case LEFTOPER:
                newOutput = left;
                firePropertyChange(DefaultController.ELEMENT_BUTTONPRESS_PROPERTY, oldOutput,newOutput);
                break;
            case SELOPER:
                newOutput = left + operator;
                firePropertyChange(DefaultController.ELEMENT_BUTTONPRESS_PROPERTY, oldOutput,newOutput);
                break;
            case RIGHTOPER:
                newOutput = right;
                firePropertyChange(DefaultController.ELEMENT_BUTTONPRESS_PROPERTY, oldOutput,newOutput);
                break;
            case RESULT:
                newOutput = left;
                firePropertyChange(DefaultController.ELEMENT_BUTTONPRESS_PROPERTY, oldOutput,newOutput);
                break;
            case ERROR:
                newOutput = "ERROR";
                firePropertyChange(DefaultController.ELEMENT_BUTTONPRESS_PROPERTY, oldOutput,newOutput);
                break;
        }

        Log.i("MainActivity", oldOutput.toString() + "   |   " + newOutput.toString());
    }

}
