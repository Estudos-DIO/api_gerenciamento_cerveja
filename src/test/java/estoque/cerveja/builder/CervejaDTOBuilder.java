package estoque.cerveja.builder;

import estoque.cerveja.dto.CervejaDTO;
import estoque.cerveja.enums.TipoCerveja;
import lombok.Builder;

@Builder
public class CervejaDTOBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String nome = "Brahma";

    @Builder.Default
    private String marca = "Ambev";

    @Builder.Default
    private int maximo = 50;

    @Builder.Default
    private int quantidade = 10;

    @Builder.Default
    private TipoCerveja tipo = TipoCerveja.LAGER;

    public CervejaDTO paraCervejaDTO() {

        CervejaDTO novaCerveja = new CervejaDTO( id, nome, marca, maximo, quantidade, tipo );

        return novaCerveja;
    }

} // fim de CervejaDTOBuilder{...}
