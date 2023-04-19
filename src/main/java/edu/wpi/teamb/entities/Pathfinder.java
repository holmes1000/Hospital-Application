package edu.wpi.teamb.entities;

import edu.wpi.teamb.DBAccess.ORMs.Node;

import java.awt.*;
import java.awt.image.BufferedImage;

import edu.wpi.teamb.pathfinding.PathFinding;
import io.github.palexdev.materialfx.utils.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Pathfinder {
    edu.wpi.teamb.pathfinding.Pathfinder pathfinder;
    ArrayList<Integer> path = new ArrayList<Integer>();

    HashMap<String,Image> floorToPNG = new HashMap<String,Image>();
    String ll1 = "edu/wpi/teamb/img/FloorMapsNoLocationNames/00_thelowerlevel1.png";
    String ll2 = "edu/wpi/teamb/img/FloorMapsNoLocationNames/00_thelowerlevel2.png";
    String groundFloor = "edu/wpi/teamb/img/FloorMapsNoLocationNames/00_thegroundfloor.png";
    String firstFloor = "edu/wpi/teamb/img/FloorMapsNoLocationNames/01_thefirstfloor.png";
    String secondFloor = "edu/wpi/teamb/img/FloorMapsNoLocationNames/02_thesecondfloor.png";
    String thirdFloor = "edu/wpi/teamb/img/FloorMapsNoLocationNames/03_thethirdfloor.png";

    public Pathfinder() throws SQLException{
        PathFinding.ASTAR.init_pathfinder();
//        this.pathfinder = new edu.wpi.teamb.pathfinding.Pathfinder();
        populateFloorsToPNG();
    }

//    public edu.wpi.teamb.pathfinding.Pathfinder getPathfinder(){return pathfinder;}

    public ArrayList<Integer> getPath(){return path;}

    public void populateFloorsToPNG(){
        floorToPNG.put("ll1", new Image("edu/wpi/teamb/img/FloorMapsNoLocationNames/00_thelowerlevel1.png"));
        floorToPNG.put("ll2", new Image("edu/wpi/teamb/img/FloorMapsNoLocationNames/00_thelowerlevel1.png"));
        floorToPNG.put("groundFloor", new Image("edu/wpi/teamb/img/FloorMapsNoLocationNames/00_thelowerlevel1.png"));
        floorToPNG.put("firstFloor", new Image("edu/wpi/teamb/img/FloorMapsNoLocationNames/00_thelowerlevel1.png"));
        floorToPNG.put("secondFloor", new Image("edu/wpi/teamb/img/FloorMapsNoLocationNames/00_thelowerlevel1.png"));
        floorToPNG.put("thirdFloor", new Image("edu/wpi/teamb/img/FloorMapsNoLocationNames/00_thelowerlevel1.png"));
    }

    public HashMap<Integer,Node> get_node_map(){
        return PathFinding.ASTAR.get_node_map();
    }


    public String[] getShortestPath(String algorithm, String bias, int start, int end) throws SQLException {
        if (bias.equals("Elevator")) {path = PathFinding.ELEVATOR_BIAS.findPath(start,end);}
        else if (bias.equals("Stairs")) {path = PathFinding.STAIR_BIAS.findPath(start,end);}
        else if (algorithm.equals("AStar")) {path = PathFinding.ASTAR.findPath(start,end);}
        else if (algorithm.equals("Depth First Search")) {path = PathFinding.DEPTH_FIRST.findPath(start,end);}
        else if (algorithm.equals("Breadth First Search")) {path = PathFinding.BREADTH_FIRST.findPath(start,end);}

//        BufferedImage image = null;
//        try {
//            image = ImageIO.read(new File("src/main/resources/edu/wpi/teamb/img/hospitalmaps/00_thelowerlevel1.png"));
//            Integer width = image.getWidth();
//            Integer height = image.getHeight();
//            image = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
////        Graphics2D graphics = image.createGraphics();
////        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
////        Graphics g = image.getGraphics();
//
//        drawPath(image, path);
//
//        try {
//            ImageIO.write(image, "png", new File("src/main/resources/edu/wpi/teamb/img/hospitalFloorWithPath/mapWithPath.png"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return PathFinding.ASTAR.getPathAsStrings(path);
    }

    public Image getImagesFromShortestPath(String imgURL, Integer mapNumber) throws SQLException {
        //Get the shortest path by running getshortestpath before this function

        //create a List for all the floors that are visited

//        ArrayList<Image> floorsVisitedImage = new ArrayList<Image>();
//        for (int i = 0; i < floorsVisited.size(); i++){
//            String floor = floorsVisited.get(i);
//            floorsVisitedImage.add(floorToPNG.get(floor));
//        }

        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(imgURL));
            //Integer width = image.getWidth();
            //Integer height = image.getHeight();
            //image = new BufferedImage(width,height,BufferedImage.TYPE_3BYTE_BGR);

        } catch (IOException e) {
            e.printStackTrace();
        }
//        Graphics2D graphics = image.createGraphics();
//        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0f));
//        Graphics g = image.getGraphics();

        //drawPath(image, path, mapNumber);

        try {
            ImageIO.write(image, "png", new File("src/main/resources/edu/wpi/teamb/img/hospitalFloorWithPath/mapWithPath"+ mapNumber + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image new_image = SwingFXUtils.toFXImage(image,null);
        return new_image;
    }


    public ArrayList<Integer> shortestPath (int start, int goal) throws SQLException{
        return PathFinding.ASTAR.findPath(start,goal);
    }

    public void drawPath(BufferedImage image, ArrayList<Integer> shortestPath, String mapNumber) throws SQLException {
        Graphics g = image.getGraphics();
        g.setColor(Color.RED);
        ((Graphics2D) g).setStroke(new BasicStroke(6));

        //Get a hashmap with NodeID -> Node Objects.
        HashMap<Integer, Node> node_map = PathFinding.ASTAR.get_node_map();


        for (int i = 0; i < shortestPath.size() - 1; i++){
            Node initialNode = node_map.get(shortestPath.get(i));
            Node endNode = node_map.get(shortestPath.get(i + 1));

            if(initialNode.getFloor().equals(mapNumber) && endNode.getFloor().equals(mapNumber)){
                int initialNodeXCoord = initialNode.getxCoord();
                int initialNodeYCoord = initialNode.getyCoord();


                int endNodeXCoord = endNode.getxCoord();
                int endNodeYCoord = endNode.getyCoord();

                g.drawLine(initialNodeXCoord, initialNodeYCoord, endNodeXCoord, endNodeYCoord);
            }
        }

    }


//    public static void main(String[] args){
//
//    }





}
