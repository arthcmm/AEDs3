package aed3;
import java.io.*;
import java.lang.reflect.Constructor;
import java.util.ArrayList;

public class Arquivo<T extends Registro & RegistroHashExtensivel<T>> {
    private RandomAccessFile arquivo;
    private Constructor<T> construtor;
    private String nomeArquivo;
    private int ultimoID;
    private HashExtensivel<ParChaveEndereco> indice; // Índice direto

    public Arquivo(Constructor<T> construtor, String nomeArquivo) throws Exception {
        this.construtor = construtor;
        this.nomeArquivo = nomeArquivo;
        arquivo = new RandomAccessFile(nomeArquivo, "rw");

        // Inicializa o índice
        indice = new HashExtensivel<>(
                ParChaveEndereco.class.getConstructor(),
                4, // quantidade de registros por cesto
                "src/dados/diretorio.idx",
                "src/dados/cestos.idx"
        );

        // Verifica se o arquivo está vazio para inicializar o último ID
        if (arquivo.length() == 0) {
            arquivo.writeInt(0); // Cabeçalho com o último ID
            ultimoID = 0;
        } else {
            arquivo.seek(0);
            ultimoID = arquivo.readInt();
        }
    }

    public int create(T objeto) throws Exception {
        arquivo.seek(0);
        ultimoID = arquivo.readInt();
        ultimoID++;
        objeto.setID(ultimoID);

        arquivo.seek(arquivo.length());
        long endereco = arquivo.getFilePointer();
        byte[] ba = objeto.toByteArray();
        short tamanho = (short) ba.length;

        arquivo.writeByte(' '); // Lápide
        arquivo.writeShort(tamanho);
        arquivo.write(ba);

        // Atualiza o último ID no cabeçalho
        arquivo.seek(0);
        arquivo.writeInt(ultimoID);

        // Insere no índice
        ParChaveEndereco par = new ParChaveEndereco(objeto.getID(), endereco);
        indice.create(par);

        return ultimoID;
    }

    public T read(int id) throws Exception {
        // Usa o índice para obter o endereço
        ParChaveEndereco par = indice.read(id);
        if (par != null) {
            long endereco = par.getEndereco();
            arquivo.seek(endereco);
            byte lapide = arquivo.readByte();
            short tamanho = arquivo.readShort();
            byte[] ba = new byte[tamanho];
            arquivo.read(ba);

            if (lapide != '*') {
                T objeto = construtor.newInstance();
                objeto.fromByteArray(ba);
                return objeto;
            }
        }
        return null; // Não encontrado
    }

    public boolean update(T novoObjeto) throws Exception {
        // Usa o índice para obter o endereço
        ParChaveEndereco par = indice.read(novoObjeto.getID());
        if (par != null) {
            long endereco = par.getEndereco();
            arquivo.seek(endereco);
            byte lapide = arquivo.readByte();
            short tamanho = arquivo.readShort();
            byte[] ba = new byte[tamanho];
            arquivo.read(ba);

            if (lapide != '*') {
                T objeto = construtor.newInstance();
                objeto.fromByteArray(ba);

                byte[] novoBa = novoObjeto.toByteArray();
                short novoTamanho = (short) novoBa.length;

                if (novoTamanho <= tamanho) {
                    arquivo.seek(endereco + 3);
                    arquivo.write(novoBa);
                } else {
                    // Marca o registro atual como excluído
                    arquivo.seek(endereco);
                    arquivo.writeByte('*');

                    // Move para o final do arquivo e escreve o novo registro
                    arquivo.seek(arquivo.length());
                    long novoEndereco = arquivo.getFilePointer();
                    arquivo.writeByte(' ');
                    arquivo.writeShort(novoTamanho);
                    arquivo.write(novoBa);

                    // Atualiza o índice com o novo endereço
                    indice.update(new ParChaveEndereco(novoObjeto.getID(), novoEndereco));
                }
                return true;
            }
        }
        return false; // Não encontrado
    }

    public boolean delete(int id) throws Exception {
        // Usa o índice para obter o endereço
        ParChaveEndereco par = indice.read(id);
        if (par != null) {
            long endereco = par.getEndereco();
            arquivo.seek(endereco);
            byte lapide = arquivo.readByte();

            if (lapide != '*') {
                arquivo.seek(endereco);
                arquivo.writeByte('*');

                // Remove do índice
                indice.delete(id);

                return true;
            }
        }
        return false; // Não encontrado
    }

    public ArrayList<T> list() throws Exception {
    ArrayList<T> objects = new ArrayList<>();
    try (RandomAccessFile raf = new RandomAccessFile(this.nomeArquivo, "rw")) {
        long pos = 4;
        if(4 + 1 >= raf.length())
            throw new Exception("Arquivo vazio");
        // Percorre todo o arquivo
        while (pos < raf.length()) {
            raf.seek(pos);
            
            // Ler o metadado (lápide)
            byte lapide = raf.readByte();
            int tamArq = raf.readInt();

            // Se não está excluído
            if (lapide == 0) {
                byte[] array = new byte[tamArq];
                raf.read(array);
                
                T obj = construtor.newInstance();
                obj.fromByteArray(array); // Reconstrói o objeto a partir do array de bytes
                objects.add(obj);
                pos = raf.getFilePointer();
            }
            else{
                pos = raf.getFilePointer() + tamArq;
            }
        }
    } catch (Exception e) {
        System.out.println("Erro ao listar os registros.");
        e.printStackTrace();
    }
    return objects;
}
}
