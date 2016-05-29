package mazeGenerator;

import maze.*;

import java.util.*;

/**
 * Generate Maze using Kruskals Algorithm
 */
public class KruskalGenerator implements MazeGenerator
{
    private HashSet<Edge> edgeSet;
    private HashSet<TreeSet<Cell>> treeSet;
    private int type;
    private boolean[][] marked;


    /**
     * Maze generation algorithm:
     * ************************************************************************
     * 1. Initialise a set of edges and a set of Trees of Cells
     * 2. Randomly select an edge and remove it from the set.
     * 3. If the selected edge joins two disjoint trees:
     *      3.1. Join them and carve a path between the corresponding cells
     * 4. else
     *      4.1. Discard the edge
     * 5. Repeat from step 2 until the set of edges is empty.
     * ************************************************************************
     *
     * @param maze The reference of Maze object to generate.
     */
    @Override
    public void generateMaze(Maze maze)
    {
        initialize(maze);

        while (!edgeSet.isEmpty())
        {
            Edge edge = randomEdge();

            TreeSet<Cell> set1 = null;
            TreeSet<Cell> set2 = null;

            for (TreeSet<Cell> cells : treeSet)
            {
                if (cells.contains(edge.cell1))
                    set1 = cells;

                if (cells.contains(edge.cell2))
                    set2 = cells;

                if (set1 != null && set2 != null)
                    break;
            }

            if (set1 != null && set2 != null && !set1.equals(set2))
            {
                treeSet.remove(set1);
                treeSet.remove(set2);
                set1.addAll(set2);
                treeSet.add(set1);

                edge.cell1.wall[edge.direction].present = false;
                edge.cell2.wall[Maze.oppoDir[edge.direction]].present = false;
            }
        }
    } // end of generateMaze()

    /**
     * Initialisation:
     *
     * 1. For each pair of adjacent cells, create an edge and store them in
     *      an edge set.
     * 2. Generate a set of trees that contains cells. Initially, there will
     *      be a tree for each cell unless it is a tunnel maze, in which case, the
     *      two ends of the tunnel will belong to the same tree.
     *
     * @param maze The reference of Maze object to generate.
     */
    private void initialize(Maze maze)
    {
        edgeSet = new HashSet<>();
        treeSet = new HashSet<>();
        type = maze.type;
        marked = new boolean[maze.sizeR][maze.sizeC];

        ArrayList<Integer> directions = getDirections();

        for (int i = 0; i < maze.sizeR; i++)
        {
            for (int j = hexMapping(i); j < maze.sizeC + hexMapping(i); j++)
            {
                Cell cell1 = maze.map[i][j];
                mark(cell1);

                for (int direction : directions)
                {
                    Cell cell2 = cell1.neigh[direction];
                    if (cell2 != null && !isMarked(cell2))
                    {
                        Edge edge = new Edge(cell1, cell2, direction);
                        edgeSet.add(edge);
                    }
                }

                boolean flag = false;
                if (type == Maze.TUNNEL && cell1.tunnelTo != null)
                {
                    for (TreeSet<Cell> cells : treeSet)
                        if (cells.contains(cell1.tunnelTo))
                        {
                            treeSet.remove(cells);
                            cells.add(cell1);
                            treeSet.add(cells);
                            flag = true;
                            break;
                        }
                }
                if (!flag)
                {
                    TreeSet<Cell> cellList = new TreeSet<>(new cellCompare());
                    cellList.add(cell1);
                    treeSet.add(cellList);
                }
            }
        }
    }

    /**
     * Selects a random edge from the edge set and removes it from the set
     * before returning it
     * @return edge: The random edge that was selected.
     */
    private Edge randomEdge()
    {
        int index = new Random().nextInt(edgeSet.size());
        Iterator<Edge> iterator = edgeSet.iterator();
        for (int i = 0; i < index; i++)
        {
            iterator.next();
        }
        Edge edge = iterator.next();
        iterator.remove();
        return edge;
    }

    /**
     * Class to model the Edge that is crated between two cells. Holds the
     * two cells and the direction of the second cell with respect to the first.
     */
    private class Edge
    {
        Cell cell1;
        Cell cell2;
        int direction;

        public Edge(Cell cell1, Cell cell2, int direction)
        {
            this.cell1 = cell1;
            this.cell2 = cell2;
            this.direction = direction;
        }

        /**
         * Funtion to compare the equality of two edges.
         *
         * @param edge: THe edge to compare the current edge with.
         * @return boolean: True if they are equal else false.
         */
        private boolean equals(Edge edge)
        {
            boolean pass1 =
                    (KruskalGenerator.this.equals(this.cell1, edge.cell1) &&
                            KruskalGenerator.this
                                    .equals(this.cell2, edge.cell2));

            boolean pass2 =
                    (KruskalGenerator.this.equals(this.cell1, edge.cell2) &&
                            KruskalGenerator.this
                                    .equals(this.cell2, edge.cell1));

            return (pass1 || pass2);
        }

        @Override
        public boolean equals(Object o)
        {
            return o != null && o instanceof Edge && equals((Edge) o);
        }

    }

    /**
     * Class that acts as a comparator for the tree set to compare two cells
     */
    private class cellCompare implements Comparator<Cell>
    {
        @Override
        public int compare(Cell cell1, Cell cell2)
        {
            if (KruskalGenerator.this.equals(cell1, cell2))
                return 0;
            else if (cell1.r == cell2.r)
                return cell1.c < cell2.c ? 1 : -1;
            else
                return cell1.r < cell2.r ? 1 : -1;
        }
    }

    /**
     * Funtion to check if two cells are equal.
     *
     * @param cell1: First Cell
     * @param cell2: Second Cell
     * @return boolean True if they are the same else False
     */
    private boolean equals(Cell cell1, Cell cell2)
    {
        return ((cell1.r == cell2.r) && (cell1.c == cell2.c));
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
    private void mark(Cell cell)
    {
        marked[cell.r][cell.c - hexMapping(cell.r)] = true;
    }

    /**
     * Get the marked status of a cell
     *
     * @param cell: Cell to check the marked status
     * @return : Visited status of Cell
     */
    private boolean isMarked(Cell cell)
    {
        return marked[cell.r][cell.c - hexMapping(cell.r)];
    }
} // end of class KruskalGenerator
