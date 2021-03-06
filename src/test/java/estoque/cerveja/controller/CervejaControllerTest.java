package estoque.cerveja.controller;

import estoque.cerveja.builder.CervejaDTOBuilder;
import estoque.cerveja.dto.CervejaDTO;
import estoque.cerveja.dto.QuantidadeDTO;
import estoque.cerveja.exception.ExcecaoCervejaNaoEncontrada;
import estoque.cerveja.service.CervejaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Collections;

import static estoque.cerveja.utils.JsonConvertionUtils.asJsonString;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CervejaControllerTest {

    //----------------------------------------------------------------------------------------------------
    private static final String API_URL_PATH = "/api/v1/cervejas";
    private static final long ID_VALIDO_CERVEJA = 1L;
    private static final long ID_INVALIDO_CERVEJA = 2L;
    private static final String URL_API_INCREMENTO = "/incremento";
    private static final String URL_API_DECREMENTO = "/decremento";

    private MockMvc mockMvc;

    @Mock
    private CervejaService cervejaService;

    @InjectMocks
    private CervejaController cervejaController;
    //----------------------------------------------------------------------------------------------------
    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders.standaloneSetup( cervejaController )
                    .setCustomArgumentResolvers( new PageableHandlerMethodArgumentResolver())
                    .setViewResolvers( ( s, locale ) -> new MappingJackson2JsonView() )
                    .build();

    }
    //----------------------------------------------------------------------------------------------------
    @Test
    void quandChamarPOSTCriaCerveja() throws Exception {

        // instanciar os objetos
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().paraCervejaDTO();

        // when
        when( cervejaService.criarCerveja( cervejaDTO ) ).thenReturn( cervejaDTO );

        mockMvc.perform( post( API_URL_PATH )
            .contentType( MediaType.APPLICATION_JSON )
            .content( asJsonString(cervejaDTO)))
                .andExpect( status().isCreated() )
                .andExpect(jsonPath( "$.nome", is( cervejaDTO.getNome() ) ))
                .andExpect(jsonPath( "$.marca", is( cervejaDTO.getMarca() ) ))
                .andExpect(jsonPath( "$.tipo", is( cervejaDTO.getTipo().getDescricao().toUpperCase() ) ));
    }
    //----------------------------------------------------------------------------------------------------
    @Test
    void quandoChamarPOSTSemInformarCampoObrigatorio() throws Exception {

        // dados de entrda
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().paraCervejaDTO();
        cervejaDTO.setMarca(null);

        // then
        mockMvc.perform(post( API_URL_PATH )
                .contentType( MediaType.APPLICATION_JSON )
                .content( asJsonString( cervejaDTO) ) )
                .andExpect( status().isBadRequest() );
    }
    //----------------------------------------------------------------------------------------------------
    @Test
    void quandoChamaNomeCervejaValidoRetornaStatusOK( ) throws Exception {

        // dados de entrada
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().paraCervejaDTO();

        // when
        when( cervejaService.pesquisarPorNome( cervejaDTO.getNome() )).thenReturn( cervejaDTO );

        mockMvc.perform(MockMvcRequestBuilders.get(API_URL_PATH + "/" + cervejaDTO.getNome())
                .contentType( MediaType.APPLICATION_JSON ))
                .andExpect( status().isOk() )
                .andExpect(jsonPath( "$.nome", is( cervejaDTO.getNome() ) ))
                .andExpect(jsonPath( "$.marca", is( cervejaDTO.getMarca() ) ))
                .andExpect(jsonPath( "$.tipo", is( cervejaDTO.getTipo().getDescricao().toUpperCase() ) ));

    }
    //----------------------------------------------------------------------------------------------------
    @Test
    void quandoChamaNomeCervejaValidoRetornaStatusNotFound( ) throws Exception {

        // dados de entrada
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().paraCervejaDTO();

        // when
        when( cervejaService.pesquisarPorNome( cervejaDTO.getNome() )).thenThrow( ExcecaoCervejaNaoEncontrada.class );

        mockMvc.perform(MockMvcRequestBuilders.get(API_URL_PATH + "/" + cervejaDTO.getNome())
                .contentType( MediaType.APPLICATION_JSON ))
                .andExpect( status().isNotFound() );
    }
    //----------------------------------------------------------------------------------------------------
    @Test
    void quandoChamaListaCervejasRetornaALista( ) throws Exception {

        // dados de entrada
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().paraCervejaDTO();

        // when
        when( cervejaService.listarTodas()).thenReturn( Collections.singletonList(cervejaDTO) );

        mockMvc.perform(MockMvcRequestBuilders.get(API_URL_PATH)
                .contentType( MediaType.APPLICATION_JSON ))
                .andExpect( status().isOk() )
                .andExpect(jsonPath( "$[0].nome", is( cervejaDTO.getNome() ) ))
                .andExpect(jsonPath( "$[0].marca", is( cervejaDTO.getMarca() ) ))
                .andExpect(jsonPath( "$[0].tipo", is( cervejaDTO.getTipo().getDescricao().toUpperCase() ) ));

    }
    //----------------------------------------------------------------------------------------------------
    @Test
    void quandoMetodoExcluirEhChamadoParaIDValidoERetornaStatusNoContent() throws Exception {

        // dados de entrada
        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().paraCervejaDTO();

        // when
        doNothing().when( cervejaService ).removerPorID( cervejaDTO.getId() );

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(API_URL_PATH + "/" + cervejaDTO.getId() )
                .contentType( MediaType.APPLICATION_JSON ))
                .andExpect( status().isNoContent() );

    }
    //----------------------------------------------------------------------------------------------------
    @Test
    void quandoMetodoExcluirEhChamadoParaIDInvalidoERetornaStatusNotFound() throws Exception {

        // when
        doThrow( ExcecaoCervejaNaoEncontrada.class ).when( cervejaService )
                .removerPorID( ID_INVALIDO_CERVEJA );

        // then
        mockMvc.perform(MockMvcRequestBuilders.delete(API_URL_PATH + "/" + ID_INVALIDO_CERVEJA )
                .contentType( MediaType.APPLICATION_JSON ))
                .andExpect( status().isNotFound() );

    }
    //----------------------------------------------------------------------------------------------------
    @Test
    void quandoChamaIncrementoERetornaStatusOK() throws Exception {

        QuantidadeDTO quantidadeDTO = QuantidadeDTO.builder()
                .quantidade( 20 )
                .build();

        CervejaDTO cervejaDTO = CervejaDTOBuilder.builder().build().paraCervejaDTO();
        cervejaDTO.setQuantidade( cervejaDTO.getQuantidade() + quantidadeDTO.getQuantidade() );

        when( cervejaService.incrementar( ID_INVALIDO_CERVEJA, quantidadeDTO.getQuantidade() ) )
                .thenReturn( cervejaDTO );

        mockMvc.perform (MockMvcRequestBuilders.patch(API_URL_PATH + "/" + ID_INVALIDO_CERVEJA + URL_API_INCREMENTO )
                .contentType( MediaType.APPLICATION_JSON )
                .content( asJsonString(quantidadeDTO) ) ).andExpect( status().isOk() )
                .andExpect( jsonPath("$.nome", is( cervejaDTO.getNome() )) )
                .andExpect( jsonPath("$.marca", is( cervejaDTO.getMarca() )) )
                .andExpect( jsonPath("$.tipo", is( cervejaDTO.getTipo().toString() )) )
                .andExpect( jsonPath("$.quantidade", is( cervejaDTO.getQuantidade() )) );
    }
    //----------------------------------------------------------------------------------------------------

} // fim de CervejaControllerTest{...}
