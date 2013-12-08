/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package guiassignment2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 *
 * @author Ben
 */
public class Controller  {
    
    public MapPanel mapPanel;
    public MainFrame frame;
    public JSlider DetailSlider;
    
    public Controller()
    {    
      
        //set up the main frame and menu panel
        frame = new MainFrame();
        
        // create menu panel
        MenuPanel menuPanel = new MenuPanel();
        //set menu panels layout 
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        
        // add label to menu panel
        JLabel label = new JLabel("Detail");
        menuPanel.add(label);
        
        // add detail slider to menu panel
        DetailSlider = new JSlider();
        DetailSlider.setMaximum(16);
        DetailSlider.setMinimum(1);
        menuPanel.add(DetailSlider);
        
        // make map Painter(panel) listen to DetailSlider
        MapPainter mapPainter = new MapPainter();
        DetailSlider.addChangeListener(mapPainter);

        //set up the map panel with map painter nested in it
        mapPanel = new MapPanel();
        
        // make map panel focusable and and listeners and events
        mapPainter.setFocusable(true);
        mapPainter.addMouseListener(mapPainter);
        mapPainter.addMouseWheelListener(mapPainter);
        mapPainter.addMouseMotionListener(mapPainter);
        mapPainter.controller = this;
        // add map panel to main frame
        frame.addMouseListener(mapPainter);
        frame.addMouseWheelListener(mapPainter);
        frame.setFocusable(true);
        
        //set up the painter
        mapPanel.add(mapPainter);
        
        menuPanel.setBackground(Color.LIGHT_GRAY);
        frame.setTitle("Mammoth Site");
        frame.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new ScrollPaneLayout());
     
        frame.getContentPane().add(menuPanel, BorderLayout.WEST);
        frame.getContentPane().add(mapPainter, BorderLayout.CENTER);
        frame.setSize(1200, 720);
        frame.setLocationRelativeTo(null); // Center window in center of screen
        
        // make contents of main frame visible
        frame.setVisible(true);     
    }    
}
