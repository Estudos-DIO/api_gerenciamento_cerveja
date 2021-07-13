package estoque.cerveja.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ExcecaoEstoqueCervejaExcedido extends Exception {

    public ExcecaoEstoqueCervejaExcedido( Long id, int incrementarNaQuantidade ) {
        super(String.format("A cerveja de ID <%s> acrescida da quantidade informada <%s> ultrapassa o estoque! ",
                id, incrementarNaQuantidade));
    }

}
