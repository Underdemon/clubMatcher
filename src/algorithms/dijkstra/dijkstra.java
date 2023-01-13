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
    public Edge vertexSearch(String value, PQueue unvisited, DLL<Edge> rs)
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
    
    public boolean inQueue(String value, PQueue unvisited)
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
    
    public DLL<Integer> shortestPath(String src, Graph graph)
    {
        PQueue<Edge> unvisited = new PQueue<>();
        DLL<Edge> rs = new DLL<>();
        DLL<Integer> results;

        DLL<Pair<String, HashMap<String, Integer>>> graph_data = graph.returnSourceData();        
        
        for(int i = 0; i < graph_data.getLen(); i++)
        {
            if(graph_data.returnAtIndex(i).getKey().equals(src))
                unvisited.enqueue(new Edge(src, 0, null), 0);
            else
                unvisited.enqueue(new Edge(graph_data.returnAtIndex(i).getKey(), 2000000000, null), 2000000000);
        }

        while(!unvisited.isEmpty())
        {
            Edge u = unvisited.pop();
            rs.append(u);
            DLL<Pair<String, Integer>> connection_pairs = graph.returnConnections(u.getData()).returnData();
            for(Pair<String, Integer> v : connection_pairs)
            {
                if(vertexSearch(v.getKey(), unvisited, rs) != null)     // where vertex, v, has not yet been removed from the queue
                {
                    int alt_dist = u.getDistance() + v.getValue();
                    if(alt_dist < vertexSearch(v.getKey(), unvisited, rs).getDistance())
                    {
                        vertexSearch(v.getKey(), unvisited, rs).setDistance(alt_dist);
                        vertexSearch(v.getKey(), unvisited, rs).setPrev(u);
                    }
                }
            }
        }
                
        int i = 0;
        results = new DLL();
        for(Edge e : rs)
        {
            results.append(rs.returnAtIndex(i).getDistance());
            i++;
        }
        results.setLen(i);
        return results;        
    }
}
