/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package guiassignment2;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import guiassignment2.*;

/**
 *
 * @author Ben
 */
public class Controller {
    
    
    public Controller()
    {
        MainFrame frame = new MainFrame();
     //   frame.getSize(null)
        MapPanel mapPanel = new MapPanel();
        MenuPanel menuPanel = new MenuPanel();
        //MapPainter pnt = new MapPainter();
        
        mapPanel.setBackground(Color.WHITE);
        menuPanel.setBackground(Color.LIGHT_GRAY);
        
        frame.setTitle("Mammoth Site");
        frame.setLayout(new BorderLayout());
        frame.getContentPane().add(new MapPainter(), BorderLayout.CENTER);
     
        frame.getContentPane().add(menuPanel, BorderLayout.WEST);
        frame.setBounds(0,0,800, 800);
        frame.setLocationRelativeTo(null); // Center window in center of screen
        // pass into mapPanel
        frame.setVisible(true);
        // add drawings and menu to Panels
        mapPanel.setVisible(true);
        menuPanel.setVisible(true);
        
    }
    
    }
