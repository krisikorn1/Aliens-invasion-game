import javax.swing.*;
import java.awt.*;

public class AlienInvasionHome extends JFrame {

    public AlienInvasionHome() {
        setTitle("Alien Invasion Home");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // สร้าง JPanel ที่กำหนดพื้นหลัง
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // โหลดและวาดรูป backgroundhome.jpg
                ImageIcon backgroundImage = loadImage("/Assets/backgroundhome.jpg", getWidth(), getHeight());
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        backgroundPanel.setLayout(null);  // ใช้ layout แบบ null สำหรับกำหนดตำแหน่งเอง
        setContentPane(backgroundPanel);  // ตั้งค่าพาเนลเป็นคอนเทนต์ของเฟรม

        // ปรับขนาดและแสดงรูป title.png
        ImageIcon titleIcon = loadImage("/Assets/title.png", 400, 100);
        if (titleIcon != null) {
            JLabel titleLabel = new JLabel(titleIcon);
            titleLabel.setBounds(200, 50, 400, 100);  // ปรับขนาดและตำแหน่ง
            backgroundPanel.add(titleLabel);
        }

// ปุ่ม Start Game เปลี่ยนเป็นรูป start.png และปรับขนาด
        ImageIcon startIcon = loadImage("/Assets/start.png", 200, 120);
        JButton startButton = new JButton(startIcon);
        startButton.setBounds(300, 120, 200, 50);  // ปรับขนาดและตำแหน่ง
        startButton.setContentAreaFilled(false);   // ปิดการแสดงพื้นหลังปุ่ม
        startButton.setBorderPainted(false);       // ปิดการแสดงขอบของปุ่ม
        startButton.setFocusPainted(false);        // ปิดการแสดงการโฟกัส
        backgroundPanel.add(startButton);

// ปุ่ม Character Selection เปลี่ยนเป็นรูป inventory.png และปรับขนาด
        ImageIcon characterIcon = loadImage("/Assets/inventory.png", 200, 80);
        JButton characterButton = new JButton(characterIcon);
        characterButton.setBounds(300, 180, 200, 80);  // ปรับขนาดและตำแหน่ง
        characterButton.setContentAreaFilled(false);   // ปิดการแสดงพื้นหลังปุ่ม
        characterButton.setBorderPainted(false);       // ปิดการแสดงขอบของปุ่ม
        characterButton.setFocusPainted(false);        // ปิดการแสดงการโฟกัส
        backgroundPanel.add(characterButton);

// ปุ่ม Shop เปลี่ยนเป็นรูป shophead.png และปรับขนาด
        ImageIcon shopIcon = loadImage("/Assets/shophead.png", 200, 90);
        JButton shopButton = new JButton(shopIcon);
        shopButton.setBounds(300, 210, 200, 150);  // ปรับขนาดและตำแหน่ง
        shopButton.setContentAreaFilled(false);   // ปิดการแสดงพื้นหลังปุ่ม
        shopButton.setBorderPainted(false);       // ปิดการแสดงขอบของปุ่ม
        shopButton.setFocusPainted(false);        // ปิดการแสดงการโฟกัส
        backgroundPanel.add(shopButton);


        startButton.addActionListener(e -> {
            dispose();
            JFrame gameFrame = new JFrame("Alien Invasion Game");
            gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            gameFrame.setSize(1024, 600);
            Game game = new Game(gameFrame);
            gameFrame.add(game);
            gameFrame.setVisible(true);
        });

        characterButton.addActionListener(e -> {
            dispose();
            JFrame characterFrame = new JFrame("Character Selection");
            characterFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            characterFrame.setSize(800, 600);
            CharacterSelection characterSelection = new CharacterSelection(characterFrame);
            characterFrame.add(characterSelection);
            characterFrame.setVisible(true);
        });

        shopButton.addActionListener(e -> {
            dispose();
            JFrame shopFrame = new JFrame("Shop");
            shopFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            shopFrame.setSize(800, 600);
            Shop shop = new Shop(shopFrame);
            shopFrame.add(shop);
            shopFrame.setVisible(true);
        });
    }

    // ฟังก์ชันช่วยในการโหลดภาพและปรับขนาด
    private ImageIcon loadImage(String path, int width, int height) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } else {
            System.out.println("Couldn't find file: " + path);
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AlienInvasionHome().setVisible(true));
    }
}
