/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package algorithms.dijkstra;

import dataStructures.dll.dl_node;
import dataStructures.dll.dll;
import dataStructures.pQueue.pQueue;
import dataStructures.graphs.graph;
import dataStructures.hashmap.hashMap;
import dataStructures.hashmap.hashEntry;
import dataStructures.pQueue.pQnode;
import java.util.ArrayList;

/**
 *
 * @author rayan
 */
public class dijkstra
{
    private pQueue<edge> unvisited;
    private pQueue<edge> visited;
    private final int MAX_CAPACITY = 1 >> 7;
    private String start;
    private int[] dist;
    private String[] prev;
    private graph<String> graph;
    dll<edge> rs = new dll<>();
    
    public dijkstra(graph graph)
    {
        unvisited = new pQueue<>();
        this.graph = graph;
    }
    
    private void relax(edge u, edge v, int weight)
    {
        if(u.getDistance() + weight < v.getDistance())
        {
            v.setDistance(u.getDistance() + weight);
            v.setPrev(u);
        }
    }
    
    public edge vertexSearch(String value)
    {
        if(unvisited.isEmpty())
            return null;
        
        pQnode<edge> node = unvisited.getHead();
        
        while(node.getNext() != null && !node.getData().getData().equals(value))
        {
            node = node.getNext();
        }
        
        if(node.getNext() == null && !node.getData().getData().equals(value))
        {
            for(edge e : rs)
            {
                if(e.getData().equals(value))
                    return e;
            }
        }
        
        return node.getData();
    }
    
    public void shortestPath(String src)
    {
        dll<hashEntry<String, hashMap<String, Integer>>> graph_data = graph.returnSourceData();        
        
        for(int i = 0; i < graph_data.getLen(); i++)
        {
            if(graph_data.returnAtIndex(i).getKey().equals(src))
                unvisited.enqueue(new edge(src, 0, null), 0);
            else
                unvisited.enqueue(new edge(graph_data.returnAtIndex(i).getKey(), Integer.MAX_VALUE, null), Integer.MAX_VALUE);
        }
                
        while(!unvisited.isEmpty())
        {
            edge u = unvisited.pop();
            dll<hashEntry<String, Integer>> connection_pairs = graph.returnConnections(u.getData()).returnData();
            for(hashEntry<String, Integer> v : connection_pairs)
            {
                if(vertexSearch(v.getKey()) != null)
                {
                    if(u.getDistance() + v.getValue() < vertexSearch(v.getKey()).getDistance())
                    {
                        vertexSearch(v.getKey()).setDistance(u.getDistance() + v.getValue());
                        vertexSearch(v.getKey()).setPrev(u);
                    }
                }
            }
            rs.append(u);
        }
        
        // problem
        // path to b is initially B to A with weight 4
        // path to d is found to be path from b to d with weight 2
        // total weight from a to d is 6
        // after assigning weight, min path to b is found to be 3
        // therefore min path to d should be 5 not 6
        
        int i = 0;
        for(edge e : rs)
        {
            System.out.println(rs.returnAtIndex(i).getData() + " = " + rs.returnAtIndex(i).getDistance());
            i++;
        }
        
        /*
        dll<hashEntry<String, hashMap<String, Integer>>> graph_data = graph.returnSourceData();        
        
        for(int i = 0; i < graph_data.getLen(); i++)
        {
            if(graph_data.returnAtIndex(i).getKey().equals(src))
                unvisited.enqueue(new edge(src, 0, null), 0);
            else
                unvisited.enqueue(new edge(graph_data.returnAtIndex(i).getKey(), Integer.MAX_VALUE, null), Integer.MAX_VALUE);
        }
        
        while(!unvisited.isEmpty())
        {
            edge u = unvisited.pop();
            dll<hashEntry<String, Integer>> connection_pairs = graph.returnConnections(u.getData()).returnData();
            for(hashEntry<String, Integer> v : connection_pairs)
            {
                if(searchQueue(unvisited, v.getKey()) != null)
                {
                    if(u.getDistance() + v.getValue() < searchQueue(unvisited, v.getKey()).getDistance())
                    {
                        searchQueue(unvisited, v.getKey()).setDistance(u.getDistance() + v.getValue());
                        searchQueue(unvisited, v.getKey()).setPrev(u);
                    }
                }
            }
            rs.append(u);
        }
        
        int i = 0;
        for(edge e : rs)
        {
            System.out.println(rs.returnAtIndex(i).getData() + " = " + rs.returnAtIndex(i).getDistance());
            i++;
        }
        
        
        dll<hashEntry<String, hashMap<String, Integer>>> graph_data = graph.returnSourceData();
        for(int i = 0; i < graph.size(); i++)
        {
            edge edge;
            hashEntry<String, hashMap<String, Integer>> graph_data_at_i = graph_data.returnAtIndex(i);
            if(!(graph_data_at_i.getKey().equals(src)))
            {
                edge = new edge(graph_data_at_i.getKey(), Integer.MAX_VALUE, null);
                unvisited.enqueue(edge, edge.getDistance());
            }
            else
            {
                unvisited.enqueue(new edge(src, 0, null), 0);
            }
        }
        
        pQueue<edge> copy = unvisited.copy();
        
        while(!unvisited.isEmpty())
        {
            edge u = unvisited.pop();
            for(int i = 0; i < graph.size(); i++)
            {
                hashEntry<String, hashMap<String, Integer>> graph_data_at_i = graph_data.returnAtIndex(i);
                if(graph_data_at_i.getKey().equals(u.getData()))
                {
                    dll<hashEntry<String, Integer>> graph_data_at_j = graph_data_at_i.getValue().returnData();
                    // for (hashEntry entry : dll)
                        // relax
                    for(hashEntry entry : graph_data_at_j)
                    {
                        edge node = copy.search((String) entry.getKey()).getData();
                        relax(u, node, (int) entry.getValue());
                    }
                }
            }
        }
        */
        /*
        PSEUDOCODE (https://www.cs.dartmouth.edu/~thc/cs10/lectures/0509/0509.html):
        
void dijkstra(s) {
  queue = new PriorityQueue<Vertex>();
  for (each vertex v) {
    v.dist = infinity;  // can use Integer.MAX_VALUE or Double.POSITIVE_INFINITY
    queue.enqueue(v);
    v.pred = null;
  }   
  s.dist = 0;

  while (!queue.isEmpty()) {
    u = queue.extractMin();
    for (each vertex v adjacent to u)
      relax(u, v);
  }
}
        
void relax(u, v) {
  if (u.dist + w(u,v) < v.dist) {
    v.dist = u.dist + w(u,v);
    v.pred = u;
  }
}
        
        
        */
        
        /*
        dist = new int[graph.size()];
        prev = new String[graph.size()];
        
        dll<hashEntry<String, hashMap<String, Integer>>> graph_data = graph.returnSourceData();
        
        for(int i = 0; i < graph.size(); i++)
        {            
            if(!graph_data.returnAtIndex(i).getKey().equals(src))
            {
                dist[i] = Integer.MAX_VALUE;
                prev[i] = null;
                unvisited.enqueue(graph_data.returnAtIndex(i).getKey(), dist[i]);
            }
        }
        
        for(int i = 0; i < unvisited.size(); i++)
        {
            String best_vertex = unvisited.peek();
            
            dll<hashEntry<String, Integer>> value = graph_data.returnAtIndex(i).getValue().returnData();
            int count = 0;
            for(hashEntry entry : value)
            {
                int alt = dist[i] + (int) entry.getValue();
                if(alt < dist[count])
                {
                    dist[count] = alt;
                    prev[count] = best_vertex;
                    unvisited.replace((String) entry.getKey(), alt);
                }
                count++;
            }
        }
        */
        /*
        GRABS AND OUTPUTS ALL SRC NODES WITH CORRESPONDING END NODES && WEIGHT
        
        
        
        dll<hashEntry<String, hashMap<String, Integer>>> graph_data = graph.returnSourceData();
        
        for(int i = 0; i < graph.size(); i++)
        {
            dll<hashEntry<String, Integer>> value = graph_data.returnAtIndex(i).getValue().returnData();
            System.out.println(graph_data.returnAtIndex(i).getKey() + "\n============================");
            
            for(hashEntry entry : value)
            {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
            
            System.out.println("\n\n");
        }
        */
        
        
    }
}
