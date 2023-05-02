package edu.wpi.teamb.controllers.mapeditor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

import edu.wpi.teamb.DBAccess.DAO.Repository;
import edu.wpi.teamb.DBAccess.Full.FullNode;
import edu.wpi.teamb.DBAccess.ORMs.Move;
import edu.wpi.teamb.pathfinding.PathFinding;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Tooltip;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import static javafx.scene.paint.Color.*;

public class MoveMap {
    private String currentFloor = "1";
    private ArrayList<Move> allMoves;
    private Tooltip nameToolTip;
    private HashMap<Integer,ArrayList<Move>> move_map = new HashMap<>();
    private ArrayList<Move> upcoming_moves = new ArrayList<>();
    private ArrayList<Move> moved_today = new ArrayList<>();
    private HashMap<Integer, FullNode> fullNodesByID = new HashMap<>();
    HashMap<String,FullNode> fullNodesByLongname = new HashMap<>();
    private ArrayList<FullNode> fullNodes = new ArrayList<>();
    Group pathGroup = new Group();
    Group moveInfo = new Group();

    public MoveMap() {
        getMoveMap();
        PathFinding.ASTAR.force_init();
        this.allMoves = Repository.getRepository().getAllMoves();
        this.fullNodesByID = PathFinding.ASTAR.getFullNodesByID();
        this.fullNodes = PathFinding.ASTAR.getFullNodes();
        getFilteredLongnames();
        handle_move();
        displayMoves(currentFloor);
        System.out.println("Move Map initialized");
    }

    public void getMoveMap(){
        HashMap<Integer,ArrayList<Move>> move_map = new HashMap<>();
        ArrayList<Move> moves = Repository.getRepository().getAllMoves();
        ArrayList<Move> currentMove = new ArrayList<>();
        for (Move move : moves) {
            if (move_map.containsKey(move.getNodeID())) {currentMove = move_map.get(move.getNodeID());}
            else {currentMove = new ArrayList<>();}
            currentMove.add(move);
            move_map.put(move.getNodeID(),currentMove);
        }
        this.move_map = move_map;

    }

    public void handle_move() {
        HashMap<Integer,Move> nodes_to_update = new HashMap<>();
        upcoming_moves.clear();
        LocalDate current_date = LocalDate.now(); // possibly not getting set to the right value
        System.out.println(current_date);
        LocalDate tempDate;
        for (Integer id : move_map.keySet()){
            if (move_map.get(id).size() >= 1) {
                tempDate = move_map.get(id).get(0).getDate().toLocalDate().minusYears(1);
                for (Move move : move_map.get(id)) {
                    LocalDate move_date =  move.getDate().toLocalDate();

                    if ((move_date.isAfter(tempDate)) && move_date.isBefore(current_date)) {
                        tempDate = move_date;
                        nodes_to_update.put(move.getNodeID(),move);
                    }

                    else if (move_date.equals(current_date)) {
                        tempDate = move_date;
                        nodes_to_update.put(move.getNodeID(),move);
                    }
                    if (move_date.isAfter(current_date)) {upcoming_moves.add(move);}
                }
            }
        }
        update_nodes_from_moves(nodes_to_update);
    }

    public void update_nodes_from_moves(HashMap<Integer,Move> nodes_to_update){
        fullNodes = new ArrayList<>();
        for (Integer id : fullNodesByID.keySet()){
            if (nodes_to_update.containsKey(id)){
                FullNode newNode = fullNodesByID.get(id);
                newNode.setLongName(nodes_to_update.get(id).getLongName());
                fullNodes.add(newNode);
            }
            else {
                fullNodes.add(fullNodesByID.get(id));
            }
        }
        getFilteredLongnames();
    }

    public ArrayList<String> getFilteredLongnames(){
        ArrayList<String> filtered_names = new ArrayList<>();
        for (FullNode node : fullNodes){
            fullNodesByID.put(node.getNodeID(),node);
            fullNodesByLongname.put(node.getLongName(),node);

            if (!node.getNodeType().equals("HALL")) {filtered_names.add(node.getLongName());}
        }
        Collections.sort(filtered_names);
        return filtered_names;
    }

    public void displayMoves(String currentFloor){
        moveInfo.getChildren().clear();
        for (Move move : upcoming_moves) {
            if (fullNodesByID.get(move.getNodeID()).getNodeID() != fullNodesByLongname.get(move.getLongName()).getNodeID() && Objects.equals(currentFloor, fullNodesByID.get(move.getNodeID()).getFloor())) {
                int originalX = fullNodesByLongname.get(move.getLongName()).getxCoord();
                int originalY = fullNodesByLongname.get(move.getLongName()).getyCoord();
                System.out.println("old "+ fullNodesByLongname.get(move.getLongName()).getNodeID());
                int newX = fullNodesByID.get(move.getNodeID()).getxCoord();
                int newY = fullNodesByID.get(move.getNodeID()).getyCoord();
                System.out.println("new "+ fullNodesByID.get(move.getNodeID()).getNodeID());
                Line line = new Line(originalX, originalY, newX, newY);
                line.setStrokeWidth(4);
                animateLine(line);
                Circle c = new Circle(originalX, originalY, 5, RED);
                Circle finalC = c;
                line.setOnMouseEntered(event -> {
                    nameToolTip = new Tooltip();
                    nameToolTip.setText(display_move_info(move));
                    nameToolTip.setShowDelay(Duration.millis(1));
                    nameToolTip.hideDelayProperty().set(Duration.seconds(.5));
                    Tooltip.install(line, nameToolTip);
                        });
                pathGroup.getChildren().add(c);
                Boolean exists = false;
                for (Node child : pathGroup.getChildren()) {
                    if (child.contains(newX,newY)) {
                        exists = true;
                    }
                }
                if (!exists) {
                    c = new Circle(newX, newY, 5, PURPLE);
                    pathGroup.getChildren().add(c);
                }
                pathGroup.getChildren().add(line);
            }
        }
        pathGroup.toFront();
        moveInfo.toFront();
    }

    private void animateLine(Line line)
    {
        // https://stackoverflow.com/questions/36727777/how-to-animate-dashed-line-javafx
        line.getStrokeDashArray().setAll(25d, 15d, 10d, 20d);
        final double maxOffset =
                line.getStrokeDashArray().stream()
                        .reduce(
                                0d,
                                (a, b) -> a - b
                        );

        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.ZERO,
                        new KeyValue(
                                line.strokeDashOffsetProperty(),
                                0,
                                Interpolator.LINEAR
                        )
                ),
                new KeyFrame(
                        Duration.seconds(2),
                        new KeyValue(
                                line.strokeDashOffsetProperty(),
                                maxOffset,
                                Interpolator.LINEAR
                        )
                )
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public String display_move_info(Move move){
        String text = move.getLongName() + " will be moving to node " + move.getNodeID() + " on " + move.getDate();
        return text;
    }

    public void addToMoveMap(Move moveToAdd) {
        if (move_map.containsKey(moveToAdd.getNodeID())) {
            move_map.get(moveToAdd.getNodeID()).add(moveToAdd);
        } else {
            ArrayList<Move> moves = new ArrayList<>();
            moves.add(moveToAdd);
            move_map.put(moveToAdd.getNodeID(), moves);
        }
    }

    public String getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(String currentFloor) {
        this.currentFloor = currentFloor;
    }

    public ArrayList<Move> getAllMoves() {
        return allMoves;
    }

    public void setAllMoves(ArrayList<Move> allMoves) {
        this.allMoves = allMoves;
    }

    public Tooltip getNameToolTip() {
        return nameToolTip;
    }

    public void setNameToolTip(Tooltip nameToolTip) {
        this.nameToolTip = nameToolTip;
    }

    public HashMap<Integer, ArrayList<Move>> getMove_map() {
        return move_map;
    }

    public void setMove_map(HashMap<Integer, ArrayList<Move>> move_map) {
        this.move_map = move_map;
    }

    public ArrayList<Move> getUpcoming_moves() {
        return upcoming_moves;
    }

    public void setUpcoming_moves(ArrayList<Move> upcoming_moves) {
        this.upcoming_moves = upcoming_moves;
    }

    public ArrayList<Move> getMoved_today() {
        return moved_today;
    }

    public void setMoved_today(ArrayList<Move> moved_today) {
        this.moved_today = moved_today;
    }

    public HashMap<Integer, FullNode> getFullNodesByID() {
        return fullNodesByID;
    }

    public void setFullNodesByID(HashMap<Integer, FullNode> fullNodesByID) {
        this.fullNodesByID = fullNodesByID;
    }

    public HashMap<String, FullNode> getFullNodesByLongname() {
        return fullNodesByLongname;
    }

    public void setFullNodesByLongname(HashMap<String, FullNode> fullNodesByLongname) {
        this.fullNodesByLongname = fullNodesByLongname;
    }

    public ArrayList<FullNode> getFullNodes() {
        return fullNodes;
    }

    public void setFullNodes(ArrayList<FullNode> fullNodes) {
        this.fullNodes = fullNodes;
    }

    public Group getPathGroup() {
        return pathGroup;
    }

    public void setPathGroup(Group pathGroup) {
        this.pathGroup = pathGroup;
    }

    public Group getMoveInfo() {
        return moveInfo;
    }

    public void setMoveInfo(Group moveInfo) {
        this.moveInfo = moveInfo;
    }
}
