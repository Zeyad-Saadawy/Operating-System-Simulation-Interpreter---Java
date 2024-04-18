    public class Pcb {
        int id;
        states state;
        int pc;
        int lowerBoundary;
        int upperBoundary;

    public Pcb(int id, int pc) {
        this.id = id;
        this.state = states.READY;
        this.pc = pc;

    }
}
