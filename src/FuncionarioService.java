public class FuncionarioService {

    private FuncionarioDAO funcionarioDAO;

    public FuncionarioService() {
        funcionarioDAO = new FuncionarioDAO();
    }

    public Funcionario autenticar(String login, String senha) {

        Funcionario funcionario =
                funcionarioDAO.buscarPorLogin(login);

        // 1. Usuário existe?
        if (funcionario == null) {
            return null;
        }

        // 2. Usuário está ativo?
        if (!funcionario
                .getStatus_funcionario()
                .equalsIgnoreCase("ATIVO")) {

            return null;
        }

        // 3. A senha digitada corresponde ao hash?
        boolean senhaCorreta =
                SenhaUtil.verificarSenha(
                        senha,
                        funcionario.getSenha_funcionario()
                );

        if (!senhaCorreta) {
            return null;
        }

        // 4. Passou por todas as verificações
        return funcionario;
    }
}