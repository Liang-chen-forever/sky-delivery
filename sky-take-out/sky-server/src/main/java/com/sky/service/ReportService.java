package com.sky.service;


import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ReportService {

    /**
     * 统计指定时间区间内的营业额统计
     * @param begin 开始时间
     * @param end 结束时间
     * @return
     */
    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);

    /**
     * 统计指定时间区间内的用户统计
     * @param begin 开始时间
     * @param end 结束时间
     * @return
     */
    UserReportVO getUserStatistics(LocalDate begin, LocalDate end);

    /**
     * 统计指定时间区间内的订单统计
     * @param begin 开始时间
     * @param end 结束时间
     * @return
     */
    OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end);

    /**
     * 统计指定时间区间内的销售Top10统计
     * @param begin 开始时间
     * @param end 结束时间
     * @return
     */
    SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end);

    /**
     * 导出运营数据统计报表
     * @param response
     * @throws Exception
     */
    void exportBusinessData(HttpServletResponse response);
}
