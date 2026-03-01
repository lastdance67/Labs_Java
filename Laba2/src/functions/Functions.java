package functions;
import functions.meta.*;

public class Functions {

    private Functions(){}

    public static Function shift(Function f, double shiftX, double shiftY){
        return new Shift(f, shiftX, shiftY);
    }

    public static Function scale(Function f, double scaleX, double scaleY){
        return new Scale(f, scaleX, scaleY);
    }

    public static Function power(Function f, double power){
        return new Power(f, power);
    }

    public static Function sum(Function f1, Function f2){
        return new Sum(f1, f2);
    }

    public static Function mult(Function f1, Function f2){
        return new Mult(f1, f2);
    }

    public static Function composition(Function f1, Function f2){
        return new Composition(f1, f2);
    }

    public static double integrate(Function f, double left, double right, double step){
        if (left < f.getLeftDomainBorder() || right > f.getRightDomainBorder()) throw  new IllegalArgumentException("интервал интегрирования выходит за границы области определения функции");

        double sum = 0;
        for (double x1 = left; x1 < right; x1 += step){
            double x2 = Math.min(x1 + step, right);
            sum += (f.getFunctionValue(x2) + f.getFunctionValue(x1)) * (x2 - x1) / 2;
        }

        return sum;
    }
}
