package edu.curtin.app;

/****************************************************************************
 * This is a decoration subclass used to decorate an object in cyan colour   *
 ****************************************************************************/
public class ColorCyan extends MazeDecorator
{
    public ColorCyan(Cell next)
    {
        super(next);
    }
    
    @Override
    public String toString()
    {
        return "\033[36m" + next.toString() + "\033[m";
    }
}
