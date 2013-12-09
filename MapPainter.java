
package guiassignment2;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import org.jdom2.Element;

/** 
 *  The MapPainter class does the heavy lifting for drawing all the components
 * on the MapPanel. It also implements mouse wheel zooming event handlers,
 * mouse click handlers, mouse drag handlers and mouser pressed handlers.
 * It calls the polyLines class which retrieves all the points that make
 * up the bones. 
 * polyLines does not handle getting the walkway points that
 * make up the walkway. The points that make up the walk way are retrieved
 * by this MapPainter class.
 * 
 * @author Benjamin Sherman, Derek Stotz, & Erik Hattervig
 */
public class MapPainter extends JPanel implements MouseWheelListener,
            MouseListener, MouseMotionListener
{
    /** 
     * This array of GeneralPaths is necessary to draw the bones in different
     * colors. When a new bone is drawn, a new GeneralPath is appended to this
     * array.
     */
    private final ArrayList<GeneralPath> path;
    /** 
     * An array of size four that contains x min in spot 0, y min in spot 1
     * x max in spot 2, and y max in spot 3. This is used to scale down the 
     * points read from the xml files that are drawn with paintComponent.
     */
    private Double[] scaleMeters;
    /** 
     * Contains all the information about each bone. It contains the information
     * that is shown in the left pane of the GUI when a bone is clicked on and
     * highlighted.
     */
    public polyLines lines;
    /** 
     * Used to get reference to the controller so that the menuPanel can display
     * differnt bone information.
     */
    public Controller controller;
    /** 
     * This is necessary for the detail slider so that it can be accessed and
     * changed outside of the MapPainter class.
     */
    public int detailLevel;
    /** 
     * Controls the zoom level of the mammoth site map.
     */
    public Double scaleFactor;
    /** 
     * Stores the the x position of the mouse when it is pressed.
     */
    private int xPressed;
    /** 
     * Stores the y position of the mouse when it is pressed.
     */
    private int yPressed;
    
    /** 
     * Used to position the mammoth site map when it is dragged or when the
     * zoom level is changed. This influences the horizontal offset.
     */
    private int xOffset;
    /** 
     * Used to position the mammoth site map when it is dragged or when the
     * zoom level is changed. This influences the vertical offset.
     */
    private int yOffset;
    /** 
     * Determines how far in or out the mammoth site map is zoomed.
     */
    private int zoomLevel;
    
    /** 
     * This variable is initially an empty string. It is set to a unique id
     * of a bone record that is selected by the user on the mammoth site map
     * with a mouse click.
     */
    public String activeBone;
    
    /** This is used to prevent the mammoth site map being zoomed in on twice
     * for a a mouse wheel moved event. For some reason when the mouse wheel
     * is moved it triggers the mouse wheel moved event twice.
     */
    private Boolean wheelDebouncerOn; 
    
    /** 
     * The MapPainter constructor initializes all the necessary class variables.
     * It uses the polyLines class to initialize the class variable lines.
     */
    public MapPainter()
    {
        // set map background color
        this.setBackground(Color.white);
        
        // intialize class variables
        path = new ArrayList<>();
        lines = new polyLines();
        detailLevel = 16;
        scaleFactor = 12.5;
        xOffset = 50;
        yOffset = 50;
        zoomLevel = 0;
        activeBone = "";
       // get data from xml records
        lines.getBoneRecs();
        wheelDebouncerOn = true;
        

    }

    /** 
     * This overriden function of paintComponent is customized to cycle through
     * the list of points for each bone and draw them on the MapPanel. It
     * also allows different bones to be drawn different colors. This function
     * also access the class detailLevel variable. If a bones element value
     * is greater than the detail value, it is not drawn. The smaller the 
     * detail level the higher the importance of the bones. Bones that are less
     * than the detail level are drawn.
     *
     * paintComponent is also in charge of highlighting a selected bone record
     * magenta if it is selected by the user.
     * 
     * @param g - passed in by the system, it is what is essentially drawn to.
     * Once it is drawn to, it is shown on the component of the program that
     * invoked paintComonent.
     */
        @Override
    public void paintComponent( Graphics g )
    {
        super.paintComponent( g );	// call the base class constructor
        ArrayList<Double[]> points;// = new ArrayList<>();
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
            if(activeBone.equals(lines.uniqueID.get(i)))
            {
                g2d.setColor(Color.magenta);
                g2d.setStroke(new BasicStroke(2));
            }
            else
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
            g2d.setStroke(new BasicStroke(1));
            path.add(new GeneralPath()); 
            drawNum += 1;
        }
    }




    /** 
     * This function retrieves the points that makeup the walkway path. The
     * points obtained by this function are used to draw the walkway using
     * paintComponent. 
     * 
     * @return xyPoints - xyPoints is a list of all the points that makeup
     * the walkway path that is drawn with painComponent.
     */
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
    
    /** 
     * This function is given an array of strings. Each string in the array
     * represents a Double type value which is parsed into a Double. Once a
     * string in the string array x_y is parsed into a Double it is then stored
     * in an array of Doubles called points. This function is for general
     * purposes and is used in a variety of situations.
     * 
     * @param x_y an array of strings that is to be converted to an array of
     * Doubles.
     * 
     * @return points - contains an array of Doubles that where derived from
     * the array of strings x_y.
     */
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
    
    /** 
     * This function returns a color based on the value of elevation.
     * The purpose for this is to draw the different bones different colors
     * based on elevation.
     * 
     * @param elevation the elevation of a bone.
     * @return 
     */
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

    

    /**
     * Because MouseListener is implemented in this class, this function has to
     * be overriden, though it doesn't do anything.
     * @param e event parameter.
     */
        @Override
    public void mouseExited(MouseEvent e)
    {
        
    }

    /**
     * Because MouseListener is implemented in this class, this function has to
     * be overriden, though it doesn't do anything.
     * @param e event parameter.
     */
    @Override
    public void mouseEntered(MouseEvent e)
    {
         
    }
    
    /**
     * Because MouseListener is implemented in this class, this function has to
     * be overriden, though it doesn't do anything.
     * @param e event parameter.
     */
    @Override
    public void mouseReleased(MouseEvent e)
    {

    }

    

    
    /**
     * Saves the position of the mouse when it is pressed with the offset
     * of the mammoth site map taken into account.
     * @param e event parameter.
     */
    @Override
    public void mousePressed(MouseEvent e)
    {
        if(e.getButton() == MouseEvent.BUTTON1)
        {
            xPressed = e.getX() - xOffset;
            yPressed = e.getY() - yOffset;
        }            
    }
    
    /**
     * This overriden function of the mouseDragged event calculates how much the
     * the mammoth site map must be offset must be changed to make it appear
     * that the user is dragging the mammoth site map around with the mouse
     * when they click and drag. In order to make the mammoth site map move
     * with the mouse when the user is dragging the mouse, repaint is called
     * every time this event is registered.
     * 
     * @param e event parameter.
     */
    @Override
    public void mouseDragged(MouseEvent e)
    {
        xOffset = e.getX()-xPressed;
        yOffset = e.getY()-yPressed;
       
        repaint();
    }
    
    /**
     * <p>
     * This overriden function of the mouseCLicked event determines whether a
     * bone was clicked on. It implements this by checking for a bone that is
     * within the area clicked on the mammoth site map. The class member
     * variable lines contains the x and y min and max that is the bounding
     * area of a bone. </p>
     * 
     * <p>
     * If a click falls with in the area of a bones bounding
     * box, that bone is set as the active bone and its information is displayed
     * in the MenuPanel. When a bone is set as the active bone it is also
     * highlighted in magenta on the mammoth site map. If the mouse click
     * was not in the bounds of any bone on the mammoth site map, then the
     * bone information pane in the MenuPanel is set to not visible. </p>
     * 
     * <p>
     * In some cases, bones overlap each other and there is not way to select
     * the bone underneath it. In order to fix this problem, the function
     * will not set a bone as an active bone if it was the last bone to be
     * clicked. This doubles up to allow the the user to deselect a particular
     * bone by clicking it again. </p>
     * @param e 
     */
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
                    controller.showBoneInfo(activeBone);
                    break;
                }
                else
                    controller.menuPanel.infoPanel.setVisible(false);
            }
        }
        repaint();       
    }

    /**
     * This overriden function of the mouseWheelMoved event calculates the 
     * offset and new zoom level that is applied to the mammoth site map to
     * implement zooming relative to the mouses position on the mammoth site
     * map. The implementation is not perfect, the further the zoom level is
     * from the default value, the worse the zooming becomes. Near the default
     * zooming value, zooming relative to the mouse works relatively well.
     * 
     * @param e event parameter
     */
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
                scaleFactor = 12.5 * scaleFactorMultiplier;

                xOffset += (x - x*1.1)*scaleFactorMultiplier;
                yOffset += (y - y*1.1)*scaleFactorMultiplier;
            }
            else if(wheelDirection > 0 )
            {
                
                if(zoomLevel > -15)
                    zoomLevel--;
                else return;
                    
                scaleFactorMultiplier = Math.pow(1.1, zoomLevel);
                scaleFactor = 12.5 * scaleFactorMultiplier;

                xOffset -= (x/1.1-x)*scaleFactorMultiplier;
                yOffset -= (y/1.1-y)*scaleFactorMultiplier;
      
            }

          // keep window from going too far out of bounds
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
    
    /**
     * Because MouseListener is implemented in this class, this function has to
     * be overriden, though it doesn't do anything.
     * @param e event parameter.
     */
    @Override
    public void mouseMoved(MouseEvent e)
    {

    }
    
    /**
     * This public function is used when the "Reset View" button is clicked
     * on the MenuPanel. The function resets the xOffset, yOffset, zoomLevel,
     * and scaleFactor to their default values, effectively reseting the mammoth
     * site map to its original position.
     */
    public void resetView()
    {
        xOffset = 50;
        yOffset = 50;
        zoomLevel = 0;
        scaleFactor = 12.5;
        repaint();
    }
    
    public void setDetailLevel(int detail)
    {
        detailLevel = detail;
        repaint();
    }
}