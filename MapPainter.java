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


public class MapPainter extends JPanel implements MouseWheelListener, MouseListener, MouseMotionListener
{
    private GeneralPath gp;
    private ArrayList<GeneralPath> path;
    public Double[] scaleMeters;
    public polyLines lines;
    //private int drawNum;
    public Double scaleFactor;
    private int xPressed;
    private int yPressed;
    private int xOffset;
    private int yOffset;
    private int zoomLevel;
    public MapPainter()
    {
        // intialize drawing component
        gp = new GeneralPath();
        path = new ArrayList<GeneralPath>();
       
        // initialize scaleFactor
        scaleFactor = 15.0;
        zoomLevel = 0;
        // initialize class
        lines = new polyLines();
        xOffset = 0;
        yOffset = 0;
        // get data from xml records
        lines.getBoneRecs();
    }

    // paintComponent() is the display callback function
    public void paintComponent( Graphics g )
    {
        super.paintComponent( g );	// call the base class constructor
        ArrayList<Double[]> points = new ArrayList<Double[]>();
        points = getWalkway();
        AffineTransform transform = new AffineTransform();
        Double[] temp;
        Graphics2D g2d = ( Graphics2D )g;		// get graphics context

        int drawNum = 0;
        //GeneralPath newDraw = new GeneralPath();
        if(!path.isEmpty())
            path.clear();
        transform.scale( 1.0, -1.0 );
        //transform.translate(0, (scaleMeters[1]-scaleMeters[0])*scaleFactor);
        double offset = (scaleMeters[1]-scaleMeters[0])*scaleFactor;
        g2d.setTransform( transform );
        g2d.translate(xOffset, -yOffset - 500);
        path.add(new GeneralPath());
        //g2d.setStroke(new BasicStroke(2));
        //g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setColor(Color.BLACK);
        for(int i = 0; i < points.size(); i++)
        {
            temp = points.get(i);
            path.get(drawNum).moveTo(temp[0]*scaleFactor,temp[1]*scaleFactor);

            for(int j = 2; j < points.get(i).length;)
            {
                path.get(drawNum).lineTo(temp[j]*scaleFactor, temp[j+1]*scaleFactor);
                j+=2;
            }            
        }
        path.get(drawNum).closePath();

        //g2d.setColor(Color.red);
        g2d.draw(path.get(drawNum));
        drawNum += 1;
        path.add(new GeneralPath()); 

        for(int i = 0 ; i < lines.allPolyPoints.size(); i++)
        {
            g2d.setColor(setElevationColor(lines.elevation.get(i)));
            for(int j = 0; j < lines.allPolyPoints.get(i).size(); j++)
            {
                
                temp = lines.allPolyPoints.get(i).get(j);
                if(temp != null && lines.element.get(i) < 8)
                {
                    path.get(drawNum).moveTo((temp[0]-scaleMeters[0])*scaleFactor, (temp[1]-scaleMeters[1])*scaleFactor);
                    for(int k = 2; k < temp.length;)
                    {
                        path.get(drawNum).lineTo((temp[k]-scaleMeters[0])*scaleFactor , (temp[k+1]-scaleMeters[1])*scaleFactor);
                        k+=2;
                    }
                    //path.get(drawNum).closePath();
                }
            }
            //newDraw = (GeneralPath) newDraw.clone();
            g2d.draw(path.get(drawNum));
            path.add(new GeneralPath()); 
            drawNum += 1;
        }

        //g2d.setColor(Color.red);
        //g2d.draw( gp );
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
        
        for(i = 5; i < children.size(); i++)
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

    private Double[] getXY(String[] x_y)
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
    
    private Color setElevationColor(double elevation)
    {
        Color val;
        if(elevation <= -2.0)
            val = new Color(0, 159, 9);
        else if(elevation <= -1.5)
            val = new Color(145, 191, 0);
        else if(elevation <= -1.0)
            val = Color.green;
        else if(elevation <= -0.5)
            val = new Color(213,186,0);
        else if(elevation <= 0.0)
            val = Color.orange;
        else if( elevation <= 1.0)
            val = Color.red;
        else
            val = Color.pink;
        return val;
    }
    private Point getViewCenter() {
        JViewport vp = (JViewport)this.getParent();
        Point p = vp.getViewPosition();
        return new Point(p.x + vp.getWidth()/2, p.y + vp.getHeight()/2);

//        
//        scrolRect        
    }
    
    private void setViewCenter(Point p) 
    {
        JViewport vp = (JViewport) this.getParent();  
        Rectangle viewRect = vp.getViewRect();
        
        viewRect.x = p.x - viewRect.width/2;
        viewRect.y = p.y - viewRect.height/2;
        
        scrollRectToVisible(viewRect);
    }
    
    public void setZoomFactor(double zoom)
    {
        Point center = getViewCenter();
        
        Dimension dim = this.getSize();
        dim = new Dimension((int)(dim.width*zoom), (int)(dim.height*zoom));
        
        this.setPreferredSize(dim);
        this.setViewCenter(center);
    }
    
        @Override
    public void mouseExited(MouseEvent e)
    {
         return;
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
         return;
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {

    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        if(e.getButton() == MouseEvent.BUTTON1)
        {
            xPressed = e.getX() - xOffset;
            yPressed = e.getY() - yOffset;
        }            
    }
    @Override
    public void mouseDragged(MouseEvent e)
    {
        //System.out.println("Mouse Dragged!");
        xOffset = e.getX()-xPressed;
        yOffset = e.getY()-yPressed;

        repaint();
    }
    @Override
    public void mouseClicked(MouseEvent e)
    {
       System.out.println("mouse clicked!");
       if(e.getButton() == 0)
           ;//xPressed +=
         return;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e)
    {
        double scaleFactorMultiplier = 1;
        
        System.out.println("mouse wheel moved!");
//        setZoomFactor(.9);
        int wheelDirection = e.getWheelRotation();
        
        if(wheelDirection < 0)
        {
            zoomLevel++;
            
            scaleFactorMultiplier = Math.pow(1.1, zoomLevel);
            scaleFactor = 15 * scaleFactorMultiplier;
            
            xOffset = (int)((double)xOffset*1.1);
            yOffset = (int)((double)yOffset*1.1);
            
            xOffset += (int)((double)e.getX() / (double)scaleFactor);
            yOffset += (int)((double)e.getY() / (double)scaleFactor);
        }
        else if(wheelDirection > 0)
        {
            zoomLevel--;
            
            scaleFactorMultiplier = Math.pow(1.1, zoomLevel);
            scaleFactor = 15 * scaleFactorMultiplier;
            
            xOffset = (int)((double)xOffset*.909091);
            yOffset = (int)((double)yOffset*.909091);
            
            xOffset += (int)((double)e.getX() / (double)scaleFactor);
            yOffset += (int)((double)e.getY() / (double)scaleFactor);
        }
        repaint();
        return;
    }
    
    @Override
    public void mouseMoved(MouseEvent e)
    {
        System.out.println("Mouse moved!");
    }

}