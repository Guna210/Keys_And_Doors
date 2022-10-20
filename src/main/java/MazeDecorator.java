package edu.curtin.app;

/*********************************************************
 * This is the decoration super class used to decorate   *
 * keys, vertical door and horizontal doors              *
 *********************************************************/
public abstract class MazeDecorator implements Cell
{
    protected Cell next;

    public MazeDecorator(Cell next)
    {
        this.next = next;
    }

    @Override
    public String toString()
    {
        return next.toString();
    }

    /****************************************************
     * This method is used to obtain the original type  *
     * of the object before decoration                  *
     ****************************************************/
    public Cell getNext()
    {
        return next;
    }
}
