package edu.university.user_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.university.user_service.dto.UserFullResponseDTO;
import edu.university.user_service.exceptions.UserNotFoundException;
import edu.university.user_service.model.Administrator;
import edu.university.user_service.model.Person;
import edu.university.user_service.model.Student;
import edu.university.user_service.model.Teacher;
import edu.university.user_service.model.User;
import edu.university.user_service.repository.AdministratorRepository;
import edu.university.user_service.repository.PersonRepository;
import edu.university.user_service.repository.StudentRepository;
import edu.university.user_service.repository.TeacherRepository;

@Service
public class UserSearchService {

    @Autowired
    private AdministratorRepository adminRepo;

    @Autowired
    private TeacherRepository teacherRepo;

    @Autowired
    private StudentRepository studentRepo;

    @Autowired
    private PersonRepository personRepository;

    public UserFullResponseDTO search(String value) {

        // 1. Buscar por DNI en Person
        var byPersonDni = personRepository.findByDni(value);
        if (byPersonDni.isPresent()) {
            // Person extiende de User, así que podemos mapearlo igual
            return mapToFullResponse(byPersonDni.get());
        }

        // 2. Buscar por AdminCode
        var admin = adminRepo.findByAdminCode(value);
        if (admin.isPresent()) {
            return mapToFullResponse(admin.get());
        }

        // 3. Buscar por TeacherCode
        var teacher = teacherRepo.findByTeacherCode(value);
        if (teacher.isPresent()) {
            return mapToFullResponse(teacher.get());
        }

        // 4. Buscar por StudentCode
        var student = studentRepo.findByStudentCode(value);
        if (student.isPresent()) {
            return mapToFullResponse(student.get());
        }

        throw new UserNotFoundException("User not found for value: " + value);
    }

    private UserFullResponseDTO mapToFullResponse(User user) {

        UserFullResponseDTO.UserFullResponseDTOBuilder dto = UserFullResponseDTO.builder()
                .id(user.getIdUser())
                .email(user.getEmail())
                .status(user.getStatus().name())
                .role(user.getRole().getName()); // <--- AQUÍ EL ROL

        // Person
        if (user instanceof Person p) {
            dto.documentType(p.getDocumentType().name());
            dto.dni(p.getDni());
            dto.name(p.getName());
            dto.lastName(p.getLastName());
            dto.gender(p.getGender());
            dto.birthDate(p.getBirthDate());
            dto.phoneNumber(p.getPhoneNumber());
            dto.address(p.getAddress());
        }

        // Admin
        if (user instanceof Administrator a) {
            dto.adminCode(a.getAdminCode());
            dto.department(a.getDepartment());
            dto.position(a.getPosition());
        }

        // Teacher
        if (user instanceof Teacher t) {
            dto.teacherCode(t.getTeacherCode());
            dto.specialization(t.getSpecialization());
            dto.academicDegree(t.getAcademicDegree().name());
            dto.contractType(t.getContractType().name());
            dto.hireDate(t.getHireDate());
        }

        // Student
        if (user instanceof Student s) {
            dto.studentCode(s.getStudentCode());
            dto.admissionDate(s.getAdmissionDate());
            dto.graduationDate(s.getGraduationDate());
            dto.gpa(s.getGpa());
            dto.career(s.getCareer());
            dto.studentStatus(s.getStudentStatus().name());
        }

        return dto.build();
    }
}
