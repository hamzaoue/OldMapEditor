import javax.swing.JComponent;
import java.awt.FontMetrics;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Color;
import java.awt.Font;
/**JComponent qui paint un rectangle de selection*/  
    class Selection extends JComponent
{
    private Dimension aCaseSize;
    private Point aOrigin;
    private Point aPos1;
    private Point aPos2;
    /**Constructeur*/ 
    public Selection(final Dimension pCaseSize, final Point pOrigin)
    {
        this.setForeground(new Color(200,200,200,130));
        this.aCaseSize = pCaseSize;
        this.aPos1 = new Point();
        this.aPos2 = new Point();
        this.aOrigin = pOrigin;
    }
    /****/ 
    @Override public void paint(final Graphics g)
    {
        int vX = (Math.min(this.aPos1.x,this.aPos2.x))*this.aCaseSize.width+this.aOrigin.x;
        int vY = (Math.min(this.aPos1.y,this.aPos2.y))*this.aCaseSize.height+this.aOrigin.y;
        int vWidth = (Math.abs(this.aPos1.x-this.aPos2.x)+1)*this.aCaseSize.width;
        int vHeight = (Math.abs(this.aPos1.y-this.aPos2.y)+1)*this.aCaseSize.height;
        g.fillRect(vX,vY,vWidth,vHeight);
        
        g.setFont(new Font("Poor Richard",Font.BOLD,this.aCaseSize.width/2));
        FontMetrics vFM = g.getFontMetrics();
        g.setColor(Color.white);
        
        String vText = ""+vWidth/this.aCaseSize.width;
        int vTextX = vX+(vWidth-vFM.stringWidth(vText))/2;
        int vTextY = vY+vFM.getAscent()/2;
        g.drawString(vText,vTextX,vTextY);
        
        vText = ""+vHeight/this.aCaseSize.height;
        vTextX = vX-vFM.stringWidth(vText)/2;
        vTextY = vY+(vHeight-vFM.getHeight())/2+vFM.getAscent();
        g.drawString(vText,vTextX,vTextY);
    }
    /****/ 
    public Point getPos1(){return this.aPos1;}
    /****/ 
    public Point getPos2(){return this.aPos2;}
}