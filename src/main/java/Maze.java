package edu.curtin.app;

import java.util.*;
import java.util.logging.*;

@SuppressWarnings({"PMD.CloseResource" , "PMD.UnusedAssignment", "PMD.CompareObjectsWithEquals"})
//The justification for suppressing CloseResource is provided in the move() method.

public class Maze 
{   
    private final Logger logger = Logger.getLogger(Maze.class.getName());
    private String[][] arr;
    private MazeContents[][] arr2;
    private List<Messages> m = new ArrayList<>();
    private List<Cell> obtainedKeys = new ArrayList<>();
    private Player player;
    private int displayRows;
    private int displayColums;
    private int rows;
    private int colums;
    private int playerRow;
    private int playerColum;
    private int playerCount = 0;
    private int endRow;
    private int endColum;
    private int endCount = 0;

    /******************************************************************************
     * Obtain the file name from the Main method and store the contents to a list.*
     * Parse that list to a 2D array of size provided in the input file.          *
     ******************************************************************************/
    public void obtainMaze(MazeContents[][] a, int r, int c) throws MyMazeException
    {
        rows = r;
        colums = c;
        arr2 = a;
        Player rem = new Player();
        for(int i = 0; i < rows; i++)
        {
            for(int j = 0; j < colums; j++)
            {
                List<Cell> cell = arr2[i][j].getContents();
                for(Cell c1 : cell)
                {
                    if(c1 instanceof Player)
                    {
                        playerRow = i;
                        playerColum = j;
                        player = new Player();
                        playerCount = playerCount + 1;
                        rem = (Player) c1;
                    }
                    if(c1 instanceof End)
                    {
                        endRow = i;
                        endColum = j;
                        endCount = endCount + 1;
                    }
                }
            }
        }
        //Add in the player object created in the Maze class and remove the one created in the MyMaze class.
        arr2[playerRow][playerColum].removeContens(rem);
        arr2[playerRow][playerColum].setContents(player);
        if(playerCount == 0)
        {
            //The instance in which no starting point is provided.
            playerRow = 0;
            playerColum = 0;
            player = new Player();
            logger.info(() -> "Starting point not stated. Default starting point of "+playerRow+","+playerColum+" used.");
            arr2[playerRow][playerColum].setContents(player);
        }
        if(endCount == 0)
        {
            //The instance in which no end point is provided.
            endRow = rows-1;
            endColum = colums-1;
            arr2[endRow][endColum].setContents(new End());
            logger.info("No Ending point stated in the input file.");
        }
        boolean end = false;
        List<Cell> endpoint = arr2[playerRow][playerColum].getContents();
        for(Cell c2 : endpoint)
        {
            if(c2 instanceof End)
            {
                end = true;
            }
            if(c2 instanceof Messages)
            {
                Messages mess = (Messages)c2;
                m.add(mess);
            }
        }
        makeMaze();
        while (!end) 
        {
            try
            {
                end = move();
            }
            catch(MyMazeException e)
            {
                Messages mes = new Messages();
                mes.setMessage(e.getMessage());
                m.add(mes);
                makeMaze();
            }
        }
    }

    /**************************************************************************
     * Creates a new String 2D array mapping the 2D object array created by   *
     * the readFile() method and this String array is created anew to depict  *
     * the changes made with every move made by the player                    *
     **************************************************************************/
    public void makeMaze() throws MyMazeException
    {
        displayRows = rows + 2 + rows-1;
        displayColums = colums*3 + 2 + colums-1;
        logger.info(() -> "Initiazile 2D String array of size "+displayRows+" x "+displayColums);
        arr = new String[displayRows][displayColums];
        
        for(int i = 0; i < displayRows; i++)
        {
            for(int j = 0; j < displayColums; j++)
            {
                arr[i][j] = null;
                arr[0][j] = new HorizontalWall().toString();
                arr[i][0] = new VerticalWall().toString();
                arr[displayRows-1][j] = new HorizontalWall().toString();
                arr[i][displayColums-1] = new VerticalWall().toString();
            }
        }

        arr[0][0] = "\u250c";
        arr[0][displayColums-1] = "\u2510";
        arr[displayRows-1][0] = "\u2514";
        arr[displayRows-1][displayColums-1] = "\u2518";

        for(int i = 0; i < rows; i++)
        {
            for(int j = 0; j < colums; j++)
            {
                List<Cell> contents = arr2[i][j].getContents();
                for(Cell c : contents)
                {
                    if(c instanceof VerticalWall)
                    {
                        arr[i+i+1][j*3+(j-1)+1] = c.toString();
                    }
                    if(c instanceof HorizontalWall)
                    {
                        for(int k = 0; k < 3; k++)
                        {
                            arr[i*2][j*3+j+1+k] = c.toString();
                        }
                    }
                    if(c instanceof Player)
                    {
                        arr[i+i+1][j*3+j+2] = c.toString();
                    }
                    // if(c instanceof End)
                    // {
                    //     arr[i+i+1][j*3+j+2] = c.toString();
                    // }
                    if(c instanceof ColorRed)
                    {
                        Cell c2 = simplify(c);
                        if(c2 instanceof Key)
                        {
                            arr[i+i+1][j*3+j+2] = c.toString();
                        }
                        if(c2 instanceof VerticalDoor)
                        {
                            arr[i+i+1][j*3+(j-1)+1] = c.toString();
                        }
                        if(c2 instanceof HorizontalDoor)
                        {
                            for(int k = 0; k < 3; k++)
                            {
                                arr[i*2][j*3+j+1+k] = c.toString();
                            }
                        }
                    }
                    if(c instanceof ColorGreen)
                    {
                        Cell c2 = simplify(c);
                        if(c2 instanceof Key)
                        {
                            arr[i+i+1][j*3+j+2] = c.toString();
                        }
                        if(c2 instanceof VerticalDoor)
                        {
                            arr[i+i+1][j*3+(j-1)+1] = c.toString();
                        }
                        if(c2 instanceof HorizontalDoor)
                        {
                            for(int k = 0; k < 3; k++)
                            {
                                arr[i*2][j*3+j+1+k] = c.toString();
                            }
                        }
                    }
                    if(c instanceof ColorYellow)
                    {
                        Cell c2 = simplify(c);
                        if(c2 instanceof Key)
                        {
                            arr[i+i+1][j*3+j+2] = c.toString();
                        }
                        if(c2 instanceof VerticalDoor)
                        {
                            arr[i+i+1][j*3+(j-1)+1] = c.toString();
                        }
                        if(c2 instanceof HorizontalDoor)
                        {
                            for(int k = 0; k < 3; k++)
                            {
                                arr[i*2][j*3+j+1+k] = c.toString();
                            }
                        }
                    }
                    if(c instanceof ColorBlue)
                    {
                        Cell c2 = simplify(c);
                        if(c2 instanceof Key)
                        {
                            arr[i+i+1][j*3+j+2] = c.toString();
                        }
                        if(c2 instanceof VerticalDoor)
                        {
                            arr[i+i+1][j*3+(j-1)+1] = c.toString();
                        }
                        if(c2 instanceof HorizontalDoor)
                        {
                            for(int k = 0; k < 3; k++)
                            {
                                arr[i*2][j*3+j+1+k] = c.toString();
                            }
                        }
                    }
                    if(c instanceof ColorMagenta)
                    {
                        Cell c2 = simplify(c);
                        if(c2 instanceof Key)
                        {
                            arr[i+i+1][j*3+j+2] = c.toString();
                        }
                        if(c2 instanceof VerticalDoor)
                        {
                            arr[i+i+1][j*3+(j-1)+1] = c.toString();
                        }
                        if(c2 instanceof HorizontalDoor)
                        {
                            for(int k = 0; k < 3; k++)
                            {
                                arr[i*2][j*3+j+1+k] = c.toString();
                            }
                        }
                    }
                    if(c instanceof ColorCyan)
                    {
                        Cell c2 = simplify(c);
                        if(c2 instanceof Key)
                        {
                            arr[i+i+1][j*3+j+2] = c.toString();
                        }
                        if(c2 instanceof VerticalDoor)
                        {
                            arr[i+i+1][j*3+(j-1)+1] = c.toString();
                        }
                        if(c2 instanceof HorizontalDoor)
                        {
                            for(int k = 0; k < 3; k++)
                            {
                                arr[i*2][j*3+j+1+k] = c.toString();
                            }
                        }
                    }
                    if(c instanceof End)
                    {
                        arr[i+i+1][j*3+j+2] = c.toString();
                    }
                }
            }
        }
        logger.info("Finished mapping the objects to the String 2D array");
        printConnectors();
        printMaze();
    }
    
    /**********************************************************************
     * This method updates the 2D array created by the readFile() method  *
     * with every move made by the user and prints the updated map        *
     * using the printMaze() method.                                      *
     **********************************************************************/
    public boolean move() throws MyMazeException
    {
        boolean end = false;
        List<Cell> toRemove = new ArrayList<>();
        if((playerRow == endRow)&&(playerColum == endColum))
        {
            end = true;
            return end;
        }
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        if(input.equals("s"))
        {
            logger.info("Player moves down");
            playerRow = playerRow + 1;
            if(playerRow >= rows)
            {
                logger.info("Tried to move outside of maze.");
                playerRow = playerRow - 1;
                throw new MyMazeException("INVALID MOVE! OUTSIDE OF MAZE");
            }
            List<Cell> cell = arr2[playerRow][playerColum].getContents();
            for(Cell c : cell)
            {
                if(c instanceof HorizontalWall)
                {
                    logger.info("Tried to move into a wall");
                    playerRow = playerRow - 1;
                    throw new MyMazeException("INVALID MOVE!");
                }
                if(c instanceof End)
                {
                    logger.info("Player reached the end.");
                    end = true;
                }
                if(c instanceof Messages)
                {
                    Messages m3 = (Messages)c;
                    m.add(m3);
                }
                try
                {
                    checkHorizontalDoor(c);
                }
                catch(MyMazeException e)
                {
                    playerRow = playerRow - 1;
                    throw new MyMazeException(e.getMessage(), e);
                }
            }
            toRemove = checkKey(cell);
            obtainedKeys = player.getKeys();
            for(Cell c : toRemove)
            {
                arr2[playerRow][playerColum].removeContens(c);
            }
            arr2[playerRow][playerColum].setContents(player);
            arr2[playerRow-1][playerColum].removeContens(player);
        }
        if(input.equals("n"))
        {
            logger.info("Player moves up");
            playerRow = playerRow - 1;
            if(playerRow < 0)
            {
                logger.info("Tried to move outside of maze.");
                playerRow = playerRow + 1;
                throw new MyMazeException("INVALID MOVE! OUTSIDE OF MAZE");
            }
            List<Cell> cell2 = arr2[playerRow+1][playerColum].getContents();
            for(Cell c : cell2)
            {
                if(c instanceof HorizontalWall)
                {
                    logger.info("Tried to move into a wall");
                    playerRow = playerRow + 1;
                    throw new MyMazeException("INVALID MOVE!");
                }
                try
                {
                    checkHorizontalDoor(c);
                }
                catch(MyMazeException e)
                {
                    playerRow = playerRow + 1;
                    throw new MyMazeException(e.getMessage(), e);
                }
            }
            List<Cell> cell = arr2[playerRow][playerColum].getContents();
            for(Cell c : cell)
            {
                if(c instanceof End)
                {
                    logger.info("Player reached the end.");
                    end = true;
                }
                if(c instanceof Messages)
                {
                    Messages m3 = (Messages)c;
                    m.add(m3);
                }
            }
            toRemove = checkKey(cell);
            obtainedKeys = player.getKeys();
            for(Cell c : toRemove)
            {
                arr2[playerRow][playerColum].removeContens(c);
            }
            arr2[playerRow][playerColum].setContents(player);
            arr2[playerRow+1][playerColum].removeContens(player);
        }
        if(input.equals("e"))
        {
            logger.info("Player moves to the right");
            playerColum = playerColum + 1;
            if(playerColum >= colums)
            {
                logger.info("Tried to move outside of maze.");
                playerColum = playerColum - 1;
                throw new MyMazeException("INVALID MOVE! OUTSIDE OF MAZE");
            }
            List<Cell> cell = arr2[playerRow][playerColum].getContents();
            for(Cell c : cell)
            {
                if(c instanceof VerticalWall)
                {
                    logger.info("Tried to move into the wall");
                    playerColum = playerColum - 1;
                    throw new MyMazeException("INVALID MOVE!");
                }
                if(c instanceof End)
                {
                    logger.info("Player reached the end.");
                    end = true;
                }
                if(c instanceof Messages)
                {
                    Messages m3 = (Messages)c;
                    m.add(m3);
                }
                try
                {
                    checkVerticalDoor(c);
                }
                catch(MyMazeException e)
                {
                    playerColum = playerColum - 1;
                    throw new MyMazeException(e.getMessage(), e);
                }
            }
            toRemove = checkKey(cell);
            obtainedKeys = player.getKeys();
            for(Cell c : toRemove)
            {
                arr2[playerRow][playerColum].removeContens(c);
            }
            arr2[playerRow][playerColum].setContents(player);
            arr2[playerRow][playerColum-1].removeContens(player);
        }
        if(input.equals("w"))
        {
            logger.info("Player moves to the left");
            playerColum = playerColum - 1;
            if(playerColum < 0)
            {
                logger.info("Tried to move outside of maze.");
                playerColum = playerColum + 1;
                throw new MyMazeException("INVALID MOVE! OUTSIDE OF MAZE");
            }
            List<Cell> cell2 = arr2[playerRow][playerColum+1].getContents();
            for(Cell c : cell2)
            {
                if(c instanceof VerticalWall)
                {
                    logger.info("Tried to move into the wall");
                    playerColum = playerColum + 1;
                    throw new MyMazeException("INVALID MOVE!");
                }
                try
                {
                    checkVerticalDoor(c);
                }
                catch(MyMazeException e)
                {
                    playerColum = playerColum + 1;
                    throw new MyMazeException(e.getMessage(), e);
                }
            }
            List<Cell> cell = arr2[playerRow][playerColum].getContents();
            for(Cell c : cell)
            {
                if(c instanceof End)
                {
                    logger.info("Player reached the end.");
                    end = true;
                }
                if(c instanceof Messages)
                {
                    Messages m3 = (Messages)c;
                    m.add(m3);
                }
            }
            toRemove = checkKey(cell);
            obtainedKeys = player.getKeys();
            for(Cell c : toRemove)
            {
                arr2[playerRow][playerColum].removeContens(c);
            }
            arr2[playerRow][playerColum].setContents(player);
            arr2[playerRow][playerColum+1].removeContens(player);
        }
        makeMaze();
        
        //Source : https://coderanch.com/t/633613/java/Java-Closing-Scanner-Resource-Leak
        //Source : https://stackoverflow.com/questions/17945683/is-it-necessary-to-close-scanner-when-there-is-no-stream-underlying#:~:text=Even%20though%20a%20scanner%20is,that's%20tied%20to%20System.in!
        //Apparently you are not supposed to close Scanner tied to System.in because
        //the System.in object is opened by the JVM and should be left to the JVM to close it.
        //I didn't follow this advice as this is only a public forum and not results presented through research. So I tried to close the scanner.
        //But whenever I tried to close the Scanner my program executes, but crashes in the middle with NoSuchElementException and I don't really
        //know why this happens. So instead i commented the close command and suppressed the warning issued by PMD.

        //sc.close();
        return end;
    }

    /*****************************************************************************
     * This method prints the 2D String array created by the makeMaze() method.  *
     *****************************************************************************/
    public void printMaze()
    {
        System.out.println("\033[2J");
        System.out.println("\033[H");
        if(playerCount == 0)
        {
            //The warning issued when no starting point is given.
            System.out.println("No Starting point has been specified.");
            System.out.println("Default starting point of 0,0 used!");
            playerCount = playerCount + 1;
        }
        if(endCount == 0)
        {
            //The warning issued when no ending point is given.
            System.out.println("No end line on input file!");
            System.out.println("defualt ending point used.");
            endCount = endCount + 1;
        }
        for(int i = 0; i < displayRows; i++)
        {
            for(int j = 0; j < displayColums; j++)
            {
                if(arr[i][j] == null)
                {
                    System.out.print(" ");
                }
                else
                {
                    System.out.print(arr[i][j]);
                }
                if(j == displayColums-1)
                {
                    System.out.print("\n");
                }
            }
        }
        System.out.println(obtainedKeys);
        for(Messages mes : m)
        {
            if(!(mes.getMessage()==null))
            {
                System.out.println(mes.getMessage());
            }
        }
        m.clear();
    }

    /************************************************************************************************
     * This method is used to obtain the original type of the object before it was decorated        *
     * This method receives an object of type Cell and casts it to type MazeDecorator and this      *
     * Mazedecorator object is used to call method that returns the original type of the object     *
     * before it was decorated with a colour.                                                       *
     ************************************************************************************************/
    public Cell simplify(Cell c)
    {
        MazeDecorator m = (MazeDecorator) c;
        Cell c2 = m.getNext();
        logger.info(() -> "Obtained the original object type through casting "+c.getClass().getSimpleName()+" to "+c2.getClass().getSimpleName());
        return c2;
    }

    /********************************************************************************************************
     * This method obtains a List of type Cell objects, iterate through that list, find the keys obtained   *
     * by the player and store them on another list to be removed.                                          *
     ********************************************************************************************************/
    public List<Cell> checkKey(List<Cell> cell2)
    {
        List<Cell> toRemove = new ArrayList<>();
        for(Cell c : cell2)
        {
            if(c instanceof ColorRed)
            {
                Cell c2 = simplify(c);
                if(c2 instanceof Key)
                {
                    logger.info("Player obtained a red key");
                    player.addRedKey();
                    player.setKeys(c);
                    toRemove.add(c);
                }
            }
            if(c instanceof ColorGreen)
            {
                Cell c2 = simplify(c);
                if(c2 instanceof Key)
                {
                    logger.info("Player obtained a green key");
                    player.addGreenKey();
                    player.setKeys(c);
                    toRemove.add(c);
                }
            }
            if(c instanceof ColorYellow)
            {
                Cell c2 = simplify(c);
                if(c2 instanceof Key)
                {
                    logger.info("Player obtained a yellow key");
                    player.addYellowKey();
                    player.setKeys(c);
                    toRemove.add(c);
                }
            }
            if(c instanceof ColorBlue)
            {
                Cell c2 = simplify(c);
                if(c2 instanceof Key)
                {
                    logger.info("Player obtained a blue key");
                    player.addBlueKey();
                    player.setKeys(c);
                    toRemove.add(c);
                }
            }
            if(c instanceof ColorMagenta)
            {
                Cell c2 = simplify(c);
                if(c2 instanceof Key)
                {
                    logger.info("Player obtained a magenta key");
                    player.addMagentaKey();
                    player.setKeys(c);
                    toRemove.add(c);
                }
            }
            if(c instanceof ColorCyan)
            {
                Cell c2 = simplify(c);
                if(c2 instanceof Key)
                {
                    logger.info("Player obtained a cyan key");
                    player.addCyanKey();
                    player.setKeys(c);
                    toRemove.add(c);
                }
            }
        }
        return toRemove;
    }

    /************************************************************************************
     * This method checks the colour of the Horizontal door and opens the door if the   *
     * palyer has the key to open a door of that specific colour.                       *
     ************************************************************************************/
    public void checkHorizontalDoor(Cell c) throws MyMazeException
    {
        if(c instanceof ColorRed)
        {
            Cell c2 = simplify(c);
            if(c2 instanceof HorizontalDoor)
            {
                HorizontalDoor d = (HorizontalDoor)c2;
                if(!d.hasOpened())
                {
                    if(player.hasRedKey())
                    {
                        logger.info("Player opened a red door");
                        d.openedState();
                    }
                    else
                    {
                        logger.info("Player tried to open a red door without a red key");
                        throw new MyMazeException("Invalid Move! Red Key Not Found");
                    }
                }
            }
        }
        if(c instanceof ColorGreen)
        {
            Cell c2 = simplify(c);
            if(c2 instanceof HorizontalDoor)
            {
                HorizontalDoor d = (HorizontalDoor)c2;
                if(!d.hasOpened())
                {
                    if(player.hasGreenKey())
                    {
                        logger.info("Player opened a green door");
                        d.openedState();
                    }
                    else
                    {
                        logger.info("Player tried to open a green door without a green key");
                        throw new MyMazeException("Invalid Move! Green Key Not Found");
                    }
                }
            }
        }
        if(c instanceof ColorYellow)
        {
            Cell c2 = simplify(c);
            if(c2 instanceof HorizontalDoor)
            {
                HorizontalDoor d = (HorizontalDoor)c2;
                if(!d.hasOpened())
                {
                    if(player.hasYellowKey())
                    {
                        logger.info("Player opened a yellow door");
                        d.openedState();
                    }
                    else
                    {
                        logger.info("Player tried to open a yellow door without a yellow key");
                        throw new MyMazeException("Invalid Move! Yellow Key Not Found");
                    }
                }
            }
        }
        if(c instanceof ColorBlue)
        {
            Cell c2 = simplify(c);
            if(c2 instanceof HorizontalDoor)
            {
                HorizontalDoor d = (HorizontalDoor)c2;
                if(!d.hasOpened())
                {
                    if(player.hasBlueKey())
                    {
                        logger.info("Player opened a blue door");
                        d.openedState();
                    }
                    else
                    {
                        logger.info("Player tried to open a blue door wihtout a blue key");
                        throw new MyMazeException("Invalid Move! Blue Key Not Found");
                    }
                }
            }
        }
        if(c instanceof ColorMagenta)
        {
            Cell c2 = simplify(c);
            if(c2 instanceof HorizontalDoor)
            {
                HorizontalDoor d = (HorizontalDoor)c2;
                if(!d.hasOpened())
                {
                    if(player.hasMagentaKey())
                    {
                        logger.info("Player opened a magenta door");
                        d.openedState();
                    }
                    else
                    {
                        logger.info("Player tried to open a magenta door without a magenta key");
                        throw new MyMazeException("Invalid Move! Magenta Key Not Found");
                    }
                }
            }
        }
        if(c instanceof ColorCyan)
        {
            Cell c2 = simplify(c);
            if(c2 instanceof HorizontalDoor)
            {
                HorizontalDoor d = (HorizontalDoor)c2;
                if(!d.hasOpened())
                {
                    if(player.hasCyanKey())
                    {
                        logger.info("Player opened a cyan door");
                        d.openedState();
                    }
                    else
                    {
                        logger.info("Player tried to open a cyan door wihtout a cyan key");
                        throw new MyMazeException("Invalid Move! Cyan Key Not Found");
                    }
                }
            }
        }
    }

    /************************************************************************************
     * This method checks the colour of the Vertical door and opens the door if the     *
     * palyer has the key to open a door of that specific colour.                       *
     ************************************************************************************/
    public void checkVerticalDoor(Cell c) throws MyMazeException
    {
        if(c instanceof ColorRed)
        {
            Cell c2 = simplify(c);
            if(c2 instanceof VerticalDoor)
            {
                VerticalDoor d = (VerticalDoor)c2;
                if(!d.hasOpened())
                {
                    if(player.hasRedKey())
                    {
                        logger.info("Player opened a red door");
                        d.openedState();
                    }
                    else
                    {
                        logger.info("Player tried to open a red door without a red key");
                        throw new MyMazeException("Invalid Move! Red Key Not Found");
                    }
                }
            }
        }
        if(c instanceof ColorGreen)
        {
            Cell c2 = simplify(c);
            if(c2 instanceof VerticalDoor)
            {
                VerticalDoor d = (VerticalDoor)c2;
                if(!d.hasOpened())
                {
                    if(player.hasGreenKey())
                    {
                        logger.info("Player opened a green door");
                        d.openedState();
                    }
                    else
                    {
                        logger.info("Player tried to open a green door without a green key");
                        throw new MyMazeException("Invalid Move! Green Key Not Found");
                    }
                }
            }
        }
        if(c instanceof ColorYellow)
        {
            Cell c2 = simplify(c);
            if(c2 instanceof VerticalDoor)
            {
                VerticalDoor d = (VerticalDoor)c2;
                if(!d.hasOpened())
                {
                    if(player.hasYellowKey())
                    {
                        logger.info("Player opened a yellow door");
                        d.openedState();
                    }
                    else
                    {
                        logger.info("Player tried to open a yellow door without a yellow key");
                        throw new MyMazeException("Invalid Move! Yellow Key Not Found");
                    }
                }
            }
        }
        if(c instanceof ColorBlue)
        {
            Cell c2 = simplify(c);
            if(c2 instanceof VerticalDoor)
            {
                VerticalDoor d = (VerticalDoor)c2;
                if(!d.hasOpened())
                {
                    if(player.hasBlueKey())
                    {
                        logger.info("Player opened a blue door");
                        d.openedState();
                    }
                    else
                    {
                        logger.info("Player tried to open a blue door without a blue key");
                        throw new MyMazeException("Invalid Move! Blue Key Not Found");
                    }
                }
            }
        }
        if(c instanceof ColorMagenta)
        {
            Cell c2 = simplify(c);
            if(c2 instanceof VerticalDoor)
            {
                VerticalDoor d = (VerticalDoor)c2;
                if(!d.hasOpened())
                {
                    if(player.hasMagentaKey())
                    {
                        logger.info("Player opened a magenta door");
                        d.openedState();
                    }
                    else
                    {
                        logger.info("Player tried to open a magenta door without a magenta key");
                        throw new MyMazeException("Invalid Move! Magenta Key Not Found");
                    }
                }
            }
        }
        if(c instanceof ColorCyan)
        {
            Cell c2 = simplify(c);
            if(c2 instanceof VerticalDoor)
            {
                VerticalDoor d = (VerticalDoor)c2;
                if(!d.hasOpened())
                {
                    if(player.hasCyanKey())
                    {
                        logger.info("Player opened a cyan door");
                        d.openedState();
                    }
                    else
                    {
                        logger.info("Player tried to open a cyan door without a cyan key");
                        throw new MyMazeException("Invalid Move! Cyan Key Not Found");
                    }
                }
            }
        }
    }

    public void printConnectors()
    {
        for(int i = 1; i < displayRows-1; i++)
        {
            for(int j = 1; j < displayColums-1; j++)
            {
                if(arr[i][j] == "\u2502")
                {
                    if(arr[i-1][j] == "\u2500")
                    {
                        arr[i-1][j] = "\u252c";
                    }
                    else
                    {
                        if(arr[i-1][j-1] == "\u2500")
                        {
                            arr[i-1][j] = "\u2510";
                        }
                        if(arr[i-1][j+1] == "\u2500")
                        {
                            arr[i-1][j] = "\u250c";
                        }
                        if((arr[i-1][j-1] != null)&&(arr[i-1][j+1] != null))
                        {
                            arr[i-1][j] = "\u252c";
                        }
                    }
                    if(arr[i+1][j] == "\u2500")
                    {
                        arr[i+1][j] = "\u2534";
                    }
                    else
                    {
                        if(arr[i+1][j-1] == "\u2500")
                        {
                            arr[i+1][j] = "\u2518";
                        }
                        if(arr[i+1][j+1] == "\u2500")
                        {
                            arr[i+1][j] = "\u2514";
                        }
                        if((arr[i+1][j-1] != null)&&(arr[i+1][j+1] != null))
                        {
                            arr[i+1][j] = "\u2534";
                        }
                    }
                    if(arr[i][j-1] == "\u2500")
                    {
                        arr[i][j] = "\u2524";
                    }
                    else
                    {
                        if(arr[i][j+1] == "\u2500")
                        {
                            arr[i][j+1] = "\u251c";
                        }
                    }
                    if((i-2 > 0)&&(arr[i-2][j] != null))
                    {
                        arr[i-1][j] = "\u2502";
                    }
                    else
                    {
                        if((i+2 < displayRows)&&(arr[i+2][j] != null))
                        {
                            arr[i+1][j] = "\u2502";
                        }
                    }
                }
            }
        }
    }
}
