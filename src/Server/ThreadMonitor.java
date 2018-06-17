package Server;

import java.io.FileWriter;
import java.io.IOException;

public class ThreadMonitor extends ThreadTask {

    private volatile boolean running = true;
    private volatile boolean writeSignal = false;

    ThreadMonitor() {
        threadName = "Thread monitor";
    }

    public void StopThread() {
        running = false;
    }

    void SendWriteSignal() {
        writeSignal = true;
    }

    private void WriteThreads(ThreadDispatcher threadDispatcher) throws IOException {
        FileWriter fileWriter = new FileWriter("WorkingThreads.txt");
        fileWriter.write(String.valueOf(threadDispatcher.GetThreadInfo()));
        fileWriter.close();
    }

    @Override
    public void run() {
        while (running) {
            ThreadDispatcher threadDispatcher = ThreadDispatcher.GetInstance();
            if (writeSignal) {
                try {
                    WriteThreads(threadDispatcher);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                writeSignal = false;
            }
        }
        Terminate();
    }
}