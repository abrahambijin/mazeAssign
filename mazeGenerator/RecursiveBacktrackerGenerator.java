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

        recursiveBackTracker(maze.entrance, false);

    } // end of generateMaze()

    private void recursiveBackTracker(Cell currentCell, boolean tunneled)
    {
        visited[currentCell.r][columnValue(currentCell.r, currentCell.c)] =
                true;
        if (type == Maze.TUNNEL && !tunneled && currentCell.tunnelTo != null)
        {
            recursiveBackTracker(currentCell.tunnelTo, true);
        }

        ArrayList<Integer> directions = getDirections();

        while (!directions.isEmpty())
        {
            int index = new Random().nextInt(directions.size());
            int visitingDirection = directions.get(index);
            directions.remove(index);

            Cell nextCell = currentCell.neigh[visitingDirection];

            if (nextCell != null)
            {
                if (!visited[nextCell.r][columnValue(nextCell.r, nextCell.c)])
                {
                    currentCell.wall[visitingDirection].present = false;
                    nextCell.wall[Maze.oppoDir[visitingDirection]].present =
                            false;

                    recursiveBackTracker(nextCell, false);
                }
            }

        }

    }

    private ArrayList<Integer> getDirections()
    {
        ArrayList<Integer> directions = new ArrayList<>();

        directions.add(Maze.EAST);
        directions.add(Maze.WEST);
        if (type == Maze.HEX)
        {
            directions.addAll(Arrays
                    .asList(Maze.NORTHEAST, Maze.NORTHWEST, Maze.SOUTHEAST,
                            Maze.SOUTHWEST));
        }
        else
        {
            directions.addAll(Arrays.asList(Maze.NORTH, Maze.SOUTH));
        }

        return directions;
    }

    private int columnValue(int row, int column)
    {
        if (type == Maze.HEX)
            column -= ((row + 1) / 2);
        return column;
    }

} // end of class RecursiveBacktrackerGenerator
