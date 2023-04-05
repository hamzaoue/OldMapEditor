import javax.swing.JComponent;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Point;
/****/
public class Grid extends JComponent
{
    private Dimension aCaseSize;
    private Point aOrigin;
    /****/ 
    public Grid(final Dimension pCaseSize, final Point pOrigin)
    {
        this.setForeground(new Color(175,175,205));
        this.aCaseSize = pCaseSize;
        this.aOrigin = pOrigin;
    }
    /****/ 
    @Override public void paint(Graphics g) 
    {  
        g.drawLine(this.aOrigin.x+this.aCaseSize.width,this.aOrigin.y,this.aOrigin.x,this.aOrigin.y+this.aCaseSize.height);
        for(int vX=(this.aOrigin.x%this.aCaseSize.width);vX<this.getWidth();vX+=this.aCaseSize.width) 
            g.drawLine(vX,0,vX,this.getHeight());
        for(int vY=(this.aOrigin.y%this.aCaseSize.height);vY<this.getHeight();vY+=this.aCaseSize.height)
            g.drawLine(0,vY,this.getWidth(),vY);   
    }
}