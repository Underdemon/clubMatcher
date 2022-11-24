/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dataStructures.pQueue;

/**
 *
 * @author rayan
 */
public class pQnode<T extends Comparable<T>>
{
    private T data;
    private pQnode<T> next;
    private pQnode<T> prev;
    private int priority;
    
    public pQnode(T data, pQnode next, pQnode prev, int priority)
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

    public pQnode<T> getNext()
    {
        return next;
    }

    public void setNext(pQnode<T> next)
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

    public pQnode<T> getPrev()
    {
        return prev;
    }

    public void setPrev(pQnode<T> prev)
    {
        this.prev = prev;
    }
    
}
