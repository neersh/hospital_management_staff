package controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import controller.dto.Patient;
import controller.dto.Staff;
import controller.dto.StaffDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import repositiory.PatientRepository;
import repositiory.StaffRepository;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api")
public class HospitalController {

    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    private final StaffRepository staffRepository;

    private final PatientRepository patientRepository;

    public HospitalController(PatientRepository patientRepository, ObjectMapper objectMapper, PasswordEncoder passwordEncoder, StaffRepository staffRepository) {
        this.patientRepository = patientRepository;
        this.objectMapper = objectMapper;
        this.staffRepository = staffRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Admit new patient
    @PostMapping("/patients")
    public ResponseEntity<Patient> admitPatient(@RequestBody Patient patient) {
        try {
            log.info("inside HospitalController : {} " + objectMapper.writeValueAsString(patient));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        patient.setStatus("admitted");
        Patient savedPatient = patientRepository.save(patient);
        return new ResponseEntity<>(savedPatient, HttpStatus.CREATED);
    }

    // Fetch all admitted patients
    @GetMapping("/patients")
    public ResponseEntity<List<Patient>> getAllAdmittedPatients() {
        log.info("inside HospitalController : getAllAdmittedPatients()");
        List<Patient> patients = patientRepository.findAllByStatus("admitted");
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    // Discharge patient
    @PutMapping("/patients/{id}/discharge")
    public ResponseEntity<Patient> dischargePatient(@PathVariable Long id) {
        log.info("inside HospitalController :get discharge patients with id" + id);
        Patient patient = patientRepository.findById(id).orElse(null);
        if (patient != null) {
            patient.setStatus("discharged");
            patientRepository.save(patient);
            return new ResponseEntity<>(patient, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody StaffDto staffDto) {
        // Check if the username is already taken
        if (staffRepository.findByUsername(staffDto.getUserName()) != null) {
            return new ResponseEntity<>("Username already exists. Please choose a different username.", HttpStatus.BAD_REQUEST);
        }

        // Create a staff member and save it in the database
        Staff staff = new Staff();
        staff.setName(staffDto.getName());
        staff.setUserName(staffDto.getUserName());
        staff.setPassword(passwordEncoder.encode(staffDto.getPassword()));
        staff.setRole(staffDto.getRole());
        staffRepository.save(staff);

        return new ResponseEntity<>("Staff member registered successfully.", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String userName, @RequestParam String password) {
// add authentication process by jwt - incomplete process
        String staff = staffRepository.findByUsername(userName);
        if (staff == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        } else {
            return new ResponseEntity<>("login successfully", HttpStatus.OK);
        }
    }


}
