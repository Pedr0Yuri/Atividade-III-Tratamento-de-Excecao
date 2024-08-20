import java.util.ArrayList;
import java.util.Scanner;

class Professor {
    String nome;
    int idade;
    String sexo;
    String cpf;
    String rg;
    String endereco;
    ArrayList<Dependente> dependentes;

    public Professor(String nome, int idade, String sexo, String cpf, String rg, String endereco) {
        this.nome = nome;
        this.idade = idade;
        this.sexo = sexo;
        this.cpf = cpf;
        this.rg = rg;
        this.endereco = endereco;
        this.dependentes = new ArrayList<>();
    }

    public void adicionarDependente(Dependente dependente) {
        if (dependentes.size() < 5) {
            dependentes.add(dependente);
        } else {
            throw new RuntimeException("Limite de 5 dependentes por professor excedido.");
        }
    }

    public void removerDependente(String nomeDependente) {
        dependentes.removeIf(dependente -> dependente.nome.equals(nomeDependente));
    }

    @Override
    public String toString() {
        return "Professor{" +
                "nome='" + nome + '\'' +
                ", idade=" + idade +
                ", sexo='" + sexo + '\'' +
                ", cpf='" + cpf + '\'' +
                ", rg='" + rg + '\'' +
                ", endereco='" + endereco + '\'' +
                ", dependentes=" + dependentes +
                '}';
    }
}

class Dependente {
    String nome;
    int idade;
    String sexo;

    public Dependente(String nome, int idade, String sexo) {
        this.nome = nome;
        this.idade = idade;
        this.sexo = sexo;
    }

    @Override
    public String toString() {
        return "Dependente{" +
                "nome='" + nome + '\'' +
                ", idade=" + idade +
                ", sexo='" + sexo + '\'' +
                '}';
    }
}

class SistemaAcademico {
    private Professor[] professores;
    private ArrayList<String> erros;

    public SistemaAcademico() {
        professores = new Professor[20];
        erros = new ArrayList<>();
    }

    public void incluirProfessor(Professor professor) {
        if (professorExistente(professor.cpf)) {
            throw new RuntimeException("Professor com CPF " + professor.cpf + " já existe no sistema.");
        }
        for (int i = 0; i < professores.length; i++) {
            if (professores[i] == null) {
                professores[i] = professor;
                return;
            }
        }
        throw new RuntimeException("Limite de 20 professores atingido.");
    }

    public void excluirProfessor(String cpf) {
        if (!professorExistente(cpf)) {
            throw new RuntimeException("Professor com CPF " + cpf + " não encontrado.");
        }
        for (int i = 0; i < professores.length; i++) {
            if (professores[i] != null && professores[i].cpf.equals(cpf)) {
                professores[i] = null;
                return;
            }
        }
    }

    public Professor pesquisarProfessor(String cpf) {
        for (Professor professor : professores) {
            if (professor != null && professor.cpf.equals(cpf)) {
                return professor;
            }
        }
        throw new RuntimeException("Professor com CPF " + cpf + " não encontrado.");
    }

    public void registrarErro(Exception e) {
        erros.add(e.getMessage());
    }

    private boolean professorExistente(String cpf) {
        for (Professor professor : professores) {
            if (professor != null && professor.cpf.equals(cpf)) {
                return true;
            }
        }
        return false;
    }

    public void exibirErros() {
        if (erros.isEmpty()) {
            System.out.println("Nenhum erro registrado.");
        } else {
            System.out.println("Erros encontrados:");
            for (String erro : erros) {
                System.out.println("- " + erro);
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SistemaAcademico sistema = new SistemaAcademico();

        try {
            while (true) {
                System.out.println("\n--- Sistema Acadêmico ---");
                System.out.println("1. Incluir Professor");
                System.out.println("2. Excluir Professor");
                System.out.println("3. Pesquisar Professor");
                System.out.println("4. Exibir Erros");
                System.out.println("5. Sair");
                System.out.print("Opção: ");

                int opcao = scanner.nextInt();
                scanner.nextLine(); // Consumir a quebra de linha

                switch (opcao) {
                    case 1:
                        System.out.println("\n--- Incluir Professor ---");
                        System.out.print("Nome: ");
                        String nome = scanner.nextLine();
                        System.out.print("Idade: ");
                        int idade = scanner.nextInt();
                        scanner.nextLine(); // Consumir a quebra de linha
                        System.out.print("Sexo (M/F): ");
                        String sexo = scanner.nextLine();
                        System.out.print("CPF: ");
                        String cpf = scanner.nextLine();
                        System.out.print("RG: ");
                        String rg = scanner.nextLine();
                        System.out.print("Endereço: ");
                        String endereco = scanner.nextLine();

                        Professor novoProfessor = new Professor(nome, idade, sexo, cpf, rg, endereco);

                        // Adicionar dependentes ao professor (opcional)
                        System.out.print("Deseja adicionar dependentes? (S/N): ");
                        String resposta = scanner.nextLine();
                        if (resposta.equalsIgnoreCase("S")) {
                            int numDependentes = 0;
                            while (numDependentes < 5) {
                                System.out.println("\n--- Adicionar Dependente ---");
                                System.out.print("Nome: ");
                                String nomeDependente = scanner.nextLine();
                                System.out.print("Idade: ");
                                int idadeDependente = scanner.nextInt();
                                scanner.nextLine(); // Consumir a quebra de linha
                                System.out.print("Sexo (M/F): ");
                                String sexoDependente = scanner.nextLine();

                                Dependente dependente = new Dependente(nomeDependente, idadeDependente, sexoDependente);
                                novoProfessor.adicionarDependente(dependente);

                                System.out.print("Adicionar mais dependentes? (S/N): ");
                                resposta = scanner.nextLine();
                                if (resposta.equalsIgnoreCase("N")) {
                                    break;
                                }
                                numDependentes++;
                            }
                        }

                        try {
                            sistema.incluirProfessor(novoProfessor);
                            System.out.println("Professor incluído com sucesso!");
                        } catch (RuntimeException e) {
                            sistema.registrarErro(e);
                            System.out.println("Erro ao incluir professor: " + e.getMessage());
                        }
                        break;

                    case 2:
                        System.out.println("\n--- Excluir Professor ---");
                        System.out.print("CPF: ");
                        String cpfExcluir = scanner.nextLine();
                        try {
                            sistema.excluirProfessor(cpfExcluir);
                            System.out.println("Professor excluído com sucesso!");
                        } catch (RuntimeException e) {
                            sistema.registrarErro(e);
                            System.out.println("Erro ao excluir professor: " + e.getMessage());
                        }
                        break;

                    case 3:
                        System.out.println("\n--- Pesquisar Professor ---");
                        System.out.print("CPF: ");
                        String cpfPesquisar = scanner.nextLine();
                        try {
                            Professor professorEncontrado = sistema.pesquisarProfessor(cpfPesquisar);
                            System.out.println("\nProfessor encontrado:");
                            System.out.println(professorEncontrado);
                        } catch (RuntimeException e) {
                            sistema.registrarErro(e);
                            System.out.println("Erro ao pesquisar professor: " + e.getMessage());
                        }
                        break;

                    case 4:
                        sistema.exibirErros();
                        break;

                    case 5:
                        System.out.println("Saindo do sistema...");
                        scanner.close();
                        return;

                    default:
                        System.out.println("Opção inválida.");
                }
            }
        } catch (Exception e) {
            sistema.registrarErro(e);
            System.out.println("Erro inesperado: " + e.getMessage());
        }
    }
}