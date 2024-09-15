import java.io.IOException;

public interface Registro {
    int getID();
    void setID(int id);
    byte[] toByteArray() throws IOException;
    void fromByteArray(byte[] ba) throws IOException;
}
