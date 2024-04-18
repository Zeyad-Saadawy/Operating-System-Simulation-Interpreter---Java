import java.util.LinkedList;
import java.util.Queue;

public class Mutex {
    boolean locked;
    String name;
    int id;
    Queue<Process> mutexBlockedQueue = new LinkedList<>();

    public Mutex(String name) {
        locked = false;
        this.name = name;

    }

    public void semWait(Process Process) {
        if (locked) {
            mutexBlockedQueue.add(Process);
            Process.pcb.state = states.BLOCKED;
            // Process.mutexname = name;
            return;
        }
        locked = true;
        this.id = Process.pcb.id;
        System.out.println("Locking " + name);
    }

    public void semSignal(Process Process) {
        if (Process.pcb.id != id) {
            throw new IllegalArgumentException("Process does not own mutex");
        }

        if (!mutexBlockedQueue.isEmpty()) {
            Process p = mutexBlockedQueue.remove();
            os.ReadyQueue.add(p);
            p.pcb.state = states.READY;
            id = p.pcb.id;
        } else {

            locked = false;
            System.out.println("Unlocking " + name);
        }

    }
}