import javax.swing.ImageIcon;
import java.util.ArrayList;
import javax.swing.JFrame;
import java.util.HashMap;
import java.awt.Toolkit;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Image;
/****/
public class Tools
{
    private HashMap<String,Cursor> aCursors;
    private ArrayList<Tile> aPencil;
    private String aCurrentTool;
    private JFrame aFrame;
    /****/ 
    public Tools(final JFrame pFrame)
    {
        this.aPencil=new ArrayList<Tile>();
        this.aCurrentTool="Pencil";
        this.aFrame=pFrame;
        this.initCursors();
    }
    /****/ 
    public ArrayList<Tile> getPencil(){return this.aPencil;}
    /****/ 
    public String getCurrentTool(){return this.aCurrentTool;}
    /****/ 
    public void setCurrentTool(final String pTool)
    {
        if(pTool!="Pencil")
            this.aPencil.clear();
        this.aCurrentTool=pTool;
        this.setCursorIcon(pTool);
    } 
    /****/ 
    public void setCursorIcon(final String pCursor){this.aFrame.setCursor(this.aCursors.get(pCursor));}
    /****/ 
    public void setPencil(final String pImagePath)
    {
        this.setCurrentTool("Pencil");
        this.aPencil.clear();
        this.aPencil.add(new Tile(pImagePath,0,0));
    }
    /****/ 
    public void rotatePencil()
    {
        for(Tile vTile:this.aPencil){
            vTile.rotate();}
    }
    /****/ 
    public void veFlipPencil()
    {
        for(Tile vTile:this.aPencil){
            vTile.verticalFlip();}
    }
    /****/ 
    public void hoFlipPencil()
    {
        for(Tile vTile:this.aPencil){
            vTile.horizontalFlip();}
    }
    /****/ 
    public void replacePencil()
    {
        Integer vFirstColumn=null;
        Integer vFirstRow=null;
        for(Tile vTile:this.aPencil)
        {
            if(vFirstColumn==null||vFirstColumn>vTile.getColumn()){
                vFirstColumn=vTile.getColumn();}
            if(vFirstRow==null||vFirstRow>vTile.getRow()){
                vFirstRow=vTile.getRow();}
        }
        for(Tile vTile:this.aPencil)
        {
            vTile.setColumn(vTile.getColumn()-vFirstColumn);
            vTile.setRow(vTile.getRow()-vFirstRow);
        }
    }
    /****/ 
    private void initCursors()
    {
        this.aCursors=new HashMap<String,Cursor>();
        Toolkit vTk=Toolkit.getDefaultToolkit();
        Point vPoint1=new Point(10,13);
        
        Cursor vCursor3=vTk.createCustomCursor((new ImageIcon("Tools/Gum.png")).getImage(),vPoint1,"");
        Cursor vCursor4=vTk.createCustomCursor((new ImageIcon("Tools/DoorIn.png")).getImage(),vPoint1,"");
        Cursor vCursor5=vTk.createCustomCursor((new ImageIcon("Tools/DoorOut.png")).getImage(),vPoint1,"");
        Cursor vCursor1=vTk.createCustomCursor((new ImageIcon("Tools/Select.png")).getImage(),vPoint1,"");
        Cursor vCursor2=vTk.createCustomCursor((new ImageIcon("Tools/Pressed.png")).getImage(),vPoint1,"");
        
        this.aCursors.put("Gum",vCursor3);
        this.aCursors.put("DoorIn",vCursor4);
        this.aCursors.put("DoorOut",vCursor5);
        this.aCursors.put("Select",vCursor1);
        this.aCursors.put("Pressed",vCursor2);
        this.aCursors.put("Move",new Cursor(Cursor.MOVE_CURSOR));
        this.aCursors.put("Pencil",new Cursor(Cursor.DEFAULT_CURSOR));
    }
}