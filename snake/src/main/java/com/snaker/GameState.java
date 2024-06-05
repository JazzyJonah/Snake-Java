package com.snaker;


import java.util.*;

public class GameState{
    private ListNode coords;
    private int score;
    private int direction; // 0 = up, 1 = right, 2 = down, 3 = left
    private int[] apple;
    private boolean over;
    private Queue<Integer> directionQueue;


    public final int windowSize = 800;
    public final int pixelSize = 20;
    public final int gridSize = windowSize/pixelSize;

    public GameState(){
        int[] initCoord = {0, 0};
        coords = new ListNode(initCoord, null);
        createApple();

        score = 1;
        direction = 1;
        over = false;
        directionQueue = new LinkedList<Integer>();

    }

    public ListNode coords(){
        return coords;
    }
    public int[] apple(){
        return apple;
    }
    public int score(){
        return score;
    }
    public int direction(){
        return direction;
    }
    public boolean over(){
        return over;
    }

    public void addToQueue(int newDirection){
        directionQueue.add(newDirection);
    }

    public boolean setDirection(int newDirection){
        // Make sure the direction isn't opposite the old direction
        if (direction-newDirection != 2 && direction-newDirection != -2 && direction != newDirection){
            direction = newDirection;
            return true;
        }
        return false;
    }
    public void createApple(){
        // Can't create an apple if the snake occupies the entire grid
        if (score == gridSize*gridSize){
            return;
        }

        // Create a new apple at a random location
        int[] newApple = {(int)(Math.random()*gridSize), (int)(Math.random()*gridSize)};
        ListNode current = coords;
        while (current != null){
            if (newApple[0] == current.getValue()[0] && newApple[1] == current.getValue()[1]){
                newApple[0] = (int)(Math.random()*gridSize);
                newApple[1] = (int)(Math.random()*gridSize);
                current = coords;
            }
            else{
                current = current.getNext();
            }
        }
        apple = newApple;
    }

    public void update(){
        // Do the next input in the input queue
        boolean input = false;
        while(!input && !directionQueue.isEmpty()){
            input = setDirection(directionQueue.poll());
        }
        

        // Update the snake's position
        int[] newHead = new int[2];
        int[] oldHead = coords.getValue();
        if (direction == 0){
            newHead[0] = oldHead[0];
            newHead[1] = oldHead[1]-1;
        }
        else if (direction == 1){
            newHead[0] = oldHead[0]+1;
            newHead[1] = oldHead[1];
        }
        else if (direction == 2){
            newHead[0] = oldHead[0];
            newHead[1] = oldHead[1]+1;
        }
        else if (direction == 3){
            newHead[0] = oldHead[0]-1;
            newHead[1] = oldHead[1];
        }

        ListNode newCoords = new ListNode(newHead, coords);
        coords = newCoords;

        // Check if the snake has eaten an apple
        if (newHead[0] == apple[0] && newHead[1] == apple[1]){
            score++;
            // Check if the player has won
            if (score == gridSize*gridSize){
                System.out.println("You win! Score: " + score);
                over = true;
            }
            else{
                createApple();
            }
        }
        else{
            // Remove the last element of the snake
            ListNode current = coords;
            while (current.getNext().getNext() != null){
                current = current.getNext();
            }
            current.setNext(null);
        }

        // Check if the snake has hit a wall
        if (newHead[0] < 0 || newHead[0] >= gridSize || newHead[1] < 0 || newHead[1] >= gridSize){
            over = true;
        }

        // Check if the snake has hit itself
        ListNode current = coords.getNext();
        while (current != null){
            if (newHead[0] == current.getValue()[0] && newHead[1] == current.getValue()[1]){
                over = true;
                break;
            }
            current = current.getNext();
        }

    }
}
  