/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dataStructures.hashmap;

/**
 *
 * @author rayan
 * @param <K>
 * @param <V>
 */

public class Pair<K extends Comparable<K>, V extends Comparable<V>> implements Comparable<Pair<K, V>>
{
    private K key;
    private V value;
    
    public Pair()
    {
        this.key   = null;
        this.value = null;
    }
    
    public Pair(K key, V value)
    {
        this.key   = key;
        this.value = value;
    }
    
    public Pair(Pair<K,V> entry)
    {
        this.key   = entry.getKey();
        this.value = entry.getValue();
    }
    
    @Override
    public int compareTo(Pair<K, V> obj)
    {
        return this.getKey().compareTo(obj.getKey());
        // hashEntries are compared by key values
    }

    public K getKey()
    {
        return key;
    }

    public void setKey(K key)
    {
        this.key = key;
    }

    public V getValue()
    {
        return value;
    }

    public void setValue(V value)
    {
        this.value = value;
    }

    @Override
    public String toString()
    {
        return this.key.toString();
    }
}
