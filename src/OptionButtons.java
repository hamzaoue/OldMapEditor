import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import java.awt.GridLayout;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.util.HashMap;
import java.awt.Color;
import java.awt.Point;
import java.awt.Font;
/****/
public class OptionButtons implements ActionListener
{
    private HashMap<String,JButton> aButtonMap;
    private EditorEngine aEngine;
    private String[] aOptions;
    private JPanel aPanel;
    /****/ 
    public OptionButtons(final EditorEngine pEngine, final Point pLocation, final Dimension pSize)
    {
        this.setPanel(pLocation, pSize);
        this.aEngine = pEngine;
        this.aButtonMap =  new HashMap < String, JButton > ();
        this.aOptions =  new String[]{"Gum"    ,"Cut"   ,"(n.i)Music"   ,"Exit"   ,
                                      "Select" ,"Copy"  ,"(n.i)Door"    ,"(n.i)Isomtrc",
                                      "Undo"   ,"Clear" ,"Grid"    ,"Mirror" , 
                                      "Clean"  ,"Redo"  ,"Hitbox"  ,"Rotate" , };
        this.addButtons();
        this.setToolTips();
    }
    /****/ 
    public JPanel getPanel(){return this.aPanel;}
    /****/ 
    private void setPanel(final Point pLocation, final Dimension pSize)
    {
        this.aPanel =  new JPanel( new GridLayout(4, 4));
        this.aPanel.setBackground(new Color(20,20,30));
        this.aPanel.setLocation(pLocation);
        this.aPanel.setSize(pSize);
    }
    /****/ 
    public void actionPerformed(final ActionEvent pEvent)
    {
        String vOption = ((JButton)pEvent.getSource()).getActionCommand();
        switch(vOption)
        {
            case "Exit"  :System.exit(0);break;
            case "Cut"   :this.aEngine.copySelection(true);break;
            case "Copy"  :this.aEngine.copySelection(false);break;
            case "Undo"  :this.aEngine.undo();break;
            case "Redo"  :this.aEngine.redo();break;
            case "Clear" :this.aEngine.clearMap();break;
            case "Grid"  :this.aEngine.reverseGrid();break;
            case "Hitbox":this.aEngine.reverseHitbox();break;
            case "Rotate":this.aEngine.rotatePencil(); break;
            case "Mirror":this.aEngine.hoFlipPencil(); break;
            case "Gum":this.aEngine.setCurrentTool("Gum");break;
            case "Clean":this.aEngine.cleanMap();break;
            case "Select":this.aEngine.setCurrentTool("Select");break;
        }
    }
    /****/ 
    private void addButtons()
    {
        int vButtonWidth = this.aPanel.getWidth() / 4;
        for (int vRow = 0;vRow <= 3;vRow++) {
            for (int vColumn = 0;vColumn <= 3;vColumn++){
                String vOption = this.aOptions[vColumn + (4 * vRow)];
                JButton vButton = this.getButton(vOption);
                vButton.setText(this.getResizedText(vOption,8));
                vButton.setFont(this.getFontFromWidth(vButton.getText(),vButtonWidth,"Poor Richard"));
                vButton.setBackground( new Color(255, 43 + 27 * ((3 - vColumn) + vRow), 255 - 42 * ((3 - vColumn) + vRow)));
                this.aButtonMap.put(vOption, vButton);
                this.aPanel.add(vButton);
            }
        }
    }
    /****/ 
    private JButton getButton(final String pActionCommand)
    {
        JButton vButton = new JButton();
        vButton.setActionCommand(pActionCommand);
        vButton.addActionListener(this);
        vButton.setFocusable(false);
        vButton.setBorder(null);
        return vButton;
    }
    /****/ 
    private void setToolTips()
    {
        this.aButtonMap.get("Exit").setToolTipText("Echap");
        this.aButtonMap.get("Cut").setToolTipText("Ctrl+X");
        this.aButtonMap.get("Copy").setToolTipText("Ctrl+C");
        this.aButtonMap.get("Undo").setToolTipText("Ctrl+Z");
        this.aButtonMap.get("Redo").setToolTipText("Ctrl+Y");
        this.aButtonMap.get("Rotate").setToolTipText("Right Click");
    }
    /****/ 
    private String getResizedText(final String pText,final  int pLength)
    {
        if (pText == null) {
            return null;}
        String vText = pText;
        while (vText.length() < pLength) {
            vText = " " + vText + " ";}
        return vText;
    }
    /****/ 
    private Font getFontFromWidth(final String pText,final  int pButtonWidth, final String pFont)
    {
        if (pText == null) {
            return null;}
        Font vFont =  new  Font(pFont, Font.BOLD, 0);
        FontRenderContext vFontRenderContext =  new  FontRenderContext( new  AffineTransform(), true, true);
        int vCharSize = 0;
        while (vFont.getStringBounds(pText, vFontRenderContext).getWidth() < (pButtonWidth - 0.15*pButtonWidth)) {
            vFont =  new  Font(pFont, Font.BOLD, vCharSize);
            vCharSize = vCharSize + 1;
        }
        return vFont;
    }
}
