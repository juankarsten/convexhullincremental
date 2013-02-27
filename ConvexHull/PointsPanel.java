//**************************************************************
//  PointsPanel.java       
//
//  Represents the primary panel for user to enter points.
//*************************************************************

import java.util.ArrayList;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class PointsPanel extends JPanel {
   private ArrayList<Point> pointList;
   private ArrayList<Point> points;
   
   private boolean lineDrawn=false;

   //------------------------------------------------------------
   //  Constructor: 
   //  Sets up this panel to listen for mouse events.       
   //-----------------------------------------------------------
   public PointsPanel() {
      pointList = new ArrayList<Point>();
      points= new ArrayList<Point>();
      
      addMouseListener (new PointsListener());

      setBackground (Color.black);
      setPreferredSize (new Dimension(300, 200));
   }

   //------------------------------------------------------------
   //  Draws all of the points stored in the list.
   //-----------------------------------------------------------
   public void paintComponent (Graphics page) {
      super.paintComponent(page);

      page.setColor (Color.green);

      for (Point spot : pointList)
         page.fillOval (spot.x-3, spot.y-3, 7, 7);

      if(lineDrawn){
    	  for(int ii=0; ii<points.size()-1; ii++){
    		  page.drawLine( (int)points.get(ii).getX() , (int)points.get(ii).getY(), 
    				  (int)points.get(ii+1).getX(), (int)points.get(ii+1).getY());
    	  }
    	  page.drawLine( (int)points.get(points.size()-1).getX() , (int)points.get(points.size()-1).getY(), 
				  (int)points.get(0).getX(), (int)points.get(0).getY());
      }
      
      page.drawString ("Count: " + pointList.size(), 5, 15);
   }



   //***********************************************************
   //  Represents the listener for mouse events.
   //***********************************************************
   private class PointsListener implements MouseListener {
      //-------------------------------------------------------
      //  Adds the current point to the list of points 
      //  and redraws
      //  the panel whenever the mouse button is pressed.
      //------------------------------------------------------
      public void mousePressed (MouseEvent event) {
         pointList.add(event.getPoint());
         repaint();
      }

      //-----------------------------------------------------
      //  Provide empty definitions for unused event methods.
      //-----------------------------------------------------
      public void mouseClicked (MouseEvent event) {}
      public void mouseReleased (MouseEvent event) {}
      public void mouseEntered (MouseEvent event) {}
      public void mouseExited (MouseEvent event) {}
   }

   private class PointComparator implements Comparator<Point>{
      public int compare(Point a, Point b){
         return (int)Math.ceil(a.getX()-b.getX());
      }
   }


   public void createIncrement(){
      Collections.sort(this.pointList, new PointComparator());
      System.out.println("list: "+pointList);

      lineDrawn=true;
      
      points=new ArrayList<Point>();
      points.add(pointList.get(0));
      points.add(pointList.get(1));

      for(int ii=2; ii<pointList.size(); ii++){
         points.add(pointList.get(ii));
         int jj=points.size()-1;
         while(points.size()>2 && isLeftTurn(points.get(jj-2),points.get(jj-1),points.get(jj))){
            //System.out.println("remove"+points.get(jj-1)+"\n\n");
            points.remove(jj-1);
            jj--;
            //jj=points.size()-1;
         }
      }

      System.out.println("\nconvex hull upper: "+points);
      
      //point lower
      ArrayList<Point> pointsLower=new ArrayList<Point>();
      pointsLower.add(pointList.get(pointList.size()-1));
      pointsLower.add(pointList.get(pointList.size()-2));
      
      for(int ii=pointList.size()-3; ii>=0; ii--){
          pointsLower.add(pointList.get(ii));
          int jj=pointsLower.size()-1;
          while(pointsLower.size()>2 && isLeftTurn(pointsLower.get(jj-2),
        		  pointsLower.get(jj-1),pointsLower.get(jj))){
             //System.out.println("remove"+points.get(jj-1)+"\n\n");
             pointsLower.remove(jj-1);
             jj--;
             //jj=points.size()-1;
          }
       }
      
      System.out.println("\nconvex hull lower: "+pointsLower);
      
      for(int ii=1; ii<pointsLower.size()-1; ii++){
    	  points.add(pointsLower.get(ii));
      }
      
      repaint();
   }

   public boolean isLeftTurn(Point a, Point b, Point c){
       double area=((double)(b.getX()-a.getX())*(c.getY()-a.getY()))-((double)(c.getX()-a.getX())*(b.getY()-a.getY()));
      //System.out.println(area+""+a+b+c);
      if (area>=0) return true;
      return false;
   }
   
   public void clearScreen(){
	   pointList=new ArrayList<Point>();
	   points=new ArrayList<Point>();
	   lineDrawn=false;
	   repaint();
   }

}


