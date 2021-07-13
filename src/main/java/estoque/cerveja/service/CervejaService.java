package estoque.cerveja.service;

import estoque.cerveja.dto.CervejaDTO;
import estoque.cerveja.entity.Cerveja;
import estoque.cerveja.exception.ExcecaoCervejaJaRegistrada;
import estoque.cerveja.exception.ExcecaoCervejaNaoEncontrada;
import estoque.cerveja.exception.ExcecaoEstoqueCervejaExcedido;
import estoque.cerveja.mapper.CervejaMapper;
import estoque.cerveja.repository.CervejaRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor( onConstructor = @__(@Autowired))
public class CervejaService {

    private final CervejaRepository repositorioCerveja;
    private final CervejaMapper mapperCerveja = CervejaMapper.INSTANCIA;
    //private final CervejaMapper mapperCerveja;

    public CervejaDTO criarCerveja( CervejaDTO cervejaDTO ) throws ExcecaoCervejaJaRegistrada {
        verificarSeFoiRegistrada( cervejaDTO.getNome() );

        Cerveja cerveja = mapperCerveja.paraModelo( cervejaDTO );
        Cerveja cervejaSalva = repositorioCerveja.save( cerveja );
        return mapperCerveja.paraDTO( cervejaSalva );
    }

    public CervejaDTO pesquisarPorNome( String nome ) throws ExcecaoCervejaNaoEncontrada {
        Cerveja cervejaEncontrada = repositorioCerveja.findByNome( nome )
                .orElseThrow(() -> new ExcecaoCervejaNaoEncontrada( nome ));
        return mapperCerveja.paraDTO( cervejaEncontrada );
    }

    public List<CervejaDTO> listarTodas() {
        return repositorioCerveja.findAll()
                                .stream()
                                .map( mapperCerveja::paraDTO )
                                .collect( Collectors.toList() );
    }

    public void removerPorID( Long id ) throws ExcecaoCervejaNaoEncontrada {
        verificarSeExiste( id );
        repositorioCerveja.deleteById( id );
    }

    private void verificarSeFoiRegistrada( String nome ) throws ExcecaoCervejaJaRegistrada {
        Optional<Cerveja> optSavedBeer = repositorioCerveja.findByNome( nome );

        if (optSavedBeer.isPresent()) {
            throw new ExcecaoCervejaJaRegistrada(nome);
        }
    }

    private Cerveja verificarSeExiste(Long id) throws ExcecaoCervejaNaoEncontrada {
        return repositorioCerveja.findById(id)
                .orElseThrow(() -> new ExcecaoCervejaNaoEncontrada( id ));
    }

    public CervejaDTO incrementar( Long id, int quantidadeIncremento ) throws ExcecaoCervejaNaoEncontrada, ExcecaoEstoqueCervejaExcedido {
        Cerveja cervejasAIncrementar = verificarSeExiste( id );

        int qtdDepoisIncremento = quantidadeIncremento + cervejasAIncrementar.getQuantidade();

        if (qtdDepoisIncremento <= cervejasAIncrementar.getMaximo() ) {
            cervejasAIncrementar.setQuantidade( cervejasAIncrementar.getQuantidade() + quantidadeIncremento );
            Cerveja incrementarEstoqueCerveja = repositorioCerveja.save( cervejasAIncrementar );
            return mapperCerveja.paraDTO( incrementarEstoqueCerveja );
        }

        throw new ExcecaoEstoqueCervejaExcedido( id, quantidadeIncremento );
    }

} // fim de CervejaService{...}
