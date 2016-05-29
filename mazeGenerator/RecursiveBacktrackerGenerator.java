package mazeGenerator;

import maze.Cell;
import maze.Maze;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Generate Maze using Recursive Backtracking Algorithm
 */
public class RecursiveBacktrackerGenerator implements MazeGenerator
{

    private boolean visited[][];
    private int type;

    /**
     * Function to call the recursive function with the initial cell
     *
     * @param maze The reference of Maze object to generate.
     */
    @Override
    public void generateMaze(Maze maze)
    {
        visited = new boolean[maze.sizeR][maze.sizeC];
        type = maze.type;

        recursiveBackTracker(maze.entrance);

    } // end of generateMaze()

    /**
     * Algorithm: RBTG(cell)
     * ************************************************************************
     * 1. Set cell to visited.
     * 2. If maze is of type Tunnel and cell has a tunnel and the other end
     * of the tunnel is not visited
     *      2.1. RBTG(cell at other end of the tunnel)
     * 3. Pick a random unvisited neighbouring cell.
     * 4. Carve a path (i.e, remove the wall) to the neighbouring cells.
     * 5. RBTG(neighbouring cell)
     * 6. Repeat from stem 3 until no more unvisited neighbouring cells
     *
     * ************************************************************************
     *
     * @param currentCell: The cell that is to be processed.
     */
    private void recursiveBackTracker(Cell currentCell)
    {
        setVisited(currentCell);
        if (type == Maze.TUNNEL)
        {
            Cell nextCell = currentCell.tunnelTo;
            if (nextCell != null && !isVisited(nextCell))
                recursiveBackTracker(nextCell);
        }

        ArrayList<Integer> directions = getDirections();

        while (!directions.isEmpty())
        {
            int index = new Random().nextInt(directions.size());
            int visitingDirection = directions.get(index);
            directions.remove(index);

            Cell nextCell = currentCell.neigh[visitingDirection];

            if (nextCell != null && !isVisited(nextCell))
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
     * @return : Column position on the 2D array.
     */
    private int hexMapping(int row)
    {
        return (type == Maze.HEX) ? (row + 1) / 2 : 0;
    }

    /**
     * Set the marked status of a cell.
     *
     * @param cell: Cell to set the marked status
     */
    private void setVisited(Cell cell)
    {
        visited[cell.r][cell.c - hexMapping(cell.r)] = true;
    }

    /**
     * Get the marked status of a cell
     *
     * @param cell: Cell to check the marked status
     * @return : Visited status of Cell
     */
    private boolean isVisited(Cell cell)
    {
        return visited[cell.r][cell.c - hexMapping(cell.r)];
    }
} // end of class RecursiveBacktrackerGenerator