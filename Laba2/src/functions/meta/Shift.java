package functions.meta;
import functions.Function;

// Сдвиг
public class Shift implements Function{
    private Function func;
    private double abscissa;
    private double ordinate;

    public Shift(Function func, double abscissa, double ordinate){
        this.func = func;
        this.abscissa = abscissa;
        this.ordinate = ordinate;
    }

    public double getLeftDomainBorder() {
        return getLeftDomainBorder()+abscissa;
    }

    public double getRightDomainBorder() {
        return getRightDomainBorder()+abscissa;
    }

    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()){
            throw new IllegalArgumentException("Точка x = " + x + " не принадлежит области определения сдвинутой функции");
        }

        return func.getFunctionValue(x - abscissa) + ordinate;
    }
}
