package aed3;
import java.io.*;

public class ParChaveEndereco implements RegistroHashExtensivel<ParChaveEndereco> {
    private int chave;
    private long endereco;

    public ParChaveEndereco() {
        this(-1, -1);
    }

    public ParChaveEndereco(int chave, long endereco) {
        this.chave = chave;
        this.endereco = endereco;
    }

    public int getChave() {
        return chave;
    }

    public long getEndereco() {
        return endereco;
    }

    @Override
    public int hashCode() {
        return chave;
    }

    @Override
    public short size() {
        return (short) (Integer.BYTES + Long.BYTES);
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(chave);
        dos.writeLong(endereco);
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        chave = dis.readInt();
        endereco = dis.readLong();
    }
}
