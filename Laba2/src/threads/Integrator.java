package threads;

import functions.Function;
import functions.Functions;

public class Integrator extends Thread{
    private Task task;
    private Semaphore semaphore;

    public Integrator(Task task, Semaphore semaphore){
        this.task = task;
        this.semaphore = semaphore;
    }

    public void run() {
        try {
            for (int i = 0; i < task.getNumberTask(); i++) {

                if (isInterrupted()){
                    return;
                }

                semaphore.beginRead();

                Function func = task.getFunc();
                double left = task.getLeft();
                double right = task.getRight();
                double step = task.getStep();

                double result = Functions.integrate(func, left, right, step);
                System.out.printf("Result №"+i+" <%.2f> <%.2f> <%.2f> <%.2f>%n", task.getLeft(), task.getRight(), task.getStep(), result);

                semaphore.endRead();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
    }
}
