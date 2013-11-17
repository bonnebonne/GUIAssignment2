// GeneralPathPanel.java
// Simple program illustrating graphics programming in Java.
// JMW 081031

package guiassignment2;

import guiassignment2.Controller;
//import  guiassignment2.XMLParse.listChildren;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import org.jdom2.Element;


public class MapPainter extends JPanel
{
    private GeneralPath gp;
    public Double[] scaleMeters;

    public MapPainter()
    {
        gp = new GeneralPath();
        // get simple, normal, detailed copies of bone records
        Double[] temp;
        //polyLines lines = new polyLines();
        //ArrayList<ArrayList<Element>> bones = lines.getBones();
        ArrayList<Double[]> points = getWalkway();

        gp.moveTo(points.get(0)[0]*10.0, points.get(0)[1]*10.0);
        gp.lineTo(points.get(0)[2]*10.0, points.get(0)[3]*10.0);
        gp.closePath();
        for(int i = 1; i < points.size(); i++)
        {
            temp = points.get(i);
            gp.moveTo(temp[0]*10.0,temp[1]*10.0);
            gp.lineTo(temp[2]*10.0, temp[3]*10.0);
            gp.closePath();
        }
        gp.closePath();
        
//        gp.moveTo( 10, 10 );
//        gp.lineTo( 10, 90 );
//        gp.lineTo( 90, 90 );
//        gp.lineTo( 10, 10 );
//        gp.closePath();
    }

    // paintComponent() is the display callback function
    public void paintComponent( Graphics g )
    {
        super.paintComponent( g );	// call the base class constructor

        Graphics2D g2d = ( Graphics2D )g;		// get graphics context
        AffineTransform at = g2d.getTransform();	// get current transform

	// draw triangle outline
        g2d.setColor( Color.BLUE );
        g2d.draw( gp );
    }

    // set initial panel size
    public Dimension getPreferredSize ()
    {
        return new Dimension( 320, 320 );
    }

    public Dimension getMinimumSize ()
    {
        return new Dimension(480, 480);//this.getPreferredSize();
    }
    
    private ArrayList<Double[]> getWalkway()
    {
        XMLParse walkway = new XMLParse("bonexml/walkway.xml");
        Element root;
        int i = 0, points;
        Double xmin, ymin, xmax, ymax;
        Double[] xyMinMax;
        String Area;
        String[] x_y;
        if(walkway.getRoot() != null)
            root = walkway.getRoot();
        else root = null;
        java.util.List<Element> path = root.getChildren("xml");
        points = path.size();
        java.util.List<Element> children = root.getChildren();
        Area = children.get(1).getValue();
        Area = Area.concat(children.get(2).getValue());
        Area = Area.replace("  ", " ");
        Area = Area.trim();
        x_y = Area.split(" ");
        scaleMeters = new Double[2];
        scaleMeters[0] = 0.0; scaleMeters[1] = 0.0;

        xyMinMax = getXY(x_y); 

        scaleMeters[0] = xyMinMax[0];
        scaleMeters[1] = xyMinMax[1];
        children = children.get(3).getChildren();
        //Double[] xyPoints;
        ArrayList<Double[]> xyPoints = new ArrayList<Double[]>();
        
        for(i = 5; i < children.size()-1; i++)
        {
            Area = children.get(i).getValue();
            Area = Area.replace("\n", "");
            Area = Area.replace("  ", " ");
            Area = Area.trim();
            x_y = Area.split(" ");
            xyPoints.add(getXY(x_y));
        }        
        return xyPoints;
    }

    public Double[] getXY(String[] x_y)
    {
        Double[] points = new Double[x_y.length];
        Double temp;
        for(int i = 0; i < x_y.length; i++)
        {
            // need to scale points
            if(i%2 == 0)
                temp = Double.parseDouble(x_y[i]) - scaleMeters[0];
            else
                temp = Double.parseDouble(x_y[i]) - scaleMeters[1];
            points[i] = temp;
        }   
        return points;
    }
}