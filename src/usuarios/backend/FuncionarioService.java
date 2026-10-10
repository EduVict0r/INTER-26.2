package usuarios.backend;

import compartilhado.backend.SenhaUtil;
public class FuncionarioService {

    private FuncionarioDAO funcionarioDAO;

    public FuncionarioService() {
        funcionarioDAO = new FuncionarioDAO();
    }

    public int cadastrar(
            Funcionario administrador,
            String nome,
            String email,
            char[] senha,
            String perfil
    ) throws java.sql.SQLException {

        // 1. Verifica se existe um administrador autenticado.
        if (administrador == null
                || !"ADMINISTRADOR".equalsIgnoreCase(
                administrador.getPerfil_funcionario())
                || !"ATIVO".equalsIgnoreCase(
                administrador.getStatus_funcionario())) {

            throw new SecurityException(
                    "Somente administradores ativos podem cadastrar funcionários."
            );
        }

        // 2. Confirma a permissão atual no banco.
        // Apesar do nome, buscarPorLogin já consulta pelo e-mail.
        Funcionario atual = funcionarioDAO.buscarPorLogin(
                administrador.getEmail_funcionario()
        );

        if (atual == null
                || atual.getId_funcionario()
                != administrador.getId_funcionario()
                || !"ADMINISTRADOR".equalsIgnoreCase(
                atual.getPerfil_funcionario())
                || !"ATIVO".equalsIgnoreCase(
                atual.getStatus_funcionario())) {

            throw new SecurityException(
                    "Não foi possível confirmar a permissão do administrador."
            );
        }

        // 3. Valida os campos obrigatórios.
        if (nome == null || nome.isBlank()
                || email == null || email.isBlank()
                || senha == null || senha.length == 0) {

            throw new IllegalArgumentException(
                    "Preencha nome, e-mail e senha."
            );
        }

        nome = nome.trim();
        email = email.trim();

        if (nome.length() > 150 || email.length() > 150) {
            throw new IllegalArgumentException(
                    "Nome e e-mail devem ter no máximo 150 caracteres."
            );
        }

        if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException(
                    "Informe um e-mail válido."
            );
        }

        if (senha.length < 8) {
            throw new IllegalArgumentException(
                    "A senha deve ter pelo menos 8 caracteres."
            );
        }

        if (!"ADMINISTRADOR".equals(perfil)
                && !"VENDEDOR".equals(perfil)) {

            throw new IllegalArgumentException(
                    "Selecione um perfil válido."
            );
        }

        // 4. Gera o hash antes de enviar a senha ao DAO.
        String hash = SenhaUtil.gerarHash(new String(senha));

        Funcionario novo = new Funcionario(
                nome,
                email,
                hash
        );

        novo.setPerfil_funcionario(perfil);

        // 5. Grava o funcionário e retorna o ID gerado.
        return funcionarioDAO.cadastrar(
                novo,
                atual.getId_funcionario()
        );
    }
}