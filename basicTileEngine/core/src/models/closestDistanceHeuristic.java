package models;

public class closestDistanceHeuristic implements distanceHeuristic
{

    public float getPrixDeplacement(tileBasedMap map, int x, int y, int targetx, int targety)
    {
        float dx = targetx - x;
        float dy = targety - y;

        float resultat = (float) (Math.sqrt((dx*dx)+(dy*dy)));

        return resultat;
    }

}