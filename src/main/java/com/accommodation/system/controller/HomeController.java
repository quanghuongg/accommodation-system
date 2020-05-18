package com.accommodation.system.controller;

import com.accommodation.system.exception.ApiServiceException;
import com.accommodation.system.entity.User;
import com.accommodation.system.entity.model.RequestInfo;
import com.accommodation.system.entity.model.Response;
import com.accommodation.system.entity.response.ListUser;
import com.accommodation.system.service.ManagerService;
import com.accommodation.system.uitls.ServiceUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class HomeController {
    private final ManagerService managerService;

    public HomeController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @RequestMapping(value = {"/list-tutor"}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> getListTutor(@RequestBody RequestInfo requestInfo) throws ApiServiceException {
        if (requestInfo.getPage() == 0) {
            requestInfo.setPage(1);
        }
        if (requestInfo.getSize() == 0) {
            requestInfo.setSize(10);
        }
        if (ServiceUtils.isEmpty(requestInfo.getOrderBy())) {
            requestInfo.setOrderBy("DESC");
        }
        List<User> list = null;
        if (requestInfo.getSkillId() > 0) {
            list = managerService.listTutorBySkill(requestInfo.getSkillId());
        } else {
            list = managerService.listTutor(requestInfo);

        }
        int total = list.size();
        list = ServiceUtils.paging(list, requestInfo.getPage(), requestInfo.getSize());
        ListUser result = ListUser.builder()
                .listUser(list)
                .total(total)
                .page(requestInfo.getPage())
                .size(requestInfo.getSize())
                .build();
        Response responseObject = Response.builder()
                .code(0)
                .data(result)
                .message("success")
                .build();
        return new ResponseEntity<>(responseObject, HttpStatus.OK);
    }


}
