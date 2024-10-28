import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import aed3.Registro;

public class Categoria implements Registro {

    private int id;
    private String nome;

    public Categoria(String nome) {
        this.nome = nome;
        this.id = -1;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getID() {
        return this.id;
    }

    public String getNome() {
        return this.nome;
    }

    public byte[] toByteArray() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeInt(this.id);
            dos.writeUTF(this.nome);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] array) {
        ByteArrayInputStream bais = new ByteArrayInputStream(array);
        DataInputStream dis = new DataInputStream(bais);
        try {
            this.id = dis.readInt();
            this.nome = dis.readUTF();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}