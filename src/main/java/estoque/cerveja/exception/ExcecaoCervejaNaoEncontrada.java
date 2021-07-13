package estoque.cerveja.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ExcecaoCervejaNaoEncontrada extends Exception{

    public ExcecaoCervejaNaoEncontrada( String nomeCerveja ) {
        super( String.format("A cerveja de nome <%s> não foi encontrada no sistema!",
                nomeCerveja ) );
    }

    public ExcecaoCervejaNaoEncontrada(Long id) {
        super( String.format("A cerveja de ID <%s> não foi encontrada no sistema!",
                id ) );
    }

}
