package ru.inntotech.auth.model.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.inntotech.auth.model.dto.TokenData;
import ru.inntotech.auth.model.dto.TokenResponse;


@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TokenDataMapper {

    TokenResponse tokenDataToTokenResponse(TokenData data);
}
