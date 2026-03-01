package threads;

import functions.Function;
import functions.basic.Log;

public class SimpleGenerator implements Runnable{
    private Task task;

    public SimpleGenerator(Task task){
        this.task = task;
    }

    public void run(){
        for (int i = 0; i < task.getNumberTask(); i++){
            synchronized (task){
                double base = 1 + Math.random() * 9;
                Function log = new Log(base);

                task.setLeft(Math.random() * 100);
                task.setRight(100 + Math.random() * 100);
                task.setStep(Math.random());
                task.setFunc(log);

                System.out.printf("Source №"+i+" <%.2f> <%.2f> <%.2f>%n", task.getLeft(), task.getRight(), task.getStep());
            }
        }
    }
}