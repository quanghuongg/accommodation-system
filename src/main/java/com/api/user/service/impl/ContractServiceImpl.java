package com.api.user.service.impl;

import com.api.user.entity.Contract;
import com.api.user.entity.Feedback;
import com.api.user.entity.Skill;
import com.api.user.entity.User;
import com.api.user.entity.info.ContractInfo;
import com.api.user.entity.model.RequestInfo;
import com.api.user.entity.model.StatisticRevenue;
import com.api.user.entity.model.StatisticSkill;
import com.api.user.entity.model.StatisticTutor;
import com.api.user.entity.request.RevenueRequest;
import com.api.user.mapper.ContractMapper;
import com.api.user.mapper.FeedbackMapper;
import com.api.user.mapper.ManageMapper;
import com.api.user.mapper.UserMapper;
import com.api.user.service.ContractService;
import com.api.user.uitls.ServiceUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service(value = "contractService")
public class ContractServiceImpl implements ContractService {
    private ContractMapper contractMapper;
    private UserMapper userMapper;
    private FeedbackMapper feedbackMapper;
    private ManageMapper manageMapper;

    @Autowired
    public ContractServiceImpl(ContractMapper contractMapper, UserMapper userMapper, FeedbackMapper feedbackMapper, ManageMapper manageMapper) {
        this.contractMapper = contractMapper;
        this.userMapper = userMapper;
        this.feedbackMapper = feedbackMapper;
        this.manageMapper = manageMapper;
    }

    @Override
    public int save(Contract contract) {
        contractMapper.createContract(contract);
        return contract.getId();
    }

    @Override
    public Contract findById(int contract_id) {
        return contractMapper.findById(contract_id);
    }

    @Override
    public void update(Contract contract) {
        contractMapper.update(contract);
    }

    @Override
    public List<Contract> listContractByStudentId(Integer id) {
        List<Contract> list = contractMapper.listContractByStudentId(id);
        for (Contract contract : list) {
            contract.setTutor(userMapper.findByUserId(contract.getTutor_id()).getDisplay_name());
            contract.setSkill_name(manageMapper.findSkillById(contract.getSkill()).getName());
        }
        return list;
    }

    @Override
    public List<Contract> listContractByTutorId(Integer id) {
        List<Contract> list = contractMapper.listContractByTutorId(id);
        for (Contract contract : list) {
            contract.setStudent(userMapper.findByUserId(contract.getStudent_id()).getDisplay_name());
            contract.setSkill_name(manageMapper.findSkillById(contract.getSkill()).getName());
        }
        return list;
    }

    @Override
    public ContractInfo detailContract(int id) {
        Contract contract = contractMapper.findById(id);
        ContractInfo contractInfo = new ContractInfo(contract);
        User tutor = userMapper.findByUserId(contract.getTutor_id());
        User student = userMapper.findByUserId(contract.getStudent_id());
        contractInfo.setTutorName(tutor.getDisplay_name());
        contractInfo.setStudentName(student.getDisplay_name());
        contractInfo.setSkill(manageMapper.findSkillById(contract.getSkill()).getName());
        contractInfo.setTutor_id(contract.getTutor_id());
        contractInfo.setStudent_id(contract.getStudent_id());
        return contractInfo;
    }

    @Override
    public List<Contract> listRevenueByTime(Integer id, long date_from, long date_to) {
        return contractMapper.listRevenueByTime(id, date_from, date_to);
    }

    @Override
    public List<Contract> listRevenues(Integer userId) {
        return contractMapper.listRevenues(userId);
    }

    @Override
    public void addFeedback(Feedback feedback) {
        feedbackMapper.addFeedback(feedback);
    }

    @Override
    public List<Feedback> listFeedBacks(RequestInfo requestInfo) {
        return feedbackMapper.listFeedBacks();
    }

    @Override
    public List<StatisticSkill> statisticTopSkill(RevenueRequest revenueRequest) {
        List<StatisticSkill> result = new ArrayList<>();
        List<Skill> skills = manageMapper.listAllSkill();
        for (Skill skill : skills) {
            List<Contract> contracts;
            if (revenueRequest.getDate_from() > 0) {
                contracts = contractMapper.getListContractDoneBySkillIdByTime(skill.getId(), revenueRequest.getDate_from(), revenueRequest.getDate_to());
            } else {
                contracts = contractMapper.getListContractDoneBySkillId(skill.getId());
            }
            double total = 0;
            if (ServiceUtils.isNotEmpty(contracts)) {
                for (Contract contract : contracts) {
                    total += contract.getTotal();
                }
            }
            StatisticSkill statisticSkill = StatisticSkill.builder()
                    .skill(skill.getName())
                    .total((int) total)
                    .build();
            result.add(statisticSkill);

        }
        return result;
    }

    @Override
    public List<StatisticTutor> statisticTopByTutor(RevenueRequest revenueRequest) {
        List<StatisticTutor> result = new ArrayList<>();
        List<User> users = userMapper.findTutorAll();
        for (User user : users) {
            List<Contract> contracts;
            if (revenueRequest.getDate_from() > 0) {
                contracts = contractMapper.getListContractDoneByTutorIdByTime(user.getId(), revenueRequest.getDate_from(), revenueRequest.getDate_to());
            } else {
                contracts = contractMapper.getListContractDoneByTutorId(user.getId());
            }
            double total = 0;
            if (ServiceUtils.isNotEmpty(contracts)) {
                for (Contract contract : contracts) {
                    total += contract.getTotal();
                }
            }
            StatisticTutor statisticTutor = StatisticTutor.builder()
                    .tutor(user.getUsername())
                    .total((int) total)
                    .build();
            result.add(statisticTutor);

        }
        return result;
    }

    @Override
    public List<StatisticRevenue> statisticRevenue(RevenueRequest request) {
        List<StatisticRevenue> revenues = new ArrayList<>();
        long dateFrom = request.getDate_from();
        long dateTo = request.getDate_to();
        while (dateFrom <= dateTo) {
            int total = contractMapper.getTotalContractDone(dateFrom, dateFrom + 24 * 60 * 60 * 1000);
            StatisticRevenue temp = StatisticRevenue.builder()
                    .date(dateFrom)
                    .total(total)
                    .build();
            revenues.add(temp);
            dateFrom += 24 * 60 * 60 * 1000;
        }
        return revenues;
    }
}
