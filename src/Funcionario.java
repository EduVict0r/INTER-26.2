public class Funcionario {

    private int id_funcionario;
    private String nome_funcionario;
    private String senha_funcionario;
    private String email_funcionario;
    private String perfil_funcionario;
    private String status_funcionario;
    private String data_cadastro_funcionario;
    private int id_funcionario_cadastrou;
    private String data_exclusao;
    private String motivo_exclusao;

    public Funcionario(String nome_funcionario, String email_funcionario, String senha_funcionario) {
        this.nome_funcionario = nome_funcionario;
        this.email_funcionario = email_funcionario;
        this.senha_funcionario = senha_funcionario;
    }

    public void setId_funcionario(int id_funcionario) {
        this.id_funcionario = id_funcionario;
    }

    public void setNome_funcionario(String nome_funcionario) {
        this.nome_funcionario = nome_funcionario;
    }


    public void setEmail_funcionario(String email_funcionario) {
        this.email_funcionario = email_funcionario;
    }

    public void setSenha_funcionario(String senha_funcionario) {
        this.senha_funcionario = senha_funcionario;
    }

    public void setPerfil_funcionario(String perfil_funcionario) {
        this.perfil_funcionario = perfil_funcionario;
    }

    public void setStatus_funcionario(String status_funcionario) {
        this.status_funcionario = status_funcionario;
    }

    public void setData_cadastro_funcionario(String data_cadastro_funcionario) {
        this.data_cadastro_funcionario = data_cadastro_funcionario;
    }

    public void setId_funcionario_cadastrou(int id_funcionario_cadastrou) {
        this.id_funcionario_cadastrou = id_funcionario_cadastrou;
    }

    public int getId_funcionario() {
        return this.id_funcionario;
    }

    public String getNome_funcionario() {
        return this.nome_funcionario;
    }

    public String getEmail_funcionario() {
        return this.email_funcionario;
    }

    public String getSenha_funcionario() {
        return this.senha_funcionario;
    }

    public String getPerfil_funcionario() {
        return this.perfil_funcionario;
    }



    public String getStatus_funcionario (){
        return this.status_funcionario;
    }

}
