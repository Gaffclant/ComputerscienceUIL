
import com.sun.source.tree.WhileLoopTree;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;




public class Problem1 {
    public static void main(String[] args) throws IOException {
        //Graph g = MakeTestGraph();
        //System.out.println(g);
        //String s = g.find(g.nodes.get(0),g.nodes.get(5));
        //System.out.println(s);
        File f = new File("Test");
        Scanner sc = new Scanner(f);
        int walls = sc.nextInt();
        int lines = sc.nextInt();
        char[][] grid = null;

        Graph graph = new Graph();
        //converting the text into a grid
        for (int i =0;i<lines;i++) {
            String line = sc.next();
            if (grid == null)
                grid = new char[lines][line.length()];
            for (int j = 0; j < line.length(); j++) {
                char c = line.toCharArray()[j];
                grid[i][j] = c;
                if (c == 'o'){
                    graph.rootNode = new Node("o",new int[]{i,j});
                    //graph.nodes.add(graph.rootNode);
                }
                /**
                switch (c) {
                    case 'o':
                        graph.rootNode = new Node("Home");
                        graph.rootNode.pos = new int[]{i,j};
                        graph.nodes.add(graph.rootNode);
                        break;
                    case '-':
                        Node valley = new Node(String.format("(%s, %s)", i, j));
                        valley.pos = new int[]{i,j};
                        graph.nodes.add(valley);
                        break;
                    default:
                }
                 **/
            }
        }

        //main loop/algorithm
        while (walls>0) {
            graph.clear(); //clear the graph, we only save the root node but we remove it from the graph.nodes
            recurse(graph.rootNode.pos, grid, graph, null); //Build the graph from city point
            //System.out.println(graph);
            if(graph.Edges.isEmpty()){     //If it is already safe, don't bother to run the next step
                System.out.println("Success");
                System.exit(1);
                break;
            }
            for (Node E : graph.Edges){
                String s = graph.find(E,graph.rootNode); //Find the shortest path from every edge node to the city
                System.out.println(s);
                String[] result = s.split(" => ");
                for (String i : result){
                    for (Node n : graph.nodes){
                        if ((n.value).equals(i) && n != graph.rootNode)
                            n.counter++; //Increase the counter of each node that is visited
                    }
                }

                //System.out.println(result);

            }
            Node max = null;
            for (Node n : graph.nodes){ //There may be a better way to find max using comparable, but I was too lazy.
                if(max == null)
                    max = n;
                else
                    max = n.counter>max.counter ? n : max;
            }
            grid[max.pos[0]][max.pos[1]]='x';
            System.out.println(display(grid));

        walls--;
        }
        graph.clear(); //Clears the graph again
        recurse(graph.rootNode.pos, grid, graph, null);//final grid of city
        System.out.println(graph);
        if (graph.Edges.isEmpty()) //If there is no edges in the final graph, then there is no possible path from city to edge. Therefore it is a success
            System.out.println("success");
        else
            System.out.println("fail");


    }
    public static String display(char[][]grid){
        String lineSeparator = System.lineSeparator();
        StringBuilder sb = new StringBuilder();

        for (char[] row : grid)
            sb.append(Arrays.toString(row)).append(lineSeparator);

        return sb.toString();



    }

    //Recursive function that builds the graph
    public static void recurse(int[] pos, char[][] grid, Graph graph, Node prev){

        try {
            char val = grid[pos[0]][pos[1]];
            Node current = null;

            switch (val) {
                case 'x':
                    return;
                case 'o':
                    if ((graph.nodes.contains(graph.rootNode)))
                        return;
                    current = graph.rootNode;
                    //System.out.println(graph);
                    graph.nodes.add(graph.rootNode);
                    break;
                    /**
                    graph.rootNode = new Node("o", pos);
                    graph.nodes.add(graph.rootNode);
                    current = graph.rootNode;
                    break;
                     **/
                case '-':
                    for (Node n : graph.nodes){
                        if (Arrays.equals(n.pos,pos))
                            return;
                    }
                    if (pos[0] == 0 || pos[0] == grid.length-1 || pos[1] == 0 || pos[1] == grid[0].length-1) {
                        current = new Node(String.format("(E, %s, %s)", pos[0], pos[1]), pos);
                        graph.Edges.add(current);
                    }
                    else
                        current = new Node(String.format("(-, %s, %s)", pos[0], pos[1]), pos);
                    graph.nodes.add(current);
                    prev.connectedNodes.add(current);
                    if(!current.connectedNodes.contains(prev))
                        current.connectedNodes.add(prev);
                    break;
            }
            recurse(new int[]{pos[0] + 1, pos[1]}, grid, graph, current);
            recurse(new int[]{pos[0], pos[1] + 1}, grid, graph, current);
            recurse(new int[]{pos[0] - 1, pos[1]}, grid, graph, current);
            recurse(new int[]{pos[0], pos[1] - 1}, grid, graph, current);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }


    }


    public static Graph MakeTestGraph() {
        Node a = new Node("A");
        Node b = new Node("B");
        Node c = new Node("C");
        Node d = new Node("D");
        Node e = new Node("E");
        Node f = new Node("F");
        Node z = new Node("Z");

        a.connectedNodes.add(b);
        a.connectedNodes.add(c);

        b.connectedNodes.add(a);
        b.connectedNodes.add(c);
        b.connectedNodes.add(z);

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
        g.nodes.add(z);

        return g;
    }
}

class Graph {
    public List<Node> nodes;
    public Node rootNode;
    public List<Node> Edges;

    public Graph() {
        nodes = new ArrayList<Node>();
        Edges = new ArrayList<Node>();


    }

    //clears the graph
    public void clear(){

        rootNode.connectedNodes.clear();
        nodes.clear();
        Edges.clear();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Node n : nodes) {
            sb.append(n.toString()).append(System.getProperty("line.separator"));
        }
        return sb.toString();
    }

    public String find (Node A, Node B){
        for (Node n : nodes){
            n.path.clear();
        }
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
    public int[] pos = new int[2];
    public int counter = 0;

    public Node(String value) {
        this.value = value;
        connectedNodes = new ArrayList<Node>();
        path = new ArrayList<Node>();

    }

    public Node(String value, int [] pos) {
        this.value = value;
        this.pos = pos;
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