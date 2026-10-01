package edu.university.user_service.mapper;

import edu.university.user_service.dto.CreateUserDTO;
import edu.university.user_service.dto.UpdateUserDTO;
import edu.university.user_service.dto.UserResponseDTO;
import edu.university.user_service.model.User;
import org.mapstruct.*;

/**
 * Mapper for converting between User entities and User DTOs.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /* ================== DTO -> ENTITY (CREATE) ================== */

    @Mapping(target = "idUser", ignore = true)
    @Mapping(target = "role", ignore = true) // se asigna en el service
    @Mapping(target = "status", ignore = true) // se decide en el service (por defecto ACTIVE)
    @Mapping(target = "createdAt", ignore = true) // se manejan en service o @PrePersist
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(CreateUserDTO dto);

    /* ================== DTO -> ENTITY (UPDATE parcial) ================== */

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idUser", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(UpdateUserDTO dto, @MappingTarget User user);

    /* ================== ENTITY -> DTO (RESPONSE) ================== */

    @Mapping(source = "idUser", target = "id")
    @Mapping(source = "role.name", target = "role")
    UserResponseDTO toResponse(User user);
}
