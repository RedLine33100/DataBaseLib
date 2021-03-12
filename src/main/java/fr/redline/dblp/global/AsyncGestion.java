package fr.redline.dblp.global;

public class AsyncGestion extends Thread{
    public void execute(Runnable runnable){
        runnable.run();
    }
}
