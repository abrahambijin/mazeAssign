package mazeGenerator;

import maze.*;
import java.util.*;

public class KruskalGenerator implements MazeGenerator
{
    private HashSet<Edge> edgeSet;
    private HashSet<TreeSet<Cell>> treeSet;
    private int type;
    private boolean [][] marked;


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
                            cells.add(cell1.tunnelTo);
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

        System.out.println("Edges: " + edgeSet.size());
        System.out.println("Tree Set: " + treeSet.size());

    }

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

    private class cellCompare implements Comparator<Cell>
    {

        @Override
        public int compare(Cell cell1, Cell cell2)
        {
            if (cell1.r == cell2.r && cell1.c == cell2.c)
                return 0;
            else if (cell1.r == cell2.r)
                return cell1.c < cell2.c? 1:-1;
            else
                return cell1.r < cell2.r? 1:-1;
        }
    }

    private boolean equals(Cell c1, Cell c2)
    {
        return ((c1.r == c2.r) && (c1.c == c2.c));
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
     * @param row:    Row position of the cell
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
     * Set the marked status of a cell.
     *
     * @param cell: Cell to set the marked status
     */
    private void mark(Cell cell)
    {
        marked[cell.r][columnValue(cell.r, cell.c)] = true;
    }

    /**
     * Get the marked status of a cell
     *
     * @param cell: Cell to check the marked status
     * @return : Visited status of Cell
     */
    private boolean isMarked(Cell cell)
    {
        return marked[cell.r][columnValue(cell.r, cell.c)];
    }


} // end of class KruskalGenerator
