package edu.curtin.app;

/****************************************************************************
 * This is a decoration subclass used to decorate an object in red colour   *
 ****************************************************************************/
public class ColorRed extends MazeDecorator
{
    public ColorRed(Cell next) 
    {
        super(next);
    }
    
    @Override
    public String toString()
    {
        return "\033[31m" + next.toString() + "\033[m";
    }
}
