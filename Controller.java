package guiassignment2;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**Controller
 *
 * The intermediary object between the MapPanel and Painter, MainFrame, and
 * MenuPanel.  Takes information from each "View" object and sends it to others
 * to add collective functionality to the GUI.
 * 
 * The Controller also includes some utility functions which aid in the
 * displaying of bone information in the MenuPanel's infoPanel.
 * 
 * Controller implements ActionListener, and acts as a listener for the buttons
 * in the MenuPanel.
 * 
 * @author BenjaminSherman, Derek Stotz, Erik Hattervig
 */
public class Controller implements ActionListener, ChangeListener  {
    
    //All objects in the GUI which need to be referenced by the Controller
    public MapPanel mapPanel;
    public MainFrame frame;
    public JSlider DetailSlider;
    public MenuPanel menuPanel;
    private MapPainter mapPainter;
    
    /**Controller
     * 
     * Creates a new Controller object and sets up some contained containers and
     * components for viewing and functioning.  Specifically, it adds many
     * components to the MenuPanel, such as the detail slider and buttons.
     * 
     * The constructor begins all GUI interaction by setting many "View" object
     * to be visible and defining their layouts.
     */
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
        DetailSlider.addChangeListener(this);

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

        //set up the main panel and finalize the setup of the GUI
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new ScrollPaneLayout());
     
        frame.getContentPane().add(menuPanel, BorderLayout.WEST);
        frame.getContentPane().add(mapPainter, BorderLayout.CENTER);
        frame.setSize(1200, 720);
        frame.setLocationRelativeTo(null); // Center window in center of screen
        
        // make contents of main frame visible
        frame.setVisible(true);     
    }
    
    /**showBoneInfo
     * 
     * Loops through all bone records searching for a bone with a unique ID
     * give by the activeBone parameter.  When found, it sets the text of the
     * labels within the MenuPanel's infoPanel.  This function also utilizes
     * the getBoneIcon function to load an image and display it in the infoPanel.
     * 
     * The visually appealing spacing in the infoPanel is created by simple
     * space characters.  The result is visually unappealing code but a simple
     * solution to the issue of aligning the bone descriptors.
     * 
     * If a bone is never found, it hides the infoPanel.
     * 
     * @param activeBone the unique ID of the currently selected bone
     */
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
    
    /**trimString
     * 
     * trimString is similar to String.trim but checks for a null reference
     * first.  This was implemented because bone records do not always contain
     * information, and attempting to trim them may cause a null pointer
     * exception if not check first.  This function allows for inline trimming
     * in the showBoneInfo function.
     * 
     * @return the string without whitespace
     * @param str The string to be trimmed
     */
    private String trimString(String str)
    {
        //check for a null reference
        if(null != str)
        {
            //return the stirng without spaces if it wasn't null
           return str.trim();
        }
        else
            return "No Information"; //the string to be displayed if no record
    }
    
    /**trimString
     * 
     * getBoneIcon contains a try-catch block which attempts to load an image
     * file and store it in an ImageIcon objects.  This allows for the handling
     * of the no image found case, which is quite common due to the fact that 
     * not every bone record has an associated photograph.  This function allows
     * for inline insertion  of the correct bone icon.
     * 
     * @return the ImageIcon (or null)
     * @param uniqueID the unique id to be used as the icon's file name
     */
    public ImageIcon getBoneIcon(String uniqueID)
    {
        ImageIcon icon;
        try
        {
            //does the bone record with the given ID have an associated picture?
            icon = new ImageIcon("bonexml/" + uniqueID + ".jpg");
        }
        catch(Exception e)
        {
            //apparently not. Print the exception, in case there was another issue.
            System.out.println( "bonexml/" + uniqueID + ".jpg" + " couldn't be opened" );
            System.out.println( e.getMessage() );
            icon = null;
        }
        //this returns either an icon or null, which is handled by the Panel.
        return icon;
    }
    
    
    
    
    /**actionPerformed
     * 
     * The callback function for the button press event triggered by one of the
     * two buttons on MenuPanel. This class was the best candidate as a listener
     * for the buttons because it is the intermediary between the MapPanel and
     * the MenuPanel.
     * 
     * @param e The action event
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        //Did the button pressed have a big "Exit" slapped on it?
        if(((JButton)e.getSource()).getText().equals("Exit"))
        {
            //if it did, exit the applicaiton.
            System.exit(0);
        }
        else if (((JButton)e.getSource()).getText().equals("Reset View"))
        {
            //else, tell the map Painter to reset the view, as the button says.
            mapPainter.resetView();
        }
            
    }
    
        /** 
     * This function has been overriden to set the class member variable detail
     * level when a signal is sent that the slider on the MenuPanel. It sets
     * detailLevel using the mapPainters setDetailLEvel function.

     * @param e event parameter.
     */
    @Override
    public void stateChanged(ChangeEvent e)
    {
        mapPainter.setDetailLevel(DetailSlider.getValue());
    }
    
}
