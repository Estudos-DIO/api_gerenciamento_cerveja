package estoque.cerveja.service;

import estoque.cerveja.builder.CervejaDTOBuilder;
import estoque.cerveja.dto.CervejaDTO;
import estoque.cerveja.entity.Cerveja;
import estoque.cerveja.exception.ExcecaoCervejaJaRegistrada;
import estoque.cerveja.mapper.CervejaMapper;
import estoque.cerveja.repository.CervejaRepository;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

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
    
    //----------------------------------------------------------------------------------------------------

} // fim de CervejaServiceTest{...}
