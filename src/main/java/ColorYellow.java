package edu.curtin.app;

/********************************************************************************
 * This is a decoration subclass used to decorate an object in yellow colour    *
 ********************************************************************************/
public class ColorYellow extends MazeDecorator
{
    public ColorYellow(Cell next)
    {
        super(next);
    }
    
    @Override
    public String toString()
    {
        return "\033[33m" + next.toString() + "\033[m";
    }
}
