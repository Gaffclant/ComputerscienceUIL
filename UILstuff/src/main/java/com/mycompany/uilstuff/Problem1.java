package com.mycompany.uilstuff;
import java.util.*;
import java.util.stream.Collectors;


public class Problem1 {
     public static void main(String[] args) {
         Graph g = MakeTestGraph();
         //System.out.println(g);
         String s = g.find(g.nodes.get(0),g.nodes.get(5));
         System.out.println(s);
     }
     
     public static Graph MakeTestGraph() {
        Node a = new Node("A");
        Node b = new Node("B");
        Node c = new Node("C");
        Node d = new Node("D");
        Node e = new Node("E");
        Node f = new Node("F");
        
        a.connectedNodes.add(b);
        a.connectedNodes.add(c);
        
        b.connectedNodes.add(a);
        b.connectedNodes.add(c);
        
        c.connectedNodes.add(a);
        c.connectedNodes.add(c);
        c.connectedNodes.add(d);
        
        d.connectedNodes.add(c);
        d.connectedNodes.add(f);
        
        f.connectedNodes.add(d);
        
        Graph g = new Graph();
        g.nodes.add(a);
        g.nodes.add(b);
        g.nodes.add(c);
        g.nodes.add(d);
        g.nodes.add(e);
        g.nodes.add(f);
        
        return g;
     }
}

class Graph {
    public List<Node> nodes;
    
    public Graph() {
        nodes = new ArrayList<Node>();
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Node n : nodes) {
            sb.append(n.toString()).append(System.getProperty("line.separator"));
        }
        return sb.toString();
    }
    
    public String find (Node A, Node B){        
        PriorityQueue <Node> queue = new PriorityQueue<>();
        String output = "";
        List<Node> visited = new ArrayList<>();
        queue.add(A);
        
        //visited.add(A);
        while (!queue.isEmpty()){
            Node current = queue.remove();
            current.path.add(current);
            //System.out.println(current.path);
            if (current.compareTo(B)==0){
                List<String> values = current.path.stream().map(x -> x.value).collect(Collectors.toList());
                return String.join(" => ", values);
                
            }
            if (!visited.contains(current))
                visited.add(current);
                for (Node i : current.connectedNodes){
                    if(!visited.contains(i) && !queue.contains(i)){
                        queue.add(i);
                        i.path.addAll(current.path);
                        //visited.add(i);
                    }
            }
            
        }
        return "Not Found";
        
        
        
        
    }
}

class Node implements Comparable<Node>{
    public String value;
    public ArrayList<Node> connectedNodes;
    public ArrayList<Node> path;

    
    public Node(String value) {
        this.value = value;
        connectedNodes = new ArrayList<Node>();
        path = new ArrayList<Node>();

    }
    
    public String toString() {
        List<String> connectedValues = connectedNodes
            .stream()
            .map(x -> x.value)
            .collect(Collectors.toList());
        return value + " => " + String.join(", ", connectedValues);
    }

    @Override
    public int compareTo(Node b) {
        if (this == b) return 0;
        return this.value.compareTo(b.value);
    }
    
    
    
    
}