package canaryprism;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;

public class Main {

    public static String version = "1.1.0";

    static volatile int sides = 5;

    public static void main(String[] args) {
        FlatMacDarkLaf.setup();

        var frame = new JFrame("Star Drawer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        class StarDrawer extends JComponent {

            static final Dimension min_size = new Dimension(100, 100);
            static final Dimension pref_size = new Dimension(400, 400);


            @Override
            public void paintComponent(java.awt.Graphics g) {
                class Pencil {
                    java.awt.Graphics g;
                    double x = 0, y = 0;
                    double degrees = 0;
                    boolean down = false;

                    Pencil(java.awt.Graphics g) {
                        this.g = g;
                    }
                    void moveTo(double x, double y) {
                        if (this.down) {
                            this.g.drawLine((int) Math.round(this.x), (int) Math.round(this.y), (int) Math.round(x), (int) Math.round(y));
                        }
                        this.x = x;
                        this.y = y;
                    }
                    void turn(double degrees) {
                        this.degrees += degrees;
                    }
                    void move(double distance) {

                        double newx = (this.x + distance * Math.cos(Math.toRadians(degrees - 90)));
                        double newy = (this.y + distance * Math.sin(Math.toRadians(degrees - 90)));

                        if (this.down) {
                            this.g.drawLine((int) Math.round(this.x), (int) Math.round(this.y), (int) Math.round(newx), (int) Math.round(newy));
                        }

                        this.x = newx;
                        this.y = newy;
                    }

                    void pencilDown() {
                        this.down = true;
                    }
                    void pencilUp() {
                        this.down = false;
                    }
                }

                var pencil = new Pencil(g);


                class Point {
                    double x, y;
                    Point(double x, double y) {
                        this.x = x;
                        this.y = y;
                    }
                }
                var verteces = new Point[sides];

                var size = getSize();
                var radius = Math.min(size.width, size.height) / 2 - 10;


                for (int i = 0; i < sides; i++) {
                    pencil.moveTo(size.width / 2, size.height / 2);
                    
                    pencil.move(radius);
                    
                    verteces[i] = new Point(pencil.x, pencil.y);
                    pencil.turn(360.00 / sides);
                }

                
                for (int k = 2; k < (sides / 2) + 1; k++) {
                    for (int i = 0; i < sides; i++) {
                        pencil.moveTo(verteces[i % sides].x, verteces[i % sides].y);
                        pencil.pencilDown();
                        pencil.moveTo(verteces[(i + k) % sides].x, verteces[(i + k) % sides].y);
                        pencil.pencilUp();
                    }
                }

            }

            @Override
            public Dimension getMinimumSize() {
                return min_size;
            }
            @Override
            public Dimension getPreferredSize() {
                return pref_size;
            }
        }

        var draw_panel = new StarDrawer();

        frame.getContentPane().add(draw_panel);

        var bottom_panel = new JPanel();
        bottom_panel.setLayout(new BoxLayout(bottom_panel, BoxLayout.Y_AXIS));

        var control_panel = new JPanel();
        control_panel.setLayout(new BorderLayout());



        var sides_panel = new JPanel();
        sides_panel.setLayout(new FlowLayout(FlowLayout.LEFT));

        var slider = new JSlider(0, 100, 5);
        var label = new JLabel(STR."Sides: \{sides}");
        slider.addChangeListener((e) -> {
            sides = slider.getValue();
            draw_panel.repaint();
            label.setText("Sides: " + sides);
        });

        sides_panel.add(slider);
        sides_panel.add(label);
        sides_panel.add(new JSeparator(SwingConstants.HORIZONTAL));

        control_panel.add(sides_panel, BorderLayout.LINE_START);

        var button_panel = new JPanel();
        button_panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        var increase_button = new javax.swing.JButton("Increase");
        var decrease_button = new javax.swing.JButton("Decrease");
        increase_button.addActionListener((e) -> {
            slider.setValue(sides + 1);
        });
        decrease_button.addActionListener((e) -> {
            slider.setValue(sides - 1);
        });

        button_panel.add(decrease_button);
        button_panel.add(increase_button);

        control_panel.add(button_panel, BorderLayout.LINE_END);

        bottom_panel.add(control_panel);

        var info_panel = new JPanel(new BorderLayout());

        var version_label = new JLabel(" Star Drawer ver: " + version);
        var author_label = new JLabel("by Canary Prism ");

        info_panel.add(version_label, BorderLayout.LINE_START);
        info_panel.add(author_label, BorderLayout.LINE_END);

        bottom_panel.add(info_panel);
        bottom_panel.setMinimumSize(bottom_panel.getPreferredSize());


        frame.getContentPane().add(bottom_panel, BorderLayout.PAGE_END);


        frame.setMinimumSize(frame.getContentPane().getMinimumSize());
        frame.pack();
        frame.setVisible(true);
    }
}
