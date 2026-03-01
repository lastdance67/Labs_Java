package functions.basic;
import functions.Function;

public class Log implements Function{
    private double base;

    public Log(double base){
        if (base <= 0){
            throw new IllegalArgumentException("Основание логорифма должно быть положительным");
        }
        if (base == 1){
            throw new IllegalArgumentException("Основание логорифма не должно быть равным еденице");
        }

        this.base = base;
    }

    public double getLeftDomainBorder() {
        return 0;
    }

    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }

    public double getFunctionValue(double x) {
        if (x <= 0){
            throw new IllegalArgumentException("Аргумент логарифма должен быть положительным");
        }
        return Math.log(x) / Math.log(base);
    }
}
