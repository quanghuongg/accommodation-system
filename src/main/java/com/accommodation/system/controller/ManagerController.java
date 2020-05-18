package com.accommodation.system.controller;

import com.accommodation.system.define.Constant;
import com.accommodation.system.entity.Contract;
import com.accommodation.system.entity.Feedback;
import com.accommodation.system.entity.Skill;
import com.accommodation.system.entity.User;
import com.accommodation.system.entity.model.*;
import com.api.user.entity.model.*;
import com.accommodation.system.entity.request.RevenueRequest;
import com.accommodation.system.entity.response.ListContract;
import com.accommodation.system.entity.response.ListFeedback;
import com.accommodation.system.entity.response.ListSkill;
import com.accommodation.system.entity.response.ListUser;
import com.accommodation.system.exception.ApiServiceException;
import com.accommodation.system.service.ContractService;
import com.accommodation.system.service.ManagerService;
import com.accommodation.system.service.UserService;
import com.accommodation.system.uitls.ServiceUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = {"/manager"})
@Api(tags = {"ManagerController API"})
public class ManagerController {

    private final ManagerService managerService;

    private final UserService userService;

    private final ContractService contractService;


    public ManagerController(ManagerService managerService, UserService userService, ContractService contractService) {
        this.managerService = managerService;
        this.userService = userService;
        this.contractService = contractService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = {"/add-admin"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> addAdmin(@RequestBody User user) throws ApiServiceException {
        if (user.getUsername().isEmpty() || user.getPassword().isEmpty()) {
            throw new ApiServiceException(Constant.OBJECT_EMPTY_FIELD);
        }

        User existedUser = userService.findByUsername(user.getUsername());
        if (ServiceUtils.isNotEmpty(existedUser)) {
            throw new ApiServiceException(Constant.USER_CREATE_EXISTING);
        }
        if (user.getDisplay_name() == null) {
            user.setDisplay_name(user.getUsername());
        }
        user.setRole_id(3);
        userService.save(user);
        Response response = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = {"/list-user"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> getAllUser(@RequestBody RequestInfo requestInfo) {
        if (requestInfo.getPage() == 0) {
            requestInfo.setPage(1);
        }
        if (requestInfo.getSize() == 0) {
            requestInfo.setSize(10);
        }
        List<User> list = managerService.getAllUser(requestInfo.getRoleId());
        int total = list.size();
        list = ServiceUtils.paging(list, requestInfo.getPage(), requestInfo.getSize());
        for (User user : list) {
            user.setRole_id(userService.findRoleByUserId(user.getId()).getId());
            if (ServiceUtils.isNotEmpty(user.getSkills())) {
                String skills = "";
                for (int i = 0; i < user.getSkills().size(); i++) {
                    skills += user.getSkills().get(i).getName();
                    if (i < user.getSkills().size() - 1) {
                        skills += ";";
                    }
                }
                user.setList_skill(skills);
            }
        }
        ListUser result = ListUser.builder()
                .page(requestInfo.getPage())
                .size(requestInfo.getSize())
                .total(total)
                .listUser(list).build();
        Response responseObject = Response.builder()
                .code(0)
                .data(result)
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.OK);
    }

    @RequestMapping(value = {"/view-detail"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> viewInfoDetailUser(@RequestParam int userId) throws ApiServiceException {
        if (ServiceUtils.isEmpty(userId)) {
            throw new ApiServiceException("empty field");
        }
        User user = userService.findByUserId(userId);
        String skills = "";
        for (int i = 0; i < user.getSkills().size(); i++) {
            skills += user.getSkills().get(i).getName();
            if (i < user.getSkills().size() - 1) {
                skills += ";";
            }
        }
        user.setList_skill(skills);
        Response responseObject = Response.builder()
                .code(0)
                .data(user)
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = {"/create-skill"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> createSkill(@RequestBody Skill skill) throws ApiServiceException {
        if (ServiceUtils.isEmpty(skill) || ServiceUtils.isEmpty(skill.getName())) {
            throw new ApiServiceException("empty field");
        }
        skill.setStatus(1);
        skill.setCreated(System.currentTimeMillis());
        Response responseObject = Response.builder()
                .code(0)
                .data(managerService.addSkill(skill))
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = {"/update-skill"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> updateSkill(@RequestBody Skill skill) throws ApiServiceException {
        Skill oldSkill = managerService.findSkillById(skill.getId());
        if (ServiceUtils.isEmpty(oldSkill)) {
            throw new ApiServiceException("skill not existed");
        }
        if (ServiceUtils.isNotEmpty(skill.getName())) {
            oldSkill.setName(skill.getName());
        }
        if (ServiceUtils.isNotEmpty(skill.getDescription())) {
            oldSkill.setDescription(skill.getDescription());
        }
        managerService.updateSkill(oldSkill);
        Response responseObject = Response.builder()
                .code(0)
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = {"/list-skill"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> listSkill(@RequestBody RequestInfo requestInfo) {
        if (requestInfo.getPage() == 0) {
            requestInfo.setPage(1);
        }
        if (requestInfo.getSize() == 0) {
            requestInfo.setSize(10);
        }
        List<Skill> skillList = managerService.listSkill();
        int total = skillList.size();
        skillList = ServiceUtils.paging(skillList, requestInfo.getPage(), requestInfo.getSize());
        ListSkill result = ListSkill.builder()
                .skillList(skillList)
                .total(total)
                .page(requestInfo.getPage())
                .size(requestInfo.getSize())
                .build();
        Response responseObject = Response.builder()
                .code(0)
                .data(result)
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = {"list-contract"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> getListContract(@RequestBody RequestInfo requestInfo) {
        if (requestInfo.getPage() == 0) {
            requestInfo.setPage(1);
        }
        if (requestInfo.getSize() == 0) {
            requestInfo.setSize(10);
        }
        List<Contract> list = managerService.getListContract(requestInfo);
        int total = list.size();
        list = ServiceUtils.paging(list, requestInfo.getPage(), requestInfo.getSize());
        for (Contract contract : list) {
            contract.setTutor(userService.findByUserId(contract.getTutor_id()).getDisplay_name());
            contract.setStudent(userService.findByUserId(contract.getStudent_id()).getDisplay_name());

        }
        ListContract result = ListContract.builder()
                .contracts(list)
                .total(total)
                .page(requestInfo.getPage())
                .size(requestInfo.getSize())
                .build();
        Response responseObject = Response.builder()
                .code(0)
                .data(result)
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = {"/block"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> blockUser(@RequestParam int userId) throws ApiServiceException {
        if (userId <= 0) {
            throw new ApiServiceException("userId not found");
        }
        User user = userService.findByUserId(userId);
        if (ServiceUtils.isEmpty(user)) {
            throw new ApiServiceException("userId not found");
        }
        user.setStatus(0);
        userService.update(user);
        Response responseObject = Response.builder()
                .code(0)
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = {"/un-block"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> unBlockUser(@RequestParam int userId) throws ApiServiceException {
        if (userId <= 0) {
            throw new ApiServiceException("userId not found");
        }
        User user = userService.findByUserId(userId);
        if (ServiceUtils.isEmpty(user)) {
            throw new ApiServiceException("userId not found");
        }
        user.setStatus(1);
        userService.update(user);
        Response responseObject = Response.builder()
                .code(0)
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = {"/list-feedback"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> listFeedBack(@RequestBody RequestInfo requestInfo) throws ApiServiceException {
        if (requestInfo.getPage() == 0) {
            requestInfo.setPage(1);
        }
        if (requestInfo.getSize() == 0) {
            requestInfo.setSize(10);
        }
        List<Feedback> feedbackList = contractService.listFeedBacks(requestInfo);
        int total = feedbackList.size();
        feedbackList = ServiceUtils.paging(feedbackList, requestInfo.getPage(), requestInfo.getSize());
        for (Feedback feedback : feedbackList) {
            Contract contract = contractService.findById(feedback.getContract_id());
            User student = userService.findByUserId(contract.getStudent_id());
            feedback.setStudent(student.getDisplay_name());
            feedback.setTutor(userService.findByUserId(contract.getTutor_id()).getDisplay_name());
        }
        ListFeedback result = ListFeedback.builder()
                .feedbacks(feedbackList)
                .total(total)
                .page(requestInfo.getPage())
                .size(requestInfo.getSize())
                .build();
        Response responseObject = Response.builder()
                .code(0)
                .data(result)
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = {"/revenue-skill"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> statisticTopSkill(@RequestBody RevenueRequest revenueRequest) throws ApiServiceException {
        List<StatisticSkill> list = null;
        list = contractService.statisticTopSkill(revenueRequest);
        list.sort(Comparator.comparingInt(StatisticSkill::getTotal));
        Collections.reverse(list);
        if (list.size() > 5) {
            list = list.subList(0, 5);
        }
        Response responseObject = Response.builder()
                .code(0)
                .data(list)
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = {"/revenue-tutor"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> statisticTopByTutor(@RequestBody RevenueRequest revenueRequest) throws ApiServiceException {
        List<StatisticTutor> list = null;
        list = contractService.statisticTopByTutor(revenueRequest);
        list.sort(Comparator.comparingInt(StatisticTutor::getTotal));
        Collections.reverse(list);
        if (list.size() > 5) {
            list = list.subList(0, 5);
        }
        Response responseObject = Response.builder()
                .code(0)
                .data(list)
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = {"/pay"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> paymentTutor(@RequestParam int contractId) {
        Contract contract = contractService.findById(contractId);
        if (ServiceUtils.isNotEmpty(contract)) {
            contract.setStatus(2);
            contract.setUpdated(System.currentTimeMillis());
            contractService.update(contract);
        }
        Response response = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = {"/statistic"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> statisticRevenue(@RequestBody RevenueRequest request) {
        List<StatisticRevenue> list = contractService.statisticRevenue(request);
        Response response = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .data(list)
                .message(Constant.SUCCESS_MESSAGE)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
