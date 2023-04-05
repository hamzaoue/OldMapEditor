import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.JFrame;
/****/
public class Keyboard implements KeyListener
{
    private EditorEngine aEngine;
    private ArrayList<Integer> aKeysPressed;
    /****/ 
    public Keyboard(EditorEngine pEngine)
    {
        this.aEngine = pEngine;
        this.aKeysPressed = new ArrayList<Integer>();
    }
    /****/ 
    @Override public void keyTyped(KeyEvent e){}
    /****/ 
    @Override public void keyPressed(KeyEvent e)
    {
        if(!this.aKeysPressed.contains(new Integer(e.getKeyCode())))
            this.aKeysPressed.add(new Integer(e.getKeyCode()));
        if(this.aKeysPressed.contains(new Integer(37)))//WEST 
            this.aEngine.shiftX(-10);
        if(this.aKeysPressed.contains(new Integer(38)))//NORTH 
            this.aEngine.shiftY(-10);
        if(this.aKeysPressed.contains(new Integer(39)))//EAST
            this.aEngine.shiftX(10);
        if(this.aKeysPressed.contains(new Integer(40)))//SOUTH   
            this.aEngine.shiftY(10);
    }
    /****/ 
    public void keyReleased(KeyEvent e)
    {
        if(this.aKeysPressed.contains(e.getKeyCode())){
            this.aKeysPressed.remove(new Integer(e.getKeyCode()));}
    }
}