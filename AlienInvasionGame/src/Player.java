import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.Timer;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Player {
    private int groundLevel = 500;
    private int x = 100; // ตำแหน่งเริ่มต้นของผู้เล่น
    private int y;
    private int width, height;
    private BufferedImage characterImage;
    private BufferedImage loadingImage; // รูป loading.png
    private boolean jumping = false;
    private boolean crouching = false;
    private int jumpStrength = 15;
    private int maxJumps = 2;
    private int jumpCount = 0;
    private int gravity = 1;
    private int velocityY = 0;
    private ArrayList<Bullet> bullets;
    private int shotCount = 0; // จำนวนกระสุนที่ยิงไปแล้ว
    private boolean isLoading = false; // สถานะการแสดง loading
    private Timer cooldownTimer; // Timer สำหรับคูลดาวน์ 2 วินาที

    public Player(String character) {
        width = 50;
        height = 100;
        y = groundLevel - height;
        bullets = new ArrayList<>();
        System.out.println("Loading character: " + character);
        loadCharacterImage(character);
        loadLoadingImage(); // โหลดภาพ loading.png
    }

    public void loadCharacterImage(String character) {
        try {
            switch (character) {
                case "alien":
                    characterImage = ImageIO.read(new File("src/Assets/alien.png"));
                    break;
                case "alien_green":
                    characterImage = ImageIO.read(new File("src/Assets/aliengreen.png"));
                    break;
                case "alien_red":
                    characterImage = ImageIO.read(new File("src/Assets/alienred.png"));
                    break;
                default:
                    characterImage = ImageIO.read(new File("src/Assets/alien.png"));
                    break;
            }
        } catch (IOException e) {
            System.out.println("Error loading character image: " + e.getMessage());
        }
    }

    public void loadLoadingImage() {
        try {
            loadingImage = ImageIO.read(new File("src/Assets/loading.png"));
        } catch (IOException e) {
            System.out.println("Error loading loading image: " + e.getMessage());
        }
    }

    public void move(int speed) {
        x += speed;

        // จัดการการกระโดด
        if (jumping) {
            y += velocityY;
            velocityY += gravity;

            if (y >= groundLevel - height) {
                y = groundLevel - height;
                jumping = false;
                velocityY = 0;
                jumpCount = 0;
            }
        }

        // เคลื่อนที่กระสุน
        for (Iterator<Bullet> itr = bullets.iterator(); itr.hasNext();) {
            Bullet bullet = itr.next();
            bullet.move();
            if (bullet.isOffScreen()) {
                itr.remove();
            }
        }
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_SPACE) {
            if (jumpCount < maxJumps) {
                jumping = true;
                velocityY = -jumpStrength;
                jumpCount++;
            }
        } else if (key == KeyEvent.VK_ENTER && !isLoading) {
            shoot(); // เรียกฟังก์ชันยิง
        } else if (key == KeyEvent.VK_DOWN) {
            crouching = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_DOWN) {
            crouching = false;
        }
    }

    public void paint(Graphics g) {
        if (crouching) {
            g.drawImage(characterImage, x, y + height / 2, width, height / 2, null);
        } else {
            g.drawImage(characterImage, x, y, width, height, null);
        }

        // วาดกระสุนของผู้เล่น
        for (Bullet bullet : bullets) {
            bullet.paint(g);
        }

        // แสดง loading.png ถ้าอยู่ในสถานะกำลังโหลด
        if (isLoading && loadingImage != null) {
            g.drawImage(loadingImage, x, y - 50, 50, 50, null);
        }
    }

    public Rectangle getBounds() {
        if (crouching) {
            return new Rectangle(x, y + height / 2, width, height / 2);
        } else {
            return new Rectangle(x, y, width, height);
        }
    }

    public void shoot() {
        if (shotCount < 4) {
            BufferedImage bulletImage = null;
            try {
                bulletImage = ImageIO.read(new File("src/Assets/ammunition.png"));
            } catch (IOException e) {
                System.out.println("Error loading bullet image: " + e.getMessage());
            }

            int bulletWidth = 20;
            int bulletHeight = 15;
            bullets.add(new Bullet(x + width, y + height / 2, 10, bulletWidth, bulletHeight, bulletImage));
            shotCount++;

            if (shotCount == 4) {
                startCooldown(); // เริ่มการคูลดาวน์เมื่อยิงครบ 2 นัด
            }
        }
    }

    private void startCooldown() {
        isLoading = true; // เปิดสถานะการโหลด
        cooldownTimer = new Timer(2000, new ActionListener() { // ตั้งเวลา 2 วินาที
            @Override
            public void actionPerformed(ActionEvent e) {
                isLoading = false; // ปิดสถานะการโหลด
                shotCount = 0; // รีเซ็ตจำนวนการยิง
                cooldownTimer.stop(); // หยุดตัวจับเวลา
            }
        });
        cooldownTimer.start();
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }
}
