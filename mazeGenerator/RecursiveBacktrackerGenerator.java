package mazeGenerator;

import maze.Cell;
import maze.Maze;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class RecursiveBacktrackerGenerator implements MazeGenerator
{

    private boolean visited[][];
    private int type;

    @Override
    public void generateMaze(Maze maze)
    {
        // TODO Auto-generated method stub

        visited = new boolean[maze.sizeR][maze.sizeC];
        type = maze.type;

        recursiveBackTracker(maze.entrance);

    } // end of generateMaze()

    /**
     * Function recursively generates a maze using the DFS approach
     *
     * @param currentCell: The cell that is to be processed.
     */
    private void recursiveBackTracker(Cell currentCell)
    {
        setVisited(currentCell.r, currentCell.c);
        if (type == Maze.TUNNEL)
        {
            Cell nextCell = currentCell.tunnelTo;
            if (nextCell != null && !isVisited(nextCell.r, nextCell.c))
                recursiveBackTracker(nextCell);
        }

        ArrayList<Integer> directions = getDirections();

        while (!directions.isEmpty())
        {
            int index = new Random().nextInt(directions.size());
            int visitingDirection = directions.get(index);
            directions.remove(index);

            Cell nextCell = currentCell.neigh[visitingDirection];

            if (nextCell != null && !isVisited(nextCell.r, nextCell.c))
            {
                currentCell.wall[visitingDirection].present = false;
                nextCell.wall[Maze.oppoDir[visitingDirection]].present = false;

                recursiveBackTracker(nextCell);
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

        if (type == Maze.HEX)
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
     * @param row: Row position of the cell
     * @param column: Column position of the cell
     * @return : Column position on the 2D array.
     */
    private int columnValue(int row, int column)
    {
        if (type == Maze.HEX)
            column -= ((row + 1) / 2);
        return column;
    }

    /**
     * Set the visited status of a cell.
     *
     * @param row: Row position of the cell
     * @param column: Column position of the cell
     */
    private void setVisited(int row, int column)
    {
        visited[row][columnValue(row, column)] = true;
    }

    /**
     * Get the visited status of a cell
     *
     * @param row: Row position of the cell
     * @param column: Column position of the cell
     * @return : Visited status of Cell
     */
    private boolean isVisited(int row, int column)
    {
        return visited[row][columnValue(row, column)];
    }

} // end of class RecursiveBacktrackerGenerator