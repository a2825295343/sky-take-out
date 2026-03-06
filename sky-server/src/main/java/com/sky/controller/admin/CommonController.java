package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Api(tags="通用接口")
@Slf4j
public class CommonController {
    @Autowired
    private AliOssUtil aliOssUtil;

    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file) {
        log.info("文件上传：{}", file);
        try {
            String originalFileName = file.getOriginalFilename();
            //不带后缀的文件名
            String fileName = originalFileName.substring(0, originalFileName.lastIndexOf("."));
            //文件后缀
            String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
            //构造新文件名称
            String objectName = fileName+"_"+UUID.randomUUID().toString()+suffix;
            aliOssUtil.upload(file.getBytes(), objectName);
            String filePath = aliOssUtil.upload(file.getBytes(), objectName);
            return Result.success(filePath);
        } catch(IOException e){
            log.error("文件上传失败：{}", e.getMessage());
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
