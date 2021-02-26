import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.io.*;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

public class ImageLoad 
{
    JFrame frame;

    public ImageLoad(String path, String image) throws IOException
    {
        String home = System.getProperty("user.dir");
        String fs = File.separator;
        home = home.substring(0, home.length()-5);

        BufferedImage img = ImageIO.read(new File(home + fs + "img" + fs + path + fs + image + ".png"));

        ImageIcon icon = new ImageIcon(img);

        frame = new JFrame(image);
        frame.setLayout(new FlowLayout());
        frame.setSize(500,500);

        JLabel lbl = new JLabel();
        lbl.setIcon(icon);

        frame.add(lbl);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void close()
    {
        frame.setVisible(false); //you can't see me!
        frame.dispose(); //Destroy the JFrame object
    }
}