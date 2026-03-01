package threads;

import functions.Function;
import functions.basic.Log;

public class Generator  extends Thread{
    private Task task;
    private  Semaphore semaphore;

    public Generator(Task task, Semaphore semaphore){
        this.task = task;
        this.semaphore = semaphore;
    }

    public void run() {
        try{
            for(int i = 0; i < task.getNumberTask(); i++){
                if(isInterrupted()){
                    return;
                }

                semaphore.beginWrite();

                double base = 1 + Math.random() * 9;
                Function log = new Log(base);

                task.setLeft(Math.random() * 100);
                task.setRight(100 + Math.random() * 100);
                task.setStep(Math.random());
                task.setFunc(log);

                System.out.printf("Source №"+i+" <%.2f> <%.2f> <%.2f>%n", task.getLeft(), task.getRight(), task.getStep());

                semaphore.endWrite();
            }
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
            return;
        }
    }
}

