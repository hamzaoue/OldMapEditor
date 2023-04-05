import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import java.awt.Point;
/****/
public class Mouse implements MouseListener, MouseMotionListener, MouseWheelListener
{
    private EditorEngine aEngine;
    private  int  aPressedButton;
    private Point  aPressedPoint;
    private Point aMousePos;
    private boolean[] aFliped;//N,S,E,W
    /****/ 
    public Mouse(EditorEngine pEngine)
    {
        this.aEngine = pEngine;
        this.aPressedButton = 0;
        this.aPressedPoint = null;
        this.aMousePos=new Point(0,0);
        this.aFliped=new boolean[]{false,false,false,false};
    }
    /****/ 
    private void shiftScreen(final Point pPoint)
    {
        if(this.aPressedPoint!=null)
        {
            this.aEngine.setCursorIcon("Move");
            this.aEngine.shiftX(pPoint.x-this.aPressedPoint.x);
            this.aEngine.shiftY(pPoint.y-this.aPressedPoint.y);
            this.aPressedPoint=pPoint;
        }
    }
    /****/ 
    public Point getMousePos(){return this.aMousePos;} 
    /****/ 
    @Override public void mouseWheelMoved(MouseWheelEvent e)
    {
        this.aEngine.zoom(e.getWheelRotation());
        this.hasMoved(e.getPoint());
    }
    /****/ 
    @Override public void mouseMoved(MouseEvent e)
    {
        this.hasMoved(e.getPoint());
    }
    /****/ 
    private boolean hasMoved(final Point pMousePoint)
    {
        Point vPos = this.aEngine.getPos(pMousePoint);
        if(vPos != null && !vPos.equals(this.aMousePos))
        {
            this.aMousePos.setLocation(vPos);
            return true;
        }
        return false;
    }
    /****/ 
    @Override public void mouseDragged(MouseEvent e)
    {
        switch(this.aEngine.getCurrentTool()) 
        {
            case "Pencil":
                switch(this.aPressedButton)
                {   
                    case 1:if(this.hasMoved(e.getPoint()))
                               this.aEngine.placeTiles(this.aMousePos);return;
                    case 2:this.shiftScreen(e.getPoint());return;
                    case 3:this.testFlip(e.getPoint());return;
                }
                return;                        
            case "Select":
                switch(this.aPressedButton)
                {   
                    case 1:if(this.hasMoved(e.getPoint()))
                               this.aEngine.placePos2(this.aMousePos);return;
                    case 2:this.shiftScreen(e.getPoint());return;
                    case 3:return;
                }
                return;
            case "Gum":
                switch(this.aPressedButton)
                {   
                    case 1:if(this.hasMoved(e.getPoint()))
                                this.aEngine.removeTiles(this.aMousePos);return;
                    case 2:this.shiftScreen(e.getPoint());return;
                    case 3:return;
                }
                break;
        }
    }
    /****/ 
    private void testFlip(final Point vPoint)
    {
        if(vPoint.x>this.aPressedPoint.x+30&&this.aFliped[2]==false)
        {
            this.aEngine.hoFlipPencil();
            this.aFliped[3]=false;
            this.aFliped[2]=true;
        }
        if(vPoint.x+30<this.aPressedPoint.x&&this.aFliped[3]==false)
        {
            this.aEngine.hoFlipPencil();
            this.aFliped[2]=false;
            this.aFliped[3]=true;
        }
        if(vPoint.y>this.aPressedPoint.y+30&&this.aFliped[1]==false)
        {
            this.aEngine.veFlipPencil();
            this.aFliped[0]=false;
            this.aFliped[1]=true;
        }
        if(vPoint.y+30<this.aPressedPoint.y&&this.aFliped[1]==true)
        {
            this.aEngine.veFlipPencil();
            this.aFliped[1]=false;
            this.aFliped[0]=true;
        }
    }
    private boolean hasFliped()
    {
        boolean vFliped = false;
        for(boolean vDirection:this.aFliped)
        {
            if(vDirection==true)
                vFliped=true;
        }
        this.aFliped = new boolean[]{false,false,false,false};
        return vFliped;
    }
    /****/ 
    @Override public void mousePressed(MouseEvent e)
    {
        this.aPressedPoint = e.getPoint();
        this.aPressedButton = e.getButton();
        switch(this.aEngine.getCurrentTool())
        {
            case "Pencil":
                switch(this.aPressedButton)
                {   
                    case 1:this.aEngine.updateStack();
                           this.aEngine.placeTiles(this.aMousePos);break;
                    case 2:break;
                    case 3:break;
                }
                          break;                          
            case "Select":
                switch(this.aPressedButton)         
                {   
                    case 1:this.aEngine.setCursorIcon("Pressed");
                           this.aEngine.placePos1(this.aMousePos);
                           this.aEngine.placePos2(this.aMousePos);break;
                    case 2:break;
                    case 3:break;
                }
                          break;
            case "Gum"   :
                switch(this.aPressedButton)
                {   
                    case 1:this.aEngine.setCursorIcon("Pressed");
                           this.aEngine.updateStack();
                           this.aEngine.removeTiles(this.aMousePos);break;
                    case 2:break;
                    case 3:break;
                }
                break;
        }
    }
    /****/ 
    @Override public void mouseReleased(MouseEvent e)
    {
        switch(this.aEngine.getCurrentTool())
        {
            case "Pencil":
                switch(this.aPressedButton)
                {   
                     case 1:this.aEngine.setCursorIcon("Pencil");break;
                     case 2:this.aEngine.setCursorIcon("Pencil");break;
                     case 3:if(!hasFliped())
                               this.aEngine.rotatePencil();
                            this.hasMoved(e.getPoint());break;
                }
                break;                          
            case "Select":
                switch(this.aPressedButton)
                {   
                    case 1:this.aEngine.setCursorIcon("Select");break;
                    case 2:this.aEngine.setCursorIcon("Select");break;
                    case 3:break;
                }
                          break;
            case "Gum"   :
                switch(this.aPressedButton)
                {   
                    case 1:this.aEngine.setCursorIcon("Gum");break;
                    case 2:this.aEngine.setCursorIcon("Gum");break;
                    case 3:break;
                }
        }          
        this.aPressedPoint = null;
        this.aPressedButton = 0;
    }
    /****/ 
    @Override public void mouseClicked(MouseEvent e){}
    /****/ 
    @Override public void mouseEntered(MouseEvent e){}
    /****/ 
    @Override public void mouseExited(MouseEvent e){}
}
