package com.example.lab3amulticalclayout;

import java.math.BigDecimal;

public class Operand {
    private boolean isLeft;
    BigDecimal number;

    public Operand (boolean left,BigDecimal number){
        this.isLeft = left;
        this.number = number;

    }

    public boolean isLeft() {
        return isLeft;
    }

    public void setLeft(boolean left) {
        this.isLeft = left;
    }

    public BigDecimal getNumber() {
        return number;
    }

    public void setNumber(BigDecimal number) {
        this.number = number;
    }
}
