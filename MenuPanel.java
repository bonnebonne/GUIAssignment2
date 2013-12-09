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
public class MenuPanel extends JPanel{
    //MapPainter pnt = new MapPainter();
    
    public JPanel infoPanel;
    public JLabel Title;
    public JLabel BoneImage;
    public JLabel UniqueID;
    public JLabel ObjectNum;
    public JLabel Taxon;
    public JLabel Element;
    public JLabel SubElement;
    public JLabel Side;
    public JLabel Completeness;
    public JLabel Expside;
    public JLabel Articulate;
    public JLabel Gender;
    public JLabel DateFound;
    public JLabel Elevation;
    public JLabel ObjectID;
    public JLabel shapeLength;
    public JButton resetButton;
    public JButton exitButton;
    public JPanel buttonPanel;

    // stores a bone record data element    
    public MenuPanel()
    {
        //set the min and max size so the layout doesn't resize the menu wildly
        this.setPreferredSize(new Dimension(360, 780));
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        
        //set up the information panel, which displays the bone info
        infoPanel= new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        
        //set the min and max size so the layout doesn't resize the menu wildly
        infoPanel.setMaximumSize(new Dimension(320, 500));
        infoPanel.setMinimumSize(new Dimension(320, 0));
        infoPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        
        //set the information for the button panel and add the buttons
        buttonPanel = new JPanel();
        resetButton = new JButton("Reset View");
        exitButton = new JButton("Exit");
        buttonPanel.add(resetButton);
        buttonPanel.add(exitButton);
        buttonPanel.setBackground(Color.lightGray);
        buttonPanel.setPreferredSize(new Dimension(320, 36));
        buttonPanel.setMaximumSize(new Dimension(320, 36));
        buttonPanel.setMinimumSize(new Dimension(320, 36));
        
        Title= new JLabel();
        BoneImage= new JLabel();
        UniqueID= new JLabel();
        ObjectNum= new JLabel();
        Taxon= new JLabel();
        Element= new JLabel();
        SubElement= new JLabel();
        Side= new JLabel();
        Completeness= new JLabel();
        Expside= new JLabel();
        Articulate= new JLabel();
        Gender= new JLabel();
        DateFound= new JLabel();
        Elevation= new JLabel();
        ObjectID= new JLabel();
        shapeLength= new JLabel();
    
        infoPanel.add(Title);
        infoPanel.add(BoneImage);
        infoPanel.add(UniqueID);
        infoPanel.add(ObjectNum);
        infoPanel.add(Taxon);
        infoPanel.add(Element);
        infoPanel.add(SubElement);
        infoPanel.add(Side);
        infoPanel.add(Completeness);
        infoPanel.add(Expside);
        infoPanel.add(Articulate);
        infoPanel.add(Gender);
        infoPanel.add(DateFound);
        infoPanel.add(Elevation);
        infoPanel.add(ObjectID);
        infoPanel.add(shapeLength);
        
        infoPanel.setVisible(false);
    }        
    

    
}
