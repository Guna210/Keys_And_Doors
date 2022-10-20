package edu.curtin.app;

import java.util.*;

public class Player implements Cell
{
    private int redCount;
    private int greenCount;
    private int yellowCount;
    private int blueCount;
    private int magentaCount;
    private int cyanCount;
    private List<Cell> keys = new ArrayList<>();
    
    @Override
    public String toString()
    {
        return "P";
    }

    public void addRedKey()
    {
        redCount = redCount + 1;
    }

    public boolean hasRedKey()
    {
        boolean has = false;
        if(redCount != 0)
        {
            has = true;
        }
        return has;
    }

    public void addGreenKey()
    {
        greenCount = greenCount + 1;
    }

    public boolean hasGreenKey()
    {
        boolean has = false;
        if(greenCount != 0)
        {
            has = true;
        }
        return has;
    }

    public void addYellowKey()
    {
        yellowCount = yellowCount + 1;
    }

    public boolean hasYellowKey()
    {
        boolean has = false;
        if(yellowCount != 0)
        {
            has = true;
        }
        return has;
    }

    public void addBlueKey()
    {
        blueCount = blueCount + 1;
    }

    public boolean hasBlueKey()
    {
        boolean has = false;
        if(blueCount != 0)
        {
            has = true;
        }
        return has;
    }

    public void addMagentaKey()
    {
        magentaCount = magentaCount + 1;
    }

    public boolean hasMagentaKey()
    {
        boolean has = false;
        if(magentaCount != 0)
        {
            has = true;
        }
        return has;
    }

    public void addCyanKey()
    {
        cyanCount = cyanCount + 1;
    }

    public boolean hasCyanKey()
    {
        boolean has = false;
        if(cyanCount != 0)
        {
            has = true;
        }
        return has;
    }

    public void setKeys(Cell key)
    {
        keys.add(key);
    }

    public List<Cell> getKeys()
    {
        return keys;
    }
}
