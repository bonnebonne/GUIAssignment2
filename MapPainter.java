// GeneralPathPanel.java
// Simple program illustrating graphics programming in Java.
// JMW 081031

package guiassignment2;

//import  guiassignment2.XMLParse.listChildren;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jdom2.Element;


public class MapPainter extends JPanel implements MouseWheelListener,
            MouseListener, MouseMotionListener, ChangeListener
{
    private GeneralPath gp;
    private ArrayList<GeneralPath> path;
    private ArrayList<int[]> DrawObjects;
    public Double[] scaleMeters;
    public polyLines lines;
    public Controller controller;
    public int detailLevel;
    //private int drawNum;
    public Double scaleFactor;
    private int xPressed;
    private int yPressed;
    private int xOffset;
    private int yOffset;
    private int zoomLevel;
    private JToolTip toolTip;
    
    public MapPainter()
    {
        // intialize drawing component
        gp = new GeneralPath();
        path = new ArrayList<GeneralPath>();
        toolTip = new JToolTip();
        DrawObjects = null;
        // initialize scaleFactor
        scaleFactor = 15.0;
        zoomLevel = 0;
        // initialize class
        lines = new polyLines();
        detailLevel = 16;
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
        double flipY = scaleMeters[3];//- scaleMeters[1];
        int drawNum = 0;
        //GeneralPath newDraw = new GeneralPath();
        if(!path.isEmpty())
            path.clear();
    
        g2d.translate(xOffset,yOffset);//(xOffset+300, -yOffset - 750);
        path.add(new GeneralPath());

                
        g2d.setColor(Color.BLACK);
        for(int i = 0; i < points.size(); i++)
        {
            temp = points.get(i);
            path.get(drawNum).moveTo((temp[0]-scaleMeters[0])*scaleFactor,(flipY - temp[1])*scaleFactor);

            for(int j = 2; j < points.get(i).length;)
            {
                path.get(drawNum).lineTo((temp[j]-scaleMeters[0])*scaleFactor, (flipY-temp[j+1])*scaleFactor);
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
                if(temp != null && lines.element.get(i) < detailLevel)
                {
                    path.get(drawNum).moveTo((temp[0]-scaleMeters[0])*scaleFactor, (flipY - temp[1])*scaleFactor);
                    for(int k = 2; k < temp.length;)
                    {
                        path.get(drawNum).lineTo((temp[k]-scaleMeters[0])*scaleFactor , (flipY - temp[k+1])*scaleFactor);
                        k+=2;
                        
                        
                        
                    }
                }
            }
            g2d.draw(path.get(drawNum));
            path.add(new GeneralPath()); 
            drawNum += 1;
        }
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
        scaleMeters = new Double[4];
        for(int j = 0; j < 4; j++)
            scaleMeters[j] = 0.0;
        xyMinMax = getXY(x_y); 

        for(int j = 0; j < 4; j++)
            scaleMeters[j] = xyMinMax[j];
        
        
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
                temp = Double.parseDouble(x_y[i]);// - scaleMeters[0];
            else
                temp = Double.parseDouble(x_y[i]);// - scaleMeters[1];
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
    public void stateChanged(ChangeEvent e)
    {
        detailLevel = controller.DetailSlider.getValue();
        repaint();
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
              
       
        int numOfRecs = lines.uniqueID.size()-1;
        String[] recordMin, recordMax;
        String xyMin, xyMax;
        Double x1, y1, x2, y2;
        int x,y;
        Boolean upLeft = false;
        Double[] coordinates;
        for(int i = 0; i < numOfRecs; i++)
        {
            if(lines.xymax.get(i) != null)
            {
                xyMin = lines.xymin.get(i);
                xyMax = lines.xymax.get(i);
                xyMin = xyMin.trim();
                xyMax = xyMax.trim();
                xyMin += " ";
                xyMin += xyMax;
                recordMin = xyMin.split(" ");
                recordMax = xyMax.split(" ");

                coordinates = getXY(recordMin);

                coordinates[0] -= scaleMeters[0];
                coordinates[1] = scaleMeters[3]-coordinates[1];
                coordinates[2] -= scaleMeters[0];
                coordinates[3] = scaleMeters[3]-coordinates[3];


                coordinates[0] = coordinates[0] * scaleFactor + xOffset;
                coordinates[1] = coordinates[1] * scaleFactor + yOffset;
                coordinates[2] = coordinates[2] * scaleFactor + xOffset;
                coordinates[3] = coordinates[3] * scaleFactor + yOffset;

                //Need to scale 
                x = e.getX();
                y = e.getY();

                int currDetailLevel = lines.element.get(i);
                
                if((double)x > coordinates[0] && (double)y > coordinates[3] && (double)x < coordinates[2]
                        && (double)y < coordinates[1] && currDetailLevel < detailLevel)
                {
                    System.out.println(lines.uniqueID.get(i));      
                    break;
                }
                else
                ; // delete bone window in menu pane
            }
        }
                
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e)
    {
        double scaleFactorMultiplier = 1;
        
        //System.out.println("mouse wheel moved!");
//        setZoomFactor(.9);
        int wheelDirection = e.getWheelRotation();
        
        if(wheelDirection < 0)
        {
            zoomLevel++;
            scaleFactorMultiplier = Math.pow(1.1, zoomLevel);
            scaleFactor = 15 * scaleFactorMultiplier;
            
            xOffset = (int)((double)xOffset*1.1);
            yOffset = (int)((double)yOffset*1.1);
            
            xOffset -= (int)((double)e.getX()/ (double)scaleFactor);
            yOffset += (int)((double)e.getY() / (double)scaleFactor);
            if(xOffset > 600)//this.getWidth())
                xOffset = this.getWidth();
            if(yOffset > 600)//this.getHeight())
                yOffset = this.getHeight();
            if(xOffset < 0)
                xOffset = 0;
            if(yOffset < 0)
                yOffset = 0;
            
            
        }
        else if(wheelDirection > 0)
        {
            zoomLevel--;
            
            scaleFactorMultiplier = Math.pow(1.1, zoomLevel);
            scaleFactor = 15 * scaleFactorMultiplier;
            
            xOffset = (int)((double)xOffset*.909091);
            yOffset = (int)((double)yOffset*.909091);
            
            xOffset += (int)((double)e.getX() / (double)scaleFactor);
            yOffset -= (int)((double)e.getY() / (double)scaleFactor);
            if(xOffset > this.getWidth())
                xOffset = this.getWidth();
            if(yOffset > this.getHeight())
                yOffset = this.getHeight();
            if(xOffset < 0)
                xOffset = 0;
            if(yOffset < 0)
                yOffset = 0;
            System.out.println(this.getWidth());
            System.out.println(xOffset);
        }
        repaint();
        return;
    }
    
    @Override
    public void mouseMoved(MouseEvent e)
    {

    }

}