import java.awt.*;

public class Brush extends Tool {
    protected static final int DEFAULT_WIDTH = 3;
    private int width;

    // ------------------------------------------------------------------------

    public Brush() { this(DEFAULT_WIDTH, DEFAULT_COLOR); }
    public Brush(int width) { this(width, DEFAULT_COLOR); }
    public Brush(Color color) { super(color); }
    public Brush(Canvas canvas) { super(canvas); }
    public Brush(int width, Color color) {
        this.width = width;
        setColor(color);
    }

    public void use(int width) {
        canvas.removeMouseListener(canvas.getListener());
        canvas.removeMouseMotionListener(canvas.getMotion());
        canvas.defaultListener();
        this.width = width;
        canvas.setThickness(width);
    }

    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }

    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }
}
