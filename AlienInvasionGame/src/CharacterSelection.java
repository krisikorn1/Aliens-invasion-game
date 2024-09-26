import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CharacterSelection extends JPanel {
    private JLabel titleLabel, instructionLabel;
    private JButton startButton, backButton;
    private ArrayList<String> unlockedCharacters;  // ตัวละครที่ซื้อแล้ว
    private String selectedCharacter;  // ตัวละครที่เลือก
    private JLabel selectedCharacterLabel;
    private JLabel characterImageLabel; // เพิ่มสำหรับแสดงรูปตัวละคร
    private BufferedImage backgroundImage; // เพิ่มสำหรับพื้นหลัง

    public CharacterSelection(JFrame frame) {
        setLayout(null);

        // โหลดภาพพื้นหลัง backgroundinventory.jpg
        try {
            backgroundImage = ImageIO.read(new File("src/Assets/backgrounginventoy.jpg"));
        } catch (IOException e) {
            System.out.println("Error loading background image: " + e.getMessage());
        }

        titleLabel = new JLabel("Character Selection", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);  // เปลี่ยนสีข้อความเป็นสีขาว
        titleLabel.setBounds(250, 50, 300, 50);

        add(titleLabel);

        instructionLabel = new JLabel("Select a character to start the game", JLabel.CENTER);
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        titleLabel.setForeground(Color.WHITE);
        instructionLabel.setBounds(250, 100, 300, 50);
        add(instructionLabel);

        // ตัวละครที่ปลดล็อคแล้ว
        unlockedCharacters = new ArrayList<>();
        unlockedCharacters.add("alien");  // ตัวละครพื้นฐาน
        if (Shop.isAlienGreenBought) unlockedCharacters.add("alien_green");
        if (Shop.isAlienRedBought) unlockedCharacters.add("alien_red");

        // ตั้งค่าตัวละครเริ่มต้น
        selectedCharacter = "alien";

        int yPosition = 180;
        for (String character : unlockedCharacters) {
            String characterName = character.replace("_", " ").toUpperCase();
            JButton characterButton = new JButton("Select " + characterName);
            characterButton.setBounds(250, yPosition, 300, 50);
            characterButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedCharacter = character;
                    selectedCharacterLabel.setText("Selected Character: " + characterName);
                    // อัปเดตรูปตัวละครที่เลือก
                    updateCharacterImage(character);
                }
            });
            add(characterButton);
            yPosition += 60;
        }

        // แสดงตัวละครที่เลือก
        String initialCharacterName = selectedCharacter.replace("_", " ").toUpperCase();
        selectedCharacterLabel = new JLabel("Selected Character: " + initialCharacterName, JLabel.CENTER);
        selectedCharacterLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        selectedCharacterLabel.setBounds(250, yPosition, 300, 50);
        add(selectedCharacterLabel);

        // เพิ่ม JLabel สำหรับแสดงรูปตัวละคร
        characterImageLabel = new JLabel();
        characterImageLabel.setBounds(600, 200, 150, 150); // กำหนดขนาดและตำแหน่งของรูป
        add(characterImageLabel);

        // แสดงรูปของตัวละครเริ่มต้น
        updateCharacterImage("alien");

        // ปุ่มเริ่มเกม
        startButton = new JButton("Start");
        startButton.setBounds(250, yPosition + 60, 150, 50);
        add(startButton);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Game.selectedCharacter = selectedCharacter;  // ตั้งค่าตัวละครที่เลือก
                System.out.println("Selected Character in CharacterSelection: " + selectedCharacter);
                frame.dispose();  // ปิดหน้าจอนี้
                JFrame gameFrame = new JFrame("Alien Invasion Game");
                gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                gameFrame.setSize(1024, 600);
                Game game = new Game(gameFrame);
                gameFrame.add(game);
                gameFrame.setVisible(true);
            }
        });

        // ปุ่มกลับไปหน้า Home
        backButton = new JButton("Back");
        backButton.setBounds(410, yPosition + 60, 150, 50);
        add(backButton);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();  // ปิดหน้าจอนี้
                JFrame homeFrame = new AlienInvasionHome();  // กลับไปหน้า Home
                homeFrame.setVisible(true);
            }
        });
    }

    // ฟังก์ชันสำหรับแสดงรูปตัวละคร
    private void updateCharacterImage(String character) {
        try {
            BufferedImage characterImage = ImageIO.read(new File("src/Assets/" + character + ".png"));
            Image scaledImage = characterImage.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            characterImageLabel.setIcon(new ImageIcon(scaledImage));
        } catch (IOException e) {
            System.out.println("Error loading character image: " + e.getMessage());
        }
    }

    // Override ฟังก์ชัน paintComponent เพื่อวาดพื้นหลัง
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);  // วาดรูปพื้นหลังเต็มหน้าจอ
        }
    }
}
