import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.FlowLayout;
import javax.swing.ImageIcon;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import java.util.HashMap;
import javax.swing.JDialog;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
/****/
public class SaveButtons implements ActionListener
{
    private EditorEngine aEngine;
    private JPanel aPanel;
    private Dimension aSize;
    private HashMap <JButton,JButton> aCloseMap;
    private JButton aCurrentButton;
    /****/ 
    public SaveButtons(final EditorEngine pEngine, final Dimension pSize)
    {
        this.aEngine=pEngine;
        this.aSize=pSize;
        this.aCloseMap=new HashMap<JButton,JButton>();
        this.aCurrentButton=null;
        
        this.setPanel();
        this.setButtons();
    }
    /****/ 
    public JPanel getPanel(){return this.aPanel;}
    /****/ 
    public void actionPerformed(ActionEvent pEvent)
    {
        JButton vButton=(JButton)pEvent.getSource();
        switch(vButton.getActionCommand())
        {
            case "x |":this.closeButton(vButton);break;
            case "New":new NewFrame(this);break;
            case "Save":if(this.aCurrentButton!=null){
                            new SaveFrame(this, this.aCurrentButton.getText());}break;
            case "Open":new OpenFrame(this);break;
            default:this.requestFocus(vButton);break;
        }
        this.aPanel.repaint();
        this.aPanel.revalidate();
    }
    /****/ 
    public void requestFocus(final JButton pButton)
    {
        if(this.aCurrentButton!=null)
        {
            this.aCurrentButton.setForeground(new Color(130,130,170,240));
        }            
        this.aCurrentButton=pButton;
        pButton.setForeground(Color.white);
        this.aEngine.setCurrentDrawArea(pButton);
    }
    /****/ 
    public void addButton(final String pText)
    {
        JButton vButton=this.getButton(this.removeIllegalsChar(pText),"Poor Richard");
        vButton.setContentAreaFilled(false);
        vButton.setForeground(Color.white);
        
        this.aPanel.add(vButton);
        
        this.addCloseButton(vButton);
        
        this.aEngine.addDrawArea(vButton);
        
        this.requestFocus(vButton);
        
        this.aPanel.revalidate();
    }
    /****/ 
    private void addCloseButton(final JButton pButton)
    {
        JButton vCloseButton=this.getButton("x |","Comic Sans MS Gras");
        vCloseButton.setContentAreaFilled(false);
        vCloseButton.setForeground(Color.white);
        this.aCloseMap.put(vCloseButton,pButton);
        this.aPanel.add(vCloseButton);
    }
    /****/ 
    private String removeIllegalsChar(final String pText)
    {
        String vText=pText;   
        String[] vIllegalsChar={";","#","\\","'","\"","`","?","!","|",",",":"," ","@","£","µ","%","/","*","+","=","<",">","{","}","[","]","(",")"};
        for(String vChar:vIllegalsChar)
            vText=vText.replace(vChar,"");
        return vText;
    }
    /****/ 
    private void closeButton(final JButton pButton)
    {
        this.aEngine.removeDrawArea(this.aCloseMap.get(pButton));
        if(this.aCurrentButton==this.aCloseMap.get(pButton)){
            this.aCurrentButton=null;}
        this.aPanel.remove(this.aCloseMap.get(pButton));
        this.aPanel.remove(pButton);
    }
    /****/ 
    private void setPanel()
    {
        this.aPanel=new JPanel();
        this.aPanel.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
        this.aPanel.setBackground(new Color(20,20,30));
        this.aPanel.setSize(this.aSize);
    }
    /****/ 
    private void setButtons()
    {
        JButton vOpenButton = this.getButton("Open","Poor Richard");
        JButton vSaveButton=this.getButton("Save","Poor Richard");
        JButton vNewButton=this.getButton("New","Poor Richard");
        
        vOpenButton.setBackground(new Color( 255,178,45)); 
        vSaveButton.setBackground(new Color(255,151,87));
        vNewButton.setBackground(new Color(255,124,129));
        
        this.aPanel.add(vSaveButton);
        this.aPanel.add(vOpenButton);
        this.aPanel.add(vNewButton);
    }
    /****/ 
    public JButton getButton(final String pActionCommand, final String pFont)
    {
        Border vBorder = BorderFactory.createEmptyBorder(this.aSize.height/10,this.aSize.height/5,this.aSize.height/10,this.aSize.height/5);
        Font vFont=this.getFontFromHeight(pActionCommand,(int)(0.9*this.aSize.height),pFont);
        JButton vButton = new JButton(pActionCommand);
        vButton.setActionCommand(pActionCommand);
        vButton.addActionListener(this);
        vButton.setFocusable(false);
        vButton.setBorder(vBorder);
        vButton.setFont(vFont);
        return vButton;
    }
     /****/ 
    public void saveAs(final String pName)
    {
        this.aEngine.saveDrawArea(this.aCurrentButton,pName);
    }
    /****/ 
    public void open(final String pName)
    {
        JButton vButton=this.getButton(pName,"Poor Richard");
        vButton.setContentAreaFilled(false);
        vButton.setForeground(Color.white);
        
        this.aPanel.add(vButton);
        
        this.addCloseButton(vButton);
        
        this.aEngine.addDrawArea(vButton);
        
        this.aPanel.revalidate();
        this.aEngine.openDrawArea(vButton);
        
        this.requestFocus(vButton);
    }
    /****/ 
    private Font getFontFromHeight(final String pText, int pButtonHeight, final String pFont)
    {
        if(pText == null){
            return null;}
        FontRenderContext vFRC = new FontRenderContext(new  AffineTransform(), true, true);
        Font vFont = new Font(pFont, Font.BOLD, 0);
        int vCharSize = 0;
        while(vFont.getStringBounds(pText, vFRC).getHeight() < (pButtonHeight - 0.2*pButtonHeight)) 
        {
            vFont = new Font(pFont, Font.BOLD, vCharSize);
            vCharSize = vCharSize + 1;
        }
        return vFont;
    }
}
/****/ 
    class NewFrame implements ActionListener 
{
    private SaveButtons aSaveButtons;
    private JDialog aFrame;
    private JTextField aTextInput;
    public NewFrame(final SaveButtons pSaveButtons)
    {
        this.aSaveButtons=pSaveButtons;
        this.setDialog();
    }
    /****/ 
    public void actionPerformed(ActionEvent pEvent)
    {
        this.aSaveButtons.addButton(this.aTextInput.getText());
        this.aFrame.setVisible(false);
        this.aFrame.dispose();
    }
    /****/ 
    public void setDialog()
    {
        this.aFrame=new JDialog();
        this.aFrame.setSize(100,70);
        this.aFrame.setBackground(new Color(20,20,30));
        this.aFrame.setAlwaysOnTop(true);
        this.aFrame.setModal(true);
        
        this.aTextInput=new JTextField(8);
        this.aTextInput.setFont(new Font("Arial", Font.BOLD,13));
        this.aTextInput.setBorder(BorderFactory.createEmptyBorder(3,5,3,0));
        this.aTextInput.setBackground(new Color(20,20,30));
        this.aTextInput.setForeground(Color.white);;
        this.aTextInput.addActionListener(this);
        
        this.aFrame.add(this.aTextInput);
        this.aFrame.setVisible(true);
    }
}
/****/ 
    class SaveFrame implements ActionListener 
{
    private SaveButtons aSaveButtons;
    private JDialog aFrame;
    private JTextField aTextInput;
    public SaveFrame(final SaveButtons pSaveButtons, final String pName)
    {
        this.aSaveButtons=pSaveButtons;
        this.setDialog(pName);
    }
    /****/ 
    public void actionPerformed(ActionEvent pEvent)
    {
        this.aSaveButtons.saveAs(this.aTextInput.getText());
        this.aFrame.setVisible(false);
        this.aFrame.dispose();
    }
    /****/ 
    public void setDialog(final String pName)
    {
        this.aFrame=new JDialog();
        this.aFrame.setSize(100,70);
        this.aFrame.setBackground(new Color(20,20,30));
        this.aFrame.setAlwaysOnTop(true);
        this.aFrame.setModal(true);
        
        this.aTextInput=new JTextField(8);
        this.aTextInput.setText(pName);
        this.aTextInput.setFont(new Font("Arial", Font.BOLD,13));
        this.aTextInput.setBorder(BorderFactory.createEmptyBorder(3,5,3,0));
        this.aTextInput.setBackground(new Color(20,20,30));
        this.aTextInput.setForeground(Color.white);;
        this.aTextInput.addActionListener(this);
        
        this.aFrame.add(this.aTextInput);
        this.aFrame.setVisible(true);
    }
}
/****/ 
    class OpenFrame implements ActionListener 
{
    private SaveButtons aSaveButtons;
    private JDialog aFrame;
    private JTextField aTextInput;
    public OpenFrame(final SaveButtons pSaveButtons)
    {
        this.aSaveButtons=pSaveButtons;
        this.setDialog();
    }
    /****/ 
    public void actionPerformed(ActionEvent pEvent)
    {
        this.aSaveButtons.open(this.aTextInput.getText());
        this.aFrame.setVisible(false);
        this.aFrame.dispose();
    }
    /****/ 
    public void setDialog()
    {
        this.aFrame=new JDialog();
        this.aFrame.setSize(100,70);
        this.aFrame.setBackground(new Color(20,20,30));
        this.aFrame.setAlwaysOnTop(true);
        this.aFrame.setModal(true);
        
        this.aTextInput=new JTextField(8);
        this.aTextInput.setFont(new Font("Arial", Font.BOLD,13));
        this.aTextInput.setBorder(BorderFactory.createEmptyBorder(3,5,3,0));
        this.aTextInput.setBackground(new Color(20,20,30));
        this.aTextInput.setForeground(Color.white);;
        this.aTextInput.addActionListener(this);
        
        this.aFrame.add(this.aTextInput);
        this.aFrame.setVisible(true);
    }
}

