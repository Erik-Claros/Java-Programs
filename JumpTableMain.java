public class JumpTableMain{
    public static void main(String [] args){
        Screen screen = new Screen();
        boolean keepRunning = true;
        while(keepRunning){
            keepRunning = screen.doState();
        }
    }
    
}






