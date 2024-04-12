package com.example.t23new.tree;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class TwoThreeTreeTest<K extends Comparable<K>, V> {
    private int size;
    private TreeNode root;
    private boolean successfulInsertion;
    private boolean successfulDeletion;
    private boolean split;
    private boolean underflow;
    private boolean first;
    private boolean singleNodeUnderflow;
    private final Class<K> keyClass;

    private enum Nodes {
        LEFT, MIDDLE, RIGHT, DUMMY
    }

    public TwoThreeTreeTest(Class<K> keyClass) {
        size = 0;
        root = null;
        successfulInsertion = false;
        successfulDeletion = false;
        underflow = false;
        singleNodeUnderflow = false;
        split = false;
        first = false;
        this.keyClass = keyClass;
    }

    public class Node {

    }

    @SuppressWarnings("unchecked")
    public class TreeNode extends Node {
        public K[] keys = (K[]) Array.newInstance(keyClass, 2);
        public Node[] children = (Node[]) Array.newInstance(Node.class, 3);
        int degree;

        TreeNode() {
            degree = 0;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (K key : keys) {
                builder.append(key == null ? "-" : key).append(", ");
            }
            return builder.toString();
        }
    }

    public class LeafNode extends Node {

        K key;
        V val;

        LeafNode(K key, V val) {
            this.key = key;
            this.val = val;
        }

        void print() {
            System.out.print(key + " ");
        }

        @Override
        public String toString() {
            return "(" + key + "," + val + ")";
        }
    }

    private void insertKey(K key, V val, Runnable runnable) {
        Node[] array;
        array = insert(key, val, root, runnable);
        if (array[1] == null) {
            root = (TreeNode) array[0];
        } else {
            TreeNode treeRoot = new TreeNode();
            treeRoot.children[0] = array[0];
            treeRoot.children[1] = array[1];
            updateTree(treeRoot);
            root = treeRoot;
        }
    }

    @SuppressWarnings("unchecked")
    private Node[] insert(K key, V val, Node n, Runnable runnable) {
        runnable.run();
        Node[] array = (Node[]) Array.newInstance(Node.class, 2);
        Node[] catchArray = (Node[]) Array.newInstance(Node.class, 2);

        TreeNode t = null;
        if (n instanceof TreeNode) {
            t = (TreeNode) n;
        }
        if (root == null && !first) {
            first = true;
            t = new TreeNode();
            t.children[0] = insert(key, val, t.children[0], runnable)[0];
            updateTree(t);
            array[0] = t;
            array[1] = null;

        } else if (t != null && !(t.children[0] instanceof LeafNode)) {
            if (key.compareTo(t.keys[0]) < 0) {
                catchArray = insert(key, val, t.children[0], runnable);
                t.children[0] = catchArray[0];
                if (split) {
                    if (t.degree <= 2) {
                        split = false;
                        t.children[2] = t.children[1];
                        t.children[1] = catchArray[1];
                        updateTree(t);
                        array[0] = t;
                        array[1] = null;
                    } else {
                        TreeNode newNode = new TreeNode();
                        newNode.children[0] = t.children[1];
                        newNode.children[1] = t.children[2];
                        updateTree(newNode);
                        t.children[1] = catchArray[1];
                        t.children[2] = null;
                        updateTree(t);
                        array[0] = t;
                        array[1] = newNode;
                    }
                } else {
                    updateTree(t);
                    array[0] = t;
                    array[1] = null;
                }
            } else if (key.compareTo(t.keys[0]) >= 0 && (t.children[2] == null || key.compareTo(t.keys[1]) < 0)) {
                catchArray = insert(key, val, t.children[1], runnable);
                t.children[1] = catchArray[0];
                if (split) {
                    if (t.degree <= 2) {
                        split = false;
                        t.children[2] = catchArray[1];
                        updateTree(t);
                        array[0] = t;
                        array[1] = null;
                    } else {
                        TreeNode newNode = new TreeNode();
                        newNode.children[0] = catchArray[1];
                        newNode.children[1] = t.children[2];
                        updateTree(newNode);
                        t.children[2] = null;
                        updateTree(t);
                        array[0] = t;
                        array[1] = newNode;
                    }
                } else {
                    updateTree(t);
                    array[0] = t;
                    array[1] = null;
                }
            } else if (key.compareTo(t.keys[1]) >= 0) {
                catchArray = insert(key, val, t.children[2], runnable);
                t.children[2] = catchArray[0];
                if (split) {
                    if (t.degree > 2) {
                        TreeNode newNode = new TreeNode();
                        newNode.children[0] = catchArray[0];
                        newNode.children[1] = catchArray[1];
                        updateTree(newNode);
                        t.children[2] = null;
                        updateTree(t);
                        array[0] = t;
                        array[1] = newNode;

                    }
                } else {
                    updateTree(t);
                    array[0] = t;
                    array[1] = null;
                }
            }
        } else if (t != null) {
            LeafNode l1, l2 = null, l3 = null;
            l1 = (LeafNode) t.children[0];
            if (t.children[1] != null && t.children[1] instanceof LeafNode) {
                l2 = (LeafNode) t.children[1];
            }
            if (t.children[2] != null && t.children[2] instanceof LeafNode) {
                l3 = (LeafNode) t.children[2];
            }
            if (t.degree <= 2) {
                if (t.degree == 1 && key.compareTo(l1.key) > 0) {
                    LeafNode leaf = new LeafNode(key, val);
                    t.children[1] = leaf;
                } else if (t.degree == 1 && key.compareTo(l1.key) < 0) {
                    LeafNode leaf = new LeafNode(key, val);
                    t.children[1] = l1;
                    t.children[0] = leaf;
                } else if (t.degree == 2 && key.compareTo(l1.key) < 0) {
                    LeafNode leaf = new LeafNode(key, val);
                    t.children[2] = l2;
                    t.children[1] = l1;
                    t.children[0] = leaf;
                } else if (t.degree == 2 && key.compareTo(l2.key) < 0 && key.compareTo(l1.key) > 0) {
                    LeafNode leaf = new LeafNode(key, val);
                    t.children[2] = l2;
                    t.children[1] = leaf;
                } else if (t.degree == 2) {
                    LeafNode leaf = new LeafNode(key, val);
                    t.children[2] = leaf;
                }
                updateTree(t);
                array[0] = t;
                array[1] = null;
            } else {
                split = true;
                if (key.compareTo(l1.key) < 0) {
                    LeafNode leaf = new LeafNode(key, val);
                    TreeNode newNode = new TreeNode();
                    t.children[0] = leaf;
                    t.children[1] = l1;
                    t.children[2] = null;
                    updateTree(t);
                    newNode.children[0] = l2;
                    newNode.children[1] = l3;
                    updateTree(newNode);
                    array[0] = t;
                    array[1] = newNode;
                } else if (key.compareTo(l1.key) >= 0 && key.compareTo(l2.key) < 0) {
                    LeafNode leaf = new LeafNode(key, val);
                    TreeNode newNode = new TreeNode();
                    t.children[1] = leaf;
                    t.children[2] = null;
                    updateTree(t);
                    newNode.children[0] = l2;
                    newNode.children[1] = l3;
                    updateTree(newNode);
                    array[0] = t;
                    array[1] = newNode;
                } else if (key.compareTo(l2.key) >= 0 && key.compareTo(l3.key) < 0) {
                    LeafNode leaf = new LeafNode(key, val);
                    t.children[2] = null;
                    updateTree(t);
                    TreeNode newNode = new TreeNode();
                    newNode.children[0] = leaf;
                    newNode.children[1] = l3;
                    updateTree(newNode);
                    array[0] = t;
                    array[1] = newNode;
                } else if (key.compareTo(l3.key) >= 0) {
                    LeafNode leaf = new LeafNode(key, val);
                    t.children[2] = null;
                    updateTree(t);
                    TreeNode newNode = new TreeNode();
                    newNode.children[0] = l3;
                    newNode.children[1] = leaf;
                    updateTree(newNode);
                    array[0] = t;
                    array[1] = newNode;
                }
            }
            successfulInsertion = true;
        } else if (n == null) {
            successfulInsertion = true;
            array[0] = new LeafNode(key, val);
            array[1] = null;
            return array;
        }
        return array;
    }

    private Node remove(K key, Node n, Runnable runnable) {
        runnable.run();
        TreeNode t = null;
        if (n instanceof TreeNode) {
            t = (TreeNode) n;
        }
        if (n == null) {
            return null;
        }
        if (t != null && t.children[0] instanceof TreeNode) {
            if (key.compareTo(t.keys[0]) < 0) {
                t.children[0] = remove(key, t.children[0], runnable);
                if (singleNodeUnderflow) {
                    TreeNode child = (TreeNode) t.children[0];
                    TreeNode rightChild = (TreeNode) t.children[1];
                    if (rightChild.degree == 2) {
                        rightChild.children[2] = rightChild.children[1];
                        rightChild.children[1] = rightChild.children[0];
                        rightChild.children[0] = child;
                        updateTree(rightChild);
                        t.children[0] = rightChild;
                        t.children[1] = t.children[2];
                        t.children[2] = null;
                        if (t.degree == 2) {
                            singleNodeUnderflow = true;
                            t = (TreeNode) t.children[0];
                        } else {
                            singleNodeUnderflow = false;
                        }
                    } else if (rightChild.degree == 3) {
                        TreeNode newNode = new TreeNode();
                        newNode.children[0] = t.children[0];
                        newNode.children[1] = rightChild.children[0];
                        t.children[0] = newNode;
                        updateTree(newNode);
                        rightChild.children[0] = rightChild.children[1];
                        rightChild.children[1] = rightChild.children[2];
                        rightChild.children[2] = null;
                        updateTree(rightChild);
                        singleNodeUnderflow = false;
                    }
                } else if (underflow) {
                    underflow = false;
                    TreeNode child = (TreeNode) t.children[0];
                    TreeNode rightChild = (TreeNode) t.children[1];
                    if (rightChild.degree == 3) {
                        Node reference = rightChild.children[0];
                        rightChild.children[0] = rightChild.children[1];
                        rightChild.children[1] = rightChild.children[2];
                        rightChild.children[2] = null;
                        updateTree(rightChild);
                        child.children[1] = reference;
                        updateTree(child);
                    } else if (rightChild.degree == 2) {
                        Node reference = child.children[0];
                        rightChild.children[2] = rightChild.children[1];
                        rightChild.children[1] = rightChild.children[0];
                        rightChild.children[0] = reference;
                        updateTree(rightChild);
                        t.children[0] = rightChild;
                        if (t.degree == 3) {
                            t.children[1] = t.children[2];
                            t.children[2] = null;
                        } else {
                            Node ref = t.children[0];
                            t = (TreeNode) ref;
                            singleNodeUnderflow = true;
                        }
                    }
                }
                updateTree(t);
            } else if (key.compareTo(t.keys[0]) >= 0 && (t.children[2] == null || key.compareTo(t.keys[1]) < 0)) {
                t.children[1] = remove(key, t.children[1], runnable);
                if (singleNodeUnderflow) {
                    TreeNode leftChild = (TreeNode) t.children[0];
                    TreeNode child = (TreeNode) t.children[1];
                    TreeNode rightChild = (TreeNode) t.children[2];
                    if (leftChild.degree == 2) {
                        leftChild.children[2] = child;
                        t.children[1] = rightChild;
                        t.children[2] = null;
                        updateTree(leftChild);
                        if (t.degree == 2) {
                            singleNodeUnderflow = true;
                            t = (TreeNode) t.children[0];
                        } else {
                            singleNodeUnderflow = false;
                        }
                    } else if (rightChild != null && rightChild.degree == 2) {
                        rightChild.children[2] = rightChild.children[1];
                        rightChild.children[1] = rightChild.children[0];
                        rightChild.children[0] = child;
                        updateTree(rightChild);
                        t.children[1] = rightChild;
                        t.children[2] = null;
                        singleNodeUnderflow = false;

                    } else if (leftChild.degree == 3) {
                        TreeNode newNode = new TreeNode();
                        newNode.children[0] = leftChild.children[2];
                        newNode.children[1] = child;
                        t.children[1] = newNode;
                        updateTree(newNode);
                        updateTree(leftChild);
                        singleNodeUnderflow = false;
                    } else if (rightChild != null && rightChild.degree == 3) {
                        TreeNode newNode = new TreeNode();
                        newNode.children[0] = child;
                        newNode.children[1] = rightChild.children[0];
                        rightChild.children[0] = rightChild.children[1];
                        rightChild.children[1] = rightChild.children[2];
                        rightChild.children[2] = null;
                        t.children[1] = newNode;
                        updateTree(newNode);
                        updateTree(rightChild);
                        singleNodeUnderflow = false;
                    }
                } else if (underflow) {
                    underflow = false;
                    TreeNode leftChild = (TreeNode) t.children[0];
                    TreeNode child = (TreeNode) t.children[1];
                    TreeNode rightChild = (TreeNode) t.children[2];
                    if (leftChild.degree == 3) {
                        Node reference = leftChild.children[2];
                        leftChild.children[2] = null;
                        child.children[1] = child.children[0];
                        child.children[0] = reference;
                        updateTree(leftChild);
                        updateTree(child);
                    } else if (rightChild != null && rightChild.degree == 3) {
                        Node reference = rightChild.children[0];
                        rightChild.children[0] = rightChild.children[1];
                        rightChild.children[1] = rightChild.children[2];
                        rightChild.children[2] = null;
                        updateTree(rightChild);
                        child.children[1] = reference;
                        updateTree(child);
                    } else if (leftChild.degree == 2) {
                        Node reference = child.children[0];
                        leftChild.children[2] = reference;
                        updateTree(leftChild);
                        t.children[1] = null;
                        if (t.degree == 3) {
                            t.children[1] = t.children[2];
                            t.children[2] = null;
                        } else {
                            singleNodeUnderflow = true;
                            t = (TreeNode) t.children[0];
                        }
                    } else if (rightChild != null && rightChild.degree == 2) {
                        Node reference = child.children[0];
                        rightChild.children[2] = rightChild.children[1];
                        rightChild.children[1] = rightChild.children[0];
                        rightChild.children[0] = reference;
                        updateTree(rightChild);
                        t.children[1] = rightChild;
                        t.children[2] = null;
                        singleNodeUnderflow = false;
                    }
                }
                updateTree(t);
            } else if (key.compareTo(t.keys[1]) >= 0) {
                t.children[2] = remove(key, t.children[2], runnable);
                if (singleNodeUnderflow) {
                    TreeNode child = (TreeNode) t.children[2];
                    TreeNode leftChild = (TreeNode) t.children[1];
                    if (leftChild.degree == 2) {
                        leftChild.children[2] = child;
                        t.children[2] = null;
                        updateTree(leftChild);
                    } else if (leftChild.degree == 3) {
                        TreeNode newNode = new TreeNode();
                        newNode.children[0] = leftChild.children[2];
                        newNode.children[1] = t.children[2];
                        t.children[2] = newNode;
                        updateTree(newNode);
                        updateTree(leftChild);
                    }
                    singleNodeUnderflow = false;
                } else if (underflow) {
                    underflow = false;
                    TreeNode leftChild = (TreeNode) t.children[1];
                    TreeNode child = (TreeNode) t.children[2];
                    if (leftChild.degree == 3) {
                        Node reference = leftChild.children[2];
                        leftChild.children[2] = null;
                        child.children[1] = child.children[0];
                        child.children[0] = reference;
                        updateTree(leftChild);
                        updateTree(child);
                    } else if (leftChild.degree == 2) {
                        Node reference = child.children[0];
                        leftChild.children[2] = reference;
                        updateTree(leftChild);
                        t.children[2] = null;
                    }
                }
                updateTree(t);
            }
        } else if (t != null && t.children[0] instanceof LeafNode) {
            LeafNode l1 = null, l2 = null, l3 = null;
            if (t.children[0] != null && t.children[0] instanceof LeafNode) {
                l1 = (LeafNode) t.children[0];
            }
            if (t.children[1] != null && t.children[1] instanceof LeafNode) {
                l2 = (LeafNode) t.children[1];
            }
            if (t.children[2] != null && t.children[2] instanceof LeafNode) {
                l3 = (LeafNode) t.children[2];
            }
            if (t.degree == 3) {
                if (key.compareTo(l1.key) == 0) {
                    t.children[0] = l2;
                    t.children[1] = l3;
                    t.children[2] = null;
                } else if (l2 != null && key.compareTo(l2.key) == 0) {
                    t.children[1] = l3;
                    t.children[2] = null;
                } else if (l3 != null && key.compareTo(l3.key) == 0) {
                    t.children[2] = null;
                }
                updateTree(t);
            } else if (t.degree == 2) {
                underflow = true;
                if (l1.key.compareTo(key) == 0) {
                    t.children[0] = l2;
                    t.children[1] = null;
                } else if (key.compareTo(l2.key) == 0) {
                    t.children[1] = null;
                }
            } else if (t.degree == 1) {
                if (key.compareTo(l1.key) == 0) {
                    t.children[0] = null;
                }
            }
            successfulDeletion = true;
        }
        return t;
    }

    private void updateTree(TreeNode t) {
        if (t != null) {
            if (t.children[2] != null && t.children[1] != null && t.children[0] != null) {
                t.degree = 3;
                t.keys[0] = getValueForKey(t, Nodes.LEFT);
                t.keys[1] = getValueForKey(t, Nodes.RIGHT);
            } else if (t.children[1] != null && t.children[0] != null) {
                t.degree = 2;
                t.keys[0] = getValueForKey(t, Nodes.LEFT);
                t.keys[1] = null;
            } else if (t.children[0] != null) {
                t.degree = 1;
                t.keys[1] = t.keys[0] = null;
            }
        }
    }

    private K getValueForKey(Node n, Nodes whichVal) {
        K key = null;
        TreeNode t = null;
        LeafNode l = null;
        if (n instanceof TreeNode) {
            t = (TreeNode) n;
        } else {
            l = (LeafNode) n;
        }
        if (l != null) {
            key = l.key;
        }
        if (t != null) {
            if (null != whichVal) {
                switch (whichVal) {
                    case LEFT:
                        key = getValueForKey(t.children[1], Nodes.DUMMY);
                        break;
                    case RIGHT:
                        key = getValueForKey(t.children[2], Nodes.DUMMY);
                        break;
                    case DUMMY:
                        key = getValueForKey(t.children[0], Nodes.DUMMY);
                        break;
                    default:
                        break;
                }
            }
        }
        return key;
    }

    private boolean search(K key, Node n) {
        boolean found = false;
        TreeNode t = null;
        LeafNode l = null;
        if (n instanceof TreeNode) {
            t = (TreeNode) n;
        } else {
            l = (LeafNode) n;
        }
        if (t != null) {
            if (t.degree == 1) {
                found = search(key, t.children[0]);
            } else if (t.degree == 2 && key.compareTo(t.keys[0]) < 0) {
                found = search(key, t.children[0]);
            } else if (t.degree == 2 && key.compareTo(t.keys[0]) >= 0) {
                found = search(key, t.children[1]);
            } else if (t.degree == 3 && key.compareTo(t.keys[0]) < 0) {
                found = search(key, t.children[0]);
            } else if (t.degree == 3 && key.compareTo(t.keys[0]) >= 0 && key.compareTo(t.keys[1]) < 0) {
                found = search(key, t.children[1]);
            } else if (t.degree == 3 && key.compareTo(t.keys[1]) >= 0) {
                found = search(key, t.children[2]);
            }
        } else if (l != null && key.compareTo(l.key) == 0) {
            return true;
        }
        return found;
    }

    public V get(K key, Runnable runnable) {
        LeafNode searched = searchNode(key, root, runnable);
        if (searched == null) {
            throw new IllegalArgumentException("Node with key " + key + " not found");
        }
        return searched.val;
    }

    private LeafNode searchNode(K key, Node n, Runnable runnable) {
        runnable.run();
        LeafNode found = null;
        TreeNode t = null;
        LeafNode l = null;
        if (n instanceof TreeNode) {
            t = (TreeNode) n;
        } else {
            l = (LeafNode) n;
        }
        if (t != null) {
            if (t.degree == 1) {
                found = searchNode(key, t.children[0], runnable);
            } else if (t.degree == 2 && key.compareTo(t.keys[0]) < 0) {
                found = searchNode(key, t.children[0], runnable);
            } else if (t.degree == 2 && key.compareTo(t.keys[0]) >= 0) {
                found = searchNode(key, t.children[1], runnable);
            } else if (t.degree == 3 && key.compareTo(t.keys[0]) < 0) {
                found = searchNode(key, t.children[0], runnable);
            } else if (t.degree == 3 && key.compareTo(t.keys[0]) >= 0 && key.compareTo(t.keys[1]) < 0) {
                found = searchNode(key, t.children[1], runnable);
            } else if (t.degree == 3 && key.compareTo(t.keys[1]) >= 0) {
                found = searchNode(key, t.children[2], runnable);
            }
        } else if (l != null && key.compareTo(l.key) == 0) {
            return l;
        }
        return found;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public void clear() {
        root = null;
        size = 0;
        successfulInsertion = false;
        successfulDeletion = false;
        underflow = false;
        singleNodeUnderflow = false;
        split = false;
        first = false;
    }

    private void keyOrderList(Node n) {
        TreeNode t = null;
        LeafNode l = null;
        if (n instanceof TreeNode) {
            t = (TreeNode) n;
        } else {
            l = (LeafNode) n;
        }
        if (t != null) {
            if (t.children[0] != null) {
                keyOrderList(t.children[0]);
            }
            if (t.children[1] != null) {
                keyOrderList(t.children[1]);
            }
            if (t.children[2] != null) {
                keyOrderList(t.children[2]);
            }
        } else if (l != null) {
            System.out.print(l.key + " ");
        }
    }


    private int height(Node n) {
        TreeNode t = null;
        LeafNode l = null;
        if (n instanceof TreeNode) {
            t = (TreeNode) n;
        } else {
            l = (LeafNode) n;
        }
        if (t != null) {
            return 1 + height(t.children[0]);
        }

        return 0;
    }

    public boolean insert(K key, V val, Runnable runnable) {
        boolean insert = false;
        split = false;
        if (!search(key)) {
            insertKey(key, val, runnable);
        }
        if (successfulInsertion) {
            size++;
            insert = successfulInsertion;
            successfulInsertion = false;
        }
        return insert;
    }

    public boolean search(K key) {
        return search(key, root);
    }

    public boolean remove(K key, Runnable runnable) {
        boolean delete = false;
        singleNodeUnderflow = false;
        underflow = false;
        if (search(key)) {
            root = (TreeNode) remove(key, root, runnable);
            if (root.degree == 1 && root.children[0] instanceof TreeNode) {
                root = (TreeNode) root.children[0];
            }
        }
        if (successfulDeletion) {
            size--;
            delete = successfulDeletion;
            successfulDeletion = false;
            if (size == 0) {
                root = null;
                first = false;
            } else {
                updateTree(root);
            }
        }

        return delete;
    }

    public Iterator<K, V> begin() {
        return new TreeIterator(nodes(), 0);
    }

    public Iterator<K, V> end() {
        var nodes = nodes();
        return new TreeIterator(nodes, nodes.size() - 1);
    }

    public Iterator<K, V> rBegin() {
        var nodes = nodes();
        return new ReverseTreeIterator(nodes, nodes.size() - 1);
    }

    public Iterator<K, V> rEnd() {
        return new ReverseTreeIterator(nodes(), 0);
    }

    public List<LeafNode> nodes() {
        if (root == null)
            return Collections.emptyList();
        return nodes(root);
    }

    private List<LeafNode> nodes(Node n) {
        if (n instanceof LeafNode l) {
            return List.of(l);
        }
        List<LeafNode> leafNodes = new ArrayList<>();
        var t = (TreeNode) n;
        if (t.children[0] != null) {
            leafNodes.addAll(nodes(t.children[0]));
        }
        if (t.children[1] != null) {
            leafNodes.addAll(nodes(t.children[1]));
        }
        if (t.children[2] != null) {
            leafNodes.addAll(nodes(t.children[2]));
        }
        return leafNodes;
    }

    public void keyOrderList() {
        System.out.println("Keys");
        keyOrderList(root);
        System.out.println();
    }


    public int numberOfNodes() {
        return size;
    }

    public int height() {
        return height(root);
    }

    public TreeNode getRoot() {
        return root;
    }


    private class TreeIterator implements Iterator<K, V> {
        private final List<LeafNode> nodes;
        private int currentIndex;

        public TreeIterator(List<LeafNode> nodes, int startIndex) {
            this.nodes = nodes;
            this.currentIndex = startIndex;
        }

        @Override
        public V get() {
            return nodes.get(currentIndex).val;
        }

        @Override
        public void set(V val) {
            nodes.get(currentIndex).val = val;
        }

        @Override
        public void next() {
            if (inBounds()) {
                currentIndex++;
            }
        }

        @Override
        public void prev() {
            if (inBounds()) {
                currentIndex--;
            }
        }

        @Override
        public boolean inBounds() {
            return currentIndex >= 0 && currentIndex < nodes.size();
        }
    }

    private class ReverseTreeIterator implements Iterator<K, V> {
        private final List<LeafNode> nodes;
        private int currentIndex;

        public ReverseTreeIterator(List<LeafNode> nodes, int startIndex) {
            this.nodes = nodes;
            this.currentIndex = startIndex;
        }

        @Override
        public V get() {
            return nodes.get(currentIndex).val;
        }

        @Override
        public void set(V val) {
            nodes.get(currentIndex).val = val;
        }

        @Override
        public void next() {
            if (inBounds()) {
                currentIndex--;
            }
        }

        @Override
        public void prev() {
            if (inBounds()) {
                currentIndex++;
            }
        }

        @Override
        public boolean inBounds() {
            return currentIndex >= 0 && currentIndex < nodes.size();
        }
    }
}