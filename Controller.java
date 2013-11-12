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
        
        MapPanel mapPanel = new MapPanel();
        MenuPanel menuPanel = new MenuPanel();
        
        mapPanel.setBackground(Color.WHITE);
        menuPanel.setBackground(Color.LIGHT_GRAY);
        
        frame.setTitle("Mammoth Site");
        frame.setLayout(new BorderLayout());
        frame.add(mapPanel, BorderLayout.CENTER);
        frame.add(menuPanel, BorderLayout.WEST);
        
        // add drawings and menut to Panels
        
        frame.setVisible(true);
        mapPanel.setVisible(true);
        menuPanel.setVisible(true);
    }
    
    }
