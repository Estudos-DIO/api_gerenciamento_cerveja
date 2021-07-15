package estoque.cerveja.controller;

import estoque.cerveja.dto.CervejaDTO;
import estoque.cerveja.dto.QuantidadeDTO;
import estoque.cerveja.exception.ExcecaoCervejaJaRegistrada;
import estoque.cerveja.exception.ExcecaoCervejaNaoEncontrada;
import estoque.cerveja.exception.ExcecaoEstoqueCervejaExcedido;
import estoque.cerveja.service.CervejaService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cervejas")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CervejaController {

    private final CervejaService servicoCerveja;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CervejaDTO criarCerveja(@RequestBody @Valid CervejaDTO cervejaDTO ) throws ExcecaoCervejaJaRegistrada {
        return servicoCerveja.criarCerveja( cervejaDTO );
    }

    @GetMapping("/{nome}")
    public CervejaDTO procurarPorNome( @PathVariable String nome ) throws ExcecaoCervejaNaoEncontrada {
        return servicoCerveja.pesquisarPorNome( nome );
    }

    @GetMapping
    public List<CervejaDTO> listarTodos() {
        return servicoCerveja.listarTodas();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById( @PathVariable Long id ) throws ExcecaoCervejaNaoEncontrada {
        servicoCerveja.removerPorID( id );
    }

    @PatchMapping("/{id}/incremento")
    public CervejaDTO increment(@PathVariable Long id, @RequestBody @Valid QuantidadeDTO quantidadeDTO) throws ExcecaoCervejaNaoEncontrada, ExcecaoEstoqueCervejaExcedido {
        return servicoCerveja.incrementar( id, quantidadeDTO.getQuantidade() );
    }

} // fim de CervejaController{...}
