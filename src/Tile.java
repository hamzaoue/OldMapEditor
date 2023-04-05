import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Point;
import java.io.File;
/****/
 class Tile
{
    protected BufferedImage aImage;
    private String aImagePath;
    
    private int aColumn;
    private int aRow;
    
    private int aLayer;
    private int aWidth;
    private int aHeight;
    
    private int aRotation;
    private boolean aVerticalFlip;
    private boolean aHorizontalFlip;
    /****/ 
    public Tile(final String pImagePath, final int pColumn, final int pRow)
    {
        try
        {this.aImage = ImageIO.read(new File(pImagePath));}
        catch (java.io.IOException ioe)
        {this.aImage = null;}
        
        this.aImagePath=pImagePath;
        
        this.aColumn=pColumn;
        this.aRow=pRow;
        
        this.aLayer=0;
        this.aWidth=1;
        this.aHeight=1;
        
        if(this.aImagePath.contains("(L"))
            this.aLayer=Character.getNumericValue(this.aImagePath.charAt(this.aImagePath.indexOf("(L")+2));
        if(this.aImagePath.contains("(W"))
            this.aWidth=Character.getNumericValue(this.aImagePath.charAt(this.aImagePath.indexOf("(W")+2));
        if(this.aImagePath.contains("(H"))
            this.aHeight=Character.getNumericValue(this.aImagePath.charAt(this.aImagePath.indexOf("(H")+2));
        
        this.aRotation=0;
        this.aVerticalFlip=false;
        this.aHorizontalFlip=false;
    }
    /****/ 
    public Tile(final Tile pTile,final int pColumnShift,final int pRowShift)
    {
        this(pTile.getImagePath(),pTile.getColumn()+pColumnShift,pTile.getRow()+pRowShift);
        while(this.aRotation!=pTile.getRotation()) 
            this.rotate();
        if(pTile.getVeFlip())
            this.verticalFlip();
        if(pTile.getHoFlip())
            this.horizontalFlip(); 
    }
    /****/ 
    public void setColumn(final int pColumn){this.aColumn=pColumn;}
    /****/ 
    public void setRow(final int pRow){this.aRow=pRow;}
    /****/ 
    public String getImagePath(){return this.aImagePath;}
    /****/ 
    public BufferedImage getImage(){return this.aImage;}  
    /****/ 
    public int getLayer(){return this.aLayer;}
    /****/ 
    public int getColumn(){return this.aColumn;}
    /****/ 
    public int getLastRow(){return this.aRow+this.getHeight();}    
    /****/ 
    public int getRow(){return this.aRow;} 
    /****/ 
    public int getRotation(){return this.aRotation;}
    /**RETURN LA LARGEUR EN NBR DE COLONNES, PREND EN COMPTE LES ROTATIONS*/ 
    public int getWidth()
    {
        if(this.aRotation==1||this.aRotation==3)
            return this.aHeight;
        else
            return this.aWidth;
    }
    /**RETURN LA HAUTEUR EN NBR DE LIGNES, PREND EN COMPTE LES ROTATIONS*/ 
    public int getHeight()
    {
        if(this.aRotation==1||this.aRotation==3)
            return this.aWidth;
        else
            return this.aHeight;
    }
    /****/ 
    public boolean getVeFlip(){return this.aVerticalFlip;}
    /****/ 
    public boolean getHoFlip(){return this.aHorizontalFlip;}
    /**HORIZONTAL FLIP*/ 
    public void horizontalFlip()
    {
        if(this.aImage!=null)
        {
            int vW=this.aImage.getWidth(null), vH=this.aImage.getHeight(null);
            BufferedImage vBufferedImage = new BufferedImage(vW,vH, BufferedImage.TYPE_INT_ARGB);
            (vBufferedImage.createGraphics()).drawImage(this.aImage, vW, 0, -vW,vH, null);
            this.aImage=vBufferedImage;
        }
        this.aHorizontalFlip=!this.aHorizontalFlip;
    }  
    /**VERTICAL FLIP*/ 
    public void verticalFlip()
    {
        if(this.aImage!=null)
        {
            int vW=this.aImage.getWidth(null), vH=this.aImage.getHeight(null);
            BufferedImage vBufferedImage = new BufferedImage(vW,vH, BufferedImage.TYPE_INT_ARGB);
            (vBufferedImage.createGraphics()).drawImage(this.aImage, 0, vH, vW,-vH, null);
            this.aImage=vBufferedImage;
        }
        this.aVerticalFlip=!this.aVerticalFlip;
    } 
    /**CLOCK ROTATION*/  
    public void rotate() 
    { 
        if((this.aVerticalFlip==this.aHorizontalFlip))
            this.aRotation=this.aRotation+1;
        else
            this.aRotation=this.aRotation-1;
        if(this.aRotation==4)
            this.aRotation=0; 
        else if(this.aRotation==-1)
            this.aRotation=3; 
        if(this.aImage!=null)
        {
            int vW=this.aImage.getWidth(null), vH=this.aImage.getHeight(null);
            BufferedImage vBufferedImage = new BufferedImage(vH,vW, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2=(vBufferedImage.createGraphics());
            g2.rotate(Math.toRadians(90),vH*0.5,vW*0.5);  
            g2.translate((vH-vW)*0.5,(vW-vH)*0.5);
            g2.drawImage(this.aImage, 0, 0, vW,vH, null);
            this.aImage=vBufferedImage;
            g2.dispose();
        }
    }
    /****/ 
    public boolean isInside(final Point pPos1 ,final Point pPos2)
    {   
        if(this.aColumn<Math.min(pPos1.getX(),pPos2.getX()))
            return false;
        if(this.aColumn+this.getWidth()>Math.max(pPos1.getX(),pPos2.getX())+1)
            return false;
        if(this.aRow<Math.min(pPos1.getY(),pPos2.getY()))
            return false;
        if(this.aRow+this.getHeight()>Math.max(pPos1.getY(),pPos2.getY())+1)
            return false;
        return true;
    }
    /****/ 
    public boolean hasInside(final Point pPos)
    {
        return (this.aColumn<=pPos.getX()&&this.aRow<=pPos.getY()&&this.aColumn+this.getWidth()>pPos.getX()&&this.aRow+this.getHeight()>pPos.getY());
    }
     /****/ 
    public boolean isOverlay(final Tile pTile)
    {
        if(pTile.getLayer()!=this.aLayer)
            return false;
        if(pTile.getColumn()>this.aColumn+this.getWidth()-1)//A droite
            return false;
        if(pTile.getColumn()+pTile.getWidth()-1<this.aColumn)//A gauche
            return false;
        if(pTile.getRow()>this.aRow+this.getHeight()-1)//En dessous
            return false;
        if(pTile.getRow()+pTile.getHeight()-1<this.aRow)//Au dessus
            return false;
        return true;
    }
    /****/ 
    public boolean isOverlay(final Point pPos1, final Point pPos2)
    {
       if(Math.min(pPos1.x,pPos2.x)>this.aColumn+this.getWidth()-1)//A droite
            return false;
       if(Math.max(pPos1.x,pPos2.x)<this.aColumn)//A gauche
            return false;
       if(Math.min(pPos1.y,pPos2.y)>this.aRow+this.getHeight()-1)//En dessous
            return false;
       if(Math.max(pPos1.y,pPos2.y)<this.aRow)//Au dessus
            return false;
       return true; 
    }
    /****/ 
    @Override public boolean equals(final Object pObject)
    {
        if(pObject instanceof Tile)
        {
            Tile vTile=(Tile)pObject;
            if(this.aRotation==vTile.getRotation()&&this.aVerticalFlip==vTile.getVeFlip()&&this.aHorizontalFlip==vTile.getHoFlip())
                return ((this.aImagePath.equals(vTile.getImagePath()))&&(this.aColumn==vTile.getColumn())&&(this.aRow==vTile.getRow()));
        }
        return false;
    }
    /****/ 
    public int[][] getBoxes()
    {
       int vStartIndex=this.aImagePath.indexOf("(B");
       if(vStartIndex!=-1)
       {
           int[][] vBoxes=new int[this.aWidth][this.aHeight];
           String vHbx=this.aImagePath.substring(vStartIndex+2,this.aImagePath.lastIndexOf(")"));
           int vColumn=0, vRow=0;
           for(int vIndex=0;vIndex<vHbx.length();vIndex++)
           {
               switch(vHbx.charAt(vIndex))
               {
                   case '-':vRow++;vColumn=0; break;
                   case '1':vBoxes[vColumn][vRow]=1;
                   default:vColumn++; break;
               }
           }
           for(int i=0;i<this.aRotation;i++)
               vBoxes=this.rotateMatrix(vBoxes);
           if(this.aVerticalFlip)
               vBoxes=this.veFlipMatrix(vBoxes);
           if(this.aHorizontalFlip)
               vBoxes=this.hoFlipMatrix(vBoxes);
           return vBoxes;
       }
       return null;
    }
    /****/ 
    private int[][] rotateMatrix(final int[][] pMatrix)
    {
        int[][] vNewMatrix=new int[pMatrix[0].length][pMatrix.length];
        for (int vColumn= 0;vColumn<pMatrix.length; vColumn++) 
            for (int vRow= 0;vRow<pMatrix[vColumn].length;vRow++) 
                vNewMatrix[pMatrix[vColumn].length-1-vRow][vColumn]=pMatrix[vColumn][vRow];         
        return vNewMatrix;
    }
    /****/ 
    private int[][] hoFlipMatrix (final int[][] pMatrix)
    {
        int[][] vNewMatrix=new int[pMatrix.length][pMatrix[0].length];
        for (int vColumn= 0;vColumn<pMatrix.length; vColumn++) 
            for (int vRow= 0;vRow<pMatrix[vColumn].length;vRow++) 
                vNewMatrix[vColumn][vRow]=pMatrix[pMatrix.length-vColumn-1][vRow];
        return vNewMatrix;
    }
    /****/ 
    private int[][] veFlipMatrix (final int[][] pMatrix)
    {
        int[][] vNewMatrix=new int[pMatrix.length][pMatrix[0].length];
        for (int vColumn= 0;vColumn<pMatrix.length; vColumn++) 
            for (int vRow= 0;vRow<pMatrix[vColumn].length;vRow++) 
                vNewMatrix[vColumn][vRow]=pMatrix[vColumn][pMatrix[vColumn].length-1-vRow];
        return vNewMatrix;
    }
}
