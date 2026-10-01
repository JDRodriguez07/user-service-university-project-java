package edu.university.user_service.mapper;

import edu.university.user_service.dto.AdministratorResponseDTO;
import edu.university.user_service.dto.CreateAdministratorDTO;
import edu.university.user_service.dto.UpdateAdministratorDTO;
import edu.university.user_service.model.Administrator;
import org.mapstruct.*;

/**
 * Mapper for converting between Administrator entity and DTOs.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdministratorMapper {

    /* CREATE DTO -> ENTITY */
    @Mapping(target = "idUser", ignore = true)
    @Mapping(target = "adminCode", ignore = true) // se genera a partir del DNI
    @Mapping(target = "role", ignore = true) // se asigna en el service
    @Mapping(target = "status", ignore = true) // se asigna en el service (ACTIVE)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Administrator toEntity(CreateAdministratorDTO dto);

    /* UPDATE DTO -> ENTITY (partial) */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idUser", ignore = true)
    @Mapping(target = "adminCode", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(UpdateAdministratorDTO dto, @MappingTarget Administrator admin);

    /* ENTITY -> RESPONSE DTO */
    @Mapping(source = "idUser", target = "id")
    @Mapping(source = "role.name", target = "role")
    AdministratorResponseDTO toResponse(Administrator admin);
}
