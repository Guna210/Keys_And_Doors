package edu.curtin.app;

import java.util.*;

public class MazeContents 
{
    private List<Cell> contents = new ArrayList<>();
    
    public MazeContents()
    { }

    public void setContents(Cell inContents)
    {
        contents.add(inContents);
    }

    public List<Cell> getContents()
    {
        return contents;
    }

    public void removeContens(Cell content)
    {
        contents.remove(content);
    }
}
