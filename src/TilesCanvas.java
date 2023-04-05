import javax.swing.JComponent;
import java.util.Comparator;
import java.util.ArrayList;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
/****/ 
public class TilesCanvas extends JComponent
{
    private ArrayList<Tile> aTiles;
    private Dimension aCaseSize;
    private Point aOrigin;
    private Point aShift;
    public double rpntTime;
    /****/ 
    public TilesCanvas(ArrayList<Tile> pTiles, final Dimension pCaseSize, final Point pOrigin, final Point pShift)
    {
        this.aCaseSize = pCaseSize;
        this.aOrigin = pOrigin;
        this.aShift = pShift;
        this.aTiles = pTiles;
    }
    /****/ 
    @Override public void paint(Graphics g)
    {
        double vTest=System.currentTimeMillis();
        
        this.aTiles.sort(Comparator.comparing(Tile::getLayer).thenComparing(Tile::getLastRow));
        for(Tile vTile:this.aTiles)
        {
            int vX=(vTile.getColumn()+this.aShift.x)*this.aCaseSize.width+this.aOrigin.x;
            int vY=(vTile.getRow()+this.aShift.y)*this.aCaseSize.height+this.aOrigin.y;
            int vWidth=vTile.getWidth()*this.aCaseSize.width;
            int vHeight=vTile.getHeight()*this.aCaseSize.height;
            g.drawImage(vTile.getImage(),vX,vY,vWidth,vHeight,this);
        }
        
        rpntTime=System.currentTimeMillis()-vTest;
    } 
}