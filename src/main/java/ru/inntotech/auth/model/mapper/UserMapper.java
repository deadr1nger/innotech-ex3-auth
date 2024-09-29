package ru.inntotech.auth.model.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.inntotech.auth.model.dto.UserPostRequest;
import ru.inntotech.auth.model.dto.UserResponse;
import ru.inntotech.auth.model.entity.UserEntity;
@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    UserResponse entityToUserDTO(UserEntity message);
    UserEntity userDtoToEntity(UserPostRequest dto);

}
