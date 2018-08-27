import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;

public abstract class ShaderProgram {

    private int programId;
    private int vertexShaderId;
    private int fragmentShaderId;

    public ShaderProgram(final String vertexShaderFile, final String fragmentShaderFile){

        vertexShaderId = loadShader(vertexShaderFile, GL20.GL_VERTEX_SHADER);
        fragmentShaderId = loadShader(fragmentShaderFile, GL20.GL_FRAGMENT_SHADER);

        programId = GL20.glCreateProgram();

        GL20.glAttachShader(programId, vertexShaderId);
        GL20.glAttachShader(programId, fragmentShaderId);
        GL20.glLinkProgram(programId);
        GL20.glValidateProgram(programId);
    }

    public void start(){
        GL20.glUseProgram(programId);
    }

    public void stop(){
        GL20.glUseProgram(0);
    }

    public void cleanUp(){
        stop();
        GL20.glDetachShader(programId, vertexShaderId);
        GL20.glDetachShader(programId, fragmentShaderId);
        GL20.glDeleteShader(vertexShaderId);
        GL20.glDeleteShader(fragmentShaderId);
        GL20.glDeleteProgram(programId);
    }

    protected void bindAttribute(final int attribute, final String variableName){
        GL20.glBindAttribLocation(programId, attribute, variableName);
    }

    protected abstract void bindAttributes();

    public static int loadShader(final String file, final int type){
        try {
            final String source = new String(Files.readAllBytes(FileSystems.getDefault().getPath(file)));
            final int shaderId = GL20.glCreateShader(type);
            GL20.glShaderSource(shaderId, source);
            GL20.glCompileShader(shaderId);

            if(GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE){
                System.err.println(GL20.glGetShaderInfoLog(shaderId, 500));
                System.err.println("Trouble with shader ID " + shaderId + ". Failed to compile. Shutting down.");
                System.exit(1);
            }

            return shaderId;
        } catch(final IOException e){
            System.out.println(System.getProperty(("user.dir")));
            e.printStackTrace();
            System.exit(1);
        }

        return -1;
    }
}
