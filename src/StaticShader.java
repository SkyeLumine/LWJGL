public class StaticShader extends ShaderProgram {


    private final static String VERTEX_SHADER = "res/vertex_shader";
    private final static String FRAGMENT_SHADER = "res/fragment_shader";

    public StaticShader() {
        super(VERTEX_SHADER, FRAGMENT_SHADER);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
}
