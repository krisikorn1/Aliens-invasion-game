import java.awt.*;
import java.awt.image.BufferedImage;

public class Bullet {
    private int x, y, width, height;
    private int velocityX;
    private BufferedImage bulletImage;

    public Bullet(int x, int y, int velocityX, int width, int height, BufferedImage image) {
        this.x = x;
        this.y = y;
        this.velocityX = velocityX;
        this.width = width;
        this.height = height;
        this.bulletImage = image;
    }

    public void move() {
        x += velocityX;
    }

    public void paint(Graphics g) {
        if (bulletImage != null) {
            g.drawImage(bulletImage, x, y, width, height, null);
        } else {
            g.setColor(Color.RED);
            g.fillRect(x, y, width, height);
        }
    }

    public boolean isOffScreen() {
        return x < 0 || x > 1024;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
