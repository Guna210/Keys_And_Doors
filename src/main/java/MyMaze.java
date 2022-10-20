package edu.curtin.app;

import java.util.*;
import java.util.logging.*;
import java.io.*;

@SuppressWarnings("PMD.FieldNamingConventions")
//Only the logger variable triggers the FieldNamingConventions warning and not sure what else I should name it other than logger.

public class MyMaze
{
    /************************************************************
     * This method obtains the file name from the user and      *
     * passes it to the readFile() method in the Maze class.    *
     * **********************************************************/
    private static final Logger logger = Logger.getLogger(Maze.class.getName());
    private static List<String> list = new ArrayList<>();
    private static MazeContents[][] arr;
    private static int rows;
    private static int colums;
    private static int objectRow;
    private static int objectColum;
    private static int playerRow;
    private static int playerColum;
    private static int playerCount;
    private static int endRow;
    private static int endColum;
    private static int endCount;
    private static int color;
    public static void main(String[] args)
    {
        String fileName;
        
        System.out.print("Enter the file name: ");

        try (Scanner sc = new Scanner(System.in)) 
        {
            fileName = sc.nextLine();
            readFile(fileName);

        } 
        catch (MyMazeException e) 
        {
            System.out.println(e.getMessage());
        }
    }

    public static void readFile(String fileName) throws MyMazeException
    {
        Maze maze = new Maze();
        File file = new File(fileName);
        logger.info("Read the file using Scanner");
        try (Scanner sc = new Scanner(file)) 
        {
            while(sc.hasNextLine())
            {
                list.add(sc.nextLine());
            }
        } 
        catch (IOException e) 
        {
            throw new MyMazeException("File not found!", e);
        }
        for(String s : list)
        {
            String[] splitLine = s.split(" ");
            if(splitLine.length == 2)
            {
                try 
                {
                    rows = Integer.parseInt(splitLine[0]);
                    colums = Integer.parseInt(splitLine[1]);
                } 
                catch(NumberFormatException e) 
                {
                    throw new MyMazeException("Invalid Input File!", e);
                }
            }
        }
        arr = new MazeContents[rows][colums];
        initializeArray();
        for(String s : list)
        {
            String[] splitLine = s.split(" ",4);
            if(splitLine[0].equals("WV"))
            {
                try 
                {
                    objectRow = Integer.parseInt(splitLine[1]);
                    objectColum = Integer.parseInt(splitLine[2]);
                } 
                catch(NumberFormatException e) 
                {
                    throw new MyMazeException("Invalid Input File!", e);
                }
                if((objectRow >= rows)||(objectRow < 0)||(objectColum <= 0)||(objectColum >= colums))
                {
                    logger.info("Object out of bounds. So move to next increament.");
                    continue;
                }
                else
                {
                    logger.info(() -> "Insert a new Vertical Wall in the location "+objectRow+","+objectColum);
                    setMaze(objectRow, objectColum, new VerticalWall());
                }
            }
            if(splitLine[0].equals("WH"))
            {
                try 
                {
                    objectRow = Integer.parseInt(splitLine[1]);
                    objectColum = Integer.parseInt(splitLine[2]);
                } 
                catch(NumberFormatException e) 
                {
                    throw new MyMazeException("Invalid Input File!", e);
                }
                if((objectRow >= rows)||(objectRow <= 0)||(objectColum < 0)||(objectColum >= colums))
                {
                    logger.info("Object out of bounds. So move to next increament.");
                    continue;
                }
                else
                {
                    logger.info(() -> "Insert a new Horizontal Wall in the location "+objectRow+","+objectColum);
                    setMaze(objectRow, objectColum, new HorizontalWall());
                }
            }
            if(splitLine[0].equals("S"))
            {
                if(playerCount == 0)
                {
                    try 
                    {
                        playerRow = Integer.parseInt(splitLine[1]);
                        playerColum = Integer.parseInt(splitLine[2]);
                    } 
                    catch(NumberFormatException e) 
                    {
                        throw new MyMazeException("Invalid Input File!", e);
                    }
                    if((playerRow < 0)||(playerRow >= rows)||(playerColum < 0)||(playerColum >= colums))
                    {
                        logger.info("Object out of bounds. So move to next increament.");
                        continue;
                    }
                    else
                    {
                        Player player = new Player();
                        logger.info(() -> "Insert a new Player in the location "+playerRow+","+playerColum);
                        setMaze(playerRow, playerColum, player);
                        playerCount = playerCount + 1;
                    }
                }
            }
            if(splitLine[0].equals("K"))
            {
                try 
                {
                    objectRow = Integer.parseInt(splitLine[1]);
                    objectColum = Integer.parseInt(splitLine[2]);
                    color = Integer.parseInt(splitLine[3]);
                } 
                catch(NumberFormatException e) 
                {
                    throw new MyMazeException("Invalid Input File!", e);
                }
                if((objectRow < 0)||(objectRow >= rows)||(objectColum < 0)||(objectColum >= colums))
                {
                    logger.info("Object out of bounds. So move to next increament.");
                    continue;
                }
                else
                {
                    if(color == 1)
                    {
                        logger.info(() -> "Insert a new Red Key in the location "+objectRow+","+objectColum);
                        setMaze(objectRow, objectColum, new ColorRed(new Key()));
                    }
                    if(color == 2)
                    {
                        logger.info(() -> "Insert a new Green Key in the location "+objectRow+","+objectColum);
                        setMaze(objectRow, objectColum, new ColorGreen(new Key()));
                    }
                    if(color == 3)
                    {
                        logger.info(() -> "Insert a new Yellow Key in the location "+objectRow+","+objectColum);
                        setMaze(objectRow, objectColum, new ColorYellow(new Key()));
                    }
                    if(color == 4)
                    {
                        logger.info(() -> "Insert a new Blue Key in the location "+objectRow+","+objectColum);
                        setMaze(objectRow, objectColum, new ColorBlue(new Key()));
                    }
                    if(color == 5)
                    {
                        logger.info(() -> "Insert a new Magenta Key in the location "+objectRow+","+objectColum);
                        setMaze(objectRow, objectColum, new ColorMagenta(new Key()));
                    }
                    if(color == 6)
                    {
                        logger.info(() -> "Insert a new Cyan Key in the location "+objectRow+","+objectColum);
                        setMaze(objectRow, objectColum, new ColorCyan(new Key()));
                    }
                }
            }
            if(splitLine[0].equals("E"))
            {
                try 
                {
                    endRow = Integer.parseInt(splitLine[1]);
                    endColum = Integer.parseInt(splitLine[2]);
                } 
                catch(NumberFormatException e) 
                {
                    throw new MyMazeException("Invalid Input File!", e);
                }
                if((endRow < 0)||(endRow >= rows)||(endColum < 0)||(endColum >= colums))
                {
                    logger.info("Object out of bounds. So move to next increament.");
                    continue;
                }
                else
                {
                    logger.info(() -> "Insert a new End Point in the location "+endRow+","+endColum);
                    setMaze(endRow, endColum, new End());
                    endCount = endCount + 1;
                }
            }
            if(splitLine[0].equals("DV"))
            {
                try 
                {
                    objectRow = Integer.parseInt(splitLine[1]);
                    objectColum = Integer.parseInt(splitLine[2]);
                    color = Integer.parseInt(splitLine[3]);
                } 
                catch(NumberFormatException e) 
                {
                    throw new MyMazeException("Invalid Input File!", e);
                }
                if((objectRow < 0)||(objectRow >= rows)||(objectColum <= 0)||(objectColum >= colums))
                {
                    logger.info("Object out of bounds. So move to next increament.");
                    continue;
                }
                else
                {
                    if(color == 1)
                    {
                        logger.info(() -> "Insert a new Red Vertical Door in the location "+objectRow+","+objectColum);
                        setMaze(objectRow, objectColum, new ColorRed(new VerticalDoor()));
                    }
                    if(color == 2)
                    {
                        logger.info(() -> "Insert a new Green Vertical Door in the location "+objectRow+","+objectColum);
                        setMaze(objectRow, objectColum, new ColorGreen(new VerticalDoor()));
                    }
                    if(color == 3)
                    {
                        logger.info(() -> "Insert a new Yellow Vertical Door in the location "+objectRow+","+objectColum);
                        setMaze(objectRow, objectColum, new ColorYellow(new VerticalDoor()));
                    }
                    if(color == 4)
                    {
                        logger.info(() -> "Insert a new Blue Vertical Door in the location "+objectRow+","+objectColum);
                        setMaze(objectRow, objectColum, new ColorBlue(new VerticalDoor()));
                    }
                    if(color == 5)
                    {
                        logger.info(() -> "Insert a new Magenta Vertical Door in the location "+objectRow+","+objectColum);
                        setMaze(objectRow, objectColum, new ColorMagenta(new VerticalDoor()));
                    }
                    if(color == 6)
                    {
                        logger.info(() -> "Insert a new Cyan Vertical Door in the location "+objectRow+","+objectColum);
                        setMaze(objectRow, objectColum, new ColorCyan(new VerticalDoor()));
                    }
                }
                
            }
            if(splitLine[0].equals("DH"))
            {
                try 
                {
                    objectRow = Integer.parseInt(splitLine[1]);
                    objectColum = Integer.parseInt(splitLine[2]);
                    color = Integer.parseInt(splitLine[3]);
                } 
                catch(NumberFormatException e) 
                {
                    throw new MyMazeException("Invalid Input File!", e);
                }
                if((objectRow <= 0)||(objectRow >= rows)||(objectColum < 0)||(objectColum >= colums))
                {
                    logger.info("Object out of bounds. So move to next increament.");
                    continue;
                }
                else
                {
                    if(color == 1)
                    {
                        logger.info(() -> "Insert a new Red Horizontal Door in the location "+objectRow+","+objectColum);
                        setMaze(objectRow, objectColum, new ColorRed(new HorizontalDoor()));
                    }
                    if(color == 2)
                    {
                        logger.info(() -> "Insert a new Green Horizontal Door in the location "+objectRow+","+objectColum);
                        setMaze(objectRow, objectColum, new ColorGreen(new HorizontalDoor()));
                    }
                    if(color == 3)
                    {
                        logger.info(() -> "Insert a new Yellow Horizontal Door in the location "+objectRow+","+objectColum);
                        setMaze(objectRow, objectColum, new ColorYellow(new HorizontalDoor()));
                    }
                    if(color == 4)
                    {
                        logger.info(() -> "Insert a new Blue Horizontal Door in the location "+objectRow+","+objectColum);
                        setMaze(objectRow, objectColum, new ColorBlue(new HorizontalDoor()));
                    }
                    if(color == 5)
                    {
                        logger.info(() -> "Insert a new Magenta Horizontal Door in the location "+objectRow+","+objectColum);
                        setMaze(objectRow, objectColum, new ColorMagenta(new HorizontalDoor()));
                    }
                    if(color == 6)
                    {
                        logger.info(() -> "Insert a new Cyan Horizontal Door in the location "+objectRow+","+objectColum);
                        setMaze(objectRow, objectColum, new ColorCyan(new HorizontalDoor()));
                    }
                }
            }
            if(splitLine[0].equals("M"))
            {
                try
                {
                    objectRow = Integer.parseInt(splitLine[1]);
                    objectColum = Integer.parseInt(splitLine[2]);
                }
                catch(NumberFormatException e)
                {
                    throw new MyMazeException("Invalid Input File!", e);
                }
                if((objectRow < 0)||(objectRow >= rows)||(objectColum < 0)||(objectColum >= colums))
                {
                    logger.info("Object out of bounds. So move to next increament.");
                    continue;
                }
                else
                {
                    Messages m2 = new Messages();
                    m2.setMessage(splitLine[3]);
                    logger.info(() -> "Insert a new Message in the location "+objectRow+","+objectColum);
                    setMaze(objectRow, objectColum, m2);
                }
            }
        }
        maze.obtainMaze(arr, rows, colums);
    }

    /**************************************************************************
     * Initialize the 2D array created by the readFile() method to store the  *
     * contents of the input file.                                            *
     **************************************************************************/
    public static void initializeArray()
    {
        for(int i = 0; i < rows; i++)
        {
            for(int j = 0; j < colums; j++)
            {
                arr[i][j] = new MazeContents();
            }
        }
    }

    /**************************************************************************
     * Creates a new String 2D array mapping the 2D object array created by   *
     * the readFile() method and this String array is created anew to depict  *
     * the changes made with every move made by the player                    *
     **************************************************************************/
    public static void setMaze(int row, int colum, Cell contents)
    {
        arr[row][colum].setContents(contents);
    }
}
