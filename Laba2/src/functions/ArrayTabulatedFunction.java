package functions;

import java.io.*;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayTabulatedFunction implements TabulatedFunction, Externalizable {

    public static class ArrayTabulatedFunctionFactory implements TabulatedFunctionFactory{

        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
            return new ArrayTabulatedFunction(leftX, rightX, pointsCount);
        }

        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
            return new ArrayTabulatedFunction(leftX, rightX, values);
        }

        public TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
            return new ArrayTabulatedFunction(points);
        }
    }

    private FunctionPoint[] points;
    private int pointsCount;

    // Конструкторы

    public ArrayTabulatedFunction(){}

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(pointsCount);
        for (int i = 0; i < pointsCount; i++){
            out.writeDouble(points[i].getX());
            out.writeDouble(points[i].getY());
        }
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        pointsCount = in.readInt();
        points = new FunctionPoint[pointsCount];
        for (int i = 0; i < pointsCount; i++){
            double x = in.readDouble();
            double y = in.readDouble();
            points[i] = new FunctionPoint(x, y);
        }
    }

    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount){
        if (leftX >= rightX){
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }

        this.pointsCount = pointsCount;

        if(pointsCount < 2){
            throw new IllegalArgumentException("Количестов точек должно быть больше двух");
        }

        points = new FunctionPoint[pointsCount];

        // Шаг для равных интервалов по х
        double step = (rightX - leftX) / (pointsCount - 1);
        for ( int i = 0; i < pointsCount; i++){
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x,0);
        }
    }

    public ArrayTabulatedFunction(double leftX, double rightX, double[] values){
        if (leftX >= rightX){
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }

        this.pointsCount = values.length;

        if(pointsCount < 2){
            throw new IllegalArgumentException("Количестов точек должно быть больше двух");
        }

        points = new FunctionPoint[pointsCount];

        // Шаг для равных интервалов по х
        double step = (rightX - leftX) / (pointsCount - 1);
        for ( int i = 0; i < pointsCount; i++){
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x, values[i]);
        }
    }

    public ArrayTabulatedFunction(FunctionPoint[] points){
        pointsCount = points.length;
        if (pointsCount < 2){
            throw new IllegalArgumentException("Количество точек должно быть больше двух");
        }

        this.points = new FunctionPoint[pointsCount];

        for (int i = 0; i < pointsCount;i++){
            if (i != pointsCount - 1 && points[i].getX() >= points[i+1].getX()){
                throw new IllegalArgumentException("Точки не упорядочены по возрастанию X");
            }

            this.points[i] = new FunctionPoint(points[i]);
        }
    }

    // Методы
    public double getLeftDomainBorder(){
        return points[0].getX();
    }

    public double getRightDomainBorder(){
        return points[pointsCount - 1].getX();
    }

    public  int getPointsCount(){
        return pointsCount;
    }

    public String toString(){
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < pointsCount; i++){
            str.append(points[i].toString());
            if (i < pointsCount - 1) {
                str.append(", ");
            }
        }

        return str.toString();
    }

    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null) return false;

        if (o instanceof ArrayTabulatedFunction){
            ArrayTabulatedFunction func = (ArrayTabulatedFunction) o;

            if (pointsCount != func.pointsCount) return false;

            for (int i = 0; i < pointsCount; i++){
                if (!points[i].equals(func.points[i])) return false;
            }

            return true;
        }

        if (o instanceof TabulatedFunction){
            TabulatedFunction func = (TabulatedFunction) o;

            if(pointsCount != func.getPointsCount()) return false;

            for (int i = 0; i < pointsCount; i++){
                if (!points[i].equals(func.getPoint(i))) return false;
            }

            return true;
        }

        return false;
    }

    public int hashCode(){
        int hash = 13 * pointsCount;

        for (int i = 0; i < pointsCount; i++){
            hash = 31 * hash + points[i].hashCode();
        }

        return hash;
    }

    public Object clone(){
        FunctionPoint[] clone = new FunctionPoint[pointsCount];

        for (int i = 0; i < pointsCount; i++){
            clone[i] = (FunctionPoint) points[i].clone();
        }

        return new ArrayTabulatedFunction(clone);
    }

    public FunctionPoint getPoint(int index){
        if (index < 0 || index >= pointsCount){
            throw new FunctionPointIndexOutOfBoundsException("Индекс "+ index + " выходит за границы [0,"+(pointsCount-1)+"]");
        }

        return new FunctionPoint(points[index]);
    }

    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount){
            throw new FunctionPointIndexOutOfBoundsException("Индекс "+ index + " выходит за границы [0,"+(pointsCount-1)+"]");
        }

        if (index == 0 && (pointsCount > 1 && point.getX() > points[1].getX()) ) {
            throw new InappropriateFunctionPointException("Координата Х точки должна быть меньше следующей точки");
        }
        else if (index == pointsCount - 1 && (point.getX() < points[index - 1].getX()) ){
            throw new InappropriateFunctionPointException("Координата Х точки должна быть больше предыдущей точки");
        }
        else if (index > 0 && index < pointsCount -1){
            if (point.getX() < points[index - 1].getX() || point.getX() > points[index + 1].getX()) {
                throw new InappropriateFunctionPointException("Координата Х точки должна быть больше предыдущей и меньше следующей точки");
            }
        }
        points[index] = new FunctionPoint(point);
    }

    public double getPointX(int index){
        if (index < 0 || index >= pointsCount){
            throw new FunctionPointIndexOutOfBoundsException("Индекс "+ index + " выходит за границы [0,"+(pointsCount-1)+"]");
        }

        return points[index].getX();
    }

    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount){
            throw new FunctionPointIndexOutOfBoundsException("Индекс "+ index + " выходит за границы [0,"+(pointsCount-1)+"]");
        }

        if (index == 0 && (pointsCount > 1 && x > points[1].getX()) ) {
            throw new InappropriateFunctionPointException("Координата Х точки должна быть меньше следующей точки");
        }
        else if (index == pointsCount - 1 && (x < points[index - 1].getX()) ){
            throw new InappropriateFunctionPointException("Координата Х точки должна быть больше предыдущей точки");
        }
        else if (index > 0 && index < pointsCount - 1){
            if (x < points[index - 1].getX() || x > points[index + 1].getX()) {
                throw new InappropriateFunctionPointException("Координата Х точки должна быть больше предыдущей и меньше следующей точки");
            }
        }
        points[index].setX(x);
    }

    public double getPointY(int index){
        if (index < 0 || index >= pointsCount){
            throw new FunctionPointIndexOutOfBoundsException("Индекс "+ index + " выходит за границы [0,"+(pointsCount-1)+"]");
        }

        return points[index].getY();
    }

    public void setPointY(int index, double y) {
        if (index < 0 || index >= pointsCount){
            throw new FunctionPointIndexOutOfBoundsException("Индекс "+ index + " выходит за границы [0,"+(pointsCount-1)+"]");
        }

        points[index].setY(y);
    }

    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }

        for (int i = 0; i < pointsCount - 1; i++){
            double x1 = points[i].getX();
            double x2 = points[i+1].getX();
            if (x >= x1 && x <= x2){
                double y1 = points[i].getY();
                double y2 = points[i+1].getY();
                return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            }
        }

        return Double.NaN;
    }

    public void deletePoint(int index){
        if (index < 0 || index >= pointsCount){
            throw new FunctionPointIndexOutOfBoundsException("Индекс "+ index + " выходит за границы [0,"+(pointsCount-1)+"]");
        }
        if (pointsCount < 3){
            throw new IllegalStateException("Нельзя удалить точку если их количество меньше трёх");
        }

        System.arraycopy(points, index + 1, points, index, pointsCount - 1 - index);
        pointsCount--;
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        if (pointsCount == points.length){
            FunctionPoint[] newPoints = new FunctionPoint[pointsCount * 2];
            System.arraycopy(points,0,newPoints,0, pointsCount);
            points = newPoints;
        }

        boolean flag = true;
        int index = 0;
        while(flag && index < pointsCount){
            if(points[index].getX() > point.getX()){
                flag = false;
                index--;
            }
            index++;
        }

        if (index != 0 && (Math.abs(points[index-1].getX() - point.getX()) <1e-9) ){
            throw new InappropriateFunctionPointException("Точка "+point.getX()+" уже существует");
        }

        if (flag){
            points[index] = new FunctionPoint(point);
        }
        else{
            System.arraycopy(points, index, points, index + 1, pointsCount - index);
            points[index] = new FunctionPoint(point);
        }
        pointsCount++;
    }

    public Iterator<FunctionPoint> iterator() {
        return new Iterator<FunctionPoint>() {

            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < pointsCount;
            }

            @Override
            public FunctionPoint next() {
                if (!hasNext()){
                    throw new NoSuchElementException();
                }

                FunctionPoint p = points[index];
                index +=2;
                return new FunctionPoint(p);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
