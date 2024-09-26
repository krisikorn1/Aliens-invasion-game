import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Shop extends JPanel {
    private JLabel coinLabel;
    private JButton buyButtonGreen, buyButtonRed, backButton;
    public static boolean isAlienGreenBought = false;
    public static boolean isAlienRedBought = false;
    private BufferedImage backgroundImage; // ภาพพื้นหลังหลัก (shophead.jpg)

    public Shop(JFrame frame) {
        // โหลดภาพพื้นหลังหลัก (shophead.jpg)
        try {
            backgroundImage = ImageIO.read(new File("src/Assets/shophourse.jpg")); // ใช้ภาพพื้นหลัง shophead.jpg
        } catch (IOException e) {
            System.out.println("Error loading background image: " + e.getMessage());
        }

        setLayout(new BorderLayout());

        // เพิ่มภาพ shop.png ด้านบนเป็นหัวข้อ
        ImageIcon shopTitleIcon = loadIcon("src/Assets/shop.png", 400, 150); // ปรับขนาดภาพ shop.png ตามที่ต้องการ
        if (shopTitleIcon != null) {
            JLabel titleLabel = new JLabel(shopTitleIcon, JLabel.CENTER);
            add(titleLabel, BorderLayout.NORTH); // วางภาพหัวข้อด้านบนสุด
        }

        // สร้าง panel สำหรับแสดงเหรียญและ coin.png
        JPanel coinPanel = new JPanel();
        coinPanel.setOpaque(false); // ทำให้ panel โปร่งใสเพื่อแสดงพื้นหลังหลัก
        coinPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // โหลดภาพ coin.png
        ImageIcon coinIcon = loadIcon("src/Assets/coin.png", 30, 30); // ขนาด 30x30
        if (coinIcon != null) {
            JLabel coinImageLabel = new JLabel(coinIcon);
            coinPanel.add(coinImageLabel);
        }

        // แสดงจำนวนเหรียญ
        coinLabel = new JLabel("Coins: " + Game.totalCoins);
        coinLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        coinPanel.add(coinLabel);

        add(coinPanel, BorderLayout.SOUTH);

        // สร้าง panel สำหรับแสดงตัวละคร
        JPanel charactersPanel = new JPanel();
        charactersPanel.setOpaque(false); // ทำให้ panel โปร่งใส
        charactersPanel.setLayout(new GridLayout(1, 2, 10, 10)); // ลดขนาดช่องว่างระหว่างกรอบ

        try {
            BufferedImage alienGreenImage = loadImage("src/Assets/aliengreen.png");
            BufferedImage alienRedImage = loadImage("src/Assets/alienred.png");

            // Alien Green Panel
            JPanel alienGreenPanel = new JPanel();
            alienGreenPanel.setLayout(new BorderLayout());
            alienGreenPanel.setBackground(Color.WHITE); // ตั้งพื้นหลังให้เป็นสีขาว
            ImageIcon alienGreenIcon = new ImageIcon(alienGreenImage.getScaledInstance(80, 100, Image.SCALE_SMOOTH)); // ขนาดตัวละครยังคงเดิม
            JLabel alienGreenLabel = new JLabel(alienGreenIcon);
            alienGreenLabel.setHorizontalAlignment(JLabel.CENTER);
            alienGreenPanel.add(alienGreenLabel, BorderLayout.CENTER);
            buyButtonGreen = new JButton("<html><center>Buy Alien Green<br>(500 coins)</center></html>");
            buyButtonGreen.setFont(new Font("Arial", Font.PLAIN, 10)); // ลดขนาดตัวหนังสือของปุ่ม
            buyButtonGreen.addActionListener(e -> buyAlienGreen());
            alienGreenPanel.add(buyButtonGreen, BorderLayout.SOUTH);
            charactersPanel.add(alienGreenPanel);

            // Alien Red Panel
            JPanel alienRedPanel = new JPanel();
            alienRedPanel.setLayout(new BorderLayout());
            alienRedPanel.setBackground(Color.WHITE); // ตั้งพื้นหลังให้เป็นสีขาว
            ImageIcon alienRedIcon = new ImageIcon(alienRedImage.getScaledInstance(80, 100, Image.SCALE_SMOOTH)); // ขนาดตัวละครยังคงเดิม
            JLabel alienRedLabel = new JLabel(alienRedIcon);
            alienRedLabel.setHorizontalAlignment(JLabel.CENTER);
            alienRedPanel.add(alienRedLabel, BorderLayout.CENTER);
            buyButtonRed = new JButton("<html><center>Buy Alien Red<br>(700 coins)</center></html>");
            buyButtonRed.setFont(new Font("Arial", Font.PLAIN, 10)); // ลดขนาดตัวหนังสือของปุ่ม
            buyButtonRed.addActionListener(e -> buyAlienRed());
            alienRedPanel.add(buyButtonRed, BorderLayout.SOUTH);
            charactersPanel.add(alienRedPanel);

        } catch (IOException e) {
            System.out.println("Error loading character images: " + e.getMessage());
        }

        add(charactersPanel, BorderLayout.CENTER);

        // ปุ่มกลับไปหน้า Home
        backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            frame.dispose();
            JFrame homeFrame = new AlienInvasionHome();
            homeFrame.setVisible(true);
        });
        add(backButton, BorderLayout.NORTH);
    }

    // ฟังก์ชันสำหรับวาดพื้นหลังหลัก (shophead.jpg)
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); // วาดพื้นหลังหลัก
        }
    }

    // ฟังก์ชันสำหรับโหลดไอคอนและปรับขนาด
    private ImageIcon loadIcon(String path, int width, int height) {
        try {
            BufferedImage image = loadImage(path);
            if (image != null) {
                Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(scaledImage);
            }
        } catch (IOException e) {
            System.out.println("Error loading icon: " + e.getMessage());
        }
        return null;
    }

    // ฟังก์ชันช่วยในการโหลดรูปภาพ
    private BufferedImage loadImage(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            System.out.println("File not found: " + path);
            return null;
        }
        return ImageIO.read(file);
    }

    private void buyAlienGreen() {
        if (isAlienGreenBought) {
            JOptionPane.showMessageDialog(null, "You already own Alien Green!");
            return;
        }
        if (Game.totalCoins >= 500) {
            Game.totalCoins -= 500;
            coinLabel.setText("Coins: " + Game.totalCoins);
            isAlienGreenBought = true;
            JOptionPane.showMessageDialog(null, "Alien Green purchased!");
        } else {
            JOptionPane.showMessageDialog(null, "Not enough coins!");
        }
    }

    private void buyAlienRed() {
        if (isAlienRedBought) {
            JOptionPane.showMessageDialog(null, "You already own Alien Red!");
            return;
        }
        if (Game.totalCoins >= 700) {
            Game.totalCoins -= 700;
            coinLabel.setText("Coins: " + Game.totalCoins);
            isAlienRedBought = true;
            JOptionPane.showMessageDialog(null, "Alien Red purchased!");
        } else {
            JOptionPane.showMessageDialog(null, "Not enough coins!");
        }
    }
}
