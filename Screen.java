////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/// Name: Erik Claros Turcios
/// Date: 10/05/2023
/// Assignment 02: Creating a Jump Table visualizer for Stack, Queue, and List data Structures
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//All imports
import java.util.Stack;
import java.util.Queue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.io.*;
import java.util.Scanner;


//invoke method
interface StateEnterExitMeth{
    public void invoke();
    
}

// Invoke boolean
interface StateStayMeth{
    public boolean invoke();
}


//Enum for states
enum State{
    LIST,
    STACK,
    QUEUE,
    IDLE
}


public class Screen{
    String red = "\u001b[31m";
    String green = "\u001b[32m";
    String blue = "\u001b[34m";
    String yellow = "\u001b[33m";
    String reset = "\033[0m";
    private HashMap<State, StateEnterExitMeth> StateEnterMeths;
    private HashMap<State, StateStayMeth> StateStayMeths;
    private HashMap<State, StateEnterExitMeth> StateExitMeths;
    private State state;
    private Stack<Character> stack;
    private Queue<Character> queue;
    private ArrayList<Character> list;
    private Scanner scanner;

    private final String FILE_STACK = "stack.txt";
    private final String FILE_QUEUE= "queue.txt";
    private final String FILE_LIST = "list.txt";


    //Constructor 
    public Screen(){
        state = State.IDLE;
        scanner = new Scanner(System.in);
        stack = new Stack<Character>();
        queue = new LinkedList<Character>();
        list = new ArrayList<Character>();

        StateEnterMeths = new HashMap<>();
        StateEnterMeths.put(State.IDLE, () -> {StateEnterIdle();});
        StateEnterMeths.put(State.STACK, () ->{StateEnterStack();});
        StateEnterMeths.put(State.QUEUE, () ->{StateEnterQueue();});
        StateEnterMeths.put(State.LIST, () ->{StateEnterList();});
        StateStayMeths = new HashMap<>();
        StateStayMeths.put(State.IDLE, () -> {return StateStayIdle();});
        StateStayMeths.put(State.STACK, () ->{return StateStayStack();});
        StateStayMeths.put(State.QUEUE, () ->{return StateStayQueue();});
        StateStayMeths.put(State.LIST, () ->{return StateStayList();});
        StateExitMeths = new HashMap<>();
        StateExitMeths.put(State.IDLE, () -> {StateExitIdle();});
        StateExitMeths.put(State.STACK, () -> {StateExitStack();});
        StateExitMeths.put(State.QUEUE, () -> {StateExitQueue();});
        StateExitMeths.put(State.LIST, () -> {StateExitList();});      
    }

    //Changes States
    public void changeState(State newState){
        if (state != newState){
            if (StateExitMeths.containsKey(state)){
                StateExitMeths.get(state).invoke();
            }
            state = newState;
            if (StateEnterMeths.containsKey(newState)){
                StateEnterMeths.get(newState).invoke();
            }
        }
    }

    //Do state function
    public boolean doState(){
        System.out.print("\033[H\033[2J");
        
        if (StateStayMeths.containsKey(state)){
            return StateStayMeths.get(state).invoke();
        }
        return false;
    }
    
    // ENTER 
    private void StateEnterIdle(){
    }
    
    private void StateEnterStack(){
        fillingStack();

    }
    
    private void StateEnterQueue(){ 
        fillingQueue();
    }
    
    private void StateEnterList(){
        fillingList();
    }
    
    //STAY
    private boolean StateStayIdle(){
        System.out.println(yellow + "\n1. Stack" + "\n2. Queue" + "\n3. List" + "\n4. Quit" + reset);
        String action = scanner.nextLine();
        if(action.charAt(0) == '1'){
            changeState(State.STACK);
        }
        else if(action.charAt(0) == '2'){
            changeState(State.QUEUE);
        }
        else if(action.charAt(0) == '3'){
            changeState(State.LIST);
        }
        else if(action.charAt(0) == '4'){
            return false;
        }
        else{
            System.out.println("Please Select an option above");
        }
        return true;
    }
    
    //Stack menu and takes in commands
    private boolean StateStayStack(){
        drawStack();
        System.out.println(yellow + "\n1. Push" + "\n2. Pop" + "\n3. Save & Move to Queue" + "\n4. Save & Move to List" + "\n5. Quit" + reset);
        String action = scanner.nextLine();
        if(action.charAt(0) == '1'){
            stack.push(action.charAt(2));
        }
        else if(action.charAt(0) == '2'){
            stack.pop();
        }
        else if(action.charAt(0) == '3'){
            System.out.println("Save and go to queue");
            changeState(State.QUEUE);
        }
        else if(action.charAt(0) == '4'){
            System.out.println("Save and go to List");
            changeState(State.LIST);
        }
        else if(action.charAt(0) == '5'){
            writeStack(FILE_STACK);
        }
        else{
            System.out.println("Please select a menu option");
        }
        
        return true;
    }

    // Draws stack
    private void drawStack(){
        System.out.println(red + "|   |\n|---|" + reset);
        Stack <Character> temp = new Stack<>();
        while(!stack.isEmpty()){
            System.out.println(red + "| " + stack.peek() + " |" + reset);
            System.out.println(red + "|"  + "---" + "|" + reset);
            temp.push(stack.pop());
        }
        while(!temp.isEmpty()){
            stack.push(temp.pop());
         }

    }

    //Adding content to the stack
    private void fillingStack(){
        String a = readFile(FILE_STACK);
        for(int i = 0; i < a.length(); i++){
            stack.push(a.charAt(i));
        }
    }
    
    //Queue Menu and request intake options
    private boolean StateStayQueue(){
        drawQueue();
        System.out.println(yellow + "\n1. Enqueue" + "\n2. Dequeue" + "\n3. Save & Move to Stack" + "\n4. Save & Move to List" + "\n5. Quit" + reset);
        String action = scanner.nextLine();
        if(action.charAt(0) == '1'){
            queue.add(action.charAt(2));
        }
        else if(action.charAt(0) == '2'){
            queue.poll();            
        }
        else if(action.charAt(0) == '3'){
            changeState(State.STACK);
        }
        else if(action.charAt(0) == '4'){
            changeState(State.LIST);
        }
        else if(action.charAt(0) == '5'){
            writeQueue(FILE_QUEUE);
        }
        else{
            System.out.println("Please select a menu option");
        }
        return true;        
    }

    //Draw Queue
    private void drawQueue(){
        for(Object element : queue){
            System.out.print(green + "| " + element + reset);
        }
        System.out.print(green + " |" + reset);
    }

    //Filling up queue from reader file
    private void fillingQueue(){
        String a = readFile(FILE_QUEUE);
        for(int i = 0; i < a.length(); i++){
            queue.add(a.charAt(i));
        }
    }
    
    //Menu and action requests
    private boolean StateStayList(){
        drawList();
        System.out.println(yellow + "\n1. Add" + "\n2. Delete" + "\n3. Save & Move to Stack" + "\n4. Save & Move to Queue" + "\n5. Quit" + reset);
        String action = scanner.nextLine();
        if(action.charAt(0) == '1'){
            list.add(action.charAt(2));
        }
        else if(action.charAt(0) == '2'){
            list.remove(list.size()-1);

        }
        else if(action.charAt(0) == '3'){
            changeState(State.STACK);
        }
        else if(action.charAt(0) == '4'){
            changeState(State.QUEUE);
        }
        else if(action.charAt(0) == '5'){
            writingArray(FILE_LIST);
        }
        else{
            System.out.println("Please select a menu option");
        }

        return true;
        
    }

    //Draws List
    private void drawList(){
        System.out.print(blue + "{ " + reset);
        for(char element : list){
            System.out.print(blue + element + "," + reset);
        }
        System.out.print(blue + " }" + reset);


    }
    //Filling in the list from reader
    private void fillingList(){
        String a = readFile(FILE_LIST);
        for(int i = 0; i < a.length(); i++){
            list.add(a.charAt(i));
        }
    }

    
    
    //EXIT
    private void StateExitIdle(){        
    }
    
    private void StateExitStack(){
        writeStack(FILE_STACK);
    }
    
    private void StateExitQueue(){
        writeQueue(FILE_QUEUE);
     
    }
    
    private void StateExitList(){
        writingArray(FILE_LIST);

    }

    // Reading Files
    private String readFile(String file){
        String result= "";
        String line;
        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            if((line = reader.readLine()) != null){
                for(int i = 0; i < line.length(); i++){
                    char chars = line.charAt(i);
                    if(((chars != '\0' ) && (chars != ' ') && (chars != ',' ))){
                        result += chars;
                    }
                }
            }
            else{
                reader.close();
                return result;
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return result;
    }
    

    //Stack Writer
    private String writeStack(String file){
        Stack <Character> TEMP_STACK = new Stack<>();
        while(!stack.isEmpty()){
            TEMP_STACK.push(stack.pop());
        }
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))){
            while(!TEMP_STACK.isEmpty()){
                writer.write(TEMP_STACK.pop() + ",");
            }
        } 
        catch(IOException e){
            e.printStackTrace();
        }
        return file;
    }

    //Queue Writer
    private String writeQueue(String file){
        Queue <Character> TEMP_QUEUE = new LinkedList<>();
        while(!queue.isEmpty()){
            TEMP_QUEUE.add(queue.remove());
        }
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))){
            while(!TEMP_QUEUE.isEmpty()){
                writer.write(TEMP_QUEUE.remove() + ",");
            }
            writer.close();
        } 
        catch(IOException e){
            e.printStackTrace();
        }
        System.out.println("Writing Queue");
        return file;
    }

    //Array Writer
    private String writingArray(String file){
        ArrayList <Character> TEMP_LIST = new ArrayList<>();
        while(list.size() > 0){
            TEMP_LIST.add(list.remove(list.size()-1));
        }
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))){
            while(!TEMP_LIST.isEmpty()){
                writer.write(TEMP_LIST.remove(list.size()) + ",");
            }
            writer.close();
        } 
        catch(IOException e){
            e.printStackTrace();
        }
        return file;
    }
}

  

