package login.backend;

import compartilhado.backend.SenhaUtil;
import usuarios.backend.Funcionario;
import usuarios.backend.FuncionarioDAO;
import java.sql.SQLException;

// Regras do login (antes ficavam no FuncionarioService).
public class AutenticacaoService {

    private FuncionarioDAO funcionarioDAO;

    public AutenticacaoService() {
        funcionarioDAO = new FuncionarioDAO();
    }

    public Funcionario autenticar(String login, String senha)
            throws SQLException {

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
