import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        FuncionarioService funcionarioService =
                new FuncionarioService();

        System.out.print("Login: ");
        String login = scanner.nextLine();

        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        Funcionario funcionario =
                funcionarioService.autenticar(login, senha);

        if (funcionario == null) {

            System.out.println(
                    "Login ou senha inválidos."
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
        }

        scanner.close();
    }
}