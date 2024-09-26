import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Obstacle {
    private int x, y, width, height;
    private BufferedImage obstacleImage;

    // Constructor ที่รับตำแหน่ง x, y, ขนาด width, height และประเภทของอุปสรรค (type)
    public Obstacle(int x, int y, int width, int height, String type) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        // โหลดภาพอุปสรรคตามชนิด (type)
        try {
            obstacleImage = ImageIO.read(new File("src/Assets/" + type + ".png"));
        } catch (IOException e) {
            System.out.println("Error loading obstacle image for type: " + type);
        }
    }

    // เมธอด move รับพารามิเตอร์ speedMultiplier สำหรับปรับความเร็ว
    public void move(int speedMultiplier) {
        // ลดค่าความเร็วของอุปสรรคให้น้อยลง
        x -= 2 * speedMultiplier;  // ปรับค่าความเร็วเพื่อให้อุปสรรคเคลื่อนที่ช้าลง
    }

    // เมธอด paint ใช้วาดอุปสรรคลงบนหน้าจอ
    public void paint(Graphics g) {
        if (obstacleImage != null) {
            g.drawImage(obstacleImage, x, y, width, height, null);
        } else {
            g.setColor(Color.RED);
            g.fillRect(x, y, width, height);  // วาดอุปสรรคเป็นสี่เหลี่ยมสีแดงหากโหลดภาพไม่ได้
        }
    }

    // คืนค่า Rectangle ที่ครอบอุปสรรค เพื่อใช้ในการตรวจสอบการชน (collision)
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    // Getter สำหรับตำแหน่ง x ของอุปสรรค
    public int getX() {
        return x;
    }

    // Setter สำหรับตำแหน่ง x ของอุปสรรค
    public void setX(int x) {
        this.x = x;
    }
}
