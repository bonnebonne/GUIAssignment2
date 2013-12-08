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
        //frame.getSize(null)
        //MapPanel mapPanel = new MapPanel();
        MenuPanel menuPanel = new MenuPanel();
        DetailSlider = new JSlider();
        DetailSlider.setMaximum(16);
        DetailSlider.setMinimum(1);
        menuPanel.add(DetailSlider);
        
        
        //JScrollPane scroller = new JScrollPane(mapPainter);
        //scroller.setPreferredSize(new Dimension(200,200));
        MapPainter mapPainter = new MapPainter();
        DetailSlider.addChangeListener(mapPainter);

        //set up the map panel
        mapPanel = new MapPanel();
        mapPainter.setFocusable(true);
        mapPainter.addMouseListener(mapPainter);
        mapPainter.addMouseWheelListener(mapPainter);
        mapPainter.addMouseMotionListener(mapPainter);
        mapPainter.controller = this;
        frame.addMouseListener(mapPainter);
        frame.addMouseWheelListener(mapPainter);
        frame.setFocusable(true);
        
        //set up the painter
        mapPanel.add(mapPainter);
        
        menuPanel.setBackground(Color.LIGHT_GRAY);
        //scroller.setPreferredSize(new Dimension(6000, 6000));
        frame.setTitle("Mammoth Site");
        frame.setLayout(new BorderLayout());

        //mapPainter.setPreferredSize(new Dimension(10, 10));
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new ScrollPaneLayout());
     
        frame.getContentPane().add(menuPanel, BorderLayout.WEST);
        frame.getContentPane().add(mapPainter, BorderLayout.CENTER);
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null); // Center window in center of screen
        // pass into mapPanel
        frame.setVisible(true);
        //scroller.setVisible(true);
        // add drawings and menu to Panels
        //mapPanel.setVisible(true);
        //menuPanel.setVisible(true);
        
    }
    
    }
