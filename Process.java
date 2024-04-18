public class Process {
    Pcb pcb;
    int timeofarrival;
    String filename;
    
    int id;
    public Process(int id, int timeofarrival, String filename) {
        this.timeofarrival = timeofarrival;
        this.filename = filename;
        
        this.id=id ;
        this.pcb = new Pcb(id,0);
    }

}
