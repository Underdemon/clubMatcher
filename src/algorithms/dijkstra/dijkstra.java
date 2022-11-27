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
    private pQueue<String> unvisited;
    private hashMap<String, hashEntry<Integer, String>> results;
    private final int MAX_CAPACITY = 1 >> 7;
    private String start;
    private int[] dist;
    private String[] prev;
    
    public dijkstra()
    {
        results = new hashMap<>();
        unvisited = new pQueue<>();
    }
    
    public void shortestPath(graph graph, String src)
    {
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
