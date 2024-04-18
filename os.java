import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class os {

  Mutex file;
  Mutex userInput;
  Mutex userOutput;
  Memory memory;
  static int clock = 0;
  public static Queue<Process> ReadyQueue = new LinkedList<>();
  public static Queue<Process> BlockedQueue = new LinkedList<>();
  static int quantum = 2;
  static int timeofarrival1 = 0;
  static int timeofarrival2 = 1;
  static int timeofarrival3 = 4;
  static String filename1 = "Program_1";
  static String filename2 = "Program_2";
  static String filename3 = "Program_3";
  static String readfilestring = null;
  static String filename1new = "Program_1_new";
  static String filename2new = "Program_2_new";
  static String filename3new = "Program_3_new";

  public os() {
    file = new Mutex("filemutex");
    userInput = new Mutex("userInput");
    userOutput = new Mutex("userOutput");
    // scheduler = new Scheduler();
    memory = new Memory();
    Scanner sc = new Scanner(System.in);
    System.out.println("Enter the quantum: ");
    quantum = sc.nextInt();
    System.out.println("Enter the time of arrival of process 1: ");
    timeofarrival1 = sc.nextInt();
    System.out.println("Enter the time of arrival of process 2: ");
    timeofarrival2 = sc.nextInt();
    System.out.println("Enter the time of arrival of process 3: ");
    timeofarrival3 = sc.nextInt();
  }

  public void createProcess(int id, int timeofarrival, String filename) {
    Process p = new Process(id, timeofarrival, filename);
    setBoundaries(p);
    readfileforprocess(p, filename);

    ReadyQueue.add(p);
    for (Process process : ReadyQueue) {
      System.out.println("ready queue has procces id:" + process.pcb.id);

    }
  }

  public void readfileforprocess(Process p, String filename) {
    BufferedReader reader;
    ArrayList<String> instructions = new ArrayList<String>();
    try {
      reader = new BufferedReader(new FileReader(filename + ".txt"));
      String line = reader.readLine();
      while (line != null) {
        instructions.add(line);
        line = reader.readLine();
      }
      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("File does not exist.");
    }

    memory.writeprocesstomemory(p, instructions);
    // schedule(p); // schedule el process w put it in the ready queue
  }

  public String readfile(String filename) {
    BufferedReader reader;
    String data = "";
    try {
      reader = new BufferedReader(new FileReader(filename + ".txt"));
      String line = reader.readLine();
      while (line != null) {
        data += line;
        line = reader.readLine();
      }
      reader.close();
      return data;
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("File does not exist.");
    }
    return data;
  }

  /**
   * @param p
   */
  public void setBoundaries(Process p) {
    if (memory.isempty() || memory.getEmptySpace() == 0) {
      p.pcb.lowerBoundary = 0;
      p.pcb.upperBoundary = 19;
    } else if (memory.getEmptySpace() == 20) {
      p.pcb.lowerBoundary = 20;
      p.pcb.upperBoundary = 39;
    } else {

      int[] Boundaries = new int[2];

      Boundaries = freeSpaceFromMemory();
      for (int i = Boundaries[0]; i <= Boundaries[1]; i++) {
        memory.memoryarray[i] = null;
      }
      p.pcb.lowerBoundary = Boundaries[0];
      p.pcb.upperBoundary = Boundaries[1];

    }

  }

  public Process lastElement(Queue<Process> queue) {
    Queue<Process> temp = new LinkedList<Process>();

    Process lastprocess = null;

    while (!queue.isEmpty()) {
      lastprocess = queue.peek(); // Get the first element in the queue
      if (queue.size() != 1) {
        temp.add(queue.peek());
      }
      queue.remove(); // Remove the first element from the queue
    }

    while (!temp.isEmpty()) {
      queue.add(temp.remove());
    }
    return lastprocess;

  }

  public int[] freeSpaceFromMemory() {
    int[] Boundaries = new int[2];
    if (memory.memoryarray[1] == states.TERMINATED) {
      Boundaries[0] = 0;
      Boundaries[1] = 19;
      return Boundaries;
    } else if (memory.memoryarray[21] == states.TERMINATED) {
      Boundaries[0] = 20;
      Boundaries[1] = 39;
      return Boundaries;
    }
    if (!file.mutexBlockedQueue.isEmpty()) {
      LinkedList<Process> linkedList = new LinkedList<>(file.mutexBlockedQueue);
      Process p1 = linkedList.getLast();

      ArrayList<String> instructions = memory.getProcessinstructions(p1);

      Boundaries[0] = p1.pcb.lowerBoundary;
      Boundaries[1] = p1.pcb.upperBoundary;
      switch (p1.pcb.id) {

        case 1:
          p1.filename = filename1new;
          break;
        case 2:
          p1.filename = filename2new;
          ;
          break;
        case 3:
          p1.filename = filename3new;
          ;
          break;

      }
      writeFile(p1.filename, instructions);
    } else if (!userInput.mutexBlockedQueue.isEmpty()) {
      LinkedList<Process> linkedList = new LinkedList<>(userInput.mutexBlockedQueue);
      Process p1 = linkedList.getLast();

      ArrayList<String> instructions = memory.getProcessinstructions(p1);

      Boundaries[0] = p1.pcb.lowerBoundary;
      Boundaries[1] = p1.pcb.upperBoundary;
      switch (p1.pcb.id) {

        case 1:
          p1.filename = filename1new;
          break;
        case 2:
          p1.filename = filename2new;
          ;
          break;
        case 3:
          p1.filename = filename3new;
          ;
          break;

      }
      writeFile(p1.filename, instructions);
    } else if (!userOutput.mutexBlockedQueue.isEmpty()) {
      LinkedList<Process> linkedList = new LinkedList<>(userOutput.mutexBlockedQueue);
      Process p1 = linkedList.getLast();

      ArrayList<String> instructions = memory.getProcessinstructions(p1);

      Boundaries[0] = p1.pcb.lowerBoundary;
      Boundaries[1] = p1.pcb.upperBoundary;
      switch (p1.pcb.id) {

        case 1:
          p1.filename = filename1new;
          break;
        case 2:
          p1.filename = filename2new;
          ;
          break;
        case 3:
          p1.filename = filename3new;
          ;
          break;

      }
      writeFile(p1.filename, instructions);
    } else if (!ReadyQueue.isEmpty()) {
      LinkedList<Process> linkedList = new LinkedList<>(ReadyQueue);
      Process p2 = linkedList.getLast();

      ArrayList<String> instructions = memory.getProcessinstructions(p2);

      Boundaries[0] = p2.pcb.lowerBoundary;
      Boundaries[1] = p2.pcb.upperBoundary;

      switch (p2.pcb.id) {
        case 1:
          p2.filename = filename1new;
          break;
        case 2:
          p2.filename = filename2new;
          break;
        case 3:
          p2.filename = filename3new;
          break;
      }
      writeFile(p2.filename, instructions);
    } else {
      throw new IllegalArgumentException("No process to remove from memory");
    }

    return Boundaries;
  }

  public void print(Object x) {
    System.out.println(x);
  }

  public void writeFile(String filename, ArrayList<String> datainstructions) {
    try (
        BufferedWriter writer = new BufferedWriter(
            new FileWriter(filename + ".txt"))) {
      for (String line : datainstructions) {
        writer.write(line);
        writer.newLine();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static String getUserInput() {
    System.out.println("Please enter a value: ");
    Scanner scanner = new Scanner(System.in);
    String input = scanner.nextLine();
    // scanner.close();
    return input;
  }

  public static void printFromTo(int x, int y) {
    // check which is bigger
    if (x > y) {
      for (int i = y; i <= x; i++) {
        System.out.println(i);
      }
    } else {
      for (int i = x; i <= y; i++) {
        System.out.println(i);
      }
    }
  }

  public static Variable assign(String name, Object value) {
    return new Variable(name, value);
  }

  public void execute(Process p) {

    Pcb pcb = p.pcb;
    int pc = pcb.pc;
    p.pcb.state = states.RUNNING;
    int lowerBoundary = pcb.lowerBoundary;
    int id = pcb.id;
    int position = -99;
    if (lowerBoundary == 0) {
      position = pcb.lowerBoundary + 5 + pcb.pc;
    } else {
      position = pcb.lowerBoundary + 5 + pcb.pc;
    }
    if (memory.memoryarray[position] == null)
      p.pcb.state = states.TERMINATED;
    while ((memory.memoryarray[position] instanceof Variable || memory.memoryarray[position] == null)
        && pc < pcb.upperBoundary && position < pcb.upperBoundary) {
      pc++;
      pcb.pc = pc;
      if (lowerBoundary == 0) {
        position = pcb.lowerBoundary + 5 + pcb.pc;
      } else {
        position = pcb.lowerBoundary + 5 + pcb.pc;
      }
    }
    if (position == pcb.upperBoundary
        && (memory.memoryarray[position] instanceof Variable || memory.memoryarray[position] == null)) {
      if (lowerBoundary == 0) {
        memory.memoryarray[1] = states.TERMINATED;
      } else {
        memory.memoryarray[lowerBoundary + 1] = states.TERMINATED;
      }
      pcb.state = states.TERMINATED;
      return;
    }

    String[] parts = ((String) memory.memoryarray[position]).split(" ");
    String instruction = parts[0];

    if (instruction.equals("semWait")) {
      String mutexname = parts[1];
      if (mutexname.equals("file")) {
        file.semWait(p);
      } else if (mutexname.equals("userInput")) {
        userInput.semWait(p);
      } else if (mutexname.equals("userOutput")) {
        userOutput.semWait(p);
      } else {
        throw new IllegalArgumentException("Mutex not found");
      }
      System.out.println("instruction: semWait");
      System.out.println("mutexname: " + mutexname);
      System.out.println("pcb: " + p.pcb.id);
    } else if (instruction.equals("semSignal")) {
      String mutexname = parts[1];
      if (mutexname.equals("file")) {
        file.semSignal(p);
      } else if (mutexname.equals("userInput")) {
        userInput.semSignal(p);
      } else if (mutexname.equals("userOutput")) {
        userOutput.semSignal(p);
      } else {
        throw new IllegalArgumentException("Mutex not found");
      }
      System.out.println("instruction: semWait");
      System.out.println("mutexname: " + mutexname);
      System.out.println("pcb: " + p.pcb.id);
    } else if (instruction.equals("print")) {
      String variablename = parts[1];
      Variable x = memory.getVariable(pcb, variablename);
      print(x.value);
    } else if (instruction.equals("assign")) {
      String variablename = parts[1];
      String value = parts[2];
      int ifRead = -98;
      if (value.equals("input")) {
        value = getUserInput();
        System.out.println("instruction: input");
        memory.memoryarray[position] = instruction + " " + parts[1] + " " + value;
        return;
      } else if (value.equals("readFile")) {
        String variablename2 = parts[3];
        Variable x = memory.getVariable(pcb, variablename2);
        String tmp = readfile(x.value.toString());
        memory.memoryarray[position] = instruction + " " + parts[1] + " " + tmp;
        System.out.println("instruction: readFile");
        return;
      } else {
        try {
          ifRead = Integer.parseInt(value);
        } catch (Exception e) {
        }
      }
      if (ifRead == -98) {
        Variable x = new Variable(variablename, value);
        memory.writeVariable(pcb, x);
      } else {
        Variable x = new Variable(variablename, ifRead);
        memory.writeVariable(pcb, x);

      }

    } else if (instruction.equals("printFromTo")) {
      String variablename1 = parts[1];
      String variablename2 = parts[2];
      Variable x = memory.getVariable(pcb, variablename1);
      Variable y = memory.getVariable(pcb, variablename2);
      printFromTo(Integer.parseInt(x.value.toString()), Integer.parseInt(y.value.toString()));
    } else if (instruction.equals("writeFile")) {
      String filenamevariable = parts[1];
      String filename = memory.getVariable(pcb, filenamevariable).value.toString();

      String variablename = parts[2];
      Variable variable = memory.getVariable(pcb, variablename);
      ArrayList<String> datainstructions = new ArrayList<String>();
      Object value = variable.value;
      if (value instanceof Integer) {
        datainstructions.add(value.toString());
      } else if (value instanceof String) {
        datainstructions.add((String) value);
      }
      writeFile(filename, datainstructions);
    }
    System.out.println("Instruction: " + instruction);
    if (position == pcb.upperBoundary
        || (memory.memoryarray[position] instanceof Variable || memory.memoryarray[position] == null)) {
      if (lowerBoundary == 0) {
        memory.memoryarray[1] = states.TERMINATED;
      } else {
        memory.memoryarray[lowerBoundary + 1] = states.TERMINATED;
      }
      pcb.state = states.TERMINATED;
      return;
    }
    pc++;
    pcb.pc = pc;

  }

  public boolean checkIfProcessInMemory(Process p) {

    if (p.pcb.id == (int) memory.memoryarray[0] || p.pcb.id == (int) memory.memoryarray[20]) {
      return true;
    } else {
      return false;
    }
  }

  public void schedule() {
    if (clock == timeofarrival1) {
      createProcess(1, timeofarrival1, filename1);
    } else if (clock == timeofarrival2) {
      createProcess(2, timeofarrival2, filename2);
    } else if (clock == timeofarrival3) {
      createProcess(3, timeofarrival3, filename3);
    }

    while (!ReadyQueue.isEmpty() || !file.mutexBlockedQueue.isEmpty() || !userInput.mutexBlockedQueue.isEmpty()
        || !userOutput.mutexBlockedQueue.isEmpty()) {

      if (clock == timeofarrival2) {

        createProcess(2, timeofarrival2, filename2);

      }
      if (clock == timeofarrival3) {
        createProcess(3, timeofarrival3, filename3);
      }

      if (!ReadyQueue.isEmpty()) {
        Process runningProcess = ReadyQueue.remove();
        if (!checkIfProcessInMemory(runningProcess)) {
          setBoundaries(runningProcess);
          readfileforprocess(runningProcess, runningProcess.filename);
        }

        for (int j = 0; j < quantum; j++) {

          if (runningProcess.pcb.state == states.BLOCKED || runningProcess.pcb.state == states.TERMINATED) {
            System.out.println("Process " + runningProcess.pcb.id + " is in state " + runningProcess.pcb.state);

            break;
          }
          System.out.println(
              "The running process ID is: " + runningProcess.pcb.id + " is in state " + runningProcess.pcb.state);
          System.out.println("Clock: " + clock);
          System.out.println("PC: " + runningProcess.pcb.pc);
          execute(runningProcess);
          if (runningProcess.pcb.state == states.TERMINATED) {

            System.out.println("Process " + runningProcess.pcb.id + " is in state " + runningProcess.pcb.state);

            break;
          }

          for (int i = 0; i < 40; i++) {
            System.out.println("Memory[" + i + "]: " + memory.memoryarray[i]);
          }
          System.out.println("memory[0]" + memory.memoryarray[0]);
          System.out.println("memory[20]" + memory.memoryarray[20]);
          for (Process process : file.mutexBlockedQueue) {
            System.out.println(process.pcb.id + " is in file mutex blocked queue");

          }
          for (Process process : userInput.mutexBlockedQueue) {
            System.out.println(process.pcb.id + " is in userInput mutex blocked queue");

          }
          for (Process process : userOutput.mutexBlockedQueue) {
            System.out.println(process.pcb.id + " is in userOutput mutex blocked queue");

          }
          for (Process process : ReadyQueue) {
            System.out.println("ready queue has procces id:" + process.pcb.id);

          }
          System.out.println("--------------------------------------------------");

          clock++;

          if (clock == timeofarrival2) {

            createProcess(2, timeofarrival2, filename2);

          }
          if (clock == timeofarrival3) {
            createProcess(3, timeofarrival3, filename3);
          }

        }

        if (runningProcess.pcb.state == states.RUNNING) {

          ReadyQueue.add(runningProcess);

          runningProcess.pcb.state = states.READY;
          for (Process process : ReadyQueue) {
            System.out.println("ready queue has procces id:" + process.pcb.id);

          }

        }

      }
    }
  }

  public static void main(String[] args) throws IOException {
    os Os = new os();
    Os.schedule();

  }
}
