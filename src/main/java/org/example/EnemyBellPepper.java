package org.example;

public class EnemyBellPepper extends Enemy {

    private Player targetPlayer;

    public EnemyBellPepper(int x, int y, int width, int height, Player player) {
        super(x, y, width, height);
        this.targetPlayer = player;

        setFrontImage(loadImage("/BellPepper_Front.png"));
        setBackImage(loadImage("/BellPepper_Back.png"));
        setRightImage(loadImage("/BellPepper_Right.png"));
        setLeftImage(loadImage("/BellPepper_Left.png"));

        setCurrentImage(getFrontImage());
    }

    @Override
    public void move() {
        if (!isMoving() || targetPlayer == null) return;

        int diffX = targetPlayer.getX() - getX();
        int diffY = targetPlayer.getY() - getY();

        if (Math.abs(diffX) > Math.abs(diffY)) {
            if (diffX > 0) {
                setDirection(RIGHT);
                setCurrentImage(getRightImage());
                if (!isAtRightBoundary()) {
                    moveHorizontally(2);
                }
            } else if (diffX < 0) {
                setDirection(LEFT);
                setCurrentImage(getLeftImage());
                if (!isAtLeftBoundary()) {
                    moveHorizontally(-2);
                }
            }
        } else {
            if (diffY > 0) {
                setDirection(DOWN);
                setCurrentImage(getFrontImage());
                if (!isAtBottomBoundary()) {
                    moveVertically(2);
                }
            } else if (diffY < 0) {
                setDirection(UP);
                setCurrentImage(getBackImage());
                if (!isAtTopBoundary()) {
                    moveVertically(-2);
                }
            }
        }
    }
}