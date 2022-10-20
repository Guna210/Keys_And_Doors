package edu.curtin.app;

/****************************************************************************
 * This is a decoration subclass used to decorate an object in blue colour   *
 ****************************************************************************/
public class ColorBlue extends MazeDecorator
{
    public ColorBlue(Cell next) 
    {
        super(next);
    }
    
    @Override
    public String toString()
    {
        return "\033[34m" + next.toString() + "\033[m";
    }
}
