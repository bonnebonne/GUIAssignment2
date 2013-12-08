// ElementLister.java
// Use JDOM to parse an XML file
// Based on Java example in Processing XML with Java (Elliotte Harold).
// JMW 131029

package guiassignment2;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import java.io.IOException;
import java.util.*;

public class XMLParse
{
    private Document doc = null;
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