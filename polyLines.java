/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package guiassignment2;

import javax.swing.*;
import java.util.*;
import org.jdom2.*;
import guiassignment2.MapPainter;

/**
 *
 * @author Ben
 */

public class polyLines
{
    
    // stores a bone records poly lines
        public String xymin;
        public String xymax;
        public String uniqueID;
        public int objectnum;
        public String taxon;
        public int element;
        public String subElement;
        public String side;
        public String completeness;
        public String expside;
        public String articulate;
        public String gender;
        public String datefound;
        public Double elevation;
        public int objectid;
        public Double shapelength;   
        
        public ArrayList<ArrayList<Double[]>> allPolyPoints;

    // stores all bone record poly lines
    private  List<Element> bonerecs;
    // constructor gets working tree of bones.xml data,
    // then it calls getBoneRecs to get all bone record poly lines
    public  polyLines()
    {
    }
    
    public class boneDetails
    {

        
    }
    // iteratives through each record id in bones.xml, finds the records
    // files, and stores the polyline elements
    public void getBoneRecs()
    {
        polyLines line = new polyLines();
       XMLParse BoneRecs = new XMLParse("bonexml/bones.xml");
       Element root = BoneRecs.getRoot();
//       ArrayList<ArrayList<Double[]>> allPolyPoints
//               = new ArrayList<ArrayList<Double[]>>();
       bonerecs = root.getChildren("bonerec");
       allPolyPoints = new ArrayList<ArrayList<Double[]>>(); 
       //polyLines rec = new polyLines();
       Element record;
       Element boneRec;
       int importance;
       for(int i = 0; i < bonerecs.size(); i++)
       {
           record = bonerecs.get(i).clone();
           uniqueID = record.getChildTextTrim("uniqueid");
           datefound = record.getChildTextTrim("datefound");
           objectnum = Integer.parseInt(record.getChildTextTrim("objectnum"));
           taxon = record.getChildTextTrim("taxon");
           element = Integer.parseInt(record.getChildTextTrim("element"));
           subElement = record.getChildTextTrim("subelement");
           side = record.getChildTextTrim("side");
           completeness = record.getChildTextTrim("completeness");
           expside = record.getChildTextTrim("expside");
           articulate = record.getChildTextTrim("articulate");
           gender = record.getChildTextTrim("gender");
           elevation = Double.parseDouble(record.getChildTextTrim("elevation"));
           objectid = Integer.parseInt(record.getChildTextTrim("objectid"));
           shapelength = Double.parseDouble(record.getChildTextTrim("shapelength"));
           XMLParse bone  = new XMLParse("bonexml/" + uniqueID + ".xml");
           boneRec = bone.getRoot();
           // if the xml file reference from 'bones.xml' does not exist, the
           // loop is broken since 'getChildTextTrim' method will throw
           // an exception if boneRec is null
           if(boneRec != null)
           {
                xymin = boneRec.getChildTextTrim("xymin");
                xymax = boneRec.getChildTextTrim("xymax");
                allPolyPoints.add(getPolyLines(bone.getRoot()));
           }
       }
    }
    
    private ArrayList<Double[]> getPolyLines( Element current )
    {
        String[] numbers;
        String numStr;
        Double[] points;
        int npolylines;
        Double x, y;

        // get list of children
        List<Element> children = current.getChildren("polyline");
        // get iterator for recurion base case
        Iterator iterator = children.iterator();
        children = current.getChildren();
        children = children.get(3).getChildren("polyline");
        ArrayList<Double[]> xyPoints = new ArrayList<Double[]>();
        for(int i = 0; i < children.size(); i++)
        {
            numStr = children.get(i).getValue();
            numStr = numStr.replace("\n", "");
            numStr = numStr.replace("  ", " ");
            numStr = numStr.trim();
            numbers = numStr.split(" ");
            xyPoints.add(getPolyPoints(numbers));
        }
        return xyPoints;
    }
    

    private Double[] getPolyPoints(String[] x_y)
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
}

