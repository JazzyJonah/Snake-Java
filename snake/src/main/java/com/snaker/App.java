package com.snaker;

import org.lwjgl.*;

public class App {
  public static void main(String[] args) {    
    System.out.println("Hello LWJGL! " + Version.getVersion());
    Game game = new Game();
    System.out.println("Game over! Score: " + game.gameState().score());
  }
}