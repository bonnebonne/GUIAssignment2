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

/**
 *
 * @author Ben
 */
public class MapPanel extends JPanel implements MouseWheelListener, MouseListener {
    
    public MapPanel() {
        
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
         return;
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
         return;
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e)
    {

    }
} 

