package com.accommodation.system.service;

import com.accommodation.system.entity.Contract;
import com.accommodation.system.entity.Feedback;
import com.accommodation.system.entity.info.ContractInfo;
import com.accommodation.system.entity.model.RequestInfo;
import com.accommodation.system.entity.model.StatisticRevenue;
import com.accommodation.system.entity.model.StatisticSkill;
import com.accommodation.system.entity.model.StatisticTutor;
import com.accommodation.system.entity.request.RevenueRequest;

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
