package com.jeesite.modules.ems.service;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.constant.DefaultConstant;
import com.jeesite.common.expr.BusinessException;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.utils.SpringUtils;
import com.jeesite.modules.cache.service.RedisService;
import com.jeesite.modules.constant.RedisConstant;
import com.jeesite.modules.ems.entity.EmsArea;
import com.jeesite.modules.job.task.utils.Calculatehelper;
import com.jeesite.modules.sys.entity.Company;
import com.jeesite.modules.sys.entity.Employee;
import com.jeesite.modules.sys.entity.User;
import com.jeesite.modules.sys.service.support.EmployeeServiceSupport;
import com.jeesite.modules.sys.utils.EmpUtils;
import com.jeesite.modules.sys.utils.UserHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * EMS 用户辅助类
 */
@Component
public class EmsUserHelper {
    private static RedisService redisService;
    private static String REDIS_CASH_EMPLOYEE_KEY_PREFIX = "jeesite_custom_user_cache_employee_";
    private static EmployeeServiceSupport employeeServiceSupport;
    private static EmsAreaService emsAreaService;

    @PostConstruct
    public void construct() {
        this.redisService = SpringUtils.getBean(RedisService.class);
        this.employeeServiceSupport = SpringUtils.getBean(EmployeeServiceSupport.class);
        this.emsAreaService = SpringUtils.getBean(EmsAreaService.class);
    }

    /**
     * 获取当前用户--雇佣人信息
     *
     * @return
     */
    public static Employee userEmployee() {
        User user = UserHelper.getUser();
//        String key = REDIS_CASH_EMPLOYEE_KEY_PREFIX + user.getUserCode();
//        Employee employee = (Employee) redisService.get(key);
//        if (Objects.isNull(employee)) {
//            //查询
//            Employee where = new Employee();
//            where.setEmpCode(user.getUserCode());
//            employee = employeeServiceSupport.findByEmpCode(where);
//            redisService.set(key, employee, 60 * 60);
//        }
        Employee employee = EmpUtils.get(user);
        return employee;
    }

    /**
     * 获取当前用户--公司信息
     *
     * @return
     */
    public static Company userCompany(boolean isThrow, String expInfo) {
        Company company = null;
        Employee employee = userEmployee();
        if (Objects.isNull(employee)) {
            if (isThrow) {
                throw new BusinessException(expInfo);
            }
        } else {
            company = employee.getCompany();
            if (Objects.isNull(company) && isThrow) {
                throw new BusinessException(expInfo);
            }
        }
        return company;
    }

    /**
     * 获取当前用户--公司信息
     *
     * @return
     */
    public static Company userCompany() {
        return userCompany(false, null);
    }

    public static List<String> getMeterMarkList() {
        return getMeterMarkList(null);
    }

    public static List<String> getMeterMarkList(String areaCode) {
        return getMeterMarkList(null, areaCode);
    }

    /**
     * 公司计量标识电表查询，缓存 300S
     *
     * @return
     */
    public static List<String> getMeterMarkList(String companyCode, String areaCode) {
        List<String> meterMarkList = ListUtils.newArrayList();
        if (StringUtils.isEmpty(companyCode)) {
            //默认取所在公司编码
            Company company = userCompany();
            if (Objects.nonNull(company)) {
                companyCode = company.getCompanyCode();
            }
        }

        String key = RedisConstant._COMPANY_MARKED_METER_CACHE_ + companyCode + DefaultConstant.UNDERLINE_SPLIT + areaCode;
        //先查缓存里有没有
        meterMarkList = (List<String>) redisService.get(key);
        if (CollectionUtils.isEmpty(meterMarkList)) {
            //查询所有区域编码
            Map<String, List<EmsArea>> areaCompanyMap = findAreaCompanyMap(companyCode, areaCode);
            List<EmsArea> emsAreas = areaCompanyMap.get(companyCode);
            meterMarkList = Calculatehelper.getMetersMarkList(emsAreas);
            redisService.set(key, meterMarkList, 60 * 60);
        }
        return meterMarkList;
    }

    public static Map<String, List<EmsArea>> findAreaCompanyMap(String companyCode, String areaCode) {
        EmsArea where = new EmsArea();
        where.setCompanyCode(companyCode);
        where.setAreaCode(areaCode);
        return emsAreaService.findAreaCompanyMap(where);
    }

}
