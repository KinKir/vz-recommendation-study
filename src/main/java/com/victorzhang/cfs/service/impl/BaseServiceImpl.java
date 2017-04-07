package com.victorzhang.cfs.service.impl;

import com.victorzhang.cfs.mapper.BaseMapper;
import com.victorzhang.cfs.service.BaseService;
import com.victorzhang.cfs.util.CommonUtils;
import com.victorzhang.cfs.util.query.BuildQueryParam;
import com.victorzhang.cfs.util.query.GenericQueryParam;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.victorzhang.cfs.util.Constants.*;

public abstract class BaseServiceImpl<T, ID extends Serializable> implements BaseService<T, ID> {

    protected abstract BaseMapper<T, ID> getMapper();

    @Override
    public boolean save(T entity) throws Exception {
        boolean flag = false;
        int i = getMapper().save(entity);
        if (i > 0) {
            flag = true;
        }
        return flag;
    }

    @Override
    public boolean remove(ID id) throws Exception {
        boolean flag = false;
        int i = getMapper().remove(id);
        if (i > 0) {
            flag = true;
        }
        return flag;
    }

    @Override
    public boolean removeAll(List<String> ids) throws Exception {
        boolean flag = false;
        int i = getMapper().removeAll(ids);
        if (i > 0) {
            flag = true;
        }
        return flag;
    }

    @Override
    public boolean update(T entity) throws Exception {
        boolean flag = false;
        int i = getMapper().update(entity);
        if (i > 0) {
            flag = true;
        }
        return flag;
    }

    @Override
    public T getById(ID id) throws Exception {
        return getMapper().getById(id);
    }

    @Override
    public List<T> list(T entity) throws Exception {
        GenericQueryParam param = new GenericQueryParam();
        BuildQueryParam.buildParam(entity, param);
        List<T> data = getMapper().list(param);
        if(data.isEmpty() || data.size() == 0){
            data = new ArrayList<>();
        }
        return data;
    }

    @Override
    public Map<String, Object> listPaging(T entity, String page, String pageSize, String startDate, String endDate) throws Exception {
        Map<String, Object> result = new HashMap<>();
        GenericQueryParam param = new GenericQueryParam();
        BuildQueryParam.buildParam(entity, param);
        if (StringUtils.isNotEmpty(startDate)) {
            startDate += START_DATE_FORMAT;
            param.fill(START_DATE, startDate);
        }
        if (StringUtils.isNotEmpty(endDate)) {
            endDate += END_DATE_FORMAT;
            param.fill(END_DATE, endDate);
        }
        int rowCount = count(param);
        result = CommonUtils.para4Page(result, CommonUtils.paraPage(page), CommonUtils.paraPageSize(pageSize), rowCount);
        if (rowCount > 0) {
            param.fill(BEGIN, result.get(BEGIN));
            param.fill(PAGE_SIZE, result.get(PAGE_SIZE));
            result.put(DATA, CommonUtils.dataNull(getMapper().listPaging(param)));
        } else {
            result.put(DATA, EMPTY_STRING);
        }
        return result;
    }

    @Override
    public int count(T entity) throws Exception {
        GenericQueryParam param = new GenericQueryParam();
        BuildQueryParam.buildParam(entity, param);
        return getMapper().count(param);
    }

    @Override
    public int count(GenericQueryParam param) throws Exception {
        return getMapper().count(param);
    }

}
