package Server;

abstract class ThreadTask implements Runnable {
    String threadName;

    void Terminate() {
        ThreadDispatcher threadDispatcher = ThreadDispatcher.GetInstance();
        threadDispatcher.DeleteThread(Thread.currentThread().getId());
        Thread.currentThread().interrupt();
    }
}
