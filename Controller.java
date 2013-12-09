/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package guiassignment2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Ben
 */
public class Controller implements ActionListener  {
    
    //All objects in the GUI which need to be referenced by the Controller
    public MapPanel mapPanel;
    public MainFrame frame;
    public JSlider DetailSlider;
    public MenuPanel menuPanel;

    private MapPainter mapPainter;
    
    
    public Controller()
    {    

        
        //set up the main frame and menu panel
        frame = new MainFrame();
        
        // create menu panel
        menuPanel = new MenuPanel();
        //set menu panels layout 
        //menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        
        // add label to menu panel
        JLabel label = new JLabel("Detail");
        menuPanel.add(label);
        
        //add information to 
        
        // add detail slider to menu panel
        DetailSlider = new JSlider();
        DetailSlider.setBackground(Color.lightGray);
        DetailSlider.setMaximum(16);
        DetailSlider.setMinimum(1);
        menuPanel.exitButton.addActionListener(this);
        menuPanel.resetButton.addActionListener(this);
        menuPanel.add(DetailSlider);
        menuPanel.add(menuPanel.buttonPanel);
        
        
        //add bone information viewer
        menuPanel.infoPanel.setLayout(new BoxLayout(menuPanel.infoPanel, BoxLayout.Y_AXIS));
        menuPanel.add(menuPanel.infoPanel);
        
        
        
        // make map Painter(panel) listen to DetailSlider
        mapPainter = new MapPainter();
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
    
    public void showBoneInfo(String activeBone)
    {
        boolean found  = false;
        
        //find which set of bone information matches activeBone
        for(int i = 0; i < mapPainter.lines.uniqueID.size(); i++ )
        {
            //set the information in the info panel if the bone is found
            if(activeBone.equals(mapPainter.lines.uniqueID.get(i)))
            {
            menuPanel.Articulate.setText(" Articulate:           " + trimString(mapPainter.lines.articulate.get(i)));
            menuPanel.BoneImage.setIcon((Icon)getBoneIcon(trimString(mapPainter.lines.uniqueID.get(i))));
            menuPanel.Completeness.setText(" Completeness:  " + activeBone);
            menuPanel.Title.setText(" Bone Information");
            menuPanel.UniqueID.setText(" UniqueID:             " + trimString(mapPainter.lines.uniqueID.get(i)));
            menuPanel.ObjectNum.setText(" ObjectNum:        " + mapPainter.lines.objectnum.get(i));
            menuPanel.Taxon.setText(" Taxon:                  " + trimString(mapPainter.lines.taxon.get(i)));
            menuPanel.Element.setText(" Element:              " + mapPainter.lines.element.get(i));
            menuPanel.SubElement.setText(" SubElement:       " + trimString(mapPainter.lines.subElement.get(i)));
            menuPanel.Side.setText(" Side:                     " + trimString(mapPainter.lines.side.get(i)));
            menuPanel.Expside.setText(" Expside:              " + trimString(mapPainter.lines.expside.get(i)));
            menuPanel.Gender.setText(" Gender:                " + trimString(mapPainter.lines.gender.get(i)));
            menuPanel.DateFound.setText(" DateFound:         " + trimString(mapPainter.lines.datefound.get(i)));
            menuPanel.Elevation.setText(" Elevation:            " + mapPainter.lines.elevation.get(i));
            menuPanel.ObjectID.setText(" ObjectID:             " + mapPainter.lines.objectid.get(i));
            menuPanel.shapeLength.setText(" ShapeLength:    " + mapPainter.lines.shapelength.get(i));
            found = true;
            }
        }
        
        menuPanel.infoPanel.setVisible(found);
        
    }
    
    private String trimString(String str)
    {
        if(null != str)
        {
           return str.trim();
        }
        else
            return "No Information";
    }
    
    public ImageIcon getBoneIcon(String uniqueID)
    {
        ImageIcon icon;
        try
        {
            icon = new ImageIcon("bonexml/" + uniqueID + ".jpg");
        }
        catch(Exception e)
        {
            System.out.println( "bonexml/" + uniqueID + ".jpg" + " couldn't be opened" );
            System.out.println( e.getMessage() );
            icon = null;
        }
        return icon;
    }
    
    
    
    
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(((JButton)e.getSource()).getText().equals("Exit"))
        {
            System.exit(0);
        }
        else if (((JButton)e.getSource()).getText().equals("Reset View"))
        {
            mapPainter.resetView();
        }
            
    }
    
}
