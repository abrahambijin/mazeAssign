package mazeGenerator;

import maze.Cell;
import maze.Maze;

import java.util.*;

// Frontier definition :  set of all cells that are not yet in the maze,
// but are adjacent to a cell that is in the maze.


public class ModifiedPrimsGenerator implements MazeGenerator {

	private boolean visited [][];
	private int type;

	/**
	 * Modified Prim's Algorithmn for generating the Maze
	 *
	 * ******************************************************************************************
	 *
	 * ALGORITHM Modified Prim's
	 *
	 *
	 * 1. Start by adding the maze entrance to the collection of visited cells
	 * 2. Mark the cell as visited
	 * 3. Find all the frontiers with respect to the cell(Frontier definition provided above)
	 * 4. Randomly select a frontier and carve a path
	 * 5. Mark the frontier as visited
	 * 6. Shuffle the collection of visited cell to randomize the collection
	 * 7. Iterate over the collection(shuffled collection of visited cell) and
	 * 	  find a cell which has frontiers
	 * 8. Make this cell, the current cell and continue the loop
	 * 9. The loop exits when there are no un-visited cell or all the cells in the maze have been
	 *    added to the visited cells collection.
	 * ******************************************************************************************
	 * */

	@Override
	public void generateMaze(Maze maze) {
		// TODO Auto-generated method stub

		visited = new boolean [maze.sizeR][maze.sizeC];
		type = maze.type;

        if(type != Maze.TUNNEL)
		    modifiedPrims(maze.entrance);

        else
        {
            System.err.println(
                    "This algorithm can not generate a tunneled " + "maze!!");
            System.exit(0);
        }
	} // end of generateMaze()

	/**
	 *
	 * @param currentCell: The entrance cell of the maze
     */
	private void modifiedPrims(Cell currentCell){

		ArrayList<Cell> visitedCells = new ArrayList<>();
		setVisited(currentCell.r, currentCell.c);
		// marking entry point as visited
		visitedCells.add(currentCell);

		do {
			HashMap<Integer, Cell> frontiers;
			frontiers = findFrontiers(currentCell);
			if(frontiers != null) {
				int randomDirection = getRandomDirectionFromPossibleDirections(frontiers);
				Cell nextCell = frontiers.get(randomDirection);
				destructWall(currentCell, randomDirection, nextCell);

				setVisited(nextCell.r, nextCell.c);
				visitedCells.add(nextCell);

				currentCell = findCellWithFrontiers(visitedCells);
			}
		} while(currentCell != null); // the currentCell will only be null only if all the cells have been visited and added to
		// the visitedCells list
	}

	private int getRandomDirectionFromPossibleDirections(HashMap<Integer, Cell> frontiers) {
		ArrayList<Integer> keys = new ArrayList<>(frontiers.keySet());
		return keys.get(new Random().nextInt(keys.size()));
	}

	/**
	 * Destructing a wall to carve a path for the maze generation
	 * @param currentCell
	 * @param randomKey
	 * @param nextCell
     */
	private void destructWall(Cell currentCell, int randomKey, Cell nextCell) {
		currentCell.wall[randomKey].present = false;
		nextCell.wall[Maze.oppoDir[randomKey]].present = false;
	}

	private int columnValue(int row, int column)
	{
		if (type == Maze.HEX)
			column -= ((row + 1) / 2);
		return column;
	}

	private void setVisited(int row, int column)
	{
		visited[row][columnValue(row, column)] = true;
	}

	/**
	 * select only the neighbouring cells which are not already present in the maze
	 iterate over the possible directions and find the neighbours
	 eliminate the cells which are already been visited and add to the collection
	 * @param currentCell
	 * @return Map of Cells with directions as the key
     */
	private HashMap<Integer, Cell> findFrontiers(Cell currentCell) {

		HashMap<Integer, Cell> frontiers = new HashMap<>();

		for (int index : getDirections()) {
			Cell cell = currentCell.neigh[index];

			if (cell != null && !isVisited(cell.r, cell.c)) frontiers.put(index, cell);
		}

		return frontiers.isEmpty() ? null : frontiers;
	}

	/**
	 * randomize the collection of visited cells and the loop through the collection of visited cells
	 * and exit the loop as soon as we find a
	 * visited cell with frontiers
	 * @param visitedCells
	 * @return	Cell: cell with frontiers
     */
	private Cell findCellWithFrontiers(ArrayList<Cell> visitedCells){

		Collections.shuffle(visitedCells);

		for (Cell currentCell : visitedCells) {
			for (int index : getDirections()) {
				Cell cell = currentCell.neigh[index];

				if (cell != null && !isVisited(cell.r, cell.c)){
					return currentCell;
				}
			}
		}
		return null;
	}
	/**
	 * Finding the directions
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
		Collections.shuffle(directions);
		return directions;
	}

	private boolean isVisited(int row, int column)
	{
		return visited[row][columnValue(row, column)];
	}


} // end of class ModifiedPrimsGenerator
