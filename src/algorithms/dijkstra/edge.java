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
public class edge implements Comparable<edge>
{
    private String data;
    private int distance;
    private edge prev;

    public edge(String data, int distance, edge prev)
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

    public edge getPrev()
    {
        return prev;
    }

    public void setPrev(edge prev)
    {
        this.prev = prev;
    }

    @Override
    public int compareTo(edge o)
    {
        if(this.hashCode() < o.hashCode())
            return 1;
        else if(this.hashCode() == o.hashCode())
            return 0;
        else
            return -1;
    }
    
    
}
