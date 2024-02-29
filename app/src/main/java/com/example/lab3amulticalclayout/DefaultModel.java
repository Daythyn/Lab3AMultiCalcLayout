package com.example.lab3amulticalclayout;

import static com.example.lab3amulticalclayout.CalcState.*;

import android.util.Log;
import android.widget.Switch;

import java.math.BigDecimal;
import java.math.MathContext;

public class DefaultModel extends AbstractModel {

    public static final String TAG = "DefaultModel";
    private static final int DIGIT_LIMIT = 10;
    private static final MathContext PRECISION = new MathContext(15);

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

        fireChange(calcState);
    }

    public void setSquareRoot(String oper){

        if(calcState == LEFTOPER){
                left = String.valueOf(Math.sqrt(Double.parseDouble(left)));
                if(left.equals("NaN")){
                    calcState = ERROR;
                }

        }

        else if(calcState == RIGHTOPER){
            right = String.valueOf(Math.sqrt(Double.parseDouble(right)));
            if(left.equals("NaN")){
                calcState = ERROR;
            }
        }

        else if(calcState == RESULT){
            left = String.valueOf(Math.sqrt(Double.parseDouble(left)));
            if(left.equals("NaN")){
                calcState = ERROR;
            }
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

        boolean accountantFired = false;

        if(calcState == LEFTOPER){
            operator = oper;
            calcState = SELOPER;
        }

        else if(calcState == SELOPER){
            operator = oper;
        }

        if(calcState == RIGHTOPER){
            operator = oper;
            left = doMath(left, operator, right, PRECISION).toString();
            right = "";
            calcState = RIGHTOPER;
            accountantFired = true;
            fireChange(LEFTOPER);
        }

        else if(calcState == RESULT){
            operator = oper;
            right = "";
            calcState = SELOPER;
        }

        if(!accountantFired){
            fireChange(calcState);
        }
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
            right = (new BigDecimal(left).multiply(new BigDecimal(right).divide(new BigDecimal(100),PRECISION),PRECISION)).toString(); //get percent of left
        }

        else if(calcState == RESULT){
            operator = special;
            right = "";
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
            left = appendDigit(left, "0" + special, DIGIT_LIMIT + 1);
            calcState = LEFTOPER;
        }

        else if(calcState == LEFTOPER){
            if(!left.contains(special)) {
                left = appendDigit(left, special, DIGIT_LIMIT + 1);
            }
        }

        else if(calcState == SELOPER){
            right = appendDigit(right, "0" + special, DIGIT_LIMIT + 1);
            calcState = RIGHTOPER;
        }

        else if(calcState == RIGHTOPER){
            right = appendDigit(right, special, DIGIT_LIMIT + 1);
        }

        else if(calcState == RESULT){
            left = appendDigit(left, special, DIGIT_LIMIT + 1);
        }

        fireChange(calcState);
    }

    public void setEquals(String special){



        if(calcState == SELOPER){
            right = left;
            left = doMath(left, operator, right, PRECISION).toString();
            calcState = RESULT;
        }

        else if(calcState == RIGHTOPER){
            left = doMath(left, operator, right, PRECISION).toString();
            calcState = RESULT;
        }

        else if(calcState == RESULT){
            left = doMath(left, operator, right, PRECISION).toString();
        }

        fireChange(calcState);
    }

    private String appendDigit(String bd, String digit, int dLimit){
        //Log.i("MainActivity", "Digit Append  " + bd);
        String value = bd;
        String justDigits = bd.replace("-","").replace(".","");

        if(justDigits.length() < dLimit) {
            StringBuilder bdString = new StringBuilder();
            bdString.append(bd).append(digit);
            value = bdString.toString();
        }
        //Log.i("MainActivity", "Finished Append   " + value);
        return value;
    }

    private void fireChange(CalcState state){
        //Log.i("MainActivity", "Firing change");
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
                newOutput = left;
                firePropertyChange(DefaultController.ELEMENT_BUTTONPRESS_PROPERTY, oldOutput,newOutput);
                break;
        }

    }

    private BigDecimal doMath(String operand1, String operator, String operand2, MathContext precision){
        BigDecimal bd1 = new BigDecimal(operand1);
        BigDecimal bd2 = new BigDecimal(operand2);
        BigDecimal result = null;

        if(operator.equals("÷")){
            if(!bd2.equals(new BigDecimal(0))) {
                result = bd1.divide(bd2, precision);
            }
            else{
                left = "Divide by Zero";
                calcState = ERROR;
                fireChange(calcState);
            }
        }
        else if(operator.equals("%")){
            result = bd1.remainder(bd2,precision);
        }
        else if(operator.equals("*")){
            result = bd1.multiply(bd2,precision);
        }
        else if(operator.equals("-")){
            result = bd1.subtract(bd2, precision);
        }
        else if(operator.equals("+")){
            result = bd1.add(bd2,precision);
        }

        return result;
    }

}
