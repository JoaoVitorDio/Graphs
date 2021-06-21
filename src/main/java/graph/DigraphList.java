package graph;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static guru.nidi.graphviz.model.Factory.mutGraph;
import static guru.nidi.graphviz.model.Factory.mutNode;

public class DigraphList extends AbstractGraph
{
    private List<List<Edge>> adjacencyList;

    private static final Logger LOGGER = Logger.getLogger("DigraphList.class");

    public DigraphList(List<Vertex> vertices)
    {
        super(vertices);
        initializeAdjacencyList();
    }

    private void initializeAdjacencyList()
    {
        setAdjacencyList(new ArrayList<>());
        for (var i = 0; i < getNumberOfVertices(); i++)
        {
            getAdjacencyList().add(new ArrayList<>());
        }
    }

    @Override
    public void addVertex(Vertex vertex)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeVertex(Vertex vertex)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addEdge(Vertex source, Vertex destination)
    {

        if(!edgeExists(source, destination))
        {
            int sourceIndex = getVertices().indexOf(source);
            getAdjacencyList().get(sourceIndex).add(new Edge(destination, 1));
        }
    }

    @Override
    public void removeEdge(Vertex source, Vertex destination)
    {
        int sourceIndex = getVertices().indexOf(source);
        List<Edge> sourceEdges = getAdjacencyList().get(sourceIndex);
        for (int i = sourceEdges.size()-1; i > -1; i--)
        {
            if(sourceEdges.get(i).getDestination() == destination)
            {
                sourceEdges.remove(i);
            }
        }
    }

    @Override
    public boolean edgeExists(Vertex source, Vertex destination)
    {

        int sourceIndex = getVertices().indexOf(source);
        List<Edge> sourceEdges = getAdjacencyList().get(sourceIndex);
        for (Edge sourceEdge : sourceEdges)
        {
            if (sourceEdge.getDestination() == destination)
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasAnyEdge(Vertex vertex)
    {
        int vertexIndex = getVertices().indexOf(vertex);

        if(!getAdjacencyList().get(vertexIndex).isEmpty())
        {
            return true;
        }

        for (var i = 0; i < getNumberOfVertices(); i++)
        {
            for (var j = 0; j < getAdjacencyList().get(i).size(); ++j)
            {
                if(getAdjacencyList().get(i).get(j).getDestination() == vertex)
                {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int getFirstConnectedVertexIndex(Vertex vertex)
    {
        if(getAdjacencyList().get(getVertices().indexOf(vertex)).isEmpty())
        {
            return -1;
        }
        else
        {
            return getVertices().indexOf(getAdjacencyList().get(getVertices().indexOf(vertex)).get(0).getDestination());
        }
    }

    @Override
    public int getNextConnectedVertexIndex(Vertex vertex, int currentEdge)
    {
        int vertexIndex = getVertices().indexOf(vertex);
        var currentAdjacentVertexIndex = 0;
        while(getAdjacencyList().get(vertexIndex).get(currentAdjacentVertexIndex).getDestination() != getVertices().get(currentEdge))
        {
            currentAdjacentVertexIndex++;
        }
        currentAdjacentVertexIndex++;
        if(getAdjacencyList().get(vertexIndex).size() > currentAdjacentVertexIndex)
        {
            return getVertices().indexOf(getAdjacencyList().get(vertexIndex).get(currentAdjacentVertexIndex).getDestination());
        }
        else
        {
            return -1;
        }
    }


    @Override
    public String toString() {
        var s = new StringBuilder();
        for (var i = 0; i < getNumberOfVertices(); i++)
        {
            s.append(i).append(": ");
            for (var j = 0; j < getAdjacencyList().get(i).size(); ++j)
            {
                s.append(getAdjacencyList().get(i).get(j).getWeight()).append(" ");
            }
            s.append("\n");
        }
        return s.toString();
    }

    @Override
    public void printInGraphViz(String fileName)
    {
        MutableGraph g = mutGraph("example1Digraph").setDirected(true);

        for (var i = 0; i < getNumberOfVertices(); i++)
        {
            for (var j = 0; j < getAdjacencyList().get(i).size(); ++j)
            {
                int destinationIndex = getVertices().indexOf(getAdjacencyList().get(i).get(j).getDestination());
                g.add(mutNode(getVertices().get(i).getName()).addLink(getVertices().get(destinationIndex).getName()));
            }
        }
        try
        {
            Graphviz.fromGraph(g).width(GRAPHVIZ_IMAGE_WIDTH).render(Format.PNG).toFile(new File(GRAPHVIZ_FOLDER+fileName+GRAPHVIZ_FILE_EXTENSION));
        }
        catch ( IOException e )
        {
            LOGGER.log(Level.SEVERE, "IO Exception thrown when saving Graphviz file", e);
        }
    }

    public List<List<Edge>> getAdjacencyList()
    {
        return adjacencyList;
    }

    public void setAdjacencyList(List<List<Edge>> adjacencyList)
    {
        this.adjacencyList = adjacencyList;
    }

    @Override
    public float getDistance(Vertex source, Vertex destination)
    {
        int sourceIndex = getVertices().indexOf(source);
        for(var i = 0; i < adjacencyList.get(sourceIndex).size(); i++)
        {
            if(adjacencyList.get(sourceIndex).get(i).getDestination() == destination)
            {
                return adjacencyList.get(sourceIndex).get(i).getWeight();
            }
        }
        return -1;
    }
}
