package com.accommodation.system.controller;

import com.accommodation.system.define.Constant;
import com.accommodation.system.entity.Contract;
import com.accommodation.system.entity.User;
import com.accommodation.system.entity.info.RevenueInfo;
import com.accommodation.system.entity.model.Response;
import com.accommodation.system.entity.request.RevenueRequest;
import com.accommodation.system.exception.ApiServiceException;
import com.accommodation.system.service.ContractService;
import com.accommodation.system.service.MailSendingService;
import com.accommodation.system.service.ManagerService;
import com.accommodation.system.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = {"/user"})
public class TutorController {
    private final MailSendingService mailSendingService;
    private final UserService userService;
    private final ManagerService managerService;
    private final ContractService contractService;


    @Autowired
    public TutorController(MailSendingService mailSendingService, UserService userService, ContractService contractService
            , ManagerService managerService) {
        this.mailSendingService = mailSendingService;
        this.userService = userService;
        this.managerService = managerService;
        this.contractService = contractService;
    }

    @PreAuthorize("hasRole('TUTOR')")
    @RequestMapping(value = {"/tutor-contract"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> listContract() throws ApiServiceException {
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        if (authUser.getName().isEmpty()) {
            throw new ApiServiceException("Token invalid");
        }
        User user = userService.findByUsername(authUser.getName());
        List<Contract> contractList = contractService.listContractByTutorId(user.getId());
        Response response = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESSAGE)
                .data(contractList)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('TUTOR')")
    @RequestMapping(value = {"/statistic-revenue"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> statisticRevenue(@RequestBody RevenueRequest request) throws ApiServiceException {
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        if (authUser.getName().isEmpty()) {
            throw new ApiServiceException("Token invalid");
        }
        List<Contract> contractList;
        User user = userService.findByUsername(authUser.getName());
        if (request.getDate_from() == 0 || request.getDate_to() == 0) {
            contractList = contractService.listRevenues(user.getId());
        } else {
            contractList = contractService.listRevenueByTime(user.getId(), request.getDate_from(), request.getDate_to());
        }
        double total = 0;
        for (Contract contract : contractList) {
            total += contract.getTotal();
        }
        RevenueInfo revenueInfo = RevenueInfo.builder()
                .list(contractList)
                .total(total)
                .build();
        Response response = Response.builder()
                .code(Constant.SUCCESS_CODE)
                .message(Constant.SUCCESS_MESSAGE)
                .data(revenueInfo)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
