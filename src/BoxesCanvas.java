import javax.swing.JComponent;
import java.awt.BasicStroke;
import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Color;
/****/
public class BoxesCanvas extends JComponent
{
    private ArrayList<Tile> aTiles; 
    private Dimension aCaseSize;
    private Point aOrigin;
    public double rpntTime;
    public BoxesCanvas(final ArrayList<Tile> pTiles, final Dimension pCaseSize, final Point pOrigin)
    {
        this.aCaseSize=pCaseSize;
        this.aOrigin=pOrigin;
        this.aTiles=pTiles;
        this.setVisible(false);
    }
    /****/ 
    public void reverse(){this.setVisible(!this.isVisible());} 
    /****/ 
    @Override public void paint(Graphics g)
    {
        double vTest=System.currentTimeMillis();
        
        ((Graphics2D)g).setStroke(new BasicStroke(2));
        g.setColor(Color.red);
        for(Tile vTile:this.aTiles)
        {
            int[][] vBoxes=vTile.getBoxes();
            if(vBoxes!=null)
            {
                for(int vColumn = 0;vColumn < vBoxes.length; vColumn++){ 
                    for(int vRow = 0;vRow < vBoxes[vColumn].length; vRow++){ 
                         if(vBoxes[vColumn][vRow]==1)
                         {
                             int vX=(vTile.getColumn()+vColumn)*this.aCaseSize.width+this.aOrigin.x;
                             int vY=(vTile.getRow()+vRow)*this.aCaseSize.height+this.aOrigin.y;
                             g.drawRect(vX,vY,this.aCaseSize.width,this.aCaseSize.height);
                         }
                    }
                }
            }
        }
        
        rpntTime=System.currentTimeMillis()-vTest;
    }
}