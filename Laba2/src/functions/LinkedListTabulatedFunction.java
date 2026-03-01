package functions;

import java.io.*;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedListTabulatedFunction implements TabulatedFunction, Externalizable {

    public static class LinkedListTabulatedFunctionFactory implements TabulatedFunctionFactory{

        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
            return new LinkedListTabulatedFunction(leftX, rightX, pointsCount);
        }

        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
            return new LinkedListTabulatedFunction(leftX, rightX, values);
        }

        public TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
            return new LinkedListTabulatedFunction(points);
        }
    }

    private class FunctionNode{
        private FunctionPoint point;
        private FunctionNode next;
        private FunctionNode prev;

        //Конструкторы
        public FunctionNode(FunctionPoint point) {
            this.point = point;
            next = null;
            prev = null;
        }

        public FunctionNode(FunctionPoint point, FunctionNode next, FunctionNode prev){
            this.point = point;
            this.next = next;
            this.prev = prev;
        }

        // Геттеры/сеттеры
        public FunctionPoint getPoint(){
            return point;
        }

        public void setPoint(FunctionPoint point){
            this.point = point;
        }
    }

    private transient FunctionNode head;
    private transient int pointsCount;
    private int lastUsedIndex;
    private FunctionNode lastUsedNode;

    //Конструкторы

    public LinkedListTabulatedFunction(){}

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(pointsCount);
        FunctionNode node = head.next;
        while(node != head){
            out.writeDouble(node.getPoint().getX());
            out.writeDouble(node.getPoint().getY());
            node = node.next;
        }
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        int count = in.readInt();

        creatingList();

        for (int i = 0; i < count; i++){
            double x = in.readDouble();
            double y = in.readDouble();
            addNodeToTail().setPoint(new FunctionPoint(x, y));
        }
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount){
        if (leftX >= rightX){
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        if(pointsCount < 2){
            throw new IllegalArgumentException("Количестов точек должно быть больше двух");
        }

        creatingList();

        // Шаг для равных интервалов по х
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++){
            double x = leftX + i * step;
            addNodeToTail().setPoint(new FunctionPoint(x,0));
        }
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values){
        if (leftX >= rightX){
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        if(values.length < 2){
            throw new IllegalArgumentException("Количестов точек должно быть больше двух");
        }

        creatingList();

        // Шаг для равных интервалов по х
        int valCount = values.length;
        double step = (rightX - leftX) / (valCount - 1);
        for ( int i = 0; i < valCount; i++) {
            double x = leftX + i * step;
            addNodeToTail().setPoint(new FunctionPoint(x, values[i]));
        }
    }

    public LinkedListTabulatedFunction(FunctionPoint[] points){
        int count = points.length;
        if(count < 2){
            throw new IllegalArgumentException("Количество точек должно быть больше двух");
        }

        creatingList();

        for (int i = 0; i < count; i++){
            if (i != count - 1 && points[i].getX() >= points[i+1].getX()){
                throw new IllegalArgumentException("Точки не упорядочены по возрастанию X");
            }

            addNodeToTail().setPoint(new FunctionPoint(points[i]));
        }
    }

    private void creatingList(){
        head = new FunctionNode(null);
        head.next = head;
        head.prev = head;
        pointsCount = 0;
        lastUsedIndex = -1;
        lastUsedNode = head;
    }

    //Методы
    private FunctionNode getNodeByIndex(int index) {
        if (index < 0 || index >= pointsCount){
            throw new FunctionPointIndexOutOfBoundsException("Индекс "+index+" выходит за границы [0,"+(pointsCount-1)+"]");
        }

        int startIndex;
        FunctionNode node;

        if (lastUsedIndex != -1 && Math.abs(index - lastUsedIndex) < Math.min( (index+1),(pointsCount - index) )){
            startIndex = lastUsedIndex;
            node = lastUsedNode;
        }
        else {
            if (index+1 < pointsCount - index){
                startIndex = 0;
                node = head.next;
            }
            else {
                startIndex = pointsCount - 1;
                node = head.prev;
            }
        }

        if (index > startIndex){
            for (int i = startIndex; i < index; i++){
                node = node.next;
            }
        }
        else {
            for (int i = startIndex; i > index; i--){
                node = node.prev;
            }
        }

        lastUsedIndex = index;
        lastUsedNode = node;

        return node;
    }

    private FunctionNode addNodeToTail() {
        FunctionNode node = new FunctionNode(null, head, head.prev);
        head.prev.next = node;
        head.prev = node;
        pointsCount++;

        lastUsedIndex = -1;

        return node;
    }

    private FunctionNode addNodeByIndex(int index){
        if (index <0 || index > pointsCount){
            throw new FunctionPointIndexOutOfBoundsException("Индекс "+index+" выходит за границы [0,"+(pointsCount-1)+"]");
        }

        if (index == pointsCount){
            return addNodeToTail();
        }

        FunctionNode rightNode = getNodeByIndex(index);
        FunctionNode leftNode = rightNode.prev;
        FunctionNode node = new FunctionNode(null,rightNode, leftNode);

        leftNode.next = node;
        rightNode.prev = node;
        pointsCount++;

        lastUsedIndex = -1;

        return node;

    }

    private FunctionNode deleteNodeByIndex(int index) {
        if (index <0 || index >= pointsCount){
            throw new FunctionPointIndexOutOfBoundsException("Индекс "+index+" выходит за границы [0,"+(pointsCount-1)+"]");
        }

        FunctionNode delNode = getNodeByIndex(index);
        FunctionNode leftNode = delNode.prev;
        FunctionNode rightNode = delNode.next;

        leftNode.next = rightNode;
        rightNode.prev = leftNode;
        pointsCount--;

        if (lastUsedIndex == index){
            lastUsedIndex = -1;
        }
        else if (lastUsedIndex > index){
            lastUsedIndex--;
        }

        return delNode;
    }

    //Методы TabulatedFunction

    public String toString(){
        StringBuilder str = new StringBuilder();
        FunctionNode node = head.next;

        while (node != head){
            str.append(node.getPoint().toString());
            if (node.next != head){
                str.append(", ");
            }

            node = node.next;
        }

        return str.toString();
    }

    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null) return false;

        if (o instanceof LinkedListTabulatedFunction){
            LinkedListTabulatedFunction func = (LinkedListTabulatedFunction) o;

            if (pointsCount != func.pointsCount) return false;

            FunctionNode thisPoint = head.next;
            FunctionNode funcPoint = func.head.next;
            while (thisPoint != head && funcPoint != func.head){
                if (!thisPoint.getPoint().equals(funcPoint.getPoint())) return false;
                thisPoint = thisPoint.next;
                funcPoint = funcPoint.next;
            }

            return true;
        }

        if (o instanceof TabulatedFunction){
            TabulatedFunction func = (TabulatedFunction) o;

            if (pointsCount != func.getPointsCount()) return false;

            FunctionNode node = head.next;
            int index = 0;
            while (node != head){
                if (!node.getPoint().equals(func.getPoint(index))) return false;
                node = node.next;
                index++;
            }
            return true;
        }

        return false;
    }

    public int hashCode(){
        int hash = 13 * pointsCount;

        FunctionNode node = head.next;
        while (node != head){
            hash = 31 * hash + node.getPoint().hashCode();
            node = node.next;
        }

        return hash;
    }

    public Object clone(){
        FunctionPoint[] clone = new FunctionPoint[pointsCount];

        FunctionNode node = head.next;
        int index = 0;
        while (node != head){
            clone[index] = (FunctionPoint) node.getPoint().clone();
            node = node.next;
            index++;
        }

        return  new LinkedListTabulatedFunction(clone);
    }

    public double getLeftDomainBorder() {
        return head.next.getPoint().getX();
    }

    public double getRightDomainBorder() {
        return head.prev.getPoint().getX();
    }

    public int getPointsCount() {
        return pointsCount;
    }

    public FunctionPoint getPoint(int index) {
        if (index < 0 || index >= pointsCount){
            throw new FunctionPointIndexOutOfBoundsException("Индекс "+ index + " выходит за границы [0,"+(pointsCount-1)+"]");
        }

        return new FunctionPoint(getNodeByIndex(index).getPoint());
    }

    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount){
            throw new FunctionPointIndexOutOfBoundsException("Индекс "+ index + " выходит за границы [0,"+(pointsCount-1)+"]");
        }

        FunctionNode node = getNodeByIndex(index);

        if (index == 0 && (pointsCount > 1 && point.getX() > node.next.getPoint().getX() )){
            throw new InappropriateFunctionPointException("Координата Х точки должна быть меньше следующей точки");
        }
        else if (index == pointsCount - 1 && (point.getX() < node.prev.getPoint().getX())){
            throw new InappropriateFunctionPointException("Координата Х точки должна быть больше предыдущей точки");
        }
        else if (index > 0 && index < pointsCount - 1) {
            if (point.getX() < node.prev.getPoint().getX() || point.getX() > node.next.getPoint().getX()){
                throw new InappropriateFunctionPointException("Координата Х точки должна быть больше предыдущей и меньше следующей точки");
            }
        }

        node.setPoint(new FunctionPoint(point));
    }

    public double getPointX(int index) {
        if (index < 0 || index >= pointsCount){
            throw new FunctionPointIndexOutOfBoundsException("Индекс "+ index + " выходит за границы [0,"+(pointsCount-1)+"]");
        }

        return getNodeByIndex(index).getPoint().getX();
    }

    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount){
            throw new FunctionPointIndexOutOfBoundsException("Индекс "+ index + " выходит за границы [0,"+(pointsCount-1)+"]");
        }

        FunctionNode node = getNodeByIndex(index);

        if (index == 0 && (pointsCount > 1 && x > node.next.getPoint().getX() )){
            throw new InappropriateFunctionPointException("Координата Х точки должна быть меньше следующей точки");
        }
        else if (index == pointsCount - 1 && x < node.prev.getPoint().getX()){
            throw new InappropriateFunctionPointException("Координата Х точки должна быть больше предыдущей точки");
        }
        else if (index > 0 && index < pointsCount - 1){
            if (x < node.prev.getPoint().getX() || x > node.next.getPoint().getX()){
                throw new InappropriateFunctionPointException("Координата Х точки должна быть больше предыдущей и меньше следующей точки");
            }
        }

        double y = node.getPoint().getY();
        node.setPoint(new FunctionPoint(x, y));
    }

    public double getPointY(int index) {
        if (index < 0 || index >= pointsCount){
            throw new FunctionPointIndexOutOfBoundsException("Индекс "+ index + " выходит за границы [0,"+(pointsCount-1)+"]");
        }

        return getNodeByIndex(index).getPoint().getY();
    }

    public void setPointY(int index, double y) {
        if (index < 0 || index >= pointsCount){
            throw new FunctionPointIndexOutOfBoundsException("Индекс "+ index + " выходит за границы [0,"+(pointsCount-1)+"]");
        }

        FunctionNode node = getNodeByIndex(index);
        double x = node.getPoint().getX();
        node.setPoint(new FunctionPoint(x, y));
    }

    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()){
            return Double.NaN;
        }

        FunctionNode node = head.next;
        while (node.next != head){
            double x1 = node.getPoint().getX();
            double x2 = node.next.getPoint().getX();
            if (x >= x1 && x <= x2){
                double y1 = node.getPoint().getY();
                double y2 = node.next.getPoint().getY();
                return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
            }

            node = node.next;
        }

        return Double.NaN;
    }

    public void deletePoint(int index) {
        if (index < 0 || index >= pointsCount){
            throw new FunctionPointIndexOutOfBoundsException("Индекс "+ index + " выходит за границы [0,"+(pointsCount-1)+"]");
        }
        if (pointsCount < 3){
            throw new IllegalStateException("Нельзя удалить точку если их количество меньше трёх");
        }

        deleteNodeByIndex(index);
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        FunctionNode node = head.next;
        boolean flag = true;
        int index = 0;
        while (node != head && flag){
            if (node.getPoint().getX() > point.getX()){
                flag = false;
                index--;
            }
            index++;
            node = node.next;
        }

        if (index != 0 && (Math.abs(getNodeByIndex(index-1).getPoint().getX() - point.getX()) <1e-9) ){
            throw new InappropriateFunctionPointException("Точка "+point.getX()+" уже существует");
        }

        addNodeByIndex(index).setPoint(new FunctionPoint(point));
    }

    public Iterator<FunctionPoint> iterator() {
        return new Iterator<FunctionPoint>(){

            private FunctionNode node = head.next;

            public boolean hasNext() {
                return node != head;
            }

            public FunctionPoint next() {
                if (!hasNext()){
                    throw new NoSuchElementException();
                }

                FunctionPoint point = node.point;
                node = node.next;

                return new FunctionPoint(point);
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}