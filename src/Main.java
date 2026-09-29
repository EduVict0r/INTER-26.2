import java.util.Scanner;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        FuncionarioService funcionarioService =
                new FuncionarioService();

        System.out.print("Email: ");
        String login = scanner.nextLine();

        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        Funcionario funcionario =
                funcionarioService.autenticar(login, senha);

        if (funcionario == null) {

            System.out.println(
                    "Email ou senha inválidos."
            );

        } else {

            System.out.println();
            System.out.println(
                    "Login realizado com sucesso!"
            );

            System.out.println(
                    "Bem-vindo, "
                            + funcionario.getNome_funcionario()
            );

            System.out.println(
                    "Perfil: "
                            + funcionario.getPerfil_funcionario()
            );

            if ("ADMINISTRADOR".equalsIgnoreCase(
                    funcionario.getPerfil_funcionario())) {

                SwingUtilities.invokeLater(() -> {
                    TelaCadastroFuncionario tela =
                            new TelaCadastroFuncionario(funcionario);

                    tela.setVisible(true);
                });

            } else {
                System.out.println(
                        "Seu perfil não tem permissão para cadastrar funcionários."
                );
            }
        }

        scanner.close();
    }
}