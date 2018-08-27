import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.system.CallbackI;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Loader {

    private final List<Integer> vaos = new ArrayList<>();
    private final List<Integer> vbos = new ArrayList<>();

    public RawModel loadToVAO(float[] positions, int[] indicies){
        final int vao = createVAO();
        bindIndexBuffer(indicies);
        storeDataInAttributes(0, positions);
        unbindVao();

        return new RawModel(vao, indicies.length);
    }

    private int createVAO(){
        final int vao = GL30.glGenVertexArrays();
        vaos.add(vao);
        GL30.glBindVertexArray(vao);
        return vao;
    }

    private void storeDataInAttributes(final int attributeNumber, final float[] data){
        int vbo = GL15.glGenBuffers();
        vbos.add(vbo);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);

        final FloatBuffer floatBufferData = storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, floatBufferData, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, 3, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void unbindVao(){
        GL30.glBindVertexArray(0);
    }

    private void bindIndexBuffer(final int[] indicies){
        int vbo = GL15.glGenBuffers();
        vbos.add(vbo);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo);
        final IntBuffer buffer = storeDataInIntBuffer(indicies);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    final IntBuffer storeDataInIntBuffer(final int[] data){
        final IntBuffer intBuffer = BufferUtils.createIntBuffer(data.length);
        intBuffer.put(data);
        intBuffer.flip();
        return intBuffer;
    }

    private FloatBuffer storeDataInFloatBuffer(final float[] data){
        final FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(data.length);
        floatBuffer.put(data);
        floatBuffer.flip();
        return floatBuffer;
    }

    public void cleanup(){
        for(final int vao : vaos){
            GL30.glDeleteVertexArrays(vao);
        }

        for(final int vbo : vbos){
            GL30.glDeleteBuffers(vbo);
        }
    }
}
