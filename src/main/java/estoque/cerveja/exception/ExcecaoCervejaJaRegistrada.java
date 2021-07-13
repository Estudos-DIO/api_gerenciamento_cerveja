package estoque.cerveja.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ExcecaoCervejaJaRegistrada extends Exception {

    public ExcecaoCervejaJaRegistrada( String nomeCerveja ) {
        super( String.format("Cerveja de nome %s existe no sistema!",
                nomeCerveja) );
    }

}
