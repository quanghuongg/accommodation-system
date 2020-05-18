package com.api.user.service;

import com.api.user.entity.Contract;
import com.api.user.entity.Feedback;
import com.api.user.entity.info.ContractInfo;
import com.api.user.entity.model.RequestInfo;
import com.api.user.entity.model.StatisticRevenue;
import com.api.user.entity.model.StatisticSkill;
import com.api.user.entity.model.StatisticTutor;
import com.api.user.entity.request.RevenueRequest;

import java.util.List;

public interface ContractService {
    int save(Contract contract);

    Contract findById(int contract_id);

    void update(Contract contract);

    List<Contract> listContractByStudentId(Integer id);

    List<Contract> listContractByTutorId(Integer id);

    ContractInfo detailContract(int id);

    List<Contract> listRevenues(Integer userId);

    List<Contract> listRevenueByTime(Integer id, long date_from, long date_to);

    //Feedback
    void addFeedback(Feedback feedback);

    List<Feedback> listFeedBacks(RequestInfo requestInfo);

    List<StatisticSkill> statisticTopSkill(RevenueRequest revenueRequest);

    List<StatisticTutor> statisticTopByTutor(RevenueRequest revenueRequest);

    List<StatisticRevenue> statisticRevenue(RevenueRequest request);
}
