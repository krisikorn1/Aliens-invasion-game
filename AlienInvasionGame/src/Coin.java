import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Coin {
    private int x, y, width, height;
    private BufferedImage coinImage;

    public Coin(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        // โหลดภาพเหรียญ
        try {
            coinImage = ImageIO.read(new File("src/Assets/coin.png"));
        } catch (IOException e) {
            System.out.println("Error loading coin image.");
        }
    }

    // เมธอด move รับพารามิเตอร์ speedMultiplier สำหรับปรับความเร็ว
    public void move(int speedMultiplier) {
        // ลดค่าความเร็วของเหรียญให้น้อยลง
        x -= 2 * speedMultiplier;  // ปรับค่าความเร็วเพื่อให้เหรียญเคลื่อนที่ช้าลง
    }

    public void paint(Graphics g) {
        if (coinImage != null) {
            g.drawImage(coinImage, x, y, width, height, null);
        } else {
            g.setColor(Color.YELLOW);
            g.fillOval(x, y, width, height);  // วาดเหรียญเป็นวงกลมสีเหลืองถ้าโหลดภาพไม่สำเร็จ
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }
}
