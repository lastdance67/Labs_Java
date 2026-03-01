package functions;

public class FunctionPoint implements Cloneable {
    private double x;
    private double y;

    // Конструкторы
    public FunctionPoint (double x, double y){
        this.x = x;
        this.y = y;
    }

    public FunctionPoint (FunctionPoint point){
        this.x = point.x;
        this.y = point.y;
    }

    public FunctionPoint(){
        x = 0;
        y = 0;
    }

    // Геттеры и сеттеры
    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public void setX (double x){
        this.x = x;
    }

    public void setY (double y){
        this.y = y;
    }

    public String toString() {
        return "("+ x + "; " + y + ")";
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FunctionPoint point = (FunctionPoint) o;
        return Double.compare(point.x, x) == 0 && Double.compare(point.y, y) == 0;
    }

    public int hashCode() {
        long xBits = Double.doubleToLongBits(x);
        long yBits = Double.doubleToLongBits(y);

        int xHash = (int)(xBits ^ (xBits >>> 32));
        int yHash = (int)(yBits ^ (yBits >>> 32));

        return 31*xHash ^ 37*yHash;
    }

    public Object clone() {
        return new FunctionPoint(x, y);
    }
}