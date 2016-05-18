package mazeSolver;

import maze.Cell;
import maze.Maze;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Implements the recursive backtracking maze solving algorithm.
 */
public class RecursiveBacktrackerSolver implements MazeSolver
{

    private boolean visited[][];
    private Maze maze;
    private boolean solved = false;
    private int cellsExplored;

    @Override
    public void solveMaze(Maze maze)
    {
        visited = new boolean[maze.sizeR][maze.sizeC];
        this.maze = maze;
        cellsExplored = 0;
        recursiveBackTracker(maze.entrance);

    } // end of solveMaze()


    @Override
    public boolean isSolved()
    {
        return solved;
    } // end if isSolved()


    @Override
    public int cellsExplored()
    {
        // TODO Auto-generated method stub
        return cellsExplored;
    } // end of cellsExplored()

    /**
     * Function recursively generates a maze using the DFS approach
     *
     * @param currentCell: The cell that is to be processed.
     */
    private void recursiveBackTracker(Cell currentCell)
    {
        setVisited(currentCell.r, currentCell.c);
        cellsExplored++;
        maze.drawFtPrt(currentCell);
//        try
//        {
//            Thread.sleep(70);
//        }
//        catch (InterruptedException e)
//        {
//            e.printStackTrace();
//        }
        if (currentCell == maze.exit)
        {
            this.solved = true;
            return;
        }
        if (maze.type == Maze.TUNNEL)
        {
            Cell nextCell = currentCell.tunnelTo;
            if (nextCell != null && !isVisited(nextCell.r, nextCell.c))
                recursiveBackTracker(nextCell);
        }

        ArrayList<Integer> directions = getDirections();

        while (!solved && !directions.isEmpty())
        {
            int index = new Random().nextInt(directions.size());
            int visitingDirection = directions.get(index);
            directions.remove(index);

            if (!currentCell.wall[visitingDirection].present)
            {
                Cell nextCell = currentCell.neigh[visitingDirection];

                if (nextCell != null && !isVisited(nextCell.r, nextCell.c))
                {
                    recursiveBackTracker(nextCell);
                }
            }
        }
    }

    /**
     * Generates a list of possible move directions based on the type of maze.
     *
     * @return : ArrayList of possible move directions.
     */
    private ArrayList<Integer> getDirections()
    {
        ArrayList<Integer> directions;

        if (maze.type == Maze.HEX)
        {
            directions = new ArrayList<>(
                    Arrays.asList(Maze.EAST, Maze.NORTHEAST, Maze.NORTHWEST,
                            Maze.WEST, Maze.SOUTHWEST, Maze.SOUTHEAST));
        }
        else
        {
            directions = new ArrayList<>(
                    Arrays.asList(Maze.EAST, Maze.NORTH, Maze.WEST,
                            Maze.SOUTH));
        }

        return directions;
    }

    /**
     * Map cell position to a 2D array based on the type of maze.
     *
     * @param row:    Row position of the cell
     * @param column: Column position of the cell
     * @return : Column position on the 2D array.
     */
    private int columnValue(int row, int column)
    {
        if (maze.type == Maze.HEX)
            column -= ((row + 1) / 2);
        return column;
    }

    /**
     * Set the visited status of a cell.
     *
     * @param row:    Row position of the cell
     * @param column: Column position of the cell
     */
    private void setVisited(int row, int column)
    {
        visited[row][columnValue(row, column)] = true;
    }

    /**
     * Get the visited status of a cell
     *
     * @param row:    Row position of the cell
     * @param column: Column position of the cell
     * @return : Visited status of Cell
     */
    private boolean isVisited(int row, int column)
    {
        return visited[row][columnValue(row, column)];
    }
} // end of class RecursiveBackTrackerSolver
