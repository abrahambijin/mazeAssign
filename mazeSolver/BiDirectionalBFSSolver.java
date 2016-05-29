package mazeSolver;

import maze.Cell;
import maze.Maze;

import java.util.*;

/**
 * Implements Bi-directional BFS maze solving algorithm.
 */
public class BiDirectionalBFSSolver implements MazeSolver
{

    private boolean visited[][];
    private int type;
    private int cellsExplored;
    private boolean solved = false;

	/**
	 *	1. Declare two queue's for entrance and exit
     *  2. Add maze entrance to the entrance queue and maze exit cell to the exit queue
     *  3. remove the cells(from entrance and exit queue ) and mark them as visited
     *  4. Find the unvisited cells(possible move options where wall does not exists) wrt to the cells
     *     removed from the queue and add them to the respective queues
     *  5. Repeat step 3 until maze is solved
     *  6. Combine these paths to get the final path solution.
	 */

    @Override
    public void solveMaze(Maze maze)
    {
        visited = new boolean[maze.sizeR][maze.sizeC];
        this.type = maze.type;
        cellsExplored = 0;
        bfsSolver(maze);
    } // end of solveMaze()

    /**
     * This solver performs BFS searches starting at both the entrance and exits.
     * When the two BFS fronts first meet, the path from the entrance to the
     * point they meet, and the path from the exit to the
     * meeting point forms the two halves of a shortest path (in terms of
     * cell visited) from entrance to exit.
     * Combine these paths to get the final path solution.
     *
     * @param maze: The 2D Maze which has to be solved
     */
    private void bfsSolver(Maze maze)
    {

        Queue<Cell> entranceQueue = new LinkedList<>();
        Queue<Cell> exitQueue = new LinkedList<>();

        entranceQueue.add(maze.entrance);
        exitQueue.add(maze.exit);

        do
        {
            Cell entranceCell = entranceQueue.poll();
            if (entranceCell != null)
            {
                if (!isVisited(entranceCell.r, entranceCell.c))
                {
                    markAsVisited(maze, entranceCell);
                    entranceQueue.addAll(findPossibleMoveOptions(entranceCell));
                }
                else
                    solved = true;
            }

            Cell exitCell = exitQueue.poll();
            if (!solved && exitCell != null)
            {
                if (!isVisited(exitCell.r, exitCell.c))
                {
                    markAsVisited(maze, exitCell);
                    exitQueue.addAll(findPossibleMoveOptions(exitCell));
                }
                else
                    solved = true;
            }

        } while (!solved); // continue the loop as long as maze is not solved

    }

    /**
     * Marking the cell as visited
     *
     * @param maze: The maze which has to be solved
     * @param cell: The cell which has to be marked as visited
     */
    private void markAsVisited(Maze maze, Cell cell)
    {
        setVisited(cell.r, cell.c);
        maze.drawFtPrt(cell);
        cellsExplored++;
    }

    /**
     * Finding all the cells that can be visited wrt to a particular cell
     *
     * @param currentCell
     * @return List of cells
     */
    private ArrayList<Cell> findPossibleMoveOptions(Cell currentCell)
    {

        ArrayList<Cell> possibleCells = new ArrayList<>();

        if (type == Maze.TUNNEL && currentCell.tunnelTo != null)
        {
            Cell tunneledCell = currentCell.tunnelTo;
            if (!isVisited(tunneledCell.r, tunneledCell.c))
                possibleCells.add(currentCell.tunnelTo);
        }
        for (int index : getDirections())
        {
            if (!currentCell.wall[index].present)
            {
                Cell nextCell = currentCell.neigh[index];
                if (nextCell != null && !isVisited(nextCell.r, nextCell.c))
                {
                    possibleCells.add(nextCell);
                }
            }
        }
        return possibleCells;
    }

    /**
     * Finding the directions
     *
     * @return List containing all possible direction
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


    @Override
    public boolean isSolved()
    {
        return solved;
    } // end of isSolved()


    @Override
    public int cellsExplored()
    {
        return cellsExplored;
    } // end of cellsExplored()

    private int columnValue(int row, int column)
    {
        if (type == Maze.HEX)
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

    private boolean isVisited(int row, int column)
    {
        return visited[row][columnValue(row, column)];
    }

} // end of class BiDirectionalBFSSolver

