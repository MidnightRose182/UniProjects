import java.util.ArrayList;

public class PMTree {
    private class Node {
        Node parent;
        Node left, right;
        int days;
        String name;

        int size = 1;



        Node (Node parent, int days, String name) {
            this.parent = parent;
            this.days = days;
            this.left = this.right = null;
            this.name = name;
        }

        /*
         *  When deleting, we want to know how many children a node has.
         */
        int numChildren () {
            return (left == null ? 0 : 1) + (right == null ? 0 : 1);
        }
    }

    private Node root = null;

   /*
   remember to change getNode and class Node back to private
    */
    private Node getNode (int days) { //realised that I probably should have used the getNode function to make this simpler but...eh
        Node cur = root;
        while (cur != null) {
            if (days == cur.days)
                return cur;
            else if (days < cur.days)
                cur = cur.left;
            else
                cur = cur.right;
        }
        return null;
    }

    public String getName (int days) {
        Node cur = root;
        while (cur != null) {
            if (days == cur.days)
                return cur.name;
            else if (days < cur.days)
                cur = cur.left;
            else
                cur = cur.right;
        }
        return null;
    }

    public void insert (int days, String name) {
        Node cur = root;
        if (root == null) {
            root = new Node (null, days, name);
            return;
        }
        /*
        * cur is equal to the root node, if root is empty input becomes root
        * if days from new input match days from current node, replaces name
        * if days < current node, inserts node into left tree if free, loops if left not null
        * As the loop traverses down the tree it adds 1 to the size of nodes it passes through
         */
        while (true) {
            if (days == cur.days) {
                cur.name = name;
                return;
            } else if (days < cur.days) {
                if (cur.left == null) {
                    cur.left = new Node (cur, days, name);
                    cur.size += 1;
                    return;
                } else {
                    cur.size += 1;
                    cur = cur.left;

                }
            } else {
                if (cur.right == null) {
                    cur.right = new Node (cur, days, name);
                    cur.size += 1;
                    return;
                } else {
                    cur.size += 1;
                    cur = cur.right;

                }
            }
        }
    }

    /*
     *  Delete a node from the tree. If the node has one child or none,
     *  delete it as if it, its parent (if it exists) and its child (if
     *  it has one) are a doubly linked list. Otherwise, replace the value
     *  in the node to be deleted with the minimum element of its right
     *  subtree and delete the node that originally contained that element.
     *  Note that, by construction, that node has at most one child (it has
     *  no left child because a left child would contain a smaller value).
     */
    public void delete (int days) {
        Node node = getNode (days);
        if (node == null)
            return;

        if (node.numChildren() < 2) {
            simpleDelete (node);
        } else {
            Node min = getMinNode(node.right);
            simpleDelete (min);
            node.days = min.days;
            node.name = min.name;
        }
    }

    /*
     *  Delete a node that has one child or none. We treat this node,
     *  its parent (if it exists) and its child (if it has one) as a doubly
     *  linked list and delete accordingly. The code is fiddly because of
     *  the special cases. We might be deleting the root (parent == null)
     *  and/or we might be deleting a node with no child, with just a left
     *  child or just a right child.
     */
    private void simpleDelete (Node node) {
        Node child = node.left != null ? node.left : node.right;

        if (node == root) {
            root = child;
            if (root != null)
                root.parent = null;
        } else {
            /*
            If deleted node is left node of parent, child become left node, and vice versa
            Size only needs to change for the node parents, so while loop is used
             */
            if (node == node.parent.left) {
                node.parent.left = child;
            } else {
                node.parent.right = child;
            } if (child != null) {
                child.parent = node.parent;
            }
            while (node.parent != null) {
                node.parent.size -= 1;
                node = node.parent;
            }
        }
    }

    /*
     *  Return the node containing the smallest value in the subtree
     *  rooted at the given node. This is found by following the left
     *  child reference until we get to a node that has no left child.
     */
    private Node getMinNode (Node node) {
        while (node.left != null)
            node = node.left;
        return node;
    }

    public String nthShortest (int n) {
        int count = n;
        Node cur = root;
        if (count > cur.size) {
            return null;
        }
/*
 if n is <= size of left subtree, go left, if not go right and do n-(left.size+1) to adjust necessary number
 making size 0 if null means that if n is 1 then it will return the parent node
 */
        while (cur != null) {
            int size = cur.left != null ? cur.left.size : 0;
            if (count == size + 1) {
                return cur.name;
            } else if (count <= size) {
                cur = cur.left;
            } else {
                cur = cur.right;
                count -= (cur.parent.size - cur.size);
            }

        } return null;
    }

    //After much trial and error I managed to get it working, had a fundamental error in sizing
    public String[] allNShortest (int n) {
        String[] nShortest = new String[n];
        for (int i = 0; i < n; i++) {
            nShortest[i] = nthShortest(i+1);
        }
        return nShortest;
    }


    public static void main(String[] args) {
        PMList pml = new PMList();
        PMTree pmt = new PMTree();
        ArrayList primeList = (ArrayList) pml.getPrimeMinisters();
        for (int i = 0; i < primeList.size(); i ++) {
            PMList.Entry data = (PMList.Entry) primeList.get(i); //Adding all pms as nodes
            pmt.insert(data.days, data.name);
        }
        for (int i = 0; i < 5; i++) {
            String name = pmt.nthShortest((i+1) * 10); //printing 10th, 20th, 30th, 40th, 50th
            System.out.println(((i+1)*10) + "th - " +name);
        }
        System.out.println();
        for (int i = 0; i < 10; i++) {
            String name = pmt.allNShortest(10)[i]; //printing 10 shortest efficiently
            System.out.print(name + (i < 9 ? ", " : ".\n"));
        }
        System.out.println();
        for (int i = 0; i < pml.INCOMPLETE.length; i++) { //deleting died, resigned, still in office
            pmt.delete(pml.INCOMPLETE[i]);
        }

        for (int i = 0; i < 10; i++) {
            String name = pmt.allNShortest(10)[i]; //printing 10 shortest efficiently with the revised tree
            System.out.print(name + (i < 9 ? ", " : ".\n"));
        }
    }

}

/* My attempts at recursion however sine this is my first time using Java at all I had no idea about how to use static variables until I had
already come up with a solution using size

int count = 0;
        }
        Node nodeStart = getMinNode(nodeRoot);
        while (count <= n) {
            if (nodeStart.left != null) {
                nodeStart = nodeStart.left;
            }

            count++;
 */

/*while (cur != null) {
            if (cur.left == null) {
                return cur.name;
            } else if (count == cur.left.size+1) {
                return cur.name;
            } else if (count <= cur.left.size) {
                cur = cur.left;
            } else {
                count -= cur.left.size+1;
                cur = cur.right;
            }
        }*/
/*public String nthShortest (int n, Node node) {
        int count = n;
        Node cur = node;
        while (cur != null) {
            if ((cur.left == null) & (cur.right == null)) {
                return cur.name;
            } else if (count == cur.left.size + 1) {
                return cur.name;
            } else if (count <= cur.left.size) {
                cur = cur.left;
            } else {
                count -= cur.left.size + 1;
                cur = cur.right;
            }
        }
        return null;
    }*/

/* My attempt at making an in order traversal method that doesn't use recursion
String[] nShortest = new String[n];
        Node nodeStart = root;
        int count = 0;
        while (n <= nodeStart.left.size) {
            nodeStart = nodeStart.left; //Reassigns start node to shortest size available
        }
        Node node = getMinNode(nodeStart);
        Node prevRight = null;
        Node prevLeft = null;
        while (true) {
            if (count == n) {
                break;
            } else {
                if (node == nodeStart) {
                    node = getMinNode(nodeStart.right);
                }
                if ((node.left != null) & (node.left != prevLeft)) {
                    node = node.left;

                } else if(node.left == null) {
                    prevLeft = node;
                    nShortest[count] = node.name;
                    node = node.parent;
                    count++;

                } else if ((node.right != null) & (node.right != prevRight)) {
                    nShortest[count] = node.name;
                    count++;
                    node = node.right;

                 } else if ((node.right != null) & (node.right == prevRight)) {
                     node = node.parent;
                 } else if(node.right == null) {
                    prevRight = node;
                    nShortest[count] = node.name;
                    node = node.parent;
                    count++;
                }
            }
        }
 */