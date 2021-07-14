package estoque.cerveja.controller;

import estoque.cerveja.builder.CervejaDTOBuilder;
import estoque.cerveja.dto.CervejaDTO;
import estoque.cerveja.entity.Cerveja;
import estoque.cerveja.exception.ExcecaoCervejaNaoEncontrada;
import estoque.cerveja.service.CervejaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import static estoque.cerveja.utils.JsonConvertionUtils.asJsonString;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
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

} // fim de CervejaControllerTest{...}
