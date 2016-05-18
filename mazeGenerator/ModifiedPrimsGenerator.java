package mazeGenerator;

import maze.Cell;
import maze.Maze;

import java.util.*;

// Frontier definition :  set of all cells that are not yet in the maze,
// but are adjacent to a cell that is in the maze.

public class ModifiedPrimsGenerator implements MazeGenerator {

	private boolean visited [][];
	private int type;

	@Override
	public void generateMaze(Maze maze) {
		// TODO Auto-generated method stub

		visited = new boolean [maze.sizeR][maze.sizeC];
		type = maze.type;

		modifiedPrims(maze.entrance);
	} // end of generateMaze()

	private void modifiedPrims(Cell currentCell){

		ArrayList<Cell> visitedCells = new ArrayList<>();
		setVisited(currentCell.r, currentCell.c);
		// marking entry point as visited
		visitedCells.add(currentCell);

		while(currentCell != null) {
			// continue as long as visited cells have frontiers
			HashMap<Integer, Cell> frontiers;
			frontiers = findFrontiers(currentCell);

			int randomDirection = getRandomDirectionFromPossibleDirections(frontiers);
			Cell nextCell = frontiers.get(randomDirection);
			constructWall(currentCell, randomDirection, nextCell);

			setVisited(nextCell.r, nextCell.c);
			visitedCells.add(nextCell);

			currentCell = findCellWithFrontiers(visitedCells);
		}

	}

	private int getRandomDirectionFromPossibleDirections(HashMap<Integer, Cell> frontiers) {
		ArrayList<Integer> keys = new ArrayList<>(frontiers.keySet());
		return keys.get(new Random().nextInt(keys.size()));
	}

	private void constructWall(Cell currentCell, int randomKey, Cell nextCell) {
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

	private HashMap<Integer, Cell> findFrontiers(Cell currentCell) {
		//  select only the neighbouring cells which are not already present in the maze
		// iterate over the possible directions and find the neighbours
		// eliminate the cells which are already been visited and added to the collection

		HashMap<Integer, Cell> frontiers = new HashMap<>();

		for (int index : getDirections()) {
			Cell cell = currentCell.neigh[index];

			if (cell != null && !isVisited(cell.r, cell.c)) frontiers.put(index, cell);
		}
		return frontiers;
	}

	private Cell findCellWithFrontiers(ArrayList<Cell> visitedCells){

		Collections.shuffle(visitedCells);
		// randomize the collection of visited cells

		for (Cell currentCell : visitedCells) {
			// the loop will exit as soon as we find a visited cell with frontiers
			for (int index : getDirections()) {
				Cell cell = currentCell.neigh[index];

				if (cell != null && !isVisited(cell.r, cell.c)){
					return currentCell;
				}
			}
		}
		return null;
	}

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

	private boolean isVisited(int row, int column)
	{
		return visited[row][columnValue(row, column)];
	}


} // end of class ModifiedPrimsGenerator
