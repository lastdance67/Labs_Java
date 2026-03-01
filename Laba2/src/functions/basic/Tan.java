package functions.basic;

public class Tan extends TrigonometricFunction{

    public double getFunctionValue(double x){
        if (Math.cos(x) == 0){
            throw new ArithmeticException("Тангенс не определен в точке х = "+x);
        }
        return Math.tan(x);
    }
}
