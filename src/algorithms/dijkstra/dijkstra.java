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

/**
 *
 * @author rayan
 */
public class dijkstra
{
    private pQueue<edge> unvisited;
    private final int MAX_CAPACITY = 1 >> 7;
    private String start;
    private int[] dist;
    private String[] prev;
    private graph<String> graph;
    
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
    
    public void shortestPath(String src)
    {
        dll<hashEntry<String, hashMap<String, Integer>>> graph_data = graph.returnSourceData();
        hashMap<String, Integer> node_connections = graph.returnConnections(src);
        dll<hashEntry<String, Integer>> connection_pair = node_connections.returnData();
        
        
        for(int i = 0; i < connection_pair.getLen(); i++)
        {
            unvisited.enqueue(new edge(connection_pair.returnAtIndex(i).getKey(), Integer.MAX_VALUE, null), connection_pair.returnAtIndex(i).getValue());
        }
        
        pQueue<edge> copy = unvisited.copy();
        
        while(!unvisited.isEmpty())
        {
            edge u = unvisited.pop();
            for(int i = 0; i < node_connections.size(); i++)
            {
                if(u.getData().equals(graph_data.returnAtIndex(i).getKey()))
                {
                    for(hashEntry e : connection_pair)
                    {
                        edge node = copy.search((String) e.getKey()).getData();
                        relax(u, node, (int) e.getValue());
                    }
                }
            }
        }
        System.out.println("");
        /*
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
