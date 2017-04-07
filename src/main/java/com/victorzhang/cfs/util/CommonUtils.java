package com.victorzhang.cfs.util;

import com.victorzhang.cfs.domain.Message;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.victorzhang.cfs.util.Constants.*;

public class CommonUtils {

    private static final String PAGING_EXCEPTION = "分页异常";
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String SPLITTER_STRING = "-";
    private static final String UNKNOWN = "unknown";
    private static final String X_FORWARDED_FOR = "x-forwarded-for";
    private static final String PROXY_CLIENT_IP = "Proxy-Client-IP";
    private static final String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";
    private static final String PAGE = "page";
    private static final String PAGE_SUM = "pageSum";

    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public static HttpSession getSession(boolean flag) {
        return getRequest().getSession(flag);
    }

    public static String sesAttr(HttpServletRequest request, String id) {
        if (request.getSession().getAttribute(id) != null) {
            return request.getSession().getAttribute(id).toString();
        }
        return null;
    }

    public static String getIpAddr() {
        HttpServletRequest request = getRequest();
        String ip = request.getHeader(X_FORWARDED_FOR);

        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader(PROXY_CLIENT_IP);
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader(WL_PROXY_CLIENT_IP);
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

    public static String getDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(new Date());
    }

    public static String newUuid() {
        return UUID.randomUUID().toString().toUpperCase().replace(SPLITTER_STRING, EMPTY_STRING);
    }

    public static int paraPage(String _page) {
        int page = 1;
        return pageOrPageSizeValue(_page, page);
    }

    public static int paraPageSize(String _pageSize) {
        int pageSize = 10;
        return pageOrPageSizeValue(_pageSize, pageSize);
    }

    private static int pageOrPageSizeValue(String _pageOrPageSize, int pageOrPageSizeValue) {
        if (StringUtils.isNotEmpty(_pageOrPageSize)) {
            try {
                pageOrPageSizeValue = Integer.parseInt(_pageOrPageSize);
                if (pageOrPageSizeValue < 1)
                    pageOrPageSizeValue = 1;
            } catch (Exception e) {
                throw new IllegalArgumentException(PAGING_EXCEPTION);
            }
        }
        return pageOrPageSizeValue;
    }

    public static Map<String, Object> para4Page(Map<String, Object> result, int page, int pageSize, int count) {
        int pageSum = 1;
        if (count > 0) {
            if (count > pageSize) {
                pageSum = (count + pageSize - 1) / pageSize;
                if (page > pageSum) {
                    page = pageSum;
                }
            } else {
                page = 1;
            }
            result.put(PAGE_SIZE, pageSize);
        } else {
            count = 0;
        }
        result.put(BEGIN, (page - 1) * pageSize);
        result.put(PAGE, page);
        result.put(PAGE_SUM, pageSum);
        result.put(COUNT, count);

        return result;
    }

    public static List<Map<String, Object>> dataNull(List<Map<String, Object>> datas) {
        for (Map<String, Object> data : datas) {
            Set<String> keys = data.keySet();
            for (String key : keys) {
                if (data.get(key) == null) {
                    data.put(key, EMPTY_STRING);
                }
            }
        }
        return datas;
    }
}
