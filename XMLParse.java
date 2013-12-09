package guiassignment2;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import java.io.IOException;


/**XML
 *
 * Uses JDOM to parse an XML file
 * Based on Java example in Processing XML with Java (Elliotte Harold).
 *
 * @author BenjaminSherman, Derek Stotz, Erik Hattervig
 */
public class XMLParse
{
    //the document to be parsed;  a JDOM class
    private Document doc = null;
    
    /**XMLParse
     * Creates new XMLParse and reads the file defined in args, storing it in
     * a document.  Handles the case where the document could not be read, and
     * returns null.
     * 
     * @param args the filename
     */
    public XMLParse( String args )
    {
	// read and parse XML document
        SAXBuilder builder = new SAXBuilder();
        //Element root = null;
        try
        {
            doc = builder.build( args );	// parse XML tags
        }
        // JDOMException indicates a well-formedness error
        catch ( JDOMException e )
        {
            System.out.println( args + " is not well-formed." );
            System.out.println( e.getMessage() );
            
        }
        catch ( IOException e )
        {
            doc = null;
            System.out.println( e );
        }
        
    }
    
    /**getRoot
     * Finds and returns the root element of the stored JDOM Document.
     * 
     * @return the root element
     */
    public Element getRoot()
    {
        // only return 'doc.getRootElement()' if it is NOT null
        // as if 'doc' is null, getRootElement() method causes the
        // the program to crash when it returns null;
        if(doc != null)
            return doc.getRootElement();
        // if doc is null skip 'getRootElement() method and just return null
        else
            return null;
    }
}