package models;

import java.util.ArrayList;

/**
 * Created by 1285798 on 16-11-28.
 */
public class path
{
    private ArrayList steps = new ArrayList();


    public path()
    {

    }

    public void prependStep(int x, int y)
    {
        steps.add(0, new Step(x, y));
    }
    public boolean contains(int x, int y)
    {
        return steps.contains(new Step(x,y));
    }

    public class Step
    {
        private int x;
        private int y;


        public Step(int x, int y)
        {
            this.x = x;
            this.y = y;
        }


        public int hashCode()
        {
            return x*y;
        }

        public boolean equals(Object other)
        {
            if (other instanceof Step)
            {
                Step o = (Step) other;

                return (o.x == x) && (o.y == y);
            }

            return false;
        }
    }
}
