/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dataStructures.pQueue;

/**
 *
 * @author rayan
 * @param <T>
 */
public class PQnode<T extends Comparable<T>>
{
    private T data;
    private PQnode<T> next;
    private PQnode<T> prev;
    private int priority;
    
    public PQnode(T data, PQnode next, PQnode prev, int priority)
    {
        this.data       = data;
        this.next       = next;
        this.prev       = prev;
        this.priority   = priority;
    }

    public T getData()
    {
        return data;
    }

    public void setData(T data)
    {
        this.data = data;
    }

    public PQnode<T> getNext()
    {
        return next;
    }

    public void setNext(PQnode<T> next)
    {
        this.next = next;
    }

    public int getPriority()
    {
        return priority;
    }

    public void setPriority(int priority)
    {
        this.priority = priority;
    }

    public PQnode<T> getPrev()
    {
        return prev;
    }

    public void setPrev(PQnode<T> prev)
    {
        this.prev = prev;
    }
    
}
