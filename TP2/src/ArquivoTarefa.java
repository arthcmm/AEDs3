import java.lang.reflect.Constructor;

import aed3.Arquivo;

public class ArquivoTarefa extends Arquivo<Tarefa> {

    public ArquivoTarefa(Constructor<Tarefa> construtor, String name) throws Exception {
        super(construtor, name);
    }
}