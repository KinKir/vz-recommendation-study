package com.victorzhang.recommendation.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.victorzhang.recommendation.domain.DownloadRecord;
import com.victorzhang.recommendation.service.DownloadRecordService;
import com.victorzhang.recommendation.util.CommonUtils;
import com.victorzhang.recommendation.util.query.GenericQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.victorzhang.recommendation.util.Constants.USER_ID;

@Controller
@RequestMapping("downloadRecord")
public class DownloadRecordController {

    @Autowired
    @Qualifier("downloadRecordService")
    private DownloadRecordService downloadRecordService;

    @Autowired
    private HttpServletRequest request;

    @RequestMapping("/forwardResourceDownloadRecordUI.do")
    public String forwardResourceDownloadRecordUI() {
        return "resourceDownloadRecord";
    }

    @RequestMapping("/listPaging.do")
    @ResponseBody
    public Map<String, Object> listPaging(String _page, String _pageSize, String resourceName, String resourceType, String startDate, String endDate) throws Exception {
        DownloadRecord downloadRecord = new DownloadRecord(CommonUtils.sesAttr(request, USER_ID));
        GenericQueryParam param = new GenericQueryParam();
        param.fill("resourceName", resourceName);
        param.fill("resourceType", resourceType);
        return downloadRecordService.listPaging(downloadRecord, _page, _pageSize, startDate, endDate, param);
    }
}
