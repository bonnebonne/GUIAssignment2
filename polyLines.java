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
        public ArrayList<String> xymin;
        public ArrayList<String> xymax;
        public ArrayList<String> uniqueID;
        public ArrayList<Integer> objectnum;
        public ArrayList<String> taxon;
        public ArrayList<Integer> element;
        public ArrayList<String> subElement;
        public ArrayList<String> side;
        public ArrayList<String> completeness;
        public ArrayList<String> expside;
        public ArrayList<String> articulate;
        public ArrayList<String> gender;
        public ArrayList<String> datefound;
        public ArrayList<Double> elevation;
        public ArrayList<Integer> objectid;
        public ArrayList<Double> shapelength;   
        
        public ArrayList<ArrayList<Double[]>> allPolyPoints;

    // stores all bone record poly lines
    private  List<Element> bonerecs;
    // constructor gets working tree of bones.xml data,
    // then it calls getBoneRecs to get all bone record poly lines
    public  polyLines()
    {
        xymin = new ArrayList<String>();
        xymax = new ArrayList<String>();
        uniqueID = new ArrayList<String>();
        objectnum = new ArrayList<Integer>();
        taxon = new ArrayList<String>();
        element = new ArrayList<Integer>();
        subElement = new ArrayList<String>();
        side = new ArrayList<String>();
        completeness = new ArrayList<String>();
        expside = new ArrayList<String>();
        articulate = new ArrayList<String>();
        gender = new ArrayList<String>();
        datefound = new ArrayList<String>();
        elevation = new ArrayList<Double>();
        objectid = new ArrayList<Integer>();
        shapelength = new ArrayList<Double>();
        allPolyPoints = new ArrayList<ArrayList<Double[]>>(); 

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
       //polyLines rec = new polyLines();
       Element record;
       Element boneRec;
       int importance;
       for(int i = 0; i < bonerecs.size(); i++)
       {
           record = bonerecs.get(i).clone();
           uniqueID.add(record.getChildTextTrim("uniqueid"));
           datefound.add(record.getChildTextTrim("datefound"));
           objectnum.add(Integer.parseInt(record.getChildTextTrim("objectnum")));
           taxon.add(record.getChildTextTrim("taxon"));
           element.add(Integer.parseInt(record.getChildTextTrim("element")));
           subElement.add(record.getChildTextTrim("subelement"));
           side.add(record.getChildTextTrim("side"));
           completeness.add(record.getChildTextTrim("completeness"));
           expside.add(record.getChildTextTrim("expside"));
           articulate.add(record.getChildTextTrim("articulate"));
           gender.add(record.getChildTextTrim("gender"));
           elevation.add(Double.parseDouble(record.getChildTextTrim("elevation")));
           objectid.add(Integer.parseInt(record.getChildTextTrim("objectid")));
           shapelength.add(Double.parseDouble(record.getChildTextTrim("shapelength")));
           XMLParse bone  = new XMLParse("bonexml/" + uniqueID.get(i) + ".xml");
           boneRec = bone.getRoot();
           // if the xml file reference from 'bones.xml' does not exist, the
           // loop is broken since 'getChildTextTrim' method will throw
           // an exception if boneRec is null
           if(boneRec != null)
           {
                xymin.add(boneRec.getChildTextTrim("xymin"));
                xymax.add(boneRec.getChildTextTrim("xymax"));
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

