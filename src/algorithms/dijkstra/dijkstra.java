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

import java.awt.event.PaintEvent;
import java.lang.reflect.Parameter;

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

    public void removeFromQueue(String value, PQueue queue)
    {
        PQnode<Edge> temp = queue.getHead();

        while(!temp.getData().getData().equals(value))
            temp = temp.getNext();

        if(temp.getData().getData().equals(value))
        {
            if(temp == queue.getHead())
            {
                queue.setHead(temp.getNext());
            }
            else if(temp == queue.getTail())
            {
                queue.setTail(temp.getPrev());
            }
            else
            {
                temp.getPrev().setNext(temp.getNext());
                temp.getNext().setPrev(temp.getPrev());
            }
        }
        queue.setSize(queue.getSize() - 1);
    }

    public DLL shortestPath(String src, Graph graph, boolean returnNames)
    {
        PQueue<Edge> unvisited = new PQueue<>();
        DLL<Edge> rs = new DLL<>();

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
                        removeFromQueue(v.getKey(), unvisited);
                        unvisited.enqueue(new Edge(v.getKey(), alt_dist, vertexSearch(u.getData(), unvisited, rs)), alt_dist);
                    }
                }
            }
        }
                
        int i = 0;
        DLL results;
        if(returnNames)
        {
            results = new DLL<Pair<String, Integer>>();
            for(Edge e : rs)
            {
                results.append(new Pair<>(e.getData(), e.getDistance()));
                i++;
            }
            results.setLen(i);
        }
        else
        {
            results = new DLL<Integer>();
            for (Edge e : rs)
            {
                results.append(e.getDistance());
                i++;
            }
        }
        results.setLen(i);
        return results;
    }
}
