package guiassignment2;


/**GuiAssignment2 <br><br>
 * Program Name - GUI OOP Assignment 2: Mammoth Site Kiosk Software <br><br>
 * Professor - Dr. Weiss <br><br>
 * Class - CSC 421 <br><br>
 * Date - 12/9/2013 <br><br>
 * Authors - BenjaminSherman, Derek Stotz, Erik Hattervig <br><br>
 * Installation -   The bonexml folder should be placed in the main project
 *                  directory, on the same level as src. <br><br>
 * 
 * Use - This program uses Swing to construct a GUI with working components the
 * moment the program is started.  The interface is split into three parts: <br>
 *      1. The Map Panel, which displays a zoomable/dragable map with selectable
 *          bones.<br>
 *      2. The Menu Panel, which primarily contains a a slider to adjust the
 *          detail of the set of bones displayed in the Map Panel, an exit button
 *          and a reset button for the Map Panel.<br>
 *      3. The Information Panel, which located on top of the Menu Panel.  It
 *          displays the information of selected bones.<br><br><br>
 * 
 * To view bone information, the user simply clicks (press and release the left
 * mouse button without moving the pointer) on an area within the bounding box
 * of the bone.  Clicking the bone again will deselect the bone and select a
 * different bone whose bounding box is beneath the pointer, if one exists.  This
 * allows the user to select bones which lie beneath larger ones.<br><br>  
 * 
 * Bone information is displayed on the left, containing all information within
 * the given XML files and the image files associated with the bone.  Deselecting
 * a bone hides the Information Panel. <br><br>
 * 
 * Note that on the Linux machines, there is no frame boundry on the top of the
 * mammoth site map. Windows does not have this problem.
 * @author BenjaminSherman, Derek Stotz, Erik Hattervig
 */
public class GuiAssignment2 {

    /**main
     * The public static void main: The entry point of the application.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // launches a controller, which in turn launches the application JFrame
        Controller mainWindow = new Controller();
     

    }
    
}
