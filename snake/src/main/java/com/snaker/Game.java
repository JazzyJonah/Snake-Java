package com.snaker;
// imports are copied from https://www.lwjgl.org/
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Game {

    private long window;
    private GameState gameState;
    private int windowSize;

    private final int frameRate = 30;

    public Game(){
        // Create a new game
        gameState = new GameState();
        windowSize = gameState.windowSize;
        // initiate glfw and loop it
        init();
        loop();
    }

    public GameState gameState(){
        // Return the game state
        return gameState;
    }

    public void init(){
        // This code was partially written in https://www.lwjgl.org/, 
        // but I've adapted it to the needs of this project.
        // This initiates the game window and allows the game to be played
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        window = glfwCreateWindow(windowSize, windowSize, "Snake", 0, 0);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            // If the escape key was pressed, we will close the window
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
            }

            if (key == GLFW_KEY_UP && action == GLFW_PRESS){
                gameState.addToQueue(0);
            }
            else if (key == GLFW_KEY_RIGHT && action == GLFW_PRESS){
                gameState.addToQueue(1);
            }
            else if (key == GLFW_KEY_DOWN && action == GLFW_PRESS){
                gameState.addToQueue(2);
            }
            else if (key == GLFW_KEY_LEFT && action == GLFW_PRESS){
                gameState.addToQueue(3);
            }
        });

        // Make the OpenGL context current
            glfwMakeContextCurrent(window);
            // Enable v-sync
            glfwSwapInterval(1);

        glfwShowWindow(window);
    }

    public void loop(){
        // This code was partially written in https://www.lwjgl.org/, 
        // but I've adapted it to the needs of this project.
            // Specifically, I added the sleep loop and the clearWindow(), drawPixel(), and drawGameState() methods
        // This is the loop in which input is repeatedly taken and the game state is updated
        GL.createCapabilities(); // This line is necessary for LWJGL to work properly
                                // Not exactly sure why
        while(!glfwWindowShouldClose(window)){
            glfwSwapBuffers(window);
            glfwPollEvents();

            clearWindow();
            gameState.update();


            // Wait for the next frame to start
            try {
                Thread.sleep(1000/frameRate);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            drawGameState();
            if(gameState.over()){
                glfwSetWindowShouldClose(window, true);
            }
            
        }
    }

    public void drawGameState(){
        // This method will draw the game state to the screen

        // First, we will draw the snake
        ListNode current = gameState.coords();
        while (current != null){
            int[] coord = current.getValue();
            drawPixel(coord[0]*gameState.pixelSize, coord[1]*gameState.pixelSize, Math.random(), Math.random(), Math.random());
            current = current.getNext();
        }

        // Then, we will draw the apple
        int[] apple = gameState.apple();
        drawPixel(apple[0]*gameState.pixelSize, apple[1]*gameState.pixelSize, 1, 0, 0);
    }
    public void drawPixel(int x, int y, double red, double green, double blue){
        // This method will draw a pixel at the given x and y coordinates
        // The pixel will be a square of size gameState.pixelSize
        // System.out.println("Drawing pixel at " + x + ", " + y);
        
        GL11.glColor3f((float)red, (float)green, (float)blue);
        glBegin(GL_QUADS);

        // NOTE: center is (0,0), top left is (-1, 1)
        float topLeftX = (float)x/windowSize*2-1;
        float topLeftY = (float)y/windowSize*-2+1;

        glVertex2f(topLeftX, topLeftY);
        glVertex2f(topLeftX+(float)gameState.pixelSize/windowSize*2, topLeftY);
        glVertex2f(topLeftX+(float)gameState.pixelSize/windowSize*2, topLeftY-(float)gameState.pixelSize*2/windowSize);
        glVertex2f(topLeftX, (float)topLeftY-(float)gameState.pixelSize*2/windowSize);

        glEnd();
        glLoadIdentity(); 

    }
    public void clearWindow(){
        // This method will clear the window, so the snake doesn't lag
        GL11.glColor3f(0, 0, 0);
        glBegin(GL_QUADS);
        glVertex2f(-1, 1);
        glVertex2f(1, 1);
        glVertex2f(1, -1);
        glVertex2f(-1, -1);
        glEnd();
        glLoadIdentity();
    }
}