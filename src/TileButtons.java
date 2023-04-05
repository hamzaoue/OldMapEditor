import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.GridLayout;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Arrays;
import java.awt.Point;
import java.awt.Image;
import java.awt.Color;
import java.io.File;
/****/
public class TileButtons implements ActionListener
{
    /* key=chemin de dossier, value=tableau des fichiers contenus*/
    private HashMap<String,String[]> aFoldersMap;
    private EditorEngine aEngine;
    private JScrollPane aScrollPane;
    private JPanel aPanel;
    
    private Dimension aSize;
    private Point aLocation;
    /****/ 
    public TileButtons(EditorEngine pEngine, final Point pLocation, final Dimension pSize)
    {
        this.aFoldersMap = new HashMap<String,String[]>();
        this.aEngine = pEngine;
        this.aLocation = pLocation;
        this.aSize = pSize;
        this.setPanel();
        this.setScrollPane();
        this.setFoldersMap("Images");
        this.addFileButtons("Images");
    }
    /****/ 
    public JScrollPane getScrollPane(){return this.aScrollPane; }
    /****/ 
    private void setPanel()
    {
        this.aPanel = new JPanel();
        this.aPanel.setBackground(new Color(20,20,30));
        this.aPanel.setLocation(this.aLocation);
        this.aPanel.setSize(this.aSize);
    }
    /****/ 
    private void setScrollPane()
    {
        this.aScrollPane = new JScrollPane(this.aPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.aScrollPane.getVerticalScrollBar().setUI(new ScrollBar());
        this.aScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        this.aScrollPane.setLocation(this.aLocation);
        this.aScrollPane.setSize(this.aSize);
        this.aScrollPane.setBorder(null);
    }
    /** Remplie l'attribut HashMap this.aFiles sous la forme: key=chemin vers un dossier, value=tableau de nom des fichiers contenus 
     * @param pPath chemin vers le dossier*/
    private void setFoldersMap(String pFolderPath)
    {
        File vFolder = new File(pFolderPath);
        String[] vFileNames = vFolder.list();
        Arrays.sort(vFileNames);
        this.aFoldersMap.put(pFolderPath, vFileNames);
        for (final String vFileName : vFileNames) {
            File vFile = new File(pFolderPath + "/" + vFileName); 
            if (vFile.isDirectory()) {
                this.setFoldersMap(pFolderPath + "/" + vFileName);}
        }
    }
    /****/ 
    public void actionPerformed(ActionEvent pEvent)
    { 
        String vPath = ((JButton)pEvent.getSource()).getActionCommand();
        if (this.aFoldersMap.containsKey(vPath)) 
        {
            this.aPanel.removeAll();
            this.addFolderButton(vPath);
            this.addFileButtons(vPath);
            this.aPanel.revalidate();
            this.aPanel.repaint();
        }
        else{
            this.aEngine.setPencil(vPath);
        }
    }
    /**Ajoute au panel des boutons avec comme actionCommands les chemin vers les fichiers contenus dans le dossier en parametre*/ 
    private void addFileButtons(String pFolderPath)
    {
        String[] vFileNames = this.aFoldersMap.get(pFolderPath); 
        int vColumnNbr = 2;
        int vRowNbr = (int)Math.ceil(((double)vFileNames.length + 1) / vColumnNbr);
        int vButtonW =(int)this.aSize.getWidth()/ vColumnNbr;
        this.aPanel.setLayout( new  GridLayout(vRowNbr, vColumnNbr));
        for(final String vFileName : vFileNames){
            this.aPanel.add(this.getButton(pFolderPath + "/" + vFileName,pFolderPath + "/" + vFileName,vButtonW));}
    }
    /** Ajoute au panel un bouton avec comme actionCommand le chemin vers le dossier contenant le fichier en parametre*/ 
    private void addFolderButton(String pFilePath)
    {
        int vColumnNbr = 2; 
        int vButtonW =(int)this.aSize.getWidth()/ vColumnNbr;  
        String vFolderPath = this.getFolderPath(pFilePath);
        if (vFolderPath != null){
            this.aPanel.add(this.getButton(vFolderPath,"Tools/back.png",vButtonW));}
    }
    /****/ 
    private JButton getButton(final String pActionCommand, final String pImagePath, final int pButtonWidth)
    {
        JButton vButton = new JButton();
        if(this.getImage(pImagePath,pButtonWidth)!=null){
            vButton.setIcon(new ImageIcon(this.getImage(pImagePath,pButtonWidth)));}
        vButton.setActionCommand(pActionCommand);
        vButton.setContentAreaFilled(false);
        vButton.addActionListener(this);
        vButton.setFocusable(false);
        vButton.setBorder(null);
        return vButton;
    }
    /****/ 
    private Image getImage(String pPath, int pButtonWidth)
    {
        Image vImage = null;
        if (this.isImage(pPath))
            vImage = (new ImageIcon(pPath)).getImage();
        else
            vImage = this.addFolderImage(this.getFirstImage(pPath));
        if(vImage == null)
            return null;
        int vHeight = Math.min(vImage.getHeight(null) * pButtonWidth / vImage.getWidth(null),4*pButtonWidth);
        return vImage = vImage.getScaledInstance(pButtonWidth,vHeight,Image.SCALE_DEFAULT);
    }
    /****/ 
    private String getFolderPath(String pFilePath)
    {
        String vFolderPath = null;
        if (pFilePath.contains("/")) {
            vFolderPath = pFilePath.substring(0, pFilePath.lastIndexOf("/"));}
        return vFolderPath;
    }
    /**Return la premiere image trouvée dans le dossier*/ 
    private Image getFirstImage(String pFolderPath)
    {
        if (this.aFoldersMap.containsKey(pFolderPath)) 
        {
            String[] vFileNames = this.aFoldersMap.get(pFolderPath);
            for (final String vFileName : vFileNames) 
            {
                String vFilePath = pFolderPath + "/" + vFileName;
                if (this.isImage(vFilePath)){
                    return (new  ImageIcon(vFilePath)).getImage();}
                if (this.getFirstImage(vFilePath) != null){
                    return this.getFirstImage(vFilePath);}
            }
        }
        return null;
    }
    /****/ 
    private Image addFolderImage(final Image pImage)
    {
        if(pImage==null){
            return null;} 
            
        BufferedImage vBImage = new BufferedImage(pImage.getWidth(null),pImage.getHeight(null),BufferedImage.TYPE_INT_ARGB);
        Graphics g = vBImage.createGraphics();
        
        
        Image vFolderImage = (new ImageIcon("Tools/folder.png")).getImage();
        
         g.drawImage(pImage, 0, 0, null);
        g.drawImage(vFolderImage, 0, pImage.getHeight(null) - vBImage.getWidth(),vBImage.getWidth(),vBImage.getWidth(),null);
       
        return vBImage;
    }
    /****/ 
    private boolean isImage(final String pFilePath)
    {
        if (pFilePath.contains(".png") || pFilePath.contains(".jpg") || pFilePath.contains(".gif")){ 
            return true;}
        return false;
    }
}
