import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
/****/
public class AnimatedTile extends Tile
{
    private int aCurrentImageIndex;
    private BufferedImage[] aImages;
    private String[] aImagePaths;
    /****/ 
    public AnimatedTile(final String[] pImagePaths, final int pColumn, final int pRow)
    {
        super(pImagePaths[0], pColumn , pRow); 
        this.aCurrentImageIndex = 0;
        this.aImagePaths = pImagePaths;
        this.aImages = new BufferedImage[pImagePaths.length];
        this.aImages[0]= super.aImage;
        for(int vIndex = 1; vIndex < this.aImagePaths.length; vIndex++)
        {
            try
            {this.aImages[vIndex] = ImageIO.read(new File(this.aImagePaths[vIndex]));}
            catch (java.io.IOException ioe)
            {this.aImages[vIndex] = null;}
        }
    }
    /****/ 
    public void nextImage()
    {
        this.aCurrentImageIndex ++;
        if(this.aCurrentImageIndex > this.aImages.length)
            this.aCurrentImageIndex = 0;
        super.aImage = this.aImages[this.aCurrentImageIndex];
    }
}