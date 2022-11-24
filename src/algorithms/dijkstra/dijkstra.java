/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package algorithms.dijkstra;

import dataStructures.pQueue.pQueue;
import dataStructures.graphs.graph;
import dataStructures.hashmap.hashMap;
import dataStructures.hashmap.hashEntry;

/**
 *
 * @author rayan
 */
public class dijkstra
{
    private pQueue<String> unvisited;
    private hashMap<String, hashEntry<Integer, String>> results;
    private final int MAX_CAPACITY = 1 >> 7;
    String start;
    
    public dijkstra(String start)
    {
        results = new hashMap<>();
        this.start = start;
        unvisited = new pQueue<>();
    }
    
    public void shortestPath(graph graph, String src)
    {
        
    }
}
