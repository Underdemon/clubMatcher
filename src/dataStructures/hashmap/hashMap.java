/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dataStructures.hashmap;

import dataStructures.BST.BST;
import dataStructures.BST.DL_Node;
import dataStructures.dll.DLL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;
/**
 *
 * @author rayan
 * 
 * 
 * normally a hash map handles collisions by chaining
 *      an array of buckets where each bucket is a linked list holding the hashEntry key-value pairs
 * 
 * this makes worse case time complexity for methods such as get() O(n) from (compared to to best case O(1))
 * in order to improve this java 8 handled collisions using a (balanced) binary tree (BST) instead of a linked list
 * this improves the time complexity of methods such as get() to O(log n)
 * 
 * (note to self: often implemented with red-black BST)
 * @param <K>
 * @param <V>
 */
public class HashMap<K extends Comparable<K>, V extends Comparable<V>> implements Map<K, V>, Comparable<HashMap<K, V>>
{
    /*
        below a bitwise shift operator is used (just to test knowledge)
            1 << 7
                a binary value of 00000001 (1 in decimal) is shifted left by 7
                the resultant value is 10000000 (128 in decimal)
    
    
        https://www.javatpoint.com/operator-shifting
        https://bit-calculator.com/bit-shift-calculator
    */
    private final int capacity = (1 << 7);
    
    /*  array stores the root node for the binary tree  */
    private BST<Pair<K, V>>[] table = new BST[capacity];
    
    /*
        ... = VARARGS
        normally unneccessary boilerplate for arbitrary no of parameters (passing arguments as an array or making multiple overloaded functions each of which
        has a number of arguments eg void put(String s1); void put (String s1, String s2...)
            the ellipses here is an example of what the ellipses is being used for
                that is, an arbitrary number of hashEntries
        varargs (which is the ellipses below) handles an arbitrary number of parameters automatically using an array
    
        https://www.baeldung.com/java-varargs
        https://www.geeksforgeeks.org/variable-arguments-varargs-in-java/
    */
    public HashMap(Pair<K, V> ... entries)
    {        
        for(Pair<K, V> entry : entries)
        {
            table[hash(entry.getKey())] = new BST<>(entry);
        }
    }
    
    public int size()
    {
        int size = 0;
        
        for(int i = 0; i < table.length; i++)
        {
            if(table[i] != null)
                size += table[i].size(table[i].getRoot());
        }
        
        return size;
    }
    
    public int hash(K key)
    {
        return Math.abs(key.hashCode() % capacity);
    }
    
    @Override
    public void add(K key, V value)
    {
        if(key == null)
            return;
        
        int hash = hash(key);
        Pair<K, V> entry = new Pair<>(key, value);
        
        if(!this.containsKey(key))
        {
            if(table[hash] != null)
                table[hash].insert(table[hash].getRoot(), entry);
            else
                table[hash] = new BST<>(entry);
        }
    }
    
    @Override
    public void remove(K key)  // renmoves hashEntry with key value matching key arg
    {
        int hash = hash(key);
        table[hash].delete(table[hash].getRoot(), (Pair) key);
    }

    private Pair<K, V> searchBST(DL_Node<Pair<K, V>> temp, K key)
    {
        if(temp == null)
            return null;
        else if(temp.getData() == null)
            return null;
        else if(temp.getData().getKey().toString().equals(key.toString()))
            return temp.getData();
        
        if(isNumeric(temp.getData().getKey().toString()))
        {
            if(Integer.parseInt(key.toString()) > Integer.parseInt(temp.getData().getKey().toString()))    // traverse to right child
            {
                searchBST(temp.getNext(), key);
            }
            else if(Integer.parseInt(key.toString()) < Integer.parseInt(temp.getData().getKey().toString()))   // traverse to left child
            {
                searchBST(temp.getPrev(), key);
            }
        }
        else
        {
            if(key.toString().compareTo(temp.getData().getKey().toString()) > 0)    // traverse to right child
            {
                searchBST(temp.getNext(), key);
            }
            else if(key.toString().compareTo(temp.getData().getKey().toString()) < 0)   // traverse to left child
            {
                searchBST(temp.getPrev(), key);
            }
        }
        
        return null;
    }
    
    @Override
    public V get(K key)
    {
        
        int hash = hash(key);
        Pair<K, V> temp = new Pair<>(key, null);
        table[hash].search(table[hash].getRoot(), temp).getData();
        Pair<K, V> tmp = (Pair) table[hash].search(table[hash].getRoot(), temp).getData();
        return tmp.getValue();
        /*
        int hash = hash(key);
        return (V) searchBST(table[hash].getRoot(), key).getValue();
        */
    }
    
    private Pair<K, V> getEntry(K key)
    {
        int hash = hash(key);
        Pair<K, V> temp = new Pair<>(key, null);
        Pair<K, V> tmp = (Pair) table[hash].search(table[hash].getRoot(), temp).getData();
        return tmp;
    }
    
    public void replace(K key, V value)
    {
        getEntry(key).setValue(value);
    }
    
    @Override
    public boolean containsKey(K key)
    {
        int hash = hash(key);
        Pair<K, V> tmp = new Pair(key, null);
        if(table[hash] != null && table[hash].search(table[hash].getRoot(), tmp) != null)
            return true;
        else
            return false;
    }
    
    public void print()
    {
        for(int i = 0; i < table.length; ++i)
        {
            if(table[i] != null)
                table[i].prettyPrint(table[i].getRoot(), 0);
        }
    }
    
    public DLL returnData()
    {
        DLL list = new DLL(); 
        int len  = 0;
        for(int i = 0; i < table.length; ++i)
        {
            if(table[i] != null)
            {
                list.concatenate(table[i].returnData());
                len += table[i].returnData().getLen();
            }
        }
        list.setLen(len);
        return list;
    }
    
    /*
        below is the binary tree implementation for the buckets
        the functions are recursive in order to traverse the branches
    */
      
    
//    public mapIterator iterator()
//    {
//        return new mapIterator();
//    }
    
    /*
        to use iterator
        
        hashMap<type1, type2> map = new hashMap<>();
        Iterator itr = map.iterator();
        hashEntry<type1, type2> temp;
        while(itr.hasNext())
        {
            temp = new hashEntry<>((hashEntry) itr.next());
            sout("key " + temp.getKey() + "value " + temp.getValue());
        }
    */
    
//    public class mapIterator implements Iterator<hashEntry<K, V>>
//    {
//        int curr;
//        
//        public mapIterator()
//        {
//            curr = 0;
//        }
//        
//        public boolean hasNext()
//        {
//            return curr < kvEntries.size(); // if the current element the iterable is on is less than the arraylist's size, then there is a next element to iterate to
//        }
//        
//        /*
//            returns the next element in the hashmap
//            if there are no more elements in the hashmap, an error is thrown
//        */
//        public hashEntry<K, V> next()
//        {
//            if(curr >= kvEntries.size())
//            {
//                throw new IllegalArgumentException();
//            }
//            
//            ++curr;
//            return new hashEntry<>(kvEntries.get(curr - 1));
//        }
//    }

    public boolean isNumeric(String str)
    {
        // used as compareTo() compares values lexicographically and only strings should be compared that way
        // https://chortle.ccsu.edu/java5/Notes/chap92/ch92_2.html
        if(str.equals(null))
            return false;
        
        final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        
        /*
        -?          determines if number starts with minus
        \d+         matches one or more digits
        (\.\d+)?    matches for decimal point and digits following
        */
        
        return pattern.matcher(str).matches();
    }
    
    @Override
    public int compareTo(HashMap<K, V> o)
    {
        if(this.hashCode() < o.hashCode())
            return 1;
        else if(this.hashCode() == o.hashCode())
            return 0;
        else
            return -1;
    }
}