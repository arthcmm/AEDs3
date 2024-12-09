package aed3;

import java.io.*;
import java.util.*;

public class BPlusTree<K extends Comparable<K>, V> implements Serializable {
    private final int grau;
    private final String storageFilePath;
    private int rootNodeId;
    private int nextNodeId;

    public BPlusTree(int grau, String storageFilePath) {
        this.grau = grau;
        this.storageFilePath = storageFilePath;
        initializeStorage();
    }

    private void initializeStorage() {
        File storageFile = new File(storageFilePath);
        if (!storageFile.exists()) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storageFile))) {
                LeafNode root = new LeafNode();
                root.id = nextNodeId++;
                rootNodeId = root.id;
                oos.writeObject(root);
            } catch (IOException e) {
                throw new RuntimeException("Erro ao inicializar armazenamento: " + e.getMessage());
            }
        }
    }

    public V get(K key) {
        Node root = loadNode(rootNodeId);
        List<V> values = root.get(key);
        return (values != null && !values.isEmpty()) ? values.get(0) : null; // Retorna o primeiro valor ou null se vazio
    }

    public void insert(K key, V value) {
        Node root = loadNode(rootNodeId);
        root.insert(key, value);
        if (root.isOverflow()) {
            InternalNode newRoot = new InternalNode();
            newRoot.id = nextNodeId++;
            newRoot.children.add(root.id);
            newRoot.splitChild(0, this);
            saveNode(newRoot);
            rootNodeId = newRoot.id;
        }
        saveNode(root);
    }

    public void remove(K idCategoria, V tarefaDaCategoria) {
        Node root = loadNode(rootNodeId);
        root.remove(idCategoria, tarefaDaCategoria);
        if (root instanceof InternalNode && ((InternalNode) root).children.size() == 1) {
            rootNodeId = ((InternalNode) root).children.get(0);
        }
        saveNode(root);
    }

    private Node loadNode(int id) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storageFilePath))) {
            while (true) {
                Node node = (Node) ois.readObject();
                if (node.id == id) {
                    return node;
                }
            }
        } catch (EOFException e) {
            throw new RuntimeException("Nó não encontrado: " + id);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Erro ao carregar nó: " + e.getMessage());
        }
    }

    private void saveNode(Node node) {
        File tempFile = new File(storageFilePath + ".tmp");
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storageFilePath));
             ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(tempFile))) {
            boolean nodeWritten = false;

            while (true) {
                try {
                    Node existingNode = (Node) ois.readObject();
                    if (existingNode.id == node.id) {
                        oos.writeObject(node);
                        nodeWritten = true;
                    } else {
                        oos.writeObject(existingNode);
                    }
                } catch (EOFException e) {
                    break;
                }
            }

            if (!nodeWritten) {
                oos.writeObject(node);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Erro ao salvar nó: " + e.getMessage());
        }

        tempFile.renameTo(new File(storageFilePath));
    }

    public List<K> listAllKeys() {
        List<K> allKeys = new ArrayList<>();
        Node current = loadNode(rootNodeId);

        // Percorre até encontrar o primeiro nó folha
        while (current instanceof InternalNode) {
            current = loadNode(((InternalNode) current).children.get(0));
        }

        // Coleta as chaves de todos os nós folha
        while (current instanceof LeafNode) {
            LeafNode leaf = (LeafNode) current;
            allKeys.addAll(leaf.keys);
            if (leaf.nextNodeId != -1) {
                current = loadNode(leaf.nextNodeId); // Avança para o próximo nó folha
            } else {
                break;
            }
        }

        return allKeys;
    }

    public List<V> search(K descricao) {
        List<V> results = new ArrayList<>();
        Node current = loadNode(rootNodeId);

        // Percorre até o primeiro nó folha
        while (current instanceof InternalNode) {
            current = loadNode(((InternalNode) current).children.get(0));
        }

        // Procura por valores correspondentes em todos os nós folha
        while (current instanceof LeafNode) {
            LeafNode leaf = (LeafNode) current;
            for (int i = 0; i < leaf.keys.size(); i++) {
                if (leaf.keys.get(i).compareTo(descricao) == 0) { // Compara a chave com a descrição
                    results.addAll(leaf.values.get(i)); // Adiciona todos os valores associados à chave
                }
            }
            if (leaf.nextNodeId != -1) {
                current = loadNode(leaf.nextNodeId); // Avança para o próximo nó folha
            } else {
                break;
            }
        }

        return results;
    }


    private abstract class Node implements Serializable {
        int id;
        protected List<K> keys = new ArrayList<>();

        abstract List<V> get(K key);

        abstract void insert(K key, V value);

        abstract void remove(K key, V value);

        abstract boolean isOverflow();

        abstract boolean isUnderflow();

        abstract Node split();

        abstract void merge(Node sibling);

        abstract K getFirstLeafKey();
    }

    private class LeafNode extends Node {
        List<List<V>> values = new ArrayList<>();
        int nextNodeId = -1;

        @Override
        List<V> get(K key) {
            int idx = Collections.binarySearch(keys, key);
            if (idx >= 0) {
                return values.get(idx); // Retorna a lista de valores associada à chave
            }
            return Collections.emptyList(); // Retorna uma lista vazia se a chave não for encontrada
        }

        @Override
        void insert(K key, V value) {
            int idx = Collections.binarySearch(keys, key);
            if (idx >= 0) {
                values.get(idx).add(value); // Adiciona o valor à lista existente
            } else {
                idx = -idx - 1;
                keys.add(idx, key); // Insere a chave na posição correta
                List<V> valueList = new ArrayList<>();
                valueList.add(value);
                values.add(idx, valueList); // Adiciona a nova lista de valores
            }
        }

        @Override
        void remove(K key, V value) {
            int idx = Collections.binarySearch(keys, key);
            if (idx >= 0) {
                values.get(idx).remove(value);
                if (values.get(idx).isEmpty()) {
                    keys.remove(idx);
                    values.remove(idx);
                }
            }
        }

        @Override
        boolean isOverflow() {
            return keys.size() > grau - 1;
        }

        @Override
        boolean isUnderflow() {
            return keys.size() < Math.ceil((grau - 1) / 2.0);
        }

        @Override
        Node split() {
            LeafNode sibling = new LeafNode();
            sibling.id = nextNodeId++;
            int midIndex = keys.size() / 2;

            sibling.keys.addAll(keys.subList(midIndex, keys.size()));
            sibling.values.addAll(values.subList(midIndex, values.size()));

            keys.subList(midIndex, keys.size()).clear();
            values.subList(midIndex, values.size()).clear();

            sibling.nextNodeId = nextNodeId;
            nextNodeId = sibling.id;

            return sibling;
        }

        @Override
        void merge(Node sibling) {
            LeafNode leafSibling = (LeafNode) sibling;
            keys.addAll(leafSibling.keys);
            values.addAll(leafSibling.values);
            nextNodeId = leafSibling.nextNodeId;
        }

        @Override
        K getFirstLeafKey() {
            return keys.get(0);
        }
    }

    private class InternalNode extends Node {
        List<Integer> children = new ArrayList<>();

        @Override
        List<V> get(K key) {
            int idx = Collections.binarySearch(keys, key);
            if (idx >= 0) idx++;
            return loadNode(children.get(idx)).get(key);
        }

        @Override
        public void insert(K key, V value) {
            Node root = loadNode(rootNodeId);
            root.insert(key, value);
            if (root.isOverflow()) {
                InternalNode newRoot = new InternalNode();
                newRoot.id = nextNodeId++;
                newRoot.children.add(root.id);
                newRoot.splitChild(0, BPlusTree.this); // Passa o índice e a instância da árvore
                saveNode(newRoot);
                rootNodeId = newRoot.id;
            }
            saveNode(root);
        }

        @Override
        void remove(K key, V value) {
            int idx = Collections.binarySearch(keys, key);
            if (idx >= 0) idx++;
            Node child = loadNode(children.get(idx));
            child.remove(key, value);
            if (child.isUnderflow()) {
                mergeChild(idx);
            }
            saveNode(child);
        }

        @Override
        boolean isOverflow() {
            return keys.size() > grau - 1;
        }

        @Override
        boolean isUnderflow() {
            return keys.size() < Math.ceil((grau - 1) / 2.0);
        }

        @Override
        Node split() {
            InternalNode sibling = new InternalNode();
            sibling.id = nextNodeId++;
            int midIndex = keys.size() / 2;

            sibling.keys.addAll(keys.subList(midIndex + 1, keys.size()));
            sibling.children.addAll(children.subList(midIndex + 1, children.size()));

            keys.subList(midIndex, keys.size()).clear();
            children.subList(midIndex + 1, children.size()).clear();

            return sibling;
        }

        @Override
        void merge(Node sibling) {
            InternalNode internalSibling = (InternalNode) sibling;
            keys.addAll(internalSibling.keys);
            children.addAll(internalSibling.children);
        }

        @Override
        K getFirstLeafKey() {
            return loadNode(children.get(0)).getFirstLeafKey();
        }

        private void splitChild(int idx, BPlusTree<K, V> tree) {
            Node child = tree.loadNode(children.get(idx));
            Node sibling = child.split();
            children.add(idx + 1, sibling.id);
            keys.add(idx, sibling.getFirstLeafKey());
            tree.saveNode(child);
            tree.saveNode(sibling);
        }

        private void mergeChild(int idx) {
            Node child = loadNode(children.get(idx));
            Node sibling = loadNode(children.get(idx + 1));
            child.merge(sibling);
            children.remove(idx + 1);
            keys.remove(idx);
            saveNode(child);
        }
    }

}
