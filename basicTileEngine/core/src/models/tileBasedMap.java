package models;

/**
 * Created by 1285798 on 16-11-28.
 */
public interface tileBasedMap {
    int getWidth();
    int getHeight();
    boolean isBlocked(int x, int y);
    float getPrixDeplacement(int startx, int starty, int targetx, int targety);
    void pathFinderVisit(int x, int y);
}
