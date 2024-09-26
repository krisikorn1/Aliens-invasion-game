import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.awt.event.*;

public class Game extends JPanel implements ActionListener {
    private Timer timer;
    private Player player;
    private ArrayList<Obstacle> obstacles;
    private ArrayList<Soldier> soldiers;
    private ArrayList<Coin> coins;
    private int score;
    private int hearts;
    private int distance;
    private int obstacleCooldown = 100;
    private int soldierCooldown = 150;
    private int speedMultiplier = 2;

    public static int totalCoins = 0;
    public static int highScore = 0;
    public static String selectedCharacter = "alien"; // ตัวละครเริ่มต้น
    private BufferedImage backgroundImage, heartImage, coinImage;
    private BufferedImage bulletImage, groundImage;

    private int coinCooldown = 200;
    private int groundX = 0; // ตำแหน่งแกน x ของพื้น

    public Game(JFrame frame) {
        setFocusable(true);
        setPreferredSize(new Dimension(1024, 600));
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                player.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    player.shoot(); // เรียกฟังก์ชันยิง
                }
            }

            public void keyReleased(KeyEvent e) {
                player.keyReleased(e);
            }
        });

        System.out.println("Selected Character in Game: " + selectedCharacter);
        player = new Player(selectedCharacter);
        obstacles = new ArrayList<>();
        soldiers = new ArrayList<>();
        coins = new ArrayList<>();
        score = 0;
        hearts = 3;
        distance = 0;

        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/Assets/background.jpg"));
            heartImage = ImageIO.read(getClass().getResourceAsStream("/Assets/heart.png"));
            coinImage = ImageIO.read(getClass().getResourceAsStream("/Assets/coin.png"));
            bulletImage = ImageIO.read(getClass().getResourceAsStream("/Assets/ammunition.png"));
            groundImage = ImageIO.read(getClass().getResourceAsStream("/Assets/rock.jpg")); // แก้ไขเป็น rock.jpg
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Error loading image files: " + e.getMessage());
            e.printStackTrace(); // เพิ่มเพื่อแสดงรายละเอียดข้อผิดพลาด
        }

        timer = new Timer(20, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        player.move(0);
        moveObstacles();
        moveSoldiers();
        moveCoins();
        moveBullets();
        checkCollision();
        spawnObstacles();
        spawnCoins();
        spawnSoldiers();
        distance += 1;
        adjustSpeed();

        // อัปเดตตำแหน่งของพื้น
        groundX -= speedMultiplier;
        if (groundImage != null && groundX <= -groundImage.getWidth()) {
            groundX = 0;
        }

        repaint();
    }

    private void adjustSpeed() {
        if (distance >= 3000) speedMultiplier = 6;
        else if (distance >= 2000) speedMultiplier = 5;
        else if (distance >= 1000) speedMultiplier = 4;
    }

    private void moveObstacles() {
        for (Iterator<Obstacle> itr = obstacles.iterator(); itr.hasNext();) {
            Obstacle obstacle = itr.next();
            obstacle.move(speedMultiplier);
            if (obstacle.getX() < 0) {
                itr.remove();
            }
        }
    }

    private void moveSoldiers() {
        for (Iterator<Soldier> itr = soldiers.iterator(); itr.hasNext();) {
            Soldier soldier = itr.next();
            soldier.move(bulletImage, speedMultiplier);

            // เคลื่อนที่กระสุนของทหาร
            ArrayList<Bullet> soldierBullets = soldier.getBullets();
            for (Iterator<Bullet> bulletItr = soldierBullets.iterator(); bulletItr.hasNext();) {
                Bullet bullet = bulletItr.next();
                bullet.move();
                if (bullet.isOffScreen()) {
                    bulletItr.remove();
                }
            }

            if (soldier.getX() < 0) {
                itr.remove();
            }
        }
    }

    private void moveCoins() {
        for (Iterator<Coin> itr = coins.iterator(); itr.hasNext();) {
            Coin coin = itr.next();
            coin.move(speedMultiplier);
            if (coin.getX() < 0) {
                itr.remove();
            }
        }
    }

    private void moveBullets() {
        ArrayList<Bullet> playerBullets = player.getBullets();
        for (Iterator<Bullet> itr = playerBullets.iterator(); itr.hasNext();) {
            Bullet bullet = itr.next();
            bullet.move();
            if (bullet.isOffScreen()) {
                itr.remove();
            }
        }
    }

    private void checkCollision() {
        // การชนระหว่างกระสุนของผู้เล่นและทหาร
        for (Iterator<Soldier> soldierItr = soldiers.iterator(); soldierItr.hasNext();) {
            Soldier soldier = soldierItr.next();
            ArrayList<Bullet> playerBullets = player.getBullets();
            for (Iterator<Bullet> bulletItr = playerBullets.iterator(); bulletItr.hasNext();) {
                Bullet bullet = bulletItr.next();
                if (bullet.getBounds().intersects(soldier.getBounds())) {
                    soldierItr.remove();
                    bulletItr.remove();
                    score += 50; // เพิ่มคะแนนเมื่อกำจัดทหาร
                    break;
                }
            }
        }

        // การชนระหว่างผู้เล่นและเหรียญ
        for (Iterator<Coin> coinItr = coins.iterator(); coinItr.hasNext();) {
            Coin coin = coinItr.next();
            if (player.getBounds().intersects(coin.getBounds())) {
                score += 10;
                totalCoins += 10;
                coinItr.remove();
            }
        }

        // การชนระหว่างผู้เล่นและอุปสรรค
        for (Iterator<Obstacle> obstacleItr = obstacles.iterator(); obstacleItr.hasNext();) {
            Obstacle obstacle = obstacleItr.next();
            if (player.getBounds().intersects(obstacle.getBounds())) {
                hearts--;
                obstacleItr.remove();
            }
        }

        // การชนระหว่างผู้เล่นและทหาร
        for (Iterator<Soldier> soldierItr = soldiers.iterator(); soldierItr.hasNext();) {
            Soldier soldier = soldierItr.next();
            if (player.getBounds().intersects(soldier.getBounds())) {
                hearts--;
                soldierItr.remove();
            }

            // การชนระหว่างผู้เล่นและกระสุนของทหาร
            for (Iterator<Bullet> bulletItr = soldier.getBullets().iterator(); bulletItr.hasNext();) {
                Bullet bullet = bulletItr.next();
                if (player.getBounds().intersects(bullet.getBounds())) {
                    hearts--;
                    bulletItr.remove();
                }
            }
        }

        if (hearts <= 0) gameOver();
    }

    private void spawnObstacles() {
        if (obstacleCooldown > 0) {
            obstacleCooldown--;
            return;
        }

        Random rand = new Random();
        if (rand.nextInt(100) < 5) {
            String[] obstacleTypes = { "ufo", "spike", "car", "thorn" };
            String randomType = obstacleTypes[rand.nextInt(obstacleTypes.length)];

            int yPos;
            int obstacleWidth;
            int obstacleHeight;

            if (randomType.equals("ufo")) {
                yPos = 370; obstacleWidth = 100; obstacleHeight = 50;
            } else if (randomType.equals("car")) {
                yPos = 430; obstacleWidth = 120; obstacleHeight = 100;
            } else {
                yPos = 450; obstacleWidth = 50; obstacleHeight = 50;
            }

            obstacles.add(new Obstacle(1024, yPos, obstacleWidth, obstacleHeight, randomType));
            obstacleCooldown = 100;
        }
    }

    private void spawnSoldiers() {
        if (soldierCooldown > 0) {
            soldierCooldown--;
            return;
        }

        Random rand = new Random();
        if (rand.nextInt(100) < 5) {
            int soldierWidth = 60;
            int soldierHeight = 100;
            int yPos = 500 - soldierHeight;
            soldiers.add(new Soldier(1024, soldierWidth, soldierHeight));
            soldierCooldown = 150;
        }
    }

    private void spawnCoins() {
        if (coinCooldown > 0) {
            coinCooldown--;
            return;
        }

        Random rand = new Random();
        if (rand.nextInt(100) < 3) {
            int xPos = 1024;
            int yPos = 460;
            coins.add(new Coin(xPos, yPos, 20, 20));
        }
    }

    private void gameOver() {
        if (score > highScore) highScore = score;

        int option = JOptionPane.showOptionDialog(
                this,
                "Game Over! Your score: " + score + "\nHigh Score: " + highScore + "\nTotal Coins: " + totalCoins + "\nWhat do you want to do?",
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{"Restart", "Back to Home"},
                "Restart"
        );

        if (option == JOptionPane.YES_OPTION) restartGame();
        else backToHome();
    }

    private void restartGame() {
        score = 0;
        hearts = 3;
        player = new Player(selectedCharacter); // ใช้ตัวละครที่เลือก
        obstacles.clear();
        soldiers.clear();
        coins.clear();
        distance = 0;
        groundX = 0; // รีเซ็ตตำแหน่งของพื้น
        timer.start();
    }

    private void backToHome() {
        timer.stop();
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        topFrame.dispose();

        SwingUtilities.invokeLater(() -> {
            JFrame homeFrame = new AlienInvasionHome();
            homeFrame.setVisible(true);
            homeFrame.setSize(800, 600);
            homeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        // วาดพื้น
        if (groundImage != null) {
            int groundY = 500; // ตำแหน่งแกน y ของพื้น
            int groundHeight = 100; // ความสูงของพื้น
            int groundWidth = groundImage.getWidth(); // ความกว้างของภาพพื้น

            // วาดภาพพื้นซ้ำ ๆ เพื่อให้ครอบคลุมหน้าจอ
            for (int i = 0; i <= getWidth() / groundWidth + 1; i++) {
                g.drawImage(groundImage, groundX + i * groundWidth, groundY, groundWidth, groundHeight, this);
            }
        } else {
            g.setColor(new Color(139, 69, 19));
            g.fillRect(0, 500, 1024, 100);
        }

        player.paint(g);

        for (Obstacle obstacle : obstacles) {
            obstacle.paint(g);
        }

        for (Soldier soldier : soldiers) {
            soldier.paint(g);

            // วาดกระสุนของทหาร
            for (Bullet bullet : soldier.getBullets()) {
                bullet.paint(g);
            }
        }

        for (Coin coin : coins) {
            coin.paint(g);
        }

        int rightEdge = getWidth();
        for (int i = 0; i < hearts; i++) {
            if (heartImage != null) {
                g.drawImage(heartImage, rightEdge - 100 + i * 30, 10, 25, 25, this);
            }
        }

        g.setColor(Color.RED);
        g.drawString("Coins: " + totalCoins, rightEdge - 150, 50);

        if (coinImage != null) {
            g.drawImage(coinImage, rightEdge - 50, 35, 25, 25, this);
        }

        g.drawString("Highscore: " + highScore, 10, 80);
        g.drawString("Distance (Km): " + distance / 100, 10, 100);
    }
}
