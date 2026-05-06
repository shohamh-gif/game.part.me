package org.example;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.util.Random;

public class MainScenePanel extends JPanel {
    private Player player;
    private int score;
    private Prize[] prizes;
    // מערך המחרוזות של התמונות נשאר בדיוק אותו דבר
    private String[] candyImages = {
            "/Blue_candy.png",
            "/Orange_candy.png",
            "/Pink_candy.png",
            "/Purple_candy.png",
            "/Yellow_candy.png"
    };

    public MainScenePanel(int x, int y, int width, int height) {
        this.setBounds(x, y, width, height);
        this.setLayout(null);
        this.player = new Player(100, 100, 60, 60);
        this.setFocusable(true);//רשאי לקבל פוקוס מהמקלדת
        this.requestFocus();//מקבל פוקוס מהמקלדת- השחקן יכול להגיב ללחיצות
        this.setDoubleBuffered(true);// הפתרון לריצוד הגיף - כדי שלא נראה את המחיקה והציור בזמן אמת רק שהציור מוכן

        MovementListener movementListener = new MovementListener(player);
        this.addKeyListener(movementListener);
        JButton soundButton = Utils.createSoundButton();
        this.add(soundButton);
        spawnPrizes(10); // המספר קובע כמה סוכריות ייווצרו

        this.gameLoop();
    }

    public void gameLoop() {
        new Thread(() -> {
            while (true) {
                checkPrizeCollisions();
                repaint();
                Utils.sleep(16);
            }
        }).start();
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        // ציור הסוכריות
        if (prizes != null) {
            for (int i = 0; i < prizes.length; i++) {
                if (prizes[i] != null) {
                    prizes[i].draw(graphics);
                }
            }
        }

        // ציור השחקן
        this.player.paint(graphics);

        // --- חישוב מיקום הניקוד החדש ---
        int buttonX = 20; // ה-X של הכפתור מ-Utils
        int buttonWidth = 50; // הרוחב מ-Utils
        int scoreX = buttonX + buttonWidth + 10; // יוצא 75
        int scoreY = 55; // גובה שמתיישב יפה מול מרכז הכפתור

        // הגדרת הפונט
        graphics.setFont(new Font("Arial", Font.BOLD, 30));

        // ציור הצל (שחור) - מוזז ב-2 פיקסלים מהניקוד המקורי
        graphics.setColor(Color.BLACK);
        graphics.drawString("Score: " + this.score, scoreX + 2, scoreY + 2);

        // ציור הניקוד (ורוד)
        graphics.setColor(new Color(180, 140, 207));
        graphics.drawString("Score: " + this.score, scoreX, scoreY);
    }

    private void spawnPrizes(int amount) {
        // 1. קודם כל בונים את המערך בגודל המדויק שביקשנו
        prizes = new Prize[amount];
        Random random = new Random();

        // 2. רצים על כל המשבצות במערך ומכניסים אליהן סוכריות
        for (int i = 0; i < prizes.length; i++) {
            int x = random.nextInt(Main.WINDOW_WIDTH - 50);
            int y = random.nextInt(Main.WINDOW_HEIGHT - 80);

            String randomCandy = candyImages[random.nextInt(candyImages.length)];

            // שמים את הסוכריה החדשה בדיוק במקום ה-i במערך
            prizes[i] = new Prize(x, y, 15, 36, randomCandy);
        }
    }

    public void checkPrizeCollisions() {
        // לוקחים את המלבן (קופסת הפגיעה) של השחקן
        Rectangle playerRect = player.getBounds();

        if (prizes != null) {
            for (int i = 0; i < prizes.length; i++) {
                // בודקים ששלושת התנאים מתקיימים:
                // 1. יש סוכריה במשבצת הזו במערך
                // 2. עדיין לא אספו אותה
                // 3. המלבן של השחקן נוגע במלבן של הסוכריה
                if (prizes[i] != null && !prizes[i].isCollected()) {
                    if (playerRect.intersects(prizes[i].getBounds())) {

                        // בינגו! יש פגיעה. משנים לה את המצב ל"נאספה"
                        prizes[i].setCollected(true);
                        this.score += 10;
                    }
                }
            }
        }
    }


}
