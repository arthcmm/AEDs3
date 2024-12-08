package aed3;

import java.io.*;

public class Rotulo implements Registro, RegistroHashExtensivel<Rotulo> {
    private int id;             // Identificador único
    private String rotulo;      // Descrição do rótulo

    public Rotulo() {}

    public Rotulo(int id, String rotulo) {
        this.id = id;
        this.rotulo = rotulo;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public String getRotulo() {
        return rotulo;
    }

    public void setRotulo(String rotulo) {
        this.rotulo = rotulo;
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(id);
        dos.writeUTF(rotulo);

        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        id = dis.readInt();
        rotulo = dis.readUTF();
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public short size() {
        // Tamanho do id + tamanho serializado do rotulo
        int size = Integer.BYTES + 2 + rotulo.getBytes(java.nio.charset.StandardCharsets.UTF_8).length;
        return (short) size;
    }

    @Override
    public String toString() {
        return "Rotulo{" +
                "id=" + id +
                ", rotulo='" + rotulo + '\'' +
                '}';
    }
}
