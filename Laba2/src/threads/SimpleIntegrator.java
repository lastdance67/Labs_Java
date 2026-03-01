package threads;

import functions.Function;
import functions.Functions;

public class SimpleIntegrator implements Runnable{
    private Task task;

    public SimpleIntegrator(Task task){
        this.task = task;
    }

    public void run(){
        for (int i = 0; i < task.getNumberTask(); i++){
            synchronized (task){
                if (task.getFunc() == null){
                    continue;
                }

                Function func = task.getFunc();
                double left = task.getLeft();
                double right = task.getRight();
                double step = task.getStep();

                double result = Functions.integrate(func, left, right, step);
                System.out.printf("Result №"+i+" <%.2f> <%.2f> <%.2f> <%.2f>%n", left, right, step, result);
            }
        }
    }
}