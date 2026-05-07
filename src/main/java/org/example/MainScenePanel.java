package org.example;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.util.Random;

public class MainScenePanel extends JPanel {
    private Player player;
    private Cake[] cakes;
    private Enemy[] enemies;
    private int score;
    private Prize[] prizes;
    private String[] candyImages;
    private int cakesCount;
    private final int CAKE_SIZE = 50;
    private LevelsBackground levelsBackground;

    public MainScenePanel(int x, int y, int width, int height) {
        this.cakes = new Cake[100];
        this.cakesCount = 0;
        this.candyImages = new String[]{
                "/Blue_candy.png",
                "/Orange_candy.png",
                "/Pink_candy.png",
                "/Purple_candy.png",
                "/Yellow_candy.png"
        };
        this.setBounds(x, y, width, height);
        this.setLayout(null);
        this.levelsBackground = new LevelsBackground();
        //בניית המבוך
        cakesCount = 0;
        int cols = width / CAKE_SIZE; // כמות עמודות
        int rows = height / CAKE_SIZE; // כמות שורות
        int midX = cols / 2;
        int midY = rows / 2;

        // הגדרת מערכים חד-מימדיים
        int[] xPositions = new int[200]; // מיקומי האיקס של העוגות
        int[] yPositions = new int[200]; // מיקומי הוואי של העוגות
        int posCount = 0; // סופר כמה מיקומים שמרנו בתוך המערכים האלה

        // 1. רבע שמאלי עליון
        for (int gridX = midX - 6; gridX <= midX - 2; gridX++) {
            xPositions[posCount] = gridX;
            yPositions[posCount] = midY - 4;
            posCount++;
        }
        for (int gridY = midY - 3; gridY <= midY - 2; gridY++) {
            xPositions[posCount] = midX - 6;
            yPositions[posCount] = gridY;
            posCount++;
        }

        // 2. רבע ימני עליון
        for (int gridX = midX + 2; gridX <= midX + 6; gridX++) {
            xPositions[posCount] = gridX;
            yPositions[posCount] = midY - 4;
            posCount++;
        }
        for (int gridY = midY - 3; gridY <= midY - 2; gridY++) {
            xPositions[posCount] = midX + 6;
            yPositions[posCount] = gridY;
            posCount++;
        }

        // 3. רבע שמאלי תחתון
        for (int gridX = midX - 6; gridX <= midX - 2; gridX++) {
            xPositions[posCount] = gridX;
            yPositions[posCount] = midY + 4;
            posCount++;
        }
        for (int gridY = midY + 2; gridY <= midY + 3; gridY++) {
            xPositions[posCount] = midX - 6;
            yPositions[posCount] = gridY;
            posCount++;
        }

        // 4. רבע ימני תחתון
        for (int gridX = midX + 2; gridX <= midX + 6; gridX++) {
            xPositions[posCount] = gridX;
            yPositions[posCount] = midY + 4;
            posCount++;
        }
        for (int gridY = midY + 2; gridY <= midY + 3; gridY++) {
            xPositions[posCount] = midX + 6;
            yPositions[posCount] = gridY;
            posCount++;
        }

        // 5. --- הריבוע הפנימי החדש (עם פתח אחד בימין ופתח אחד בשמאל) ---

        // קיר עליון של הריבוע הפנימי (סגור לגמרי)
        for (int gridX = midX - 1; gridX <= midX + 2; gridX++) {
            xPositions[posCount] = gridX;
            yPositions[posCount] = midY - 1;
            posCount++;
        }

        // קיר תחתון של הריבוע הפנימי (סגור לגמרי)
        for (int gridX = midX - 1; gridX <= midX + 2; gridX++) {
            xPositions[posCount] = gridX;
            yPositions[posCount] = midY + 2;
            posCount++;
        }

        // קיר שמאלי של הריבוע הפנימי - עם רווח (בעזרת תנאי שמדלג על האמצע)
        for (int gridY = midY - 1; gridY <= midY + 2; gridY++) {
            if (gridY != midY) {
                xPositions[posCount] = midX - 2;
                yPositions[posCount] = gridY;
                posCount++;
            }
        }

        // קיר ימני של הריבוע הפנימי - עם רווח (בעזרת תנאי שמדלג על האמצע)
        for (int gridY = midY - 1; gridY <= midY + 2; gridY++) {
            if (gridY != midY) {
                xPositions[posCount] = midX + 3;
                yPositions[posCount] = gridY;
                posCount++;
            }
        }

        // --- סיום וציור ---
        // לולאה שמציירת את כל העוגות מהמערכים
        for (int i = 0; i < posCount; i++) {
            if (cakesCount < cakes.length) {
                cakes[cakesCount++] = new Cake(xPositions[i] * CAKE_SIZE, yPositions[i] * CAKE_SIZE, CAKE_SIZE, CAKE_SIZE);
            }
        }
        // 1. קודם כל יוצרים את השחקן!
        this.player = new Player(100, 100, 60, 60);

        // 2. עכשיו יוצרים אויבים בגודל 40x40
        this.enemies = new Enemy[7];
        int enemySize = 46;
        this.enemies[0] = new EnemyBroccoli(200, 100, enemySize, enemySize);
        this.enemies[1] = new EnemyBroccoli(400, 200, enemySize, enemySize);
        this.enemies[2] = new EnemyBroccoli(600, 300, enemySize, enemySize);
        this.enemies[3] = new EnemyEggplant(150, 200, enemySize, enemySize);
        this.enemies[4] = new EnemyEggplant(300, 450, enemySize, enemySize);
        this.enemies[5] = new EnemyEggplant(450, 500, enemySize, enemySize);
        this.enemies[6] = new EnemyBellPepper(600, 50, enemySize, enemySize, this.player);

        for (int i = 0; i < this.enemies.length; i++) {
            this.enemies[i].setIsMoving(true);
        }

        this.setFocusable(true);//רשאי לקבל פוקוס מהמקלדת
        this.requestFocus();//מקבל פוקוס מהמקלדת- השחקן יכול להגיב ללחיצות
        this.setDoubleBuffered(true);// הפתרון לריצוד הגיף - כדי שלא נראה את המחיקה והציור בזמן אמת רק שהציור מוכן

        MovementListener movementListener = new MovementListener(this, this.player);
        this.addKeyListener(movementListener);
        JButton soundButton = Utils.createSoundButton();
        this.add(soundButton);
        RoundedButton exitButton = new RoundedButton("Exit", 20);
        exitButton.setBounds(width - 65, 15, 60, 30);
        exitButton.setBackground(new java.awt.Color(255, 100, 100));
        exitButton.setForeground(Color.WHITE);
        exitButton.setFont(new java.awt.Font("Arial", Font.BOLD, 12));
        exitButton.setBorderPainted(false);
        exitButton.setFocusable(false);
        exitButton.addActionListener(e -> {
            System.exit(0);
        });
        this.add(exitButton);
        spawnPrizes(10); // המספר קובע כמה סוכריות ייווצרו
        this.gameLoop();


    }

    public boolean checkCakeCollision() {
        Rectangle characterRect = this.player.getRect();
// עכשיו אנחנו שולטים בכל כיוון בנפרד!
        int padLeft = 14;   // חותכים מהצדדים כדי שייכנס למעברים
        int padRight = 14;
        int padTop = 22;
        int padBottom = 5;

        Rectangle smallCharacterRect = new Rectangle(
                characterRect.x + padLeft,
                characterRect.y + padTop,
                characterRect.width - (padLeft + padRight),
                characterRect.height - (padTop + padBottom)
        );

        for (int i = 0; i < this.cakesCount; i++) {
            Cake currentCake = this.cakes[i];
            if (currentCake != null) {
                Rectangle cakeRect = currentCake.getRect();

                int cakePadding = 4;
                Rectangle smallCakeRect = new Rectangle(
                        cakeRect.x + cakePadding,
                        cakeRect.y + cakePadding,
                        cakeRect.width - (cakePadding * 2),
                        cakeRect.height - (cakePadding * 2)
                );

                if (smallCharacterRect.intersects(smallCakeRect)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void gameLoop() {
        new Thread(() -> {
            while (true) {
                for (int i = 0; i < this.enemies.length; i++) {
                    // שומרים מיקום לפני תנועה
                    int oldX = this.enemies[i].getX();
                    int oldY = this.enemies[i].getY();

                    this.enemies[i].move();

                    // אם נתקע בקיר או באויב אחר
                    boolean hitSomething = checkEnemyCakeCollision(this.enemies[i]);
                    if (!hitSomething) {
                        for (int j = 0; j < this.enemies.length; j++) {
                            if (i != j && checkEnemyCollision(this.enemies[i], this.enemies[j])) {
                                hitSomething = true;
                                break;
                            }
                        }
                    }

                    if (hitSomething) {
                        // חזרה למיקום בטוח ושינוי כיוון
                        this.enemies[i].setX(oldX);
                        this.enemies[i].setY(oldY);
                        this.enemies[i].reverseDirection();
                    }

                    // בדיקה מול השחקן
                    if (checkCollision(this.player, this.enemies[i])) {
                        // כאן אפשר להוסיף סיום משחק/הורדת חיים
                    }
                }
                checkPrizeCollisions();
                repaint();
                Utils.sleep(16);
            }
        }).start();
    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (this.levelsBackground != null) {
            this.levelsBackground.paint(graphics, this.getWidth(), this.getHeight());
        }
        for (int i = 0; i < this.enemies.length; i++) {
            this.enemies[i].paint(graphics);
        }
        if (cakes.length > 0) {
            for (int i = 0; i < cakesCount; i++) {
                if (cakes[i] != null) {
                    cakes[i].paint(graphics);
                }
            }
        }
        if (this.player != null) {
            this.player.paint(graphics);

        }

        // ציור הסוכריות
        if (prizes != null) {
            for (int i = 0; i < prizes.length; i++) {
                if (prizes[i] != null) {
                    prizes[i].draw(graphics);
                }
            }
        }
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
        prizes = new Prize[amount];
        Random random = new Random();

        int safeMargin = 20;
        int minX = 90 + safeMargin;
        int maxX = Main.WINDOW_WIDTH - 48 - 15 - safeMargin;
        int minY = 35 + safeMargin;
        int maxY = Main.WINDOW_HEIGHT - 50 - 36 - safeMargin;

        for (int i = 0; i < prizes.length; i++) {
            int x, y;

            // לולאת do-while: תעשה את ההגרלה כל עוד (while) המיקום לא חוקי
            do {
                x = random.nextInt(maxX - minX) + minX;
                y = random.nextInt(maxY - minY) + minY;
            } while (!isValidPrizeLocation(x, y, i));

            // ברגע שהלולאה הסתיימה, זה אומר שיש לנו מיקום מושלם!
            String randomCandy = candyImages[random.nextInt(candyImages.length)];
            prizes[i] = new Prize(x, y, 15, 36, randomCandy);
        }
    }

    // פונקציית העזר: מקבלת מיקום (X,Y) ואת מספר הסוכריות שכבר יצרנו, ומחזירה אם המיקום פנוי
    private boolean isValidPrizeLocation(int x, int y, int currentPrizeIndex) {
        Rectangle tempPrizeRect = new Rectangle(x, y, 15, 36);

        // 1. האם המיקום נוגע בעוגה?
        for (int j = 0; j < cakesCount; j++) {
            if (cakes[j] != null && tempPrizeRect.intersects(cakes[j].getRect())) {
                return false; // מצאנו עוגה, המיקום פסול
            }
        }

        // 2. האם המיקום קרוב מדי לסוכריה שכבר הגרלנו?
        int padding = 40;
        Rectangle safeZone = new Rectangle(x - padding, y - padding, 15 + (padding * 2), 36 + (padding * 2));

        for (int k = 0; k < currentPrizeIndex; k++) {
            if (prizes[k] != null && safeZone.intersects(prizes[k].getBounds())) {
                return false; // מצאנו סוכריה אחרת באזור, המיקום פסול
            }
        }
        // אם לא נתקלנו ב-return false עד עכשיו, המיקום מושלם!
        return true;
    }

    // פונקציה שמקבלת שחקן ואויב ומחזירה true אם יש התנגשות
    private boolean checkCollision(Player player, Enemy enemy) {
        return (player.getX() + player.getWidth() > enemy.getX()) &&   // 1. צד ימין של השחקן עבר את שמאל של האויב?
                (player.getX() < enemy.getX() + enemy.getWidth()) &&   // 2. צד שמאל של השחקן לפני ימין של האויב?
                (player.getY() + player.getHeight() > enemy.getY()) &&  // 3. הלמטה של השחקן עבר את הלמעלה של האויב?
                (player.getY() < enemy.getY() + enemy.getHeight());    // 4. הלמעלה של השחקן לפני הלמטה של האויב?
    }

    // פונקציה שמקבלת שני אויבים ומחזירה true אם הם נוגעים אחד בשני
    private boolean checkEnemyCollision(Enemy enemy1, Enemy enemy2) {
        return (enemy1.getX() + enemy1.getWidth() > enemy2.getX()) &&
                (enemy1.getX() < enemy2.getX() + enemy2.getWidth()) &&
                (enemy1.getY() + enemy1.getHeight() > enemy2.getY()) &&
                (enemy1.getY() < enemy2.getY() + enemy2.getHeight());
    }

    // תוסיפי את הפונקציה הזו למטה, ליד שאר פונקציות הבדיקה
    private boolean checkEnemyCakeCollision(Enemy enemy) {
        Rectangle enemyRect = enemy.getRect();
        for (int i = 0; i < this.cakesCount; i++) {
            if (cakes[i] != null) {
                if (enemyRect.intersects(cakes[i].getRect())) {
                    return true; // נתקע בעוגה!
                }
            }
        }
        return false;
    }

    public void checkPrizeCollisions() {
        // לוקחים את המלבן (קופסת הפגיעה) של השחקן
        Rectangle playerRect = player.getRect();

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