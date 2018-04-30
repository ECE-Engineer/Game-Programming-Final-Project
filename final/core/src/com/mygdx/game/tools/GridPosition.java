package com.mygdx.game.tools;

public class GridPosition {
    private int valX;
    private int valY;

    public GridPosition(int valX, int valY) {
        this.valX = valX;
        this.valY = valY;
    }

    public int getX() {
        return valX;
    }

    public int getY() {
        return valY;
    }

    public void setX(int valX) {
        this.valX = valX;
    }

    public void setY(int valY) {
        this.valY = valY;
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof GridPosition)) {
            return false;
        }

        GridPosition position = (GridPosition) o;

        return ((position.valX == this.valX) && (position.valY == this.valY));
    }

    @Override
    public int hashCode() {
        int width = 10000;
        return (valY * width) + valX;
    }
}