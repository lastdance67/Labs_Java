package functions.meta;
import functions.Function;

public class Composition implements Function{
    private Function exterFunc;
    private Function innerFunc;

    public Composition(Function func1, Function func2){
        exterFunc = func1;
        innerFunc = func2;
    }

    public double getLeftDomainBorder() {
        return exterFunc.getLeftDomainBorder();
    }

    public double getRightDomainBorder() {
        return exterFunc.getRightDomainBorder();
    }

    public double getFunctionValue(double x) {
        if (x < innerFunc.getLeftDomainBorder() || x > innerFunc.getRightDomainBorder()){
            throw new IllegalArgumentException("Точка x = " + x + " не принадлежит области определения композиции");
        }

        double innerValue = innerFunc.getFunctionValue(x);

        if (innerValue < exterFunc.getLeftDomainBorder() || innerValue > exterFunc.getRightDomainBorder()){
            throw new IllegalArgumentException("Значение внутренней функции " + innerValue + " не принадлежит области определения внешней функции");
        }

        return exterFunc.getFunctionValue(innerValue);
    }
}