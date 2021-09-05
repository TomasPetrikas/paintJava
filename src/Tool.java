import java.awt.*;

abstract class Tool implements Colorable {
    protected static final Color DEFAULT_COLOR = Color.BLACK;
    protected Color color;
    public Canvas canvas;

    // ------------------------------------------------------------------------

    //Overloaded constructors
    public Tool() { setColor(DEFAULT_COLOR); }
    public Tool(Color color) { setColor(color); }
    public Tool(Canvas canvas) { this.canvas = canvas; }

    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }

}