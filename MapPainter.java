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
    private final GeneralPath gp;
    private final ArrayList<GeneralPath> path;
    public Double[] scaleMeters;
    public polyLines lines;
    public Controller controller;
    public int detailLevel;
    public Double scaleFactor;
    private int xPressed;
    private int yPressed;
    private int xOffset;
    private int yOffset;
    private int zoomLevel;
    private JToolTip toolTip;
    public String activeBone;
    private Boolean wheelDebouncerOn; 
    
    public MapPainter()
    {
        // set map background color
        this.setBackground(Color.white);
        
        // intialize class variables
        gp = new GeneralPath();
        path = new ArrayList<>();
        lines = new polyLines();
        detailLevel = 16;
        scaleFactor = 15.0;
        toolTip = new JToolTip();
        xOffset = 50;
        yOffset = 50;
        zoomLevel = 0;
        activeBone = "";
       // get data from xml records
        lines.getBoneRecs();
        wheelDebouncerOn = true;
    }

    // paintComponent() is the display callback function

    /**
     *
     * @param g
     */
        @Override
    public void paintComponent( Graphics g )
    {
        super.paintComponent( g );	// call the base class constructor
        ArrayList<Double[]> points = new ArrayList<>();
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
            if(activeBone.equals(lines.uniqueID.get(i)))
            {
                g2d.setColor(Color.magenta);
                g2d.setStroke(new BasicStroke(2));
            }
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
            g2d.setStroke(new BasicStroke(1));
            path.add(new GeneralPath()); 
            drawNum += 1;
        }
    }

    // set initial panel size
    @Override
    public Dimension getPreferredSize ()
    {
        return new Dimension( 320, 320 );
    }

    /**
     *
     * @return
     */
    @Override
    public Dimension getMinimumSize ()
    {
        return new Dimension(480, 480);//this.getPreferredSize();
    }
    
    private ArrayList<Double[]> getWalkway()
    {
        XMLParse walkway = new XMLParse("bonexml/walkway.xml");
        Element root;
        Double[] xyMinMax;
        String Area;
        String[] x_y;
        if(walkway.getRoot() != null)
            root = walkway.getRoot();
        else root = null;
//        java.util.List<Element> walkwayPath;
//        walkwayPath = root.getChildren("xml");
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
        System.arraycopy(xyMinMax, 0, scaleMeters, 0, 4);
        
        
        children = children.get(3).getChildren();
        //Double[] xyPoints;
        ArrayList<Double[]> xyPoints = new ArrayList<>();
        
        for(int i = 5; i < children.size(); i++)
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
        
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
         
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
        
        // the purpose of this loop is to see if a bone was click on
        // in the map panel. Scaling and offsetting is done to get the position
        // of the bones realtive to the mouse click.
        // each bone record must be individually checked if it has been clicked
        // on every time there is a click event. However the function
        // exits once it finds a match to prevent getting multiple results from
        // one click.
        String lastBone = activeBone;
        for(int i = 0; i < numOfRecs; i++)
        {
            if(lines.xymax.get(i) != null)
            {
                activeBone = "";
                // get xy min and max, then parse into array of strings.
                xyMin = lines.xymin.get(i);
                xyMax = lines.xymax.get(i);
                xyMin = xyMin.trim();
                xyMax = xyMax.trim();
                xyMin += " ";
                xyMin += xyMax;
                recordMin = xyMin.split(" ");
                recordMax = xyMax.split(" ");
                
                // call function that given array of strings, returns array of
                // doubles with the x coordinates in the even indices and
                // y coordinates in the odd indices.
                coordinates = getXY(recordMin);

                // scale coordinates, the odd coordinates (y min and max)
                // must be scaled so as to invert ther position, essentially
                // vertically flip the y coordintas. The x coordinates are 
                // scaled down like other points.
                coordinates[0] -= scaleMeters[0];
                coordinates[1] = scaleMeters[3]-coordinates[1];
                coordinates[2] -= scaleMeters[0];
                coordinates[3] = scaleMeters[3]-coordinates[3];
                
                // now that the min and max coordinates are scaled, they are now
                // scaled and offset to the same scale and offset as the items
                // that where drawn on the panel.
                coordinates[0] = coordinates[0] * scaleFactor + xOffset;
                coordinates[1] = coordinates[1] * scaleFactor + yOffset;
                coordinates[2] = coordinates[2] * scaleFactor + xOffset;
                coordinates[3] = coordinates[3] * scaleFactor + yOffset;

                
                x = e.getX();
                y = e.getY();

                int currDetailLevel = lines.element.get(i);
                
                // check to see if the mouse click is within the area of a bone
                // on the map panel.
                if((double)x > coordinates[0] 
                        && (double)y > coordinates[3] 
                        && (double)x < coordinates[2]
                        && (double)y < coordinates[1] 
                        && currDetailLevel < detailLevel
                        && !(lastBone.equals(lines.uniqueID.get(i))))
                {
                    activeBone = lines.uniqueID.get(i);
                    lastBone = lines.uniqueID.get(i);
                    controller.showBoneInfo(activeBone);
                    break;
                }
                else
                    controller.menuPanel.infoPanel.setVisible(false);
            }
        }
        repaint();       
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e)
    {
        double scaleFactorMultiplier;
        double x = e.getX(), y = e.getY();
        int wheelDirection = e.getWheelRotation();
        if(wheelDebouncerOn == true)
        {
            if(wheelDirection < 0)
            {

                if(zoomLevel < 40)
                    zoomLevel++;
                else return;

                scaleFactorMultiplier = Math.pow(1.1, zoomLevel);
                scaleFactor = 15 * scaleFactorMultiplier;

                xOffset += (x - x*1.1)*scaleFactorMultiplier;
                yOffset += (y - y*1.1)*scaleFactorMultiplier;
                System.out.println("zoom in");
                System.out.println(xOffset);
                System.out.println(yOffset);       
            }
            else if(wheelDirection > 0 )
            {
                
                if(zoomLevel > -15)
                    zoomLevel--;
                else return;
                    
                scaleFactorMultiplier = Math.pow(1.1, zoomLevel);
                scaleFactor = 15 * scaleFactorMultiplier;

    //            xOffset = (int)((double)xOffset*.909091);
    //            yOffset = (int)((double)yOffset*.909091);
                xOffset -= (x/1.1-x)*scaleFactorMultiplier;
                yOffset -= (y/1.1-y)*scaleFactorMultiplier;

               
                System.out.println("zoom out");
                System.out.println(xOffset);
                System.out.println(yOffset);      
            }
          if(xOffset > scaleFactor*(scaleMeters[2]-scaleMeters[0]))//this.getWidth())
            xOffset = (int) (scaleFactor*(scaleMeters[2]-scaleMeters[0]) - 2.0*scaleFactor);
          if(yOffset > scaleFactor*(scaleMeters[3]-scaleMeters[1]) - 2.0 * scaleFactor)//this.getHeight())
              yOffset = (int) (scaleFactor*(scaleMeters[3]-scaleMeters[1]));//this.getHeight();
          if(xOffset < -1.0*scaleFactor*(scaleMeters[2]-scaleMeters[0]))//this.getWidth())
            xOffset = (int) (-1.0*scaleFactor*(scaleMeters[2]-scaleMeters[0]) + 2.0 * scaleFactor);
          if(yOffset < -1.0*scaleFactor*(scaleMeters[3]-scaleMeters[1]))//this.getHeight())
              yOffset = (int) (-1.0*scaleFactor*(scaleMeters[3]-scaleMeters[1])+2.0*scaleFactor);//this.getHeight();
            repaint();
        }
        wheelDebouncerOn = !wheelDebouncerOn;
    }
    
    @Override
    public void mouseMoved(MouseEvent e)
    {

    }
    
    public void resetView()
    {
        xOffset = 50;
        yOffset = 50;
        zoomLevel = 0;
        scaleFactor = 15.0;
        repaint();
    }

}