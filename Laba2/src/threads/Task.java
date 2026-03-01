package threads;

import functions.Function;

public class Task {
    private Function func;
    private double left;
    private double right;
    private double step;
    private int numberTask;

    public Task (int number){
        numberTask = number;
    }

    public Function getFunc(){
        return func;
    }

    public void setFunc(Function func){
        this.func = func;
    }

    public double getLeft(){
        return left;
    }

    public void setLeft(double left){
        this.left = left;
    }

    public double getRight(){
        return right;
    }

    public void setRight(double right){
        this.right = right;
    }

    public double getStep(){
        return step;
    }

    public void setStep(double step){
        this.step = step;
    }

    public int getNumberTask(){
        return numberTask;
    }
}
