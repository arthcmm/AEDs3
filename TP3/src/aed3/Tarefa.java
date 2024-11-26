package aed3;

import java.io.*;
import java.time.LocalDate;

public class Tarefa implements Registro, RegistroHashExtensivel<Tarefa> {
    private int id;
    private int idCategoria;
    private String nome;
    private LocalDate dataCriacao;
    private LocalDate dataConclusao;
    private String status;
    private int prioridade;

    // Construtor padrão
    public Tarefa() {
        // Inicialização padrão
    }

    // Construtor com parâmetros
    public Tarefa(int id, int idCategoria, String nome, LocalDate dataCriacao, LocalDate dataConclusao, String status, int prioridade) {
        this.id = id;
        this.idCategoria = idCategoria;
        this.nome = nome;
        this.dataCriacao = dataCriacao;
        this.dataConclusao = dataConclusao;
        this.status = status;
        this.prioridade = prioridade;
    }

    // Implementação dos métodos da interface Registro
    @Override
    public int getID() {
        return id;
    }

    @Override
    public void setID(int id) {
        this.id = id;
    }

    // Getters e Setters para os atributos

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDate getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(LocalDate dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(int prioridade) {
        this.prioridade = prioridade;
    }

    // Implementação de toByteArray()
    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(id);
        dos.writeInt(idCategoria);
        dos.writeUTF(nome);
        dos.writeLong(dataCriacao.toEpochDay());
        dos.writeBoolean(dataConclusao != null);
        if (dataConclusao != null) {
            dos.writeLong(dataConclusao.toEpochDay());
        }
        dos.writeUTF(status);
        dos.writeInt(prioridade);

        return baos.toByteArray();
    }

    // Implementação de fromByteArray()
    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        id = dis.readInt();
        idCategoria = dis.readInt();
        nome = dis.readUTF();
        dataCriacao = LocalDate.ofEpochDay(dis.readLong());
        boolean hasDataConclusao = dis.readBoolean();
        if (hasDataConclusao) {
            dataConclusao = LocalDate.ofEpochDay(dis.readLong());
        } else {
            dataConclusao = null;
        }
        status = dis.readUTF();
        prioridade = dis.readInt();
    }

    // Implementação do hashCode() da interface RegistroHashExtensivel
    @Override
    public int hashCode() {
        return id;
    }

    // Implementação do size() da interface RegistroHashExtensivel
    @Override
    public short size() {
        try {
            return (short) toByteArray().length;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public String toString() {
        return "Tarefa [id=" + id + ", idCategoria=" + idCategoria + ", nome=" + nome + ", dataCriacao=" + dataCriacao +
                ", dataConclusao=" + dataConclusao + ", status=" + status +
                ", prioridade=" + prioridade + "]";
    }
}
