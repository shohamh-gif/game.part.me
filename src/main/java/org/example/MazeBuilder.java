package org.example;

public class MazeBuilder {
    private final int CAKE_SIZE = 50;
    private Cake[] cakes;
    private int cakesCount;

    public Cake[] buildMaze(int templateIndex, int width, int height) {
        this.cakes = new Cake[300];
        this.cakesCount = 0;

        int cols = width / CAKE_SIZE;
        int rows = height / CAKE_SIZE;
        int midX = cols / 2;
        int midY = rows / 2;

        switch (templateIndex) {
            case 0:
                drawOriginalMaze(midX, midY);
                break;
            case 1:
                drawCrossMaze(cols, rows, midX, midY);
                break;
            case 2:
                drawPillarMaze(cols, rows);
                break;
        }

        return this.cakes;
    }

    public int getCakesCount() {
        return this.cakesCount;
    }

    private void drawOriginalMaze(int midX, int midY) {
        // רבע שמאלי עליון
        for (int gridX = midX - 6; gridX <= midX - 2; gridX++) addCake(gridX, midY - 4);
        for (int gridY = midY - 3; gridY <= midY - 2; gridY++) addCake(midX - 6, gridY);

        // רבע ימני עליון
        for (int gridX = midX + 2; gridX <= midX + 6; gridX++) addCake(gridX, midY - 4);
        for (int gridY = midY - 3; gridY <= midY - 2; gridY++) addCake(midX + 6, gridY);

        // רבע שמאלי תחתון
        for (int gridX = midX - 6; gridX <= midX - 2; gridX++) addCake(gridX, midY + 4);
        for (int gridY = midY + 2; gridY <= midY + 3; gridY++) addCake(midX - 6, gridY);

        // רבע ימני תחתון
        for (int gridX = midX + 2; gridX <= midX + 6; gridX++) addCake(gridX, midY + 4);
        for (int gridY = midY + 2; gridY <= midY + 3; gridY++) addCake(midX + 6, gridY);

        // ריבוע פנימי
        for (int gridX = midX - 1; gridX <= midX + 2; gridX++) addCake(gridX, midY - 1);
        for (int gridX = midX - 1; gridX <= midX + 2; gridX++) addCake(gridX, midY + 2);
        for (int gridY = midY - 1; gridY <= midY + 2; gridY++) {
            if (gridY != midY) {
                addCake(midX - 2, gridY);
                addCake(midX + 3, gridY);
            }
        }
    }

    private void drawCrossMaze(int cols, int rows, int midX, int midY) {
        for (int i = 2; i < cols - 2; i++) {
            if (i != midX && i != midX - 1) addCake(i, midY);
        }
        for (int j = 2; j < rows - 2; j++) {
            if (j != midY) addCake(midX, j);
        }
        addCake(2, 2);
        addCake(3, 2);
        addCake(2, 3);
        addCake(cols - 3, 2);
        addCake(cols - 4, 2);
        addCake(cols - 3, 3);
        addCake(2, rows - 3);
        addCake(2, rows - 4);
        addCake(3, rows - 3);
        addCake(cols - 3, rows - 3);
        addCake(cols - 3, rows - 4);
        addCake(cols - 4, rows - 3);
    }

    private void drawPillarMaze(int cols, int rows) {
        for (int i = 4; i < cols - 4; i += 4) {
            for (int j = 2; j < rows - 2; j++) {
                if (i % 8 == 4 && j < rows - 5) addCake(i, j);
                if (i % 8 == 0 && j > 4) addCake(i, j);
            }
        }
    }

    private void addCake(int gridX, int gridY) {
        if (cakesCount < cakes.length) {
            cakes[cakesCount++] = new Cake(gridX * CAKE_SIZE, gridY * CAKE_SIZE, CAKE_SIZE, CAKE_SIZE);
        }
    }
}