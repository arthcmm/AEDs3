import java.io.File;

import aed3.Arquivo;

public class IO {
    private static Arquivo<Tarefa> arqTarefas;

    public static void main(String[] args) {
        try {
            // Remove o arquivo anterior (apenas para testes)
            new File("src/dados/tarefas.db").delete();
            new File("src/dados/cestos.idx").delete();
            new File("src/dados/diretorio.idx").delete();

            // Inicializa o arquivo de tarefas
            arqTarefas = new Arquivo<>(Tarefa.class.getConstructor(), "src/dados/tarefas.db");

            // Cria algumas tarefas
            Tarefa t1 = new Tarefa(-1, -1,"Estudar Java", "2023-01-01", "", "Pendente", "Alta");
            Tarefa t2 = new Tarefa(-1, -1,"Implementar CRUD", "2023-01-02", "", "Em Progresso", "Média");
            Tarefa t3 = new Tarefa(-1, -1,"Testar Aplicação", "2023-01-03", "", "Pendente", "Baixa");

            int id1 = arqTarefas.create(t1);
            t1.setID(id1);

            int id2 = arqTarefas.create(t2);
            t2.setID(id2);

            int id3 = arqTarefas.create(t3);
            t3.setID(id3);

            // Lê e exibe as tarefas
            System.out.println(arqTarefas.read(id1));
            System.out.println(arqTarefas.read(id2));
            System.out.println(arqTarefas.read(id3));

            // Atualiza uma tarefa
            t2.setStatus("Concluída");
            t2.setDataConclusao("2023-01-10");
            arqTarefas.update(t2);

            // Exibe a tarefa atualizada
            System.out.println(arqTarefas.read(id2));

            // Exclui uma tarefa
            arqTarefas.delete(id3);

            // Verifica se a tarefa foi excluída
            Tarefa t = arqTarefas.read(id3);
            if (t == null) {
                System.out.println("Tarefa excluída com sucesso.");
            } else {
                System.out.println("Falha ao excluir a tarefa.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
