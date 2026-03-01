package functions.meta;

import functions.Function;
// Масштабирование
public class Scale implements Function {
    private Function func;
    private double abscissa;
    private double ordinate;

    public Scale(Function func, double abscissa, double ordinate){
        this.func = func;
        this.abscissa = abscissa;
        this.ordinate = ordinate;
    }

    public double getLeftDomainBorder() {
        if (abscissa > 0){
            return getLeftDomainBorder()/abscissa;
        }
        else{
            return getRightDomainBorder()/abscissa;
        }
    }

    public double getRightDomainBorder() {
        if (abscissa > 0){
            return getRightDomainBorder()/abscissa;
        }
        else{
            return getLeftDomainBorder()/abscissa;
        }
    }

    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()){
            throw new IllegalArgumentException("Точка x = " + x + " не принадлежит области определения масштабированной функции");
        }

        return ordinate * func.getFunctionValue(x * abscissa);
    }
}