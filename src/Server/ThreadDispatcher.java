package Server;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ThreadDispatcher {
    private static volatile ThreadDispatcher instance;
    private ThreadMonitor threadMonitor;
    private Map<Long, Pair<Thread, ThreadTask>> Threads;
    private ArrayList<Long> IDs;

    public ThreadDispatcher() {
        threadMonitor = new ThreadMonitor();
        Threads = new HashMap<Long, Pair<Thread, ThreadTask>>();
        IDs = new ArrayList<Long>();
        Add(threadMonitor);
    }

    public static synchronized ThreadDispatcher GetInstance() {
        if (instance == null)
            instance = new ThreadDispatcher();
        return instance;
    }

    public void Add(ThreadTask task) {
        Thread t = new Thread(task);
        t.start();
        IDs.add(t.getId());
        Threads.put(t.getId(), new Pair<Thread, ThreadTask>(t, task));
        threadMonitor.SendWriteSignal();
        System.out.println(t.getId() + " " + task.threadName);
    }

    ArrayList<Pair<Long, String>> GetThreadInfo() {
        ArrayList<Pair<Long, String>> list = new ArrayList<Pair<Long, String>>();
        for (int i = 0; i < IDs.size(); i++)
            list.add(new Pair(IDs.get(i), Threads.get(IDs.get(i)).getValue().threadName));
        return list;
    }

    synchronized void DeleteThread(Long id) {
        Threads.get(id).getKey().stop();
        threadMonitor.SendWriteSignal();
        System.out.println("killed - " + id + " " + Threads.get(id).getValue().threadName);
        Threads.remove(id);
        for (int i = 0; i < IDs.size(); i++)
            if (IDs.get(i) == id) {
                IDs.remove(i);
                break;
            }
    }
}
