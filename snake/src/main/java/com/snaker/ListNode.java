package com.snaker;

// Modified ListNode class specifically for arrays
public class ListNode{
    private int[] value;
    private ListNode next;
    
    public ListNode(int[] initValue, ListNode initNext){
      // Constructor
      value = initValue; next = initNext;
    }
    
    public int[] getValue() {return value;}
    public ListNode getNext() {return next;}
  
    public void setValue(int[] theNewValue) {value = theNewValue;}
    public void setNext(ListNode theNewNext) {next = theNewNext;}
  }