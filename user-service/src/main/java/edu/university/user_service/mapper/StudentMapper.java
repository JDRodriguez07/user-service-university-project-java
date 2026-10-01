package edu.university.user_service.mapper;

import edu.university.user_service.dto.CreateStudentDTO;
import edu.university.user_service.dto.StudentResponseDTO;
import edu.university.user_service.dto.UpdateStudentDTO;
import edu.university.user_service.model.Student;
import org.mapstruct.*;

/**
 * Mapper for converting between Student entity and DTOs.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StudentMapper {

    @Mapping(target = "idUser", ignore = true)
    @Mapping(target = "studentCode", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Student toEntity(CreateStudentDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idUser", ignore = true)
    @Mapping(target = "studentCode", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(UpdateStudentDTO dto, @MappingTarget Student student);

    @Mapping(source = "idUser", target = "id")
    @Mapping(source = "role.name", target = "role")
    StudentResponseDTO toResponse(Student student);
}
