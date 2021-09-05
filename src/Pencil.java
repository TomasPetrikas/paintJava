import java.awt.*;

public class Pencil extends Tool {
    public Pencil() { this(DEFAULT_COLOR); }
    public Pencil(Color color) { super(color); }
    public Pencil(Canvas canvas) { super(canvas); }

    public void use() {
        canvas.removeMouseListener(canvas.getListener());
        canvas.removeMouseMotionListener(canvas.getMotion());
        canvas.defaultListener();
    }

    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }
}
