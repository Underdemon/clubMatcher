/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package algorithms.dijkstra;

import dataStructures.dll.DLL;
import dataStructures.pQueue.PQueue;
import dataStructures.graphs.Graph;
import dataStructures.hashmap.HashMap;
import dataStructures.hashmap.Pair;
import dataStructures.pQueue.PQnode;

/**
 *
 * @author rayan
 */
public class Dijkstra
{
    private PQueue<Edge> unvisited;
    private Graph<String> graph;
    DLL<Edge> rs = new DLL<>();
    DLL<Pair<String, Integer>> results;
    
    public Dijkstra(Graph graph)
    {
        unvisited = new PQueue<>();
        this.graph = graph;
    }
    
    public Edge vertexSearch(String value)
    {
        if(unvisited.isEmpty())
            return null;
        
        PQnode<Edge> node = unvisited.getHead();
        
        while(node.getNext() != null && !node.getData().getData().equals(value))
        {
            node = node.getNext();
        }
        
        if(node.getNext() == null && !node.getData().getData().equals(value))
        {
            for(Edge e : rs)
            {
                if(e.getData().equals(value))
                    return e;
            }
        }
        
        return node.getData();
    }
    
    public boolean inQueue(String value)
    {
        if(unvisited.isEmpty())
            return false;
        
        PQnode<Edge> node = unvisited.getHead();
        
        while(node.getNext() != null && !node.getData().getData().equals(value))
        {
            node = node.getNext();
        }
        
        if(node.getNext() == null && !node.getData().getData().equals(value))
        {
            return false;
        }
        
        return true;
    }
    
    public DLL<Pair<String, Integer>> shortestPath(String src)
    {
        DLL<Pair<String, HashMap<String, Integer>>> graph_data = graph.returnSourceData();        
        
        for(int i = 0; i < graph_data.getLen(); i++)
        {
            if(graph_data.returnAtIndex(i).getKey().equals(src))
                unvisited.enqueue(new Edge(src, 0, null), 0);
            else
                unvisited.enqueue(new Edge(graph_data.returnAtIndex(i).getKey(), Integer.MAX_VALUE, null), Integer.MAX_VALUE);
        }
                
        while(!unvisited.isEmpty())
        {
            Edge u = unvisited.pop();
            DLL<Pair<String, Integer>> connection_pairs = graph.returnConnections(u.getData()).returnData();
            for(Pair<String, Integer> v : connection_pairs)
            {
                if(vertexSearch(v.getKey()) != null)
                {
                    if(u.getDistance() + v.getValue() < vertexSearch(v.getKey()).getDistance())
                    {
                        vertexSearch(v.getKey()).setDistance(u.getDistance() + v.getValue());
                        vertexSearch(v.getKey()).setPrev(u);
                        
                        if(inQueue(v.getKey()))
                            unvisited.replace(vertexSearch(v.getKey()), vertexSearch(v.getKey()).getDistance());
                        else
                            rs.remove(vertexSearch(v.getKey()));
                    }
                }
            }
            rs.append(u);
        }
                
        int i = 0;
        results = new DLL();
        for(Edge e : rs)
        {
            results.append(new Pair<String, Integer>(rs.returnAtIndex(i).getData(), rs.returnAtIndex(i).getDistance()));
            i++;
        }
        results.setLen(i);
        return results;        
    }
}
