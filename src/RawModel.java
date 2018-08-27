public class RawModel {

    private final int vaoId;
    private final int vertexCount;

    public RawModel(final int vaoId, final int vertexCount){
        this.vaoId = vaoId;
        this.vertexCount = vertexCount;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public int getVaoId() {
        return vaoId;
    }
}
