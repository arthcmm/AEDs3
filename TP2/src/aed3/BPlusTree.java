package aed3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BPlusTree<K extends Comparable<K>, V> {
    private int grau; // O grau da árvore B+ (máximo de chaves por nó)
    private Node root; // Raiz da árvore

    public BPlusTree(int grau) {
        this.grau = grau;
        this.root = new LeafNode();
    }

    public void insert(K key, V value) {
        root.insert(key, value);
        if (root.isOverflow()) {
            InternalNode newRoot = new InternalNode();
            newRoot.children.add(root);
            newRoot.splitChild(0);
            root = newRoot;
        }
    }

    public List<V> get(K key) {
        return root.get(key);
    }

    public void remove(K key, V value) {
        root.remove(key, value);
        if (root instanceof InternalNode && ((InternalNode) root).children.size() == 1) {
            root = ((InternalNode) root).children.get(0);
        }
    }

    private abstract class Node {
        List<K> keys;

        abstract List<V> get(K key);

        abstract void insert(K key, V value);

        abstract void remove(K key, V value);

        abstract boolean isOverflow();

        abstract boolean isUnderflow();

        abstract void merge(Node sibling);

        abstract Node split();

        abstract K getFirstLeafKey();
    }

    // Classe que representa nós folha
    private class LeafNode extends Node {
        List<V> values;
        LeafNode next;

        LeafNode() {
            this.keys = new ArrayList<>();
            this.values = new ArrayList<>();
        }

        @Override
        List<V> get(K key) {
            int idx = Collections.binarySearch(keys, key);
            if (idx >= 0) {
                return Collections.singletonList(values.get(idx));
            } else {
                return new ArrayList<>(); // Retorna lista vazia se a chave não for encontrada
            }
        }

        @Override
        void insert(K key, V value) {
            int idx = Collections.binarySearch(keys, key);
            if (idx >= 0) {
                values.add(idx, value);
            } else {
                idx = -idx - 1;
                keys.add(idx, key);
                values.add(idx, value);
            }
        }

        @Override
        void remove(K key, V value) {
            int idx = Collections.binarySearch(keys, key);
            if (idx >= 0) {
                keys.remove(idx);
                values.remove(idx);
            }
        }

        @Override
        boolean isOverflow() {
            return keys.size() > grau - 1;
        }

        @Override
        boolean isUnderflow() {
            return keys.size() < grau / 2;
        }

        @Override
        void merge(Node sibling) {
            LeafNode node = (LeafNode) sibling;
            keys.addAll(node.keys);
            values.addAll(node.values);
            next = node.next;
        }

        @Override
        Node split() {
            LeafNode sibling = new LeafNode();
            int midIndex = keys.size() / 2;

            sibling.keys.addAll(keys.subList(midIndex, keys.size()));
            sibling.values.addAll(values.subList(midIndex, values.size()));

            keys.subList(midIndex, keys.size()).clear();
            values.subList(midIndex, values.size()).clear();

            sibling.next = next;
            next = sibling;

            return sibling;
        }

        @Override
        K getFirstLeafKey() {
            return keys.get(0);
        }
    }

    // Classe que representa nós internos
    private class InternalNode extends Node {
        List<Node> children;

        InternalNode() {
            this.keys = new ArrayList<>();
            this.children = new ArrayList<>();
        }

        @Override
        List<V> get(K key) {
            return getChild(key).get(key);
        }

        @Override
        void insert(K key, V value) {
            Node child = getChild(key);
            child.insert(key, value);
            if (child.isOverflow()) {
                splitChild(children.indexOf(child));
            }
        }

        @Override
        void remove(K key, V value) {
            Node child = getChild(key);
            child.remove(key, value);
            if (child.isUnderflow()) {
                mergeChild(children.indexOf(child));
            }
        }

        @Override
        boolean isOverflow() {
            return keys.size() > grau - 1;
        }

        @Override
        boolean isUnderflow() {
            return keys.size() < grau / 2;
        }

        @Override
        void merge(Node sibling) {
            InternalNode node = (InternalNode) sibling;
            keys.addAll(node.keys);
            children.addAll(node.children);
        }

        @Override
        Node split() {
            InternalNode sibling = new InternalNode();
            int midIndex = keys.size() / 2;

            sibling.keys.addAll(keys.subList(midIndex + 1, keys.size()));
            sibling.children.addAll(children.subList(midIndex + 1, children.size()));

            keys.subList(midIndex, keys.size()).clear();
            children.subList(midIndex + 1, children.size()).clear();

            return sibling;
        }

        @Override
        K getFirstLeafKey() {
            return children.get(0).getFirstLeafKey();
        }

        private Node getChild(K key) {
            int idx = Collections.binarySearch(keys, key);
            if (idx >= 0) {
                idx++;
            } else {
                idx = -idx - 1;
            }
            return children.get(idx);
        }

        private void splitChild(int idx) {
            Node child = children.get(idx);
            Node sibling = child.split();
            children.add(idx + 1, sibling);
            keys.add(idx, sibling.getFirstLeafKey());
        }

        private void mergeChild(int idx) {
            Node child = children.get(idx);
            Node sibling = children.get(idx + 1);
            child.merge(sibling);
            children.remove(sibling);
            keys.remove(idx);
        }
    }
}
