package functions.meta;

import functions.Function;

public class Power implements Function {
    private Function func;
    private double power;

    public Power(Function func, double power){
        this.func = func;
        this.power = power;
    }

    public double getLeftDomainBorder() {
        return func.getLeftDomainBorder();
    }

    public double getRightDomainBorder() {
        return func.getRightDomainBorder();
    }

    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()){
            throw new IllegalArgumentException("Точка x = " + x + " не принадлежит области определения функции");
        }

        return Math.pow(func.getFunctionValue(x), power);
    }
}
