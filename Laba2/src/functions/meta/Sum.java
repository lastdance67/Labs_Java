package functions.meta;
import functions.Function;

public class Sum implements Function{
    private Function func1;
    private Function func2;

    public Sum(Function func1, Function func2){
        this.func1 = func1;
        this.func2 = func2;
    }

    public double getLeftDomainBorder() {
        return Math.max(func1.getLeftDomainBorder(), func2.getLeftDomainBorder());
    }

    public double getRightDomainBorder() {
        return Math.min(func1.getRightDomainBorder(), func2.getRightDomainBorder());
    }

    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()){
            throw new IllegalArgumentException("Точка x = " + x + " не принадлежит области определения суммы функций");
        }

        return func1.getFunctionValue(x) + func2.getFunctionValue(x);
    }
}
