package estoque.cerveja.service;

import estoque.cerveja.builder.CervejaDTOBuilder;
import estoque.cerveja.dto.CervejaDTO;
import estoque.cerveja.entity.Cerveja;
import estoque.cerveja.exception.ExcecaoCervejaJaRegistrada;
import estoque.cerveja.exception.ExcecaoCervejaNaoEncontrada;
import estoque.cerveja.exception.ExcecaoEstoqueCervejaExcedido;
import estoque.cerveja.mapper.CervejaMapper;
import estoque.cerveja.repository.CervejaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

// utiliza a extensão do Mockito para executar a classe de teste.
@ExtendWith(MockitoExtension.class)
public class CervejaServiceTest {

    //----------------------------------------------------------------------------------------------------
    private static final long ID_CEVEJA_INVALIDO = 1L;

    @Mock
    private CervejaRepository repositorioCeveja;

    // é inserido como constante na nesta classe
    private CervejaMapper mapperCerveja = CervejaMapper.INSTANCIA;

    @InjectMocks
    private CervejaService servicoCerveja;
    //----------------------------------------------------------------------------------------------------
    @Test
    void quandoCervejaInformadaEntaoElaDeveSerCriada() throws ExcecaoCervejaJaRegistrada {

        // instanciar os objetos
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().paraCervejaDTO();
        Cerveja salvarCerveja = mapperCerveja.paraModelo( cervejaDTO );

        // when
        when( repositorioCeveja.findByNome( cervejaDTO.getNome() ) )
                .thenReturn( Optional.empty() );

        when( repositorioCeveja.save( salvarCerveja ) )
                .thenReturn( salvarCerveja );

        // then
        CervejaDTO criarCervejaDTO = servicoCerveja.criarCerveja( cervejaDTO );

        // validar os resultados
        assertEquals( cervejaDTO.getId(), criarCervejaDTO.getId() );
        assertEquals( cervejaDTO.getNome(), criarCervejaDTO.getNome() );
    }
    //----------------------------------------------------------------------------------------------------
    @Test
    void quandoCervejaInformadaEntaoElaDeveSerCriadaViaHamcrest() throws ExcecaoCervejaJaRegistrada {

        // instanciar os objetos
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().paraCervejaDTO();
        Cerveja salvarCerveja = mapperCerveja.paraModelo( cervejaDTO );

        // when
        when( repositorioCeveja.findByNome( cervejaDTO.getNome() ) )
                .thenReturn( Optional.empty() );

        when( repositorioCeveja.save( salvarCerveja ) )
                .thenReturn( salvarCerveja );

        // then
        CervejaDTO criarCervejaDTO = servicoCerveja.criarCerveja( cervejaDTO );

        // validação via Hamcrest
        assertThat( cervejaDTO.getId(), is( equalTo( criarCervejaDTO.getId() ) ) );

        assertThat( cervejaDTO.getNome(), is( equalTo( criarCervejaDTO.getNome() ) ) );

        assertThat( cervejaDTO.getQuantidade(), is( equalTo( criarCervejaDTO.getQuantidade() ) ) );

        assertThat( criarCervejaDTO.getQuantidade(), is( greaterThan( 2 ) )  );
    }
    //----------------------------------------------------------------------------------------------------
    @Test
    void quandoCervejaCriadaEhInformadaELancaExcecao() throws ExcecaoCervejaJaRegistrada {

        // instanciar os objetos
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().paraCervejaDTO();
        Cerveja cervejaDuplicada = mapperCerveja.paraModelo( cervejaDTO );

        // when
        when( repositorioCeveja.findByNome( cervejaDTO.getNome() ) )
                .thenReturn( Optional.of( cervejaDuplicada ) );

        // then
        assertThrows( ExcecaoCervejaJaRegistrada.class, () -> servicoCerveja.criarCerveja( cervejaDTO ) );
    }
    //----------------------------------------------------------------------------------------------------
    @Test
    void quandoONomeDaCevejaEhDadoRetornaOsDados() throws ExcecaoCervejaNaoEncontrada {

        // instanciar os objetos
        CervejaDTO cervejaDTOEsperada = CervejaDTOBuilder.builder().build().paraCervejaDTO();
        Cerveja cervejaEsperada = mapperCerveja.paraModelo( cervejaDTOEsperada );

        // when
        when( repositorioCeveja.findByNome( cervejaDTOEsperada.getNome() ) )
                .thenReturn( Optional.of( cervejaEsperada ) );

        // then
        assertThrows( ExcecaoCervejaJaRegistrada.class, () -> servicoCerveja.criarCerveja( cervejaDTOEsperada ) );

        CervejaDTO cervejaDTO_encontrada = servicoCerveja.pesquisarPorNome( cervejaDTOEsperada.getNome() );

        assertThat( cervejaDTO_encontrada, is( equalTo( cervejaDTOEsperada ) ) );
    }
    //----------------------------------------------------------------------------------------------------
    @Test
    void quandoONomeDaCevejaEhNaoRegistradoLancaExcecao() throws ExcecaoCervejaNaoEncontrada {

        // instanciar os objetos
        CervejaDTO cervejaDTOEsperada = CervejaDTOBuilder.builder().build().paraCervejaDTO();

        // when
        when( repositorioCeveja.findByNome( cervejaDTOEsperada.getNome() ) )
                .thenReturn( Optional.empty() );

        // then
        assertThrows( ExcecaoCervejaNaoEncontrada.class,
                () -> servicoCerveja.pesquisarPorNome( cervejaDTOEsperada.getNome() ) );
    }
    //----------------------------------------------------------------------------------------------------
    @Test
    void quandoListaCervaEhChamadaERetornaDados() {

        // instanciar os objetos
        CervejaDTO cervejaDTOEsperada = CervejaDTOBuilder.builder().build().paraCervejaDTO();
        Cerveja cervejaEsperada = mapperCerveja.paraModelo( cervejaDTOEsperada );

        // when
        when( repositorioCeveja.findAll() ).thenReturn(Collections.singletonList(cervejaEsperada));

        // then
        List<CervejaDTO> listCervejasDTO = servicoCerveja.listarTodas();

        assertThat( listCervejasDTO, is( not( empty() ) ) );
        assertThat( listCervejasDTO.get(0), is( equalTo( cervejaDTOEsperada ) ) );
    }
    //----------------------------------------------------------------------------------------------------
    @Test
    void quandoListaCervaEhChamadaERetornaListaVazia() {

        // when
        when( repositorioCeveja.findAll() ).thenReturn( Collections.emptyList() );

        // then
        List<CervejaDTO> listCervejasDTO = servicoCerveja.listarTodas();

        assertThat( listCervejasDTO, is( empty() ) );
    }
    //----------------------------------------------------------------------------------------------------
    @Test
    void quandoMetodoExcluirEhChamadoEDadoCervejaRemovido() throws ExcecaoCervejaNaoEncontrada {

        // instanciar os objetos
        CervejaDTO cervejaDTOEsperada = CervejaDTOBuilder.builder().build().paraCervejaDTO();
        Cerveja cervejaDeletada = mapperCerveja.paraModelo( cervejaDTOEsperada );

        when( repositorioCeveja.findById( cervejaDTOEsperada.getId() ) )
                .thenReturn( Optional.of( cervejaDeletada ) );

        doNothing().when( repositorioCeveja ).deleteById( cervejaDTOEsperada.getId() );

        // then
        servicoCerveja.removerPorID( cervejaDTOEsperada.getId() );

        verify( repositorioCeveja, times( 1 ) ).findById( cervejaDTOEsperada.getId() );
        verify( repositorioCeveja, times( 1 ) ).deleteById( cervejaDTOEsperada.getId() );
    }
    //----------------------------------------------------------------------------------------------------
    @Test
    void quandoChamaIncrementoParaAlterarEstoque() throws ExcecaoCervejaNaoEncontrada,
            ExcecaoEstoqueCervejaExcedido {

        // dados
        CervejaDTO cervejaDTOEsperada = CervejaDTOBuilder.builder().build().paraCervejaDTO();
        Cerveja cervejaEseprada = mapperCerveja.paraModelo( cervejaDTOEsperada );

        // when
        when( repositorioCeveja.findById( cervejaDTOEsperada.getId() ))
                .thenReturn(Optional.of( cervejaEseprada ));

        when( repositorioCeveja.save( cervejaEseprada )).thenReturn( cervejaEseprada );

        int qtdParaIncrementar = 10;
        int qtdAposIncrementado = cervejaDTOEsperada.getQuantidade() + qtdParaIncrementar;

        // then
        CervejaDTO incrementedBeerDTO = servicoCerveja.incrementar( cervejaDTOEsperada.getId(), qtdParaIncrementar );

        assertThat(qtdAposIncrementado, equalTo( incrementedBeerDTO.getQuantidade() ));
        assertThat(qtdAposIncrementado, lessThan( cervejaDTOEsperada.getMaximo()) );
    }
    //----------------------------------------------------------------------------------------------------


} // fim de CervejaServiceTest{...}
