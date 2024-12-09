import aed3.*;
import java.time.LocalDate;
import java.util.List;

public class Teste {

    public static void main(String[] args) {
        try {
            // Instanciando os arquivos de tarefas, categorias e rótulos
            ArquivoTarefas arqTarefas = new ArquivoTarefas(Tarefa.class.getConstructor(), "tarefas_.db");
            ArquivoCategorias arqCategorias = new ArquivoCategorias(Categoria.class.getConstructor(), "categorias.db");
            ArquivoRotulos arqRotulos = new ArquivoRotulos(Rotulo.class.getConstructor(), "rotulos.db");

            // Caminhos de backup
            String caminhoBackup = "src/backups/backup_2024-12-09";
            String caminhoRecuperacao = "src/dados_recuperados";

            // Criar categorias e verificar
            System.out.println("Criando categorias...");
            Categoria c1 = new Categoria(-1, "Trabalho");
            Categoria c2 = new Categoria(-1, "Pessoal");
            c1.setID(arqCategorias.create(c1));
            c2.setID(arqCategorias.create(c2));
            System.out.println("Categorias criadas: ");
            arqCategorias.listarCategorias();

            // Criar rótulos e verificar
            System.out.println("\nCriando rótulos...");
            Rotulo r1 = new Rotulo(-1, "Urgente");
            Rotulo r2 = new Rotulo(-1, "Importante");
            Rotulo r3 = new Rotulo(-1, "Baixa prioridade");
            r1.setID(arqRotulos.create(r1));
            r2.setID(arqRotulos.create(r2));
            r3.setID(arqRotulos.create(r3));
            System.out.println("Rótulos criados: " + arqRotulos.listarRotulos());

            // Criar tarefas e verificar
            System.out.println("\nCriando tarefas...");
            Tarefa t1 = new Tarefa(-1, c1.getID(), "Estudar Java", LocalDate.parse("2023-01-01"), null, "Pendente", 1);
            Tarefa t2 = new Tarefa(-1, c1.getID(), "Fazer documentação", LocalDate.parse("2023-01-02"), null, "Em Progresso", 2);
            Tarefa t3 = new Tarefa(-1, c1.getID(), "Reunião com equipe", LocalDate.parse("2023-01-03"), null, "Pendente", 2);
            Tarefa t4 = new Tarefa(-1, c2.getID(), "Ir ao supermercado", LocalDate.parse("2023-01-02"), null, "Pendente", 1);
            Tarefa t5 = new Tarefa(-1, c2.getID(), "Teste de persistência", LocalDate.parse("2023-01-02"), null, "Pendente", 1);
            t1.setID(arqTarefas.create(t1));
            t2.setID(arqTarefas.create(t2));
            t3.setID(arqTarefas.create(t3));
            t4.setID(arqTarefas.create(t4));
            t5.setID(arqTarefas.create(t5));
            System.out.println("Tarefas criadas:");
            arqTarefas.listarTodasAsTarefas();

            // Vincular rótulos às tarefas e verificar
            System.out.println("\nVinculando rótulos às tarefas...");
            arqTarefas.vincularRotulo(t1.getID(), r1.getID());
            arqTarefas.vincularRotulo(t1.getID(), r2.getID());
            arqTarefas.vincularRotulo(t2.getID(), r2.getID());
            arqTarefas.vincularRotulo(t3.getID(), r3.getID());
            System.out.println("Rótulos vinculados com sucesso.");

            // Listar tarefas por categoria
            System.out.println("\nTarefas na categoria 'Trabalho':");
            arqTarefas.listarTarefasPorCategoria(c1.getID());

            // Listar tarefas por rótulo
            System.out.println("\nTarefas com o rótulo 'Urgente':");
            List<Tarefa> tarefasUrgentes = arqTarefas.listarTarefasPorRotulo(r1.getID());
            for (Tarefa tarefa : tarefasUrgentes) {
                System.out.println(tarefa);
            }

            // Busca por palavras
            System.out.println("\nBuscando tarefas com a palavra 'Java':");
            List<Tarefa> tarefasJava = arqTarefas.buscarPorTermos("Java");
            for (Tarefa tarefa : tarefasJava) {
                System.out.println(tarefa);
            }

            // Atualizar tarefa
            System.out.println("\nAtualizando tarefa...");
            t1.setStatus("Concluída");
            t1.setDataConclusao(LocalDate.parse("2023-01-10"));
            arqTarefas.update(t1);
            System.out.println("Tarefa atualizada:");
            arqTarefas.listarTarefasPorCategoria(c1.getID());

            // Criar backup dos dados
            System.out.println("\nCriando backup dos dados...");
            Backup.criarBackup("src/dados", caminhoBackup);

            // Restaurar backup para uma nova pasta
            System.out.println("\nRestaurando backup dos dados...");
            Backup.restaurarBackup(caminhoBackup, "src/dados_recuperados");

            // Verificar se os dados restaurados estão corretos
            System.out.println("\nVerificando dados restaurados...");
            ArquivoTarefas arqTarefasRestaurado = new ArquivoTarefas(Tarefa.class.getConstructor(),  "tarefas_.db");
            arqTarefasRestaurado.listarTodasAsTarefas();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
