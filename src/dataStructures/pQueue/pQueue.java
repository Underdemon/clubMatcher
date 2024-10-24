/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dataStructures.pQueue;

import java.util.regex.Pattern;

/**
 *
 * @author rayan
 * @param <T>
 */
public class PQueue<T extends Comparable<T>>
{
    private PQnode<T> head;
    private PQnode<T> tail;
    private int size;
    
    public PQueue()
    {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }
    
    
    private PQueue(PQnode<T> head, PQnode<T> tail, int size)
    {
        this.head = head;
        this.tail = tail;
        this.size = size;
    }
    
    
    public boolean isEmpty()
    {
        return size == 0;
    }
    
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
    
    public void enqueue(T data, int priority)
    {
        PQnode temp = new PQnode(data, null, null, priority);
            
        if(head == null)
        {
            // adds first node to queue
            this.head = temp;
            this.tail = temp;
        }
        else if(priority <= this.head.getPriority())
        {
            // add node at first pos
            temp.setNext(this.head);
            this.head.setPrev(temp);
            this.head = temp;
        }
        else if(priority >= this.tail.getPriority())
        {
            // add node to last pos
            temp.setPrev(this.tail);
            this.tail.setNext(temp);
            this.tail = temp;
        }
        else
        {
            PQnode node = this.head;

            // find the point at which the inserted data has a higher priority
            while(node.getPriority() < priority)
            {
                node = node.getNext();
            }
            
            // code to actually add the node
            temp.setPrev(node.getPrev());
            temp.setNext(node);
            node.getPrev().setNext(temp);
            node.setPrev(temp);
        }
        
        this.size++;
    }
    
    public void replace(T value, int priority)
    {
        if(this.head.getData().equals(value))
        {
            head.setPriority(priority);
        }
        else if(this.tail.getData().equals(value))
        {
            tail.setPriority(priority);
        }
        else
        {
            PQnode node = this.head;
            
            while(!node.getData().equals(value))
            {
                node = node.getNext();
            }
            
            node.setPriority(priority);
        }
    }
    
    public PQnode<T> search(T value)
    {
        if(isEmpty())
            return null;
        
        PQnode node = this.head;
        
        while(!node.getData().equals(value))
        {
            node = node.getNext();
        }
        
        return node;
    }
    
    public PQnode<T> search(String value)
    {
        if(isEmpty())
            return null;
        
        PQnode node = this.head;
        
        while(!node.getData().equals(value))
        {
            node = node.getNext();
        }
        
        return node;
    }
    
    public T peek() throws IllegalStateException    // peeks first element
    {
        if(!isEmpty())
        {
            return this.head.getData();
        }
        throw new IllegalStateException();
    }
    
    public T pop() throws IllegalStateException // peeks and removes first element
    {
        if(!isEmpty())
        {
            T data = this.peek();
            PQnode temp = this.head;
            if(this.head.equals(this.tail))
            {
                this.head = null;
                this.tail = null;
            }
            else
            {
                this.head = this.head.getNext();
                this.head.setPrev(null);
            }
            this.size--;
            return data;
        }
        throw new IllegalStateException();
    }
    
    public void remove(T d) throws IllegalStateException
    {
        if(!isEmpty())
        {
            PQnode temp = this.head;
            if(this.head.equals(this.tail))
            {
                this.head = null;
                this.tail = null;
            }
            else
            {
                while(!temp.getData().equals(d))
                {
                    temp = temp.getNext();
                }
                if(temp.getData().equals(d))
                {
                    if(temp == this.head)
                    {
                        head = temp.getNext();
                        temp.getNext().setPrev(null);
                    }
                    else if(temp == this.tail)
                    {
                        tail = temp.getPrev();
                        temp.getPrev().setNext(null);
                    }
                    else
                    {                        
                        temp.getPrev().setNext(temp.getNext());
                        temp.getNext().setPrev(temp.getPrev());
                    }
                }
                else
                {
                    System.out.println("remove() failed. No such value exists");
                    return;
                }
            }
            this.size--;
            return;
        }
        throw new IllegalStateException();
    }
    
    public int index(T data)
    {
        // index of -1 signifies value is not present or list is empty
        
        if(isEmpty())
            return -1;
        
        boolean isFound = false;
        
        PQnode tmp = this.head;
        int count = 0;
        
        do
        {
            if(tmp.getData().equals(data))
                isFound = true;
            else
            {
                tmp = tmp.getNext();
                ++count;
            }
        }
        while(tmp != null && !isFound);
        
        if(!isFound)
           return -1;   // -1 can be used to check if the pQueue contains a value
        
        return count;
    }
    
    public boolean contains(T value)
    {
        return index(value) >= 0;
    }
    
    // doesnt work
    public PQueue<T> copy()
    {
        return new PQueue<T>(this.head, this.tail, this.size);
    }
    
    public void printQueue()
    {
        PQnode temp = this.head;
        System.out.println("\n\t======== PRINTING QUEUE ========");
        while(temp != null)
        {
            System.out.println("\t - " + temp.getData());
            temp = temp.getNext();
        }
        System.out.println("\n\t======== FINISHED PRINTING ========");
    }
    
    public int size()
    {
        return this.size;
    }

    public PQnode<T> getHead()
    {
        return head;
    }

    public void setHead(PQnode<T> head)
    {
        this.head = head;
    }

    public PQnode<T> getTail()
    {
        return tail;
    }

    public void setTail(PQnode<T> tail)
    {
        this.tail = tail;
    }

    public int getSize()
    {
        return size;
    }

    public void setSize(int size)
    {
        this.size = size;
    }
    
    
}
