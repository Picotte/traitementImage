package models;

import java.util.ArrayList;
import java.util.Collections;

public class pathFinderAstar implements pathFinder
{
    private ArrayList closedList = new ArrayList();
    private SortedList openList = new SortedList();

    private tileBasedMap map;
    private int maxDistance;

    private Node[][] noeux;
    private boolean allowDiagonnalMouvement = false;
    private distanceHeuristic heuristic;


    public pathFinderAstar(tileBasedMap map, int maxDistance)
    {
        this(map, maxDistance, new closestDistanceHeuristic());
    }


    public pathFinderAstar(tileBasedMap map, int maxDistance, distanceHeuristic heuristic)
    {
        this.heuristic = heuristic;
        this.map = map;
        this.maxDistance = maxDistance;

        noeux = new Node[map.getWidth()][map.getHeight()];
        for (int x = 0; x<map.getWidth(); x++)
        {
            for (int y = 0; y<map.getHeight(); y++)
            {
                noeux[x][y] = new Node(x,y);
            }
        }
    }


    public path findPath(int startx, int starty, int targetx, int targety)
    {

        if(map.isBlocked(targetx, targety))
        {
            return null;
        }

        noeux[startx][starty].cost = 0;
        noeux[startx][starty].depth = 0;
        closedList.clear();
        openList.clear();
        openList.add(noeux[startx][starty]);

        noeux[targetx][targety].parent = null;

        int maxDepth = 0;
        while ((maxDepth < maxDistance) && (openList.size() != 0))
        {

            Node current = getFirstInOpen();
            if (current == noeux[targetx][targety])
            {
                break;
            }

            removeFromOpen(current);
            addToClosed(current);


            for (int x=-1;x<2;x++)
            {
                for (int y=-1;y<2;y++)
                {

                    if ((x == 0) && (y == 0))
                    {
                        continue;
                    }


                    if(!allowDiagonnalMouvement)
                    {
                        if ((x != 0) && (y != 0))
                        {
                            continue;
                        }
                    }


                    int xp = x + current.x;
                    int yp = y + current.y;

                    if (isValidLocation(startx,starty,xp,yp))
                    {

                        float nextStepCost = current.cost + getMovementCost(current.x, current.y, xp, yp);
                        Node neighbour = noeux[xp][yp];
                        map.pathFinderVisit(xp, yp);


                        if (nextStepCost < neighbour.cost)
                        {
                            if (inOpenList(neighbour))
                            {
                                removeFromOpen(neighbour);
                            }
                            if (inClosedList(neighbour))
                            {
                                removeFromClosed(neighbour);
                            }
                        }


                        if (!inOpenList(neighbour) && !(inClosedList(neighbour)))
                        {
                            neighbour.cost = nextStepCost;
                            neighbour.heuristic = getHeuristicCost(xp, yp, targetx, targety);
                            maxDepth = Math.max(maxDepth, neighbour.setParent(current));
                            addToOpen(neighbour);
                        }
                    }
                }
            }
        }



        if (noeux[targetx][targety].parent == null)
        {
            return null;
        }


        path path = new path();
        Node target = noeux[targetx][targety];
        while (target != noeux[startx][starty])
        {
            path.prependStep(target.x, target.y);
            target = target.parent;
        }
        path.prependStep(startx,starty);

        return path;
    }


    protected Node getFirstInOpen()
    {
        return (Node) openList.first();
    }


    protected void addToOpen(Node node)
    {
        openList.add(node);
    }


    protected boolean inOpenList(Node node)
    {
        return openList.contains(node);
    }


    protected void removeFromOpen(Node node)
    {
        openList.remove(node);
    }


    protected void addToClosed(Node node)
    {
        closedList.add(node);
    }


    protected boolean inClosedList(Node node)
    {
        return closedList.contains(node);
    }


    protected void removeFromClosed(Node node)
    {
        closedList.remove(node);
    }


    protected boolean isValidLocation(int startx, int starty, int x, int y)
    {
        boolean invalid = (x < 0) || (y < 0) || (x >= map.getWidth()) || (y >= map.getHeight());

        if ((!invalid) && ((startx != x) || (starty != y)))
        {
            invalid = map.isBlocked(x, y);
        }

        return !invalid;
    }


    public float getMovementCost(int startx, int starty, int targetx, int targety)
    {
        return map.getPrixDeplacement(startx, starty, targetx, targety);
    }


    public float getHeuristicCost(int x, int y, int targetx, int targety)
    {
        return heuristic.getPrixDeplacement(map, x, y, targetx, targety);
    }


    private class SortedList
    {
        private ArrayList list = new ArrayList();


        public Object first()
        {
            return list.get(0);
        }


        public void clear()
        {
            list.clear();
        }


        public void add(Object o)
        {
            list.add(o);
            Collections.sort(list);
        }


        public void remove(Object o)
        {
            list.remove(o);
        }


        public int size()
        {
            return list.size();
        }


        public boolean contains(Object o)
        {
            return list.contains(o);
        }
    }


    private class Node implements Comparable
    {
        private int x;
        private int y;
        private float cost;
        private Node parent;
        private float heuristic;
        private int depth;


        public Node(int x, int y)
        {
            this.x = x;
            this.y = y;
        }


        public int setParent(Node parent)
        {
            depth = parent.depth + 1;
            this.parent = parent;

            return depth;
        }


        public int compareTo(Object other)
        {
            Node o = (Node) other;

            float f = heuristic + cost;
            float of = o.heuristic + o.cost;

            if (f < of)
            {
                return -1;
            }
            else if (f > of)
            {
                return 1;
            }
            else
            {
                return 0;
            }
        }
    }
}
