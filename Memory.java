import java.util.ArrayList;

public class Memory {
    Object[] memoryarray;

    public Memory() {
        memoryarray = new Object[40];
    }

    public boolean isFull() {
        return (memoryarray[0] != null && memoryarray[20] != null);
    }

    public boolean thereEmptyspace() {
        return (memoryarray[0] == null || memoryarray[20] == null);
    }

    public boolean isempty() {
        return (memoryarray[0] == null && memoryarray[20] == null);
    }

    public int getEmptySpace() {
        if (memoryarray[0] == null) {
            return 0;
        }
        if (memoryarray[20] == null) {
            return 20;
        }
        return -1;
    }

    public ArrayList<String> getProcessinstructions(Process p) {
        Pcb pcb = p.pcb;
        int lowerBoundary = pcb.lowerBoundary;
        // int upperBoundary = pcb.upperBoundary;
        ArrayList<String> instructions = new ArrayList<String>();
        if (lowerBoundary == 0) {
            int i = 5;
            while (memoryarray[i] != null) {
                if (memoryarray[i] instanceof String) {
                    instructions.add((String) memoryarray[i]);
                }
                if (memoryarray[i] instanceof Variable) {
                    String var = ((Variable) memoryarray[i]).name + "=" + ((Variable) memoryarray[i]).value;
                    instructions.add(var);
                }
                if (i == 19) {
                    break;
                }
                i++;
            }
        }
        if (lowerBoundary == 20) {
            int i = 25;
            while (memoryarray[i] != null) {
                if (memoryarray[i] instanceof String) {
                    instructions.add((String) memoryarray[i]);
                }
                if (memoryarray[i] instanceof Variable) {
                    String var = ((Variable) memoryarray[i]).name + "=" + ((Variable) memoryarray[i]).value;
                    instructions.add(var);
                }
                if (i == 39) {
                    break;
                }
                i++;

            }
        }
        return instructions;

    }

    public void writeprocesstomemory(Process p, ArrayList<String> instructions) {
        Pcb pcb = p.pcb;
        int lowerBoundary = pcb.lowerBoundary;
        int upperBoundary = pcb.upperBoundary;
        if (instructions.size() > 15) {
            throw new IllegalArgumentException("Process is too big for memory.");
        }

        if (lowerBoundary == 0) {
            if (upperBoundary > 19) {
                throw new IllegalArgumentException("Process is too big for memory.");
            }
            memoryarray[0] = pcb.id;
            memoryarray[1] = pcb.state;
            memoryarray[2] = pcb.pc;
            memoryarray[3] = pcb.lowerBoundary;
            memoryarray[4] = pcb.upperBoundary;
            for (int i = 5; i < instructions.size() + 5; i++) {
                if (instructions.get(i - 5).contains("=")) {
                    String[] var = instructions.get(i - 5).split("=");
                    Variable x = new Variable(var[0], var[1]);
                    memoryarray[i] = x;
                } else
                    memoryarray[i] = instructions.get(i - 5);
            }
        }
        if (lowerBoundary == 20) {
            if (upperBoundary > 39) {
                throw new IllegalArgumentException("Process is too big for memory.");
            }
            memoryarray[20] = pcb.id;
            memoryarray[21] = pcb.state;
            memoryarray[22] = pcb.pc;
            memoryarray[23] = pcb.lowerBoundary;
            memoryarray[24] = pcb.upperBoundary;
            for (int i = 25; i < instructions.size() + 25; i++) {
                if (instructions.get(i - 25).contains("=")) {
                    String[] var = instructions.get(i - 25).split("=");
                    Variable x = new Variable(var[0], var[1]);
                    memoryarray[i] = x;
                } else
                    memoryarray[i] = instructions.get(i - 25);
            }
        }
    }

    public Variable getVariable(Pcb pcb, String name) {
        for (int i = pcb.lowerBoundary; i < pcb.upperBoundary; i++) {
            if (memoryarray[i] instanceof Variable) {
                if (((Variable) memoryarray[i]).name.equals(name)) {
                    return (Variable) memoryarray[i];
                }
            }
        }
        return null;
    }

    public void writeVariable(Pcb pcb, Variable x) {
        for (int i = pcb.lowerBoundary; i < pcb.upperBoundary; i++) {
            if (memoryarray[i] == null) {
                memoryarray[i] = x;
                return;
            }
        }
    }
}
