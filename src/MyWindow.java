import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;

public class MyWindow {
    private static final int minWidth = 400;
    private static final int minHeight = 400;

    private int width, height;
    private String currentTool = "Pencil";
    private Canvas canvas = new Canvas();

    private Icon pencilIcon = new ImageIcon(getClass()
            .getResource("pencil.png"));
    private Icon brushIcon = new ImageIcon(getClass()
            .getResource("paintbrush.png"));
    private Icon eraserIcon = new ImageIcon(getClass()
            .getResource("eraser.png"));

    private JButton brush, clearButton, colorPickerBG, colorPickerTools,
                    eraser, loadButton, pencil, rectangle, saveButton;
    private JFileChooser fileChooser;
    private File file;
    private JLabel thicknessStat;
    private JSlider thicknessSlider;
    //private Boolean sliderIsVisible = false;
    ChangeListener thick = new ChangeListener() {
        public void stateChanged(ChangeEvent e) {
            thicknessStat.setText(String.format("%s",
                    thicknessSlider.getValue()));
            if (currentTool == "Brush" || currentTool == "Eraser") {
                canvas.setThickness(thicknessSlider.getValue());
            }
        }
    };

    ActionListener listener = new ActionListener() {

        public void actionPerformed(ActionEvent event) {
            // Can't use switch structure here with Objects
            if (event.getSource() == pencil) {
                currentTool = "Pencil";
                canvas.callPencil();
                canvas.setThickness(1);
            }
            else if (event.getSource() == brush) {
                currentTool = "Brush";
                canvas.callBrush(thicknessSlider.getValue());
            }
            else if (event.getSource() == eraser) {
                currentTool = "Eraser";
                canvas.callEraser(thicknessSlider.getValue());
            }
            else if (event.getSource() == clearButton) {
                canvas.clear();
            }
            else if (event.getSource() == colorPickerBG) {
                Color color = JColorChooser.showDialog(null,
                        "Pick your color!", null);
                if (color == null)
                    color = (Color.WHITE);
                canvas.setColor(color);
            }
            else if (event.getSource() == colorPickerTools) {
                Color color = JColorChooser.showDialog(null,
                        "Pick your color!", null);
                if (color == null)
                    color = (Color.BLACK);
                canvas.setToolColor(color);

                // Tools need to be called for color to switch properly
                if (currentTool == "Pencil") {
                    canvas.callPencil();
                }
                else if (currentTool == "Brush") {
                    canvas.callBrush(thicknessSlider.getValue());
                }
                else if (currentTool == "Eraser") {
                    canvas.callEraser(thicknessSlider.getValue());
                }
            }
            else if (event.getSource() == saveButton) {
                fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "PNG Images", "png");
                fileChooser.setFileFilter(filter);
                fileChooser.setSelectedFile(new File("Untitled.png"));
                if (fileChooser.showSaveDialog(saveButton) ==
                        JFileChooser.APPROVE_OPTION) {
                    file = fileChooser.getSelectedFile();
                    canvas.save(file);
                }
            }
            else if (event.getSource() == loadButton) {
                fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "PNG Images", "png");
                fileChooser.setFileFilter(filter);
                if (fileChooser.showOpenDialog(loadButton) ==
                        JFileChooser.APPROVE_OPTION) {
                    file = fileChooser.getSelectedFile();
                    canvas.setColor(Color.WHITE);
                    canvas.load(file);
                    canvas.callEraser(thicknessSlider.getValue());
                }
            }
        }
    };

    // ------------------------------------------------------------------------

    public MyWindow() {
        //System.out.println("MyWindow constructor");
        showInput();
    }

    // 1st window: asks for resolution input
    private void showInput() {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        JPanel labels = new JPanel(new GridLayout(0,1,2,2));
        JPanel labels1 = new JPanel(new FlowLayout());
        labels.add(new JLabel("Width", SwingConstants.RIGHT));
        labels.add(new JLabel("Height", SwingConstants.RIGHT));
        labels1.add(new JLabel("(Minimum Width: " + minWidth + ", Height: " +
                minHeight + ")"));
        p.add(labels, BorderLayout.WEST);
        p.add(labels1, BorderLayout.SOUTH);

        JPanel controls = new JPanel(new GridLayout(0, 1, 2, 2));
        JTextField widthField = new JTextField();
        controls.add(widthField);
        JTextField heightField = new JTextField();
        controls.add(heightField);
        p.add(controls, BorderLayout.CENTER);
        JOptionPane.showMessageDialog(null, p, "Enter Canvas Width and Height",
                JOptionPane.QUESTION_MESSAGE);


        // TODO: pressing X to quit doesn't work with loop
        // Don't know how to detect if X is pressed?
        while (true) {
            try {
                width = Integer.parseInt(widthField.getText());
                height = Integer.parseInt(heightField.getText());
                if (width < minWidth || height < minHeight) {
                    throw new InvalidResolutionException("Invalid resolution");
                } else {
                    break;
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Exception occurred: " + e);
                JOptionPane.showMessageDialog(null, p,
                        "Please enter valid number!", JOptionPane.ERROR_MESSAGE);
            } catch (InvalidResolutionException e) {
                System.out.println("Exception occurred: " + e);
                JOptionPane.showMessageDialog(null, p,
                        "Width/Height Too Small!", JOptionPane.ERROR_MESSAGE);
            }
        }

        openPaint();
    }

    // 2nd window: opens main program
    public void openPaint() {
        //System.out.println(width); // For testing
        //System.out.println(height);

        JFrame frame = new JFrame("Paint ("+ width +" x " + height +")");
        Container container = frame.getContentPane();
        container.setLayout(new BorderLayout());

        container.add(canvas, BorderLayout.CENTER);

        // An area to contain tool icons, left of canvas
        Box box = Box.createVerticalBox();

        // Panel along the top of the canvas
        JPanel panel = new JPanel();

        pencil = new JButton(pencilIcon);
        pencil.setPreferredSize(new Dimension(80, 80));
        pencil.addActionListener(listener);
        brush = new JButton(brushIcon);
        brush.setPreferredSize(new Dimension(80, 80));
        brush.addActionListener(listener);
        eraser = new JButton(eraserIcon);
        eraser.setPreferredSize(new Dimension(80, 80));
        eraser.addActionListener(listener);
        /*rectangle = new JButton("Rectangle");
        rectangle.setPreferredSize(new Dimension(80, 80));
        rectangle.addActionListener(listener);*/
        thicknessSlider = new JSlider(JSlider.HORIZONTAL, 1, 40, 1);
        thicknessSlider.setMajorTickSpacing(10);
        thicknessSlider.setPaintTicks(true);
        thicknessSlider.setPreferredSize(new Dimension(40, 40));
        thicknessSlider.addChangeListener(thick);
        thicknessStat = new JLabel("1");
        clearButton = new JButton("Clear");
        clearButton.addActionListener(listener);
        colorPickerBG = new JButton("Color Picker (BG)");
        colorPickerBG.addActionListener(listener);
        colorPickerTools = new JButton("Color Picker (Tools)");
        colorPickerTools.addActionListener(listener);
        saveButton = new JButton("Save");
        saveButton.addActionListener(listener);
        loadButton = new JButton("Load");
        loadButton.addActionListener(listener);

        box.add(Box.createVerticalStrut(5));
        box.add(pencil, BorderLayout.NORTH);
        box.add(brush, BorderLayout.NORTH);
        box.add(eraser, BorderLayout.NORTH);
        box.add(thicknessSlider, BorderLayout.NORTH);
        box.add(thicknessStat, BorderLayout.NORTH);

        panel.add(clearButton);
        panel.add(colorPickerBG);
        panel.add(colorPickerTools);
        panel.add(saveButton);
        panel.add(loadButton);

        container.add(box, BorderLayout.WEST);
        container.add(panel, BorderLayout.NORTH);

        frame.setVisible(true);
        frame.setSize(width+100,height+100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}