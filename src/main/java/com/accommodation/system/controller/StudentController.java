package com.accommodation.system.controller;

import com.accommodation.system.define.Constant;
import com.accommodation.system.entity.Contract;
import com.accommodation.system.entity.Feedback;
import com.accommodation.system.entity.User;
import com.accommodation.system.entity.info.ContractInfo;
import com.accommodation.system.entity.info.FeedbackRequest;
import com.accommodation.system.entity.model.Response;
import com.accommodation.system.entity.request.ContractRequest;
import com.accommodation.system.exception.ApiServiceException;
import com.accommodation.system.service.ContractService;
import com.accommodation.system.service.UserService;
import com.accommodation.system.uitls.ServiceUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = {"/user"})
public class StudentController {

    @Autowired
    private UserService userService;

    @Autowired
    private ContractService contractService;

    @PreAuthorize("hasRole('STUDENT')")
    @RequestMapping(value = {"/register-tutor"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerCourse(@RequestBody ContractRequest request) throws ApiServiceException {
        if (request.getTutorId() == 0 || ServiceUtils.isEmpty(userService.findByUserId(request.getTutorId()))) {
            throw new ApiServiceException("Tutor not found");
        }
        if (request.getSkill() == 0) {
            throw new ApiServiceException("Skills null");
        }
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        if (authUser.getName().isEmpty()) {
            throw new ApiServiceException("Token invalid");
        }
        User student = userService.findByUsername(authUser.getName());
        User tutor = userService.findByUserId(request.getTutorId());

        Contract contract = Contract.builder()
                .student_id(student.getId())
                .tutor_id(request.getTutorId())
                .number_hour(request.getNumberHour())
                .status(0)
                .number_hour(request.getNumberHour())
                .total(tutor.getHourly_wage() * request.getNumberHour())
                .created(System.currentTimeMillis())
                .date_from(request.getDateFrom())
                .date_to(request.getDateTo())
                .skill(request.getSkill())
                .build();
        contractService.save(contract);
        Response response = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESSAGE)
                .data(contract.getId())
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @RequestMapping(value = {"/pay"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> studentPay(@RequestParam int contract_id) throws ApiServiceException {
        Contract contract = contractService.findById(contract_id);
        if (ServiceUtils.isNotEmpty(contract)) {
            contract.setStatus(1);
            contract.setUpdated(System.currentTimeMillis());
            contractService.update(contract);
        }
        Response response = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @RequestMapping(value = {"/student-contract"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listContract() throws ApiServiceException {
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        if (authUser.getName().isEmpty()) {
            throw new ApiServiceException("Token invalid");
        }
        User user = userService.findByUsername(authUser.getName());
        List<Contract> contractList = contractService.listContractByStudentId(user.getId());
        Response response = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESSAGE)
                .data(contractList)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = {"/detail-contract"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> detailContract(@RequestParam int contract_id) throws ApiServiceException {
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        if (authUser.getName().isEmpty()) {
            throw new ApiServiceException("Token invalid");
        }
        ContractInfo contractInfo = contractService.detailContract(contract_id);
        Response response = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESSAGE)
                .data(contractInfo)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @RequestMapping(value = {"/add-feedback"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addFeedback(@RequestBody FeedbackRequest request) throws ApiServiceException {
        if (ServiceUtils.isEmpty(request.getContractId()) || ServiceUtils.isEmpty(request.getType())) {
            throw new ApiServiceException("Object empty field");
        }
        Feedback feedback = Feedback.builder()
                .contract_id(request.getContractId())
                .content(request.getContent())
                .type(request.getType())
                .created(System.currentTimeMillis())
                .build();
        contractService.addFeedback(feedback);
        Response response = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
