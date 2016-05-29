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
        setVisited(currentCell);
        cellsExplored++;
        maze.drawFtPrt(currentCell);

        if (currentCell == maze.exit)
        {
            this.solved = true;
            return;
        }

        ArrayList<Integer> directions = getDirections();
        if (maze.type == Maze.TUNNEL)
            directions.add(-1);

        while (!solved && !directions.isEmpty())
        {
            int index = new Random().nextInt(directions.size());
            int visitingDirection = directions.get(index);
            directions.remove(index);

            Cell nextCell = null;
            if (visitingDirection < 0)
                nextCell = currentCell.tunnelTo;

            else if (!currentCell.wall[visitingDirection].present)
                nextCell = currentCell.neigh[visitingDirection];


            if (nextCell != null && !isVisited(nextCell))
                recursiveBackTracker(nextCell);
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
     * @param row: Row position of the cell
     * @return : Column position on the 2D array.
     */
    private int hexMapping(int row)
    {
        return (maze.type == Maze.HEX) ? (row + 1) / 2 : 0;
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
} // end of class RecursiveBackTrackerSolver
