/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package guiassignment2;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.*;
import guiassignment2.MapPainter;

/**MapPanel
 * 
 * The MapPanel is the panel in the front and center of the applicaiton which
 * contains teh MapPainter and is where most of the user interaction takes place.
 * It itself is a listener for mouse events, and contains some functions which
 * help orient the viewport exploring the mammoth site.
 *
 * @author BenjaminSherman, Derek Stotz, Erik Hattervig
 */
public class MapPanel extends JPanel implements MouseWheelListener, MouseListener {
    
    /**MapPanel
     * Creates new MapPanel 
     */
    public MapPanel() {
    }
    
    /**getViewCenter
     * 
     * getViewCenter is used to find the center of the current view, as the name
     * suggests.  The viewport is obtained from the parent and the position is
     * then obtained from the viewpoint.  The returned point is transformed to
     * reflect the center rather than the top-left of the window.
     * 
     * @return a Point representing the center of the current ViewPort
     */
    private Point getViewCenter() {
        JViewport vp = (JViewport)this.getParent();
        Point p = vp.getViewPosition();
        //return the center of the parent's viewport
        return new Point(p.x + vp.getWidth()/2, p.y + vp.getHeight()/2);
    }
    
    /**setViewCenter
     * 
     * Finds the viewport by referencing the parent and calls scrollRectToVisible
     * to a correct new rectangle calculated.
     * 
     * @param p the Point to set the view's center to
     */
    private void setViewCenter(Point p) 
    {
        JViewport vp = (JViewport) this.getParent();  
        Rectangle viewRect = vp.getViewRect();
        
        viewRect.x = p.x - viewRect.width/2;
        viewRect.y = p.y - viewRect.height/2;
        
        scrollRectToVisible(viewRect);
    }
    
    /**setZoomFactor
     * 
     * This function sets the new preferred size of the Panel to one calculated
     * by applying a scale factor, zoom, to the current dimensions of the window.
     * 
     * @param zoom the new scale factor
     */
    public void setZoomFactor(double zoom)
    {
        //find the centerpoint from the getViewCenter function
        Point center = getViewCenter();
        
        //calculate the new veiwport
        Dimension dim = this.getSize();
        dim = new Dimension((int)(dim.width*zoom), (int)(dim.height*zoom));
        
        this.setPreferredSize(dim);
        this.setViewCenter(center);
    }
    
    /**mouseExited
     * An overridden callback method.
     * @param e the mouse event
     */
    @Override
    public void mouseExited(MouseEvent e)
    {
         return;
    }

    /**mouseEntered
     * An overridden callback method.
     * @param e the mouse event
     */
    @Override
    public void mouseEntered(MouseEvent e)
    {
         return;
    }

    /**mouseReleased
     * An overridden callback method.
     * @param e the mouse event
     */
    @Override
    public void mouseReleased(MouseEvent e)
    {
         return;
    }

    /**mousePressed
     * An overridden callback method.
     * @param e the mouse event
     */
    @Override
    public void mousePressed(MouseEvent e)
    {
    }

    /**mouseClicked
     * An overridden callback method.
     * @param e the mouse event
     */
    @Override
    public void mouseClicked(MouseEvent e)
    {

    }

    /**mouseWheelMoved
     * An overridden callback method.
     * @param e the mouse event
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e)
    {

    }
} 

