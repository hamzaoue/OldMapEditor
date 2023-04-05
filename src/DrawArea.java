import javax.swing.OverlayLayout;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import java.awt.Point;
import java.awt.Color;
import java.util.HashMap;
import java.util.Arrays;
import javax.swing.JButton;
/****/
public class DrawArea 
{ 
    private JButton aButton;
    private Stack<ArrayList<Tile>> aUndoStack;
    private Stack<ArrayList<Tile>> aRedoStack;
    private ArrayList<Tile> aMap;
    private Dimension aCaseSize;
    private JPanel aPanel;
    private Point aOrigin;
    
    private Grid aGrid;
    private Infos aInfos;
    private TilesCanvas aMapCanvas;
    private Selection aSelection;
    private TilesCanvas aPencilCanvas;
    private BoxesCanvas aBoxesCanvas;
    /****/ 
    public DrawArea(final JButton pButton, final Point pMousePos, final ArrayList<Tile> pPencil)
    {
        this(pButton, new ArrayList<Tile>(), pMousePos, pPencil);
    }
    /****/ 
    public DrawArea(final JButton pButton, final ArrayList<Tile> pMap, final Point pMousePos, final ArrayList<Tile> pPencil)
    {
        this.aButton=pButton;
        
        this.aUndoStack = new Stack<ArrayList<Tile>>();
        this.aRedoStack = new Stack<ArrayList<Tile>>();
        this.aCaseSize = new Dimension(40, 40);
        this.aMap = pMap;
        this.aOrigin = new Point(0,0);
        this.setPanel();
        
        this.aGrid = new Grid(this.aCaseSize, this.aOrigin);
        this.aBoxesCanvas = new BoxesCanvas(this.aMap,this.aCaseSize,this.aOrigin);
        this.aSelection = new Selection(this.aCaseSize,this.aOrigin);
        this.aInfos = new Infos(this.aPanel.getSize(), this.aCaseSize, this.aOrigin, pMousePos);
        this.aPencilCanvas = new TilesCanvas(pPencil,this.aCaseSize,this.aOrigin,pMousePos);
        this.aMapCanvas = new TilesCanvas(pMap,this.aCaseSize,this.aOrigin,new Point(0,0));
        
        this.aPanel.add(this.aInfos);
        this.aPanel.add(this.aSelection);
        this.aPanel.add(this.aPencilCanvas);
        this.aPanel.add(this.aBoxesCanvas);
        this.aPanel.add(this.aMapCanvas);
        this.aPanel.add(this.aGrid);
    }
    public JButton getButton(){return this.aButton;} 
    public JPanel getPanel(){return this.aPanel;}
    public Point getOrigin(){return this.aOrigin;}  
    public void reverseGrid(){this.aGrid.setVisible(!this.aGrid.isVisible());} 
    public ArrayList<Tile> getMap(){return this.aMap;} 
    public void placePos1(final Point pPos){this.aSelection.getPos1().setLocation(pPos);}
    public void placePos2(final Point pPos){this.aSelection.getPos2().setLocation(pPos);}
    public Dimension getCaseSize(){return this.aCaseSize;} 
    public void reverseHitbox(){this.aBoxesCanvas.reverse();}
    public void clearMap(){this.updateStack(); this.aMap.clear();}
    public void shiftY(final int pShift){this.aOrigin.y += pShift;} 
    public void shiftX(final int pShift){this.aOrigin.x += pShift;}     
    public Point getSelectionPos1(){return this.aSelection.getPos1();}
    public Point getSelectionPos2(){return this.aSelection.getPos2();}
    private void setTilesNbr(final int pNbr){this.aInfos.setTilesNbr(pNbr);}
    public void updateStack(){this.aUndoStack.push((ArrayList<Tile>)this.aMap.clone());}
    /****/ 
    public HashMap<String,ArrayList<String>> mapToStrings()
    {
        HashMap<String,ArrayList<String>> vImagesPath = new HashMap<String,ArrayList<String>>();
        for(Tile vTile: this.aMap)
        {
            int vVeF=vTile.getVeFlip() ? 1:0;  
            int vHoF=vTile.getHoFlip() ? 1:0;
            String vLine=vTile.getColumn()+"/"+vTile.getRow()+"/"+vTile.getRotation()+"/"+vVeF+"/"+vHoF;
            if(vImagesPath.containsKey(vTile.getImagePath())){
                vImagesPath.get(vTile.getImagePath()).add(vLine);}
            else{
                vImagesPath.put(vTile.getImagePath(),new ArrayList<String>(Arrays.asList(vLine)));}
        }
        return vImagesPath;
    }
    public void setSize(final Dimension pSize)
    {
        this.aGrid.setSize(pSize);
        this.aPanel.setSize(pSize); 
        this.aPanel.revalidate();
    }
    /****/  
    public void repaint()
    {
        this.setTilesNbr(this.aMap.size());
        this.aPanel.repaint();
        aInfos.TtlRpntTime = aMapCanvas.rpntTime+aInfos.rpntTime+aBoxesCanvas.rpntTime;
    }   
    /****/  
    public void undo()
    {
        if(!this.aUndoStack.empty())
        {
            this.aRedoStack.push((ArrayList<Tile>)this.aMap.clone());
            this.aMap.clear();
            for(Tile vTile: this.aUndoStack.pop()){
                 this.aMap.add(vTile);}
        }
    }
    /****/  
    public void redo()
    {
        if(!this.aRedoStack.empty())
        {
            this.aUndoStack.push((ArrayList<Tile>)this.aMap.clone());
            this.aMap.clear();
            for(Tile vTile: this.aRedoStack.pop()){
                 this.aMap.add(vTile);}
        }
    }
    /****/  
    public void cleanMap()
    {
        ArrayList<Tile> vPreviousMap=(ArrayList<Tile>)this.aMap.clone();
        this.aMap.clear();
        for(int vIndex=vPreviousMap.size()-1;vIndex>=0;vIndex--)
        {
            if(!this.contains(this.aMap,vPreviousMap.get(vIndex)))
                this.aMap.add(vPreviousMap.get(vIndex));
        }
    }
    /****/ 
    public boolean contains(final ArrayList<Tile> pList, final Tile pTile)
    {
        for(Tile vTile: pList)
        {
            if(vTile.isOverlay(pTile)){
                return true;}
        }
        return false;
    }
    /****/ 
    public Point getPos(final Point pMousePoint)
    {
        int vColumn=(int)Math.floor((pMousePoint.x-this.aOrigin.x)/(double)this.aCaseSize.width);
        int vRow=(int)Math.floor((pMousePoint.y-this.aOrigin.y)/(double)this.aCaseSize.height);
        return new Point(vColumn,vRow);
    }
    /****/ 
    public void zoom(final int pZoom)
    {
        if(( (this.aCaseSize.width + 4*pZoom) >= 20) && ((this.aCaseSize.height + 4*pZoom) >= 20))
        {
            this.aCaseSize.width += (4*pZoom); 
            this.aCaseSize.height += (4*pZoom);
        }
    }
    /****/ 
    private void setPanel() 
    { 
        this.aPanel = new JPanel();
        this.aPanel.setLayout(new OverlayLayout(this.aPanel));
        this.aPanel.setBackground(Color.black);
    }
}