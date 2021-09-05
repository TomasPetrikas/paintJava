import java.awt.*;

/*
A brush that can only have the same color as the canvas
*/

public class Eraser extends Brush {
    protected Color color = canvas.getColor(); // Canvas BG color

    // ------------------------------------------------------------------------

    public Eraser(Canvas canvas) { super(canvas); }

    // Override from Tool
    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = canvas.getColor(); }
}