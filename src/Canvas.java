import java.awt.*;
import java.awt.event.*;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

/*
The interactive canvas, extended from Swing's JComponent

https://www.tutorialspoint.com/swing/swing_jcomponent.htm
*/

public class Canvas extends JComponent
        implements Colorable {

    private int X1, Y1, X2, Y2;
    private Graphics2D g; // Seems like the right class to use?
                          // https://www.tutorialspoint.com/awt/awt_graphics2d_class.htm
    private Image img;
    private Color bgColor = Color.WHITE; // Background is white by default
    private Color toolColor = Color.BLACK; // Tool color is black
    private Pencil pencil = new Pencil(this);
    private Brush brush   = new Brush(this);
    private Eraser eraser = new Eraser(this);

    private MouseListener listener;
    private MouseMotionListener motion;

    // ------------------------------------------------------------------------

    public Canvas() {
        //System.out.println("Canvas constructor");
        defaultListener();
    }

    // Implemented from interface, refers to background
    public Color getColor() { return bgColor; }

    public void setColor(Color color) {
        bgColor = color;
        clear();
        //System.out.println(bgColor.toString());
    }

    public Color getToolColor() { return toolColor; }
    public void setToolColor(Color color) { toolColor = color; }

    public MouseListener getListener() { return listener; }
    public MouseMotionListener getMotion() { return motion; }

    // Override from JComponent
    protected void paintComponent(Graphics g1) {
        if (img == null) {
            img = createImage(getSize().width, getSize().height);
            g = (Graphics2D) img.getGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            clear();
            repaint();

        }
        g1.drawImage(img, 0, 0, null);
    }

    public void clear() {
        g.setPaint(bgColor);
        g.fillRect(0, 0, getSize().width, getSize().height);
        g.setPaint(Color.BLACK);
        repaint();
    }

    public void save(File file) {
        try {
            ImageIO.write((RenderedImage) img, "PNG", file);
        }

        catch (IOException ex) {
            System.out.println("Exception occurred: " + ex);
        }
    }

    public void load(File file) {
        try {
            img = ImageIO.read(file);
            g = (Graphics2D) img.getGraphics();
            repaint();
        }

        catch (IOException ex) {
            System.out.println("Exception occurred: " + ex);
        }
    }

    // TODO: rewrite calls?
    public void callPencil() {
        pencil.use();
        pencil.setColor(toolColor);
        g.setPaint(pencil.getColor());
    }

    public void callBrush(int width) {
        brush.use(width);
        brush.setColor(toolColor);
        g.setPaint(brush.getColor());
    }

    public void callEraser(int width) {
        eraser.use(width);
        eraser.setColor(bgColor);
        g.setPaint(eraser.getColor());
    }

    public void setThickness(int thick) { g.setStroke(new BasicStroke(thick)); }

    public void defaultListener() {
        setDoubleBuffered(false);
        listener = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                X2 = e.getX();
                Y2 = e.getY();
            }
        };

        motion = new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                X1 = e.getX();
                Y1 = e.getY();

                if (g != null) {
                    g.drawLine(X2, Y2, X1, Y1);
                    repaint();
                    X2 = X1;
                    Y2 = Y1;
                }
            }
        };
        addMouseListener(listener);
        addMouseMotionListener(motion);
    }
}