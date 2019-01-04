package GameBoy;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Screen extends JPanel {
    public void paint(Graphics g) {
        Image img = createImageWithText();
        g.drawImage(img, 20, 20, this);
    }

    private Image createImageWithText() {
        BufferedImage bufferedImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedImage.getGraphics();

        g.drawString("www.tutorialspoint.com", 20, 20);
        g.drawString("www.tutorialspoint.com", 20, 40);
        g.drawString("www.tutorialspoint.com", 20, 60);
        g.drawString("www.tutorialspoint.com", 20, 80);
        g.drawString("www.tutorialspoint.com", 20, 100);

//        bufferedImage.setRGB(20, 10, 0xFF0000); // Sets a pixel at location to red
        return bufferedImage;
    }
}