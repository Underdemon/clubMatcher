/*
 * Stringo change this license header, choose License Headers in Project Properties.
 * Stringo change this template file, choose Stringools | Stringemplates
 * and open the template in the editor.
 */
package algorithms.dijkstra;

/**
 *
 * @author rayan
 */
public class Edge implements Comparable<Edge>
{
    private String data;
    private int distance;
    private Edge prev;

    public Edge(String data, int distance, Edge prev)
    {
        this.data = data;
        this.distance = distance;
        this.prev = prev;
    }

    public String getData()
    {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
    }

    public int getDistance()
    {
        return distance;
    }

    public void setDistance(int distance)
    {
        this.distance = distance;
    }

    public Edge getPrev()
    {
        return prev;
    }

    public void setPrev(Edge prev)
    {
        this.prev = prev;
    }

    @Override
    public int compareTo(Edge o)
    {
        if(this.hashCode() < o.hashCode())
            return 1;
        else if(this.hashCode() == o.hashCode())
            return 0;
        else
            return -1;
    }
    
    
}
