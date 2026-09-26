import com.password4j.Argon2Function;
import com.password4j.Password;
import com.password4j.types.Argon2;

public class SenhaUtil {

    private static final Argon2Function ARGON2 =
            Argon2Function.getInstance(
                    19456,
                    2,
                    1,
                    32,
                    Argon2.ID
            );

    public static String gerarHash(String senha) {

        return Password
                .hash(senha)
                .addRandomSalt()
                .with(ARGON2)
                .getResult();
    }

    public static boolean verificarSenha(
            String senhaDigitada,
            String hashArmazenado
    ) {

        return Password
                .check(senhaDigitada, hashArmazenado)
                .with(ARGON2);
    }
}