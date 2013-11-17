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
//	// check usage
//        if ( args.length == 0 )
//        {
//            System.out.println( "Usage: java ElementLister URL" );
//            return;
//        }
	// read and parse XML document
        SAXBuilder builder = new SAXBuilder();
        //Element root = null;
        try
        {
            doc = builder.build( args );	// parse XML tags
            //Element root = doc.getRootElement();	// get root of XML tree
            //listChildren( root, 0 );
            // print info in XML tree
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

    // indent to show hierarchical structure of XML tree
    private static void printSpaces( int n )
    {
        for ( int i = 0; i < n; i++ )
        {
            System.out.print( "  " );
        }
    }
    
    public Element getRoot()
    {
        // only return 'doc.getRootElement()' if it is NOT null
        // as if 'doc' is null, getRootElement() method causes the
        // the program to crash
        if(doc != null)
            return doc.getRootElement();
        // if doc is null skip 'getRootElement() method and just return null
        else
            return null;
    }
}