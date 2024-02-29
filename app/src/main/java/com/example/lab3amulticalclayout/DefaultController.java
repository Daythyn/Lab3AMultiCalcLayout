package com.example.lab3amulticalclayout;

import android.util.Log;

public class DefaultController extends AbstractController
{

    public static final String ELEMENT_BUTTONPRESS_PROPERTY = "ButtonPressed";
    public static final String ELEMENT_DIGITPRESS_PROPERTY = "Digit";
    public static final String ELEMENT_OPER_PROPERTY = "Oper";
    public static final String ELEMENT_B3_PROPERTY = "SquareRoot";
    public static final String ELEMENT_B4_PROPERTY = "Clear";
    public static final String ELEMENT_B9_PROPERTY = "ModPer";
    public static final String ELEMENT_B15_PROPERTY = "Negate";
    public static final String ELEMENT_B17_PROPERTY = "Decimal";
    public static final String ELEMENT_B19_PROPERTY = "Equals";

    public void digitPress(String newText) {
        setModelProperty(ELEMENT_DIGITPRESS_PROPERTY, newText);
        //Log.i("MainActivity", newText + "  Controller");
    }
    public void operPress(String newText) {
        setModelProperty(ELEMENT_OPER_PROPERTY, newText);
    }
    public void b3Press(String newText) {
        setModelProperty(ELEMENT_B3_PROPERTY, newText);
    }
    public void b4Press(String newText) {
        setModelProperty(ELEMENT_B4_PROPERTY, newText);
    }
    public void b9Press(String newText) {
        setModelProperty(ELEMENT_B9_PROPERTY, newText);
    }
    public void b15Press(String newText) {
        setModelProperty(ELEMENT_B15_PROPERTY, newText);
    }
    public void b17Press(String newText) {
        setModelProperty(ELEMENT_B17_PROPERTY, newText);
    }
    public void b19Press(String newText) {
        setModelProperty(ELEMENT_B19_PROPERTY, newText);
    }
}
