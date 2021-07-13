package estoque.cerveja.mapper;

import estoque.cerveja.dto.CervejaDTO;
import estoque.cerveja.entity.Cerveja;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CervejaMapper {

    CervejaMapper INSTANCIA = Mappers.getMapper( CervejaMapper.class );

    Cerveja paraModelo( CervejaDTO cervejaDTO );
    CervejaDTO paraDTO( Cerveja cerveja );

}
