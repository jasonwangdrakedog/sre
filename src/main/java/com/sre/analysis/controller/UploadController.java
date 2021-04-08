package com.sre.analysis.controller;


import com.sre.analysis.util.DateFormatterUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("upload")
@Slf4j
public class UploadController {


    @RequestMapping(value = "complain")
    @ResponseBody
    public String multiUploadComplain(HttpServletRequest request) {
        return upload(request, "complain");
    }

    @RequestMapping(value = "broken")
    @ResponseBody
    public String multiUploadBroken(HttpServletRequest request) {
        return upload(request, "broken");
    }


    @RequestMapping(value = "question")
    @ResponseBody
    public String multiUploadQuestion(HttpServletRequest request) {
        return upload(request, "question");
    }


    private String upload(HttpServletRequest request, String type) {
        log.info("上传{}}文件开始：", type);
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        MultiValueMap<String, MultipartFile> multiValueMap = multipartHttpServletRequest.getMultiFileMap();
        for (MultiValueMap.Entry<String, List<MultipartFile>> entry : multiValueMap.entrySet()) {
            List<MultipartFile> multipartFiles = entry.getValue();
            if (!CollectionUtils.isEmpty(multipartFiles)) {
                String dateStr = DateFormatterUtil.formatDate(new Date());
                String filePath = "D:\\workOrder\\origin\\" + type + "\\";
                File targetFile = null;
                for (MultipartFile multipartFile : multipartFiles) {

                    String filename = multipartFile.getOriginalFilename();
                    log.info("文件名开始：{}", filename);
                    try {
                        String[] fileNameArray = filename.split("\\.");
                        targetFile = new File(filePath + dateStr + "\\" + fileNameArray[0] + "_" + new Date().getTime() + "." + fileNameArray[1]);
                        if (!targetFile.exists()) {
                            targetFile.mkdirs();//创建目录
                            // System.out.println("测试文件夹不存在");
                        }
                        multipartFile.transferTo(targetFile);
                        // lists.add(filePath + filename);
                        log.info("上传成功 路径：{}", targetFile.getAbsolutePath());
                    } catch (Exception e) {
                        log.error("上传文件失败", e);
                        return e.getMessage();
                    }
                }
                return targetFile.getAbsolutePath();
            }
        }
        return "";
    }


}
