package edu.curtin.app;

public class HorizontalDoor implements Cell
{
    private boolean state = false;
    
    @Override
    public String toString()
    {
        return "\u2592";
    }

    public void openedState()
    {
        state = true;
    }

    public boolean hasOpened()
    {
        return state;
    }
}