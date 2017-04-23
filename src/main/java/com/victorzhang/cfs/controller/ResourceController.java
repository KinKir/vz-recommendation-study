package com.victorzhang.cfs.controller;

import com.victorzhang.cfs.domain.Resource;
import com.victorzhang.cfs.service.ResourceService;
import com.victorzhang.cfs.util.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.Map;

import static com.victorzhang.cfs.util.Constants.*;

@Controller
@RequestMapping("resource")
public class ResourceController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    @Qualifier("resourceService")
    private ResourceService resourceService;

    @RequestMapping("/forwardUserResourceUI.do")
    public String forwardUserResourceUI() {
        return "userResource";
    }

    @RequestMapping(value = "/doUploadResource.do", method = RequestMethod.POST)
    @ResponseBody
    public void doUploadResource(MultipartFile resourceFile, String resourceName, String resourceDescription, String resourceTag, String resourceType) throws Exception {
        if (StringUtils.isEmpty(resourceName) || StringUtils.isEmpty(resourceTag) || StringUtils.isEmpty(resourceTag)) {
            throw new IllegalArgumentException(ARGS_ERROR);
        }
        resourceService.doUploadResource(resourceFile, resourceName, resourceDescription, resourceTag, resourceType, request);
    }

    @RequestMapping("/listPaging.do")
    @ResponseBody
    public Map<String, Object> listPaging(String _page, String _pageSize, String resourceName, String resourceType, String verifyType, String startDate, String endDate) throws Exception {
        Resource resource = new Resource();
        resource.setUserId(CommonUtils.sesAttr(request, USER_ID));
        resource.setResourceType(resourceType);
        resource.setResourceName(resourceName);
        resource.setVerifyType(verifyType);
        return resourceService.listPaging(resource, _page, _pageSize, startDate, endDate, null);
    }

    @RequestMapping("/getById.do")
    @ResponseBody
    public Resource getById(String id) throws Exception {
        return resourceService.getById(id);
    }

    @RequestMapping(value = "/doDownloadResource.do",method = RequestMethod.GET)
    @ResponseBody
    public void doDownloadResource(HttpServletResponse response, String id) throws Exception {
        resourceService.doDownloadResource(response, request, id);
    }

    @RequestMapping("/removeResource.do")
    @ResponseBody
    public void removeResource(String id) throws Exception {
        Resource resource = resourceService.getById(id);
        File file = new File(resource.getResourceServerPath());
        if (file.isFile() && file.exists()) {
            file.delete();
            if (!resourceService.remove(id)) {
                throw new SQLException(REMOVE_ERROR);
            }
        }else{
            throw new FileNotFoundException(FILE_NOT_FOUND);
        }
    }

    @RequestMapping("/forwardSystemResourceUI")
    public String forwardSystemResourceUI() {
        return "systemResource";
    }

    @RequestMapping("/listSystemResourcePaging.do")
    @ResponseBody
    public Map<String, Object> listSystemResourcePaging(String _page, String _pageSize, String resourceName, String resourceType, String startDate, String endDate) throws Exception {
        Resource resource = new Resource();
        resource.setResourceType(resourceType);
        resource.setResourceName(resourceName);
        resource.setVerifyType(RESOURCE_VERIFY_SUCCESS);
        return resourceService.listPaging(resource, _page, _pageSize, startDate, endDate, null);
    }
}
