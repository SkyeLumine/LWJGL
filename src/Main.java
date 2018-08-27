import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;
    private static final String TITLE = "Hello, World!";

    private long windowHandle;

    private Loader loader;
    private Renderer renderer;

    private RawModel model;
    private StaticShader staticShader;

    float[] vertices = {
            -0.5f, 0.5f, 0.0f,  // Top left
            0.5f, 0.5f, 0.0f,   // Top right
            0.5f, -0.5f, 0.0f,  // Bottom right
            -0.5f, -0.5f, 0.0f  // Bottom left
    };

    int[] indicies = {
            0, 3, 1,    // Left triangle
            3, 2, 1     // Right triangle
    };

    public static void main(String[] args) {
        new Main().run();
    }

    public void run(){
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        loader = new Loader();
        renderer = new Renderer();

        init();
        loop();
    }

    public void init(){

        // All errors will be directed to std err
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize glfw
        if(!glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Invisible and resizable
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        windowHandle = glfwCreateWindow(WIDTH, HEIGHT, TITLE, NULL, NULL);
        if(windowHandle == NULL){
            throw new RuntimeException("Failed to create GLFW window");
        }

        glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
            if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE){
                glfwSetWindowShouldClose(windowHandle, true);
            }
        });

        try(final MemoryStack stack = stackPush()){
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(windowHandle, pWidth, pHeight);

            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(
                    windowHandle,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }

        glfwMakeContextCurrent(windowHandle);

        // v-sync
        glfwSwapInterval(1);

        glfwShowWindow(windowHandle);
    }

    public void loop(){
        GL.createCapabilities();
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        model = loader.loadToVAO(vertices, indicies);
        staticShader = new StaticShader();


        while(!glfwWindowShouldClose(windowHandle)){
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            staticShader.start();
            renderer.render(model);
            staticShader.stop();
            glfwSwapBuffers(windowHandle);
            glfwPollEvents();
        }

        staticShader.cleanUp();
        loader.cleanup();
    }
}
