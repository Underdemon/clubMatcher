/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dataStructures.graphs;

import dataStructures.dll.dll;
import dataStructures.hashmap.hashMap;
/**
 *
 * @author rayan
 * @param <T>
 */
public class graph<T extends Comparable<T>>
{
    private hashMap<T, hashMap<T, Integer>> map;
    private final int capacity = 1 << 7;

    public graph()
    {
        map = new hashMap<>();  // change hashmap so you can pass in a capacity
    }
    
    public void add(T src, T dest, int weight)
    {        
        hashMap<T, Integer> destinations = new hashMap<>();  // should be of size capacity
        
        if(!map.containsKey(src)) // if the outer hashmap doesn't have the source node
        {
            destinations.add(dest, weight); // inner hashmap of destination and weight
            map.add(src, destinations); // outer hashmap of source and inner hashmap
        }
        else if(map.get(src).containsKey(dest)) // does the source node have a connection to the destination node
        {
            map.get(src).replace(dest, weight);
        }
        else
        {
            map.get(src).add(dest, weight);
        }
        
        if(!map.containsKey(dest))
        {
            destinations = new hashMap<>();
            map.add(dest, destinations);
        }
    }
    
    public void delete(T src) throws IllegalArgumentException
    {
        if(!map.containsKey(src))
            throw new IllegalArgumentException();
        
        map.remove(src);
    }
    
    public void removeConnections(T src, T dest) throws IllegalArgumentException
    {
        if(!map.containsKey(src))
            throw new IllegalArgumentException();
        
        map.get(src).remove(dest);
    }
    
    public int size()
    {
        return map.size();
    }
    
    public void print()
    {
        System.out.println("\n===============\nPRINTING GRAPH |\n===============");
    }
    
    public dll returnSourceData()
    {
        return map.returnData();
    }
    public hashMap<T, Integer> returnConnections(T src)
    {
        return map.get(src);
    }
    public hashMap<T, hashMap<T, Integer>> getMap()
    {
        return map;
    }

    public void setMap(hashMap<T, hashMap<T, Integer>> map)
    {
        this.map = map;
    }
    
}