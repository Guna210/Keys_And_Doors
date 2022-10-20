package edu.curtin.app;

/********************************************************************
 * This is the custom exception class used by the MyMaze program    *
 ********************************************************************/
public class MyMazeException extends Exception
{
    public MyMazeException(String msg)
    {
        super(msg);
    }
    
    public MyMazeException(String msg, Throwable cause)
    {
        super(msg, cause);
    }
}
