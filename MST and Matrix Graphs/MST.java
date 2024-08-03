import java.util.*;
import static java.lang.Math.*;

public class MST {

    static double getTotalEdgeWeight (Graph g) {
        int v = g.numVertices();
        double total = 0;
        /*
        Loops through all combinations of vertices, checks whether it is a valid edge.
        If true, adds weight to a total which is returned.
         */
        for (int i = 0 ; i<v ; i++) {
            for (int j = 0 ; j<v ; j++) {
                if (g.isEdge(i,j) == true) {total += g.weight(i,j);}
            }
        }
        if (g.isDirected() == false) {total = total/2;}

        return total;
    }

    static Graph getRandomGraph (int n) {
        MatrixGraph g = new MatrixGraph(n, false);
        ArrayList<double[]> points = new ArrayList<>();
        Random rand = new Random();

        //Generates random points, then adds them as pairs to an array list
        for (int i = 0 ; i<n ; i++) {
            double[] p = new double[2];
            p[0] = rand.nextDouble();
            p[1] = rand.nextDouble();
            points.add(p);
        }

        /*
        Creates boolean array to track visited nodes, calculates difference in x and y coordinates,
        weight is equal to the square root of the sum of the squared differences
        */
        boolean visited[] = new boolean[n];
        for (int i = 0 ; i<n ; i++) {
            visited[i] = true;
            for (int j = 0 ; j<n ; j++) {
                if (visited[j] != true){
                    double deltaX = points.get(i)[0]-points.get(j)[0];
                    double deltaY = points.get(i)[1]-points.get(j)[1];
                    double weight = sqrt(pow(deltaX,2)+pow(deltaY,2));
                    g.addEdge(i, j, weight);
                }
            }
        } return g;
    }

    public static class Queue {
        /*
        Queue for BFS, stores integers except for remove() which
        returns a string since needs to be able to return null
         */
        private class Item {
            int value;
            Item next;

            Item(int value) {
                this.value = value;
                this.next = null;
            }
        }

            private Item front = null;
            private Item back = null;
            private int length = 0;

        public boolean isEmpty() {return this.length == 0 ? true : false;}

        public int length() {return this.length;}

        public void add (int n) {
            if (isEmpty()) {
                front = back = new Item(n);
            } else {
                back.next = new Item(n);
                back = back.next;
            }
            length++;
        }

        public String remove () {
            if(isEmpty()){
                return null;
            }
            String n = String.valueOf(front.value);
            front = front.next;
            if (front == null) {
                back = null;
            }
            length--;
            return n;
        }
    }

    static Graph getBaseTree (Graph g) {
        MatrixGraph m = new MatrixGraph(g.numVertices, g.isDirected());
        boolean visited[] = new boolean[g.numVertices];
        visited[0] = true;
        Queue q = new Queue();
        q.add(0);
        //Starts base tree at 0, uses BFS to travel along the tree and whenever
        // find vertex for the first time adds edge to the tree

        while (q.isEmpty() != true) {
            int cur = Integer.parseInt(q.remove()); //int parse because removes as a string
            for (int n : g.outNeighbours(cur)) {
                if (visited[n] != true) {
                    visited[n] = true; //assigning vertex as visited
                    q.add(n);
                    double w = g.weight(cur, n);
                    m.addEdge(cur,n,w);
                }
            }
        } return m;
    }

    static Edge longestEdgeOnPath (Graph g, int source, int dest) {
        if (source == dest) {
            return null;
        } else {
            Edge e = new Edge(0, 0, 0);
            boolean visited[] = new boolean[g.numVertices];
            visited[source] = true;
            ArrayList<Edge> edges = new ArrayList<>();
            Queue q = new Queue();

            q.add(source);
            boolean done = false;
            while (q.isEmpty() != true) {
                int cur = Integer.parseInt(q.remove());
                /*
                This part works by identifying the unique path from x to y. It does this by using a BFS
                to visit nodes, adding all edges until it finds the destination in the neighbours of the node
                it is visiting. It will then add that edge to the total edges and break the loop.
                 */
                for (int n : g.outNeighbours(cur)) {
                    if (n == dest) {
                        double w = g.weight(cur, n);
                        Edge edge = new Edge(cur, n, w);
                        edges.add(edge);
                        done = true;
                        break;
                    }
                }

                if (done == true) {
                    break;
                }

                for (int n : g.outNeighbours(cur)) {
                    if (visited[n] != true) {
                        visited[n] = true;
                        q.add(n);
                        double w = g.weight(cur, n);
                        Edge edge = new Edge(cur, n, w);
                        edges.add(edge);
                    }
                }
            }
            /*
            After the BFS has completed, loops through the list of edges to check which ones are
            ones on the path. The last one added will always be a valid edge in the path, and so
            can be our starting point. Working backwards from the end of the path, the x coordinate
            will always be equal to the y coordinate of the one previous and we can follow this back
            until we each the source edge.
             */
            ArrayList<Edge> path = new ArrayList<>();
            Edge last = edges.get(edges.size() - 1);
            path.add(last);
            int n = last.x;
            while (n != source) {
                for (int i = 0; i < edges.size(); i++) {
                    Edge edge = edges.get(i);
                    if (edge.y == n) {
                        path.add(edge);
                        n = edge.x;
                    }
                }
            }

            //Finally, looping through the path, finds the highest edge weight and returns that edge.
            for (int i = 0; i < path.size(); i++) {
                Edge edge = path.get(i);
                if (edge.w > e.w) {
                    e = edge;
                }
            }
            return e;
        }
    }

    static Graph getMST (Graph g) {
        Graph b = getBaseTree(g);
        int v = g.numVertices;
        //Loops through all, if xy is edge in g, checks if edge is already in base
        // if not then compares weight of longest edge on that unique path and xy
        // if xy is smaller, deletes longest edge and adds xy
        for (int x = 0; x<v ; x++) {
            for (int y = 0; y<v ; y++) {
                if (g.isEdge(x,y) == true) {
                    if (b.isEdge(x,y) != true) {
                        Edge e = longestEdgeOnPath(b, x, y);
                        if (g.weight(x, y) < e.w) {
                            b.deleteEdge(e.x, e.y);
                            b.addEdge(x, y, g.weight(x,y));
                        }
                    }
                }
            }
        }
        return b;
    }


    public static void main (String[] args){
        GraphOfEssex goe = new GraphOfEssex();
        Graph g1 = goe.getGraph();
        Graph g2 = getMST(g1);
        System.out.println(String.format("Essex graph weight: %.2f", getTotalEdgeWeight(g1)));
        System.out.println(String.format("Essex MST weight: %.2f", getTotalEdgeWeight(g2)));


        double totalWeight = 0;
        double totalMST = 0;
        for (int i = 0 ; i<100 ; i++) {
            Graph g3 = getRandomGraph(50);
            totalWeight += getTotalEdgeWeight(g3);
            Graph g4 = getMST(g3);
            totalMST += getTotalEdgeWeight(g4);
        }
        totalWeight /= 100;
        totalMST /= 100;
        System.out.println(String.format("\nAverage weight: %.2f", totalWeight));
        System.out.println(String.format("Average MST weight: %.2f", totalMST));
    }
}
