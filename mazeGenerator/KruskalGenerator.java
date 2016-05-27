package mazeGenerator;

import maze.*;

import java.util.*;

public class KruskalGenerator implements MazeGenerator
{
    private List<Edge> edgeSet;
    private List<List<Cell>> treeSet;
    private int type;


    @Override
    public void generateMaze(Maze maze)
    {
        initialize(maze);

        while (!edgeSet.isEmpty())
        {
            Random generator = new Random();
            int index = generator.nextInt(edgeSet.size());

            Edge edge = edgeSet.get(index);
            edgeSet.remove(index);

            int cell1Index = -1, cell2Index = -1, i = -1;

            for (List<Cell> cells : treeSet)
            {
                i++;
                if (cell1Index < 0 && cells.contains(edge.cell1))
                    cell1Index = i;
                if (cell2Index < 0 && cells.contains(edge.cell2))
                    cell2Index = i;
                if (cell1Index > 0 && cell2Index > 0)
                    break;
            }

            if (cell1Index != cell2Index)
            {
                treeSet.get(cell1Index).addAll(treeSet.get(cell2Index));
                treeSet.remove(cell2Index);

                edge.cell1.wall[edge.direction].present = false;
                edge.cell2.wall[Maze.oppoDir[edge.direction]].present = false;

            }
        }

    } // end of generateMaze()

    private void initialize(Maze maze)
    {
        edgeSet = new ArrayList<>();
        treeSet = new ArrayList<>();
        type = maze.type;

        for (int i = 0; i < maze.sizeR; i++)
        {
            for (int j = hexMapping(i); j < maze.sizeC + hexMapping(i); j++)
            {
                Cell cell1 = maze.map[i][j];

                for (int k = 0; k < Maze.NUM_DIR; k++)
                {
                    Cell cell2 = cell1.neigh[k];
                    if (cell2 != null)
                    {
                        Edge edge = new Edge(cell1, cell2, k);
                        if (!edgeSet.contains(edge))
                            edgeSet.add(edge);
                    }
                }

                boolean flag = false;
                if (type == Maze.TUNNEL && cell1.tunnelTo != null)
                {
                    for (List<Cell> cells : treeSet)
                        if (cells.contains(cell1.tunnelTo))
                        {
                            cells.add(cell1);
                            flag = true;
                            break;
                        }
                }
                if (!flag)
                {
                    ArrayList<Cell> cellList = new ArrayList<>();
                    cellList.add(cell1);
                    treeSet.add(cellList);
                }
            }
        }

        System.out.println("Edges: " + edgeSet.size());
        System.out.println("Tree Set: " + treeSet.size());

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

} // end of class KruskalGenerator
