import javax.swing.JComponent;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Color;
import java.awt.Font;

/***/
public class Infos extends JComponent
{
    private Dimension aDrawAreaSize;
    private Dimension aCaseSize;
    private Point aMousePos;
    private Point aOrigin;
    private int aTilesNbr;
    public double rpntTime=0;
    public double TtlRpntTime=0;
    /****/ 
    public Infos(final Dimension pDrawAreaSize, final Dimension pCaseSize, final Point pOrigin,final Point pMousePos)
    {
        this.aDrawAreaSize=pDrawAreaSize;
        this.aCaseSize=pCaseSize;
        this.aMousePos=pMousePos;
        this.aOrigin=pOrigin;
    }
    /****/ 
    public void setTilesNbr(final int pNbr){this.aTilesNbr=pNbr;} 
    /****/ 
    @Override public void paint(final Graphics g)
    {
        Graphics2D g2=(Graphics2D)g;
        
        this.drawText("Case Size: "+"["+this.aCaseSize.width+","+this.aCaseSize.height+"]  ",g2,1);
        this.drawText("Position : "+"["+this.aMousePos.x+","+this.aMousePos.y+"]  ",g2,2);
        this.drawText("Tiles Nubmr: "+this.aTilesNbr+"  ",g2,3);
        this.drawText("Music: None  ",g2,4);
        this.drawText("RpntTime:"+TtlRpntTime+"  ",g2,4);
    } 
    /****/ 
    private void drawText(final String pText, final Graphics2D g2, final int pRowNbr)
    {
        double vTest=System.currentTimeMillis();
        
        FontMetrics vFM=g2.getFontMetrics(g2.getFont());
        g2.setColor(new Color(20,20,30,190));
        g2.fillRect(this.aDrawAreaSize.width-vFM.stringWidth(pText),this.aDrawAreaSize.height-(pRowNbr)*vFM.getHeight(),vFM.stringWidth(pText),vFM.getHeight());
        g2.setColor(new Color(175,175,205));
        g2.drawString(pText,this.aDrawAreaSize.width-vFM.stringWidth(pText),this.aDrawAreaSize.height-(pRowNbr-1)*vFM.getHeight()-3);
        
        rpntTime=System.currentTimeMillis()-vTest;
    }
}