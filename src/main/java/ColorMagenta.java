package edu.curtin.app;

/********************************************************************************
 * This is a decoration subclass used to decorate an object in magenta colour   *
 ********************************************************************************/
public class ColorMagenta extends MazeDecorator
{
    public ColorMagenta(Cell next)
    {
        super(next);
    }
    
    @Override
    public String toString()
    {
        return "\033[35m" + next.toString() + "\033[m";
    }
}
