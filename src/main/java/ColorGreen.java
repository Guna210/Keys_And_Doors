package edu.curtin.app;

/******************************************************************************
 * This is a decoration subclass used to decorate an object in green colour   *
 ******************************************************************************/
public class ColorGreen extends MazeDecorator
{
    public ColorGreen(Cell next) 
    {
        super(next);
    }
    
    @Override
    public String toString()
    {
        return "\033[32m" + next.toString() + "\033[m";
    }
}
