import java.nio.charset.StandardCharsets;
import javax.swing.JButton;
import java.util.Iterator;
import java.awt.Dimension;
import java.util.HashMap;
import java.awt.Toolkit;
import java.awt.Point;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
/****/
public class EditorEngine
{ 
    private Dimension aOptionSize;
    private Dimension aDrawAreaSize;
    private Point aDrawAreaLocation;
    
    private Tools aTools;
    private Mouse aMouse;
    private Keyboard aKeyboard;
    private UserInterface aInterface;
    private SaveButtons aSaveButtons;
    private TileButtons aTileButtons;  
    private OptionButtons aOptionButtons;
    
    private DrawArea aCurrentDrawArea;
    private HashMap<JButton,DrawArea> aDrawAreas;
    /****/ 
    public EditorEngine()
    {
        Dimension vScreenSize=(Toolkit.getDefaultToolkit().getScreenSize()); 
        
        int vOptionsWidth=200;
        int vOptionsHeight=vOptionsWidth/2;
        int vSavesHeight=vOptionsHeight/4;
        int vDrawAreaWidth=vScreenSize.width-vOptionsWidth;
        int vTileButtonsHeight=(int)vScreenSize.getHeight()-vOptionsHeight;
        
        this.aDrawAreaSize=new Dimension(vDrawAreaWidth,(int)vScreenSize.getHeight()-vSavesHeight);
        this.aOptionSize=new Dimension(vOptionsWidth,vOptionsHeight);
        this.aDrawAreaLocation=new Point(0,vSavesHeight);
    
        this.aInterface=new UserInterface();
        this.aTools=new Tools(this.aInterface.getFrame());
        this.aMouse=new Mouse(this);
        this.aKeyboard=new Keyboard(this);
        
        this.aSaveButtons=new SaveButtons(this,new Dimension(vDrawAreaWidth,vSavesHeight));
        this.aOptionButtons=new OptionButtons(this,new Point(vDrawAreaWidth,0),this.aOptionSize);
        this.aTileButtons=new TileButtons(this,new Point(vDrawAreaWidth,vOptionsHeight),new Dimension(vOptionsWidth,vTileButtonsHeight));
        
        this.aCurrentDrawArea=null;
        this.aDrawAreas=new HashMap<JButton,DrawArea>();
        this.setInterface();
        (new RepaintThread(this,20)).start();
    }       
    /****/ 
    private void setInterface() 
    {
        this.aInterface.addComponent(this.aSaveButtons.getPanel());
        this.aInterface.addComponent(this.aOptionButtons.getPanel());
        this.aInterface.addComponent(this.aTileButtons.getScrollPane());
        this.aInterface.getFrame().addKeyListener(this.aKeyboard);
    }
    public void cleanMap(){if(this.aCurrentDrawArea!=null){this.aCurrentDrawArea.updateStack(); this.aCurrentDrawArea.cleanMap();}}
    public void shiftX(final int pShift){if(this.aCurrentDrawArea!=null){this.aCurrentDrawArea.shiftX(pShift);}} 
    public void shiftY(final int pShift){if(this.aCurrentDrawArea!=null){this.aCurrentDrawArea.shiftY(pShift);}} 
    public void zoom(final int pZoom){if(this.aCurrentDrawArea!=null){this.aCurrentDrawArea.zoom(pZoom);}} 
    public void reverseHitbox(){if(this.aCurrentDrawArea!=null){this.aCurrentDrawArea.reverseHitbox();}} 
    public void reverseGrid(){if(this.aCurrentDrawArea!=null){this.aCurrentDrawArea.reverseGrid();}} 
    public void clearMap(){if(this.aCurrentDrawArea!=null){this.aCurrentDrawArea.clearMap();}}
    public void updateStack(){if(this.aCurrentDrawArea!=null){this.aCurrentDrawArea.updateStack();}}
    public void placePos1(final Point pPos){if(this.aCurrentDrawArea!=null){this.aCurrentDrawArea.placePos1(pPos);}}  
    public void placePos2(final Point pPos){if(this.aCurrentDrawArea!=null){this.aCurrentDrawArea.placePos2(pPos);}}
    public void repaint(){if(this.aCurrentDrawArea!=null){this.aCurrentDrawArea.repaint();}}
    public void undo(){if(this.aCurrentDrawArea!=null){this.aCurrentDrawArea.undo();}}
    public void redo(){if(this.aCurrentDrawArea!=null){this.aCurrentDrawArea.redo();}}
    public void setCurrentTool(final String pTool){this.aTools.setCurrentTool(pTool);} 
    public void setPencil(final String pImagePath){this.aTools.setPencil(pImagePath);} 
    public void setCursorIcon(final String pIcon){this.aTools.setCursorIcon(pIcon);}     
    public String getCurrentTool(){return this.aTools.getCurrentTool();} 
    public void veFlipPencil(){this.aTools.veFlipPencil();}  
    public void hoFlipPencil(){this.aTools.hoFlipPencil();} 
    public void rotatePencil(){this.aTools.rotatePencil();}   
    /****/ 
    public void addDrawArea(final JButton pButton) 
    {
        DrawArea vDrawArea=new DrawArea(pButton, this.aMouse.getMousePos(), this.aTools.getPencil());
        vDrawArea.getPanel().setLocation(aDrawAreaLocation);
        vDrawArea.setSize(aDrawAreaSize);
        this.aDrawAreas.put(pButton,vDrawArea);
        vDrawArea.getPanel().addMouseListener(this.aMouse);
        vDrawArea.getPanel().addMouseMotionListener(this.aMouse);
        vDrawArea.getPanel().addMouseWheelListener(this.aMouse);
    }
    /****/ 
    public void saveDrawArea(final JButton pButton, final String pName)
    {
        if(this.aCurrentDrawArea==null){
            return;}
        pButton.setText(pName);
        List<String> vLines=new ArrayList<String>();
        for(Map.Entry<String,ArrayList<String>> vEntry : this.aCurrentDrawArea.mapToStrings().entrySet()) {
           vLines.add(vEntry.getKey());
           for(String vLine: vEntry.getValue()){
               vLines.add(vLine);}
        }
        try{Files.write(Paths.get("Maps/"+pName+".txt"),vLines,StandardCharsets.UTF_8 );}
        catch (java.io.IOException ioe){System.out.println("erreur de sauvegarde");}
    }
    /****/ 
    public void openDrawArea(final JButton pButton)
    {
        DrawArea vDrawArea=new DrawArea(pButton, this.toDrawArea("Maps/"+pButton.getText()+".txt"),this.aMouse.getMousePos(), this.aTools.getPencil());
        vDrawArea.getPanel().setLocation(aDrawAreaLocation);
        vDrawArea.setSize(aDrawAreaSize);
        this.aDrawAreas.put(pButton,vDrawArea);
        vDrawArea.getPanel().addMouseListener(this.aMouse);
        vDrawArea.getPanel().addMouseMotionListener(this.aMouse);
        vDrawArea.getPanel().addMouseWheelListener(this.aMouse);
    }
    /****/ 
    public ArrayList<Tile> toDrawArea(final String pPath) 
    {
        ArrayList<Tile> vMap=new ArrayList<Tile>();
        try
        {
            List<String> vLines = Files.readAllLines(Paths.get(pPath));
            String vImagePath=null;
            for(String vLine: vLines)
            {
                String vValues[]=vLine.split("/"); 
                try{
                    switch(vValues[0])
                    {
                        case "Images":vImagePath=vLine;break; 
                        case "Maps":break;
                        default:Tile vTile=new Tile(vImagePath, Integer.parseInt(vValues[0]), Integer.parseInt(vValues[1])); 
                                for(int vRotation=0;vRotation<Integer.parseInt(vValues[2]);vRotation++){
                                    vTile.rotate();}
                                if(vValues[3].equals("1")){
                                     vTile.verticalFlip();}
                                if(vValues[4].equals("1")){ 
                                     vTile.horizontalFlip();}
                                vMap.add(vTile);break;
                                
                    } 
                }
                            catch(Exception e){}
             }  
        }
        catch (java.io.IOException ioe){ioe.printStackTrace();}
        return vMap;
    }
    /****/ 
    public void setCurrentDrawArea(final JButton pButton)
    {
        if(!this.aDrawAreas.containsKey(pButton)){ 
            return;} 
        if(this.aCurrentDrawArea!=null){
            this.aInterface.removeComponent(this.aCurrentDrawArea.getPanel());}
        this.aCurrentDrawArea=this.aDrawAreas.get(pButton);
        this.aInterface.addComponent(this.aCurrentDrawArea.getPanel());
    }
    /****/ 
    public void removeDrawArea(final JButton pButton)
    {
        if(!this.aDrawAreas.containsKey(pButton)){ 
            return;} 
        DrawArea vRemoved=this.aDrawAreas.get(pButton);
        this.aDrawAreas.remove(pButton);   
        if(this.aCurrentDrawArea!=null&&this.aCurrentDrawArea.equals(vRemoved))
        {
            this.aInterface.removeComponent(this.aCurrentDrawArea.getPanel());
            this.aCurrentDrawArea=null;
            Optional<JButton> firstKey = this.aDrawAreas.keySet().stream().findFirst();
            if (firstKey.isPresent()) 
            {
                this.aSaveButtons.requestFocus( firstKey.get());
            }          
        }
        this.aInterface.getFrame().repaint();
    }
    /****/ 
    public void placeTiles(final Point pMousePos)
    {
        if(this.aCurrentDrawArea == null)
            return;
        for(Tile vTile: this.aTools.getPencil())
            this.aCurrentDrawArea.getMap().add(new Tile(vTile,pMousePos.x,pMousePos.y));
    }
    /****/ 
    public void removeTiles(final Point pMousePos)
    {
        if(this.aCurrentDrawArea == null)
            return;
        Iterator<Tile> vIterator=this.aCurrentDrawArea.getMap().iterator();
        while(vIterator.hasNext())
        {
            if(vIterator.next().hasInside(pMousePos))
                vIterator.remove();
        }
    }
    /****/ 
    public void copySelection(final boolean pCut)
    {
        if(this.aCurrentDrawArea==null){ 
            return;}
        this.aTools.getPencil().clear();
        Iterator<Tile> vIterator=this.aCurrentDrawArea.getMap().iterator();
        while(vIterator.hasNext())
        {
            Tile vTile=vIterator.next();
            if(vTile.isOverlay(this.aCurrentDrawArea.getSelectionPos1(),this.aCurrentDrawArea.getSelectionPos2()))
            {
                this.aTools.getPencil().add(new Tile(vTile,0,0));
                if(pCut){
                    vIterator.remove();}
            }
        }
        this.aTools.replacePencil();
        this.aTools.setCurrentTool("Pencil");
    }
    /****/ 
    public Point getPos(final Point pMousePoint)
    {
        if(this.aCurrentDrawArea==null){ 
            return null;}
        return this.aCurrentDrawArea.getPos(pMousePoint);  
    }
}
/****/
    class RepaintThread extends Thread
{
    private EditorEngine aEngine;
    private int aFPS;
    /****/ 
    public RepaintThread(final EditorEngine pEngine, final int pFramesPerSecond)
    {
        this.aEngine=pEngine;
        this.aFPS=pFramesPerSecond;
    }
    /****/ 
    @Override public void run()
    {
        // double vTest=System.currentTimeMillis();
        // System.out.println("Total:"+(System.currentTimeMillis()-vTest));
        while(true)
        {
            try
            { 
                this.aEngine.repaint();
                this.sleep(1000/this.aFPS);
            }
            catch (InterruptedException ie){ie.printStackTrace();}
        }
    }
}