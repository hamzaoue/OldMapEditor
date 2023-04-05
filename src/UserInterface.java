import javax.swing.JComponent;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Point;
/****/
public class UserInterface 
{
    private JFrame aFrame;
    /****/ 
    public UserInterface(){this.setFrame();}
    /****/ 
    public JFrame getFrame(){return this.aFrame;} 
    /****/ 
    private void setFrame() 
    {
        this.aFrame=new JFrame();
        this.aFrame.setUndecorated(true);
        this.aFrame.getContentPane().setLayout(null);
        this.aFrame.getContentPane().setBackground(Color.black);   
        this.aFrame.setDefaultCloseOperation(this.aFrame.EXIT_ON_CLOSE);
        this.aFrame.setSize((Toolkit.getDefaultToolkit().getScreenSize()));
    }
    /****/ 
    public void removeComponent(final JComponent pComponent)
    {
        this.aFrame.remove(pComponent);
        this.aFrame.revalidate();
    }
    /****/ 
    public void addComponent(final JComponent pComponent)
    {
        this.aFrame.getContentPane().add(pComponent);
        this.aFrame.revalidate();
        this.aFrame.repaint();
        this.aFrame.setVisible(true);
    }
}