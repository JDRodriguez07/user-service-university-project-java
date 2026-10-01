package edu.university.user_service.mapper;

import edu.university.user_service.dto.CreateTeacherDTO;
import edu.university.user_service.dto.TeacherResponseDTO;
import edu.university.user_service.dto.UpdateTeacherDTO;
import edu.university.user_service.model.Teacher;
import org.mapstruct.*;

/**
 * Mapper for converting between Teacher entity and DTOs.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TeacherMapper {

    @Mapping(target = "idUser", ignore = true)
    @Mapping(target = "teacherCode", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Teacher toEntity(CreateTeacherDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idUser", ignore = true)
    @Mapping(target = "teacherCode", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(UpdateTeacherDTO dto, @MappingTarget Teacher teacher);

    @Mapping(source = "idUser", target = "id")
    @Mapping(source = "role.name", target = "role")
    TeacherResponseDTO toResponse(Teacher teacher);
}
