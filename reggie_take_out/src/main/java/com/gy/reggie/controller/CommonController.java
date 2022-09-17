package com.gy.reggie.controller;

import com.gy.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {
    @Value("${reggie.path}")
    private String path;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        String originalFilename = file.getOriginalFilename();
        int i = originalFilename.lastIndexOf(".");
        String prefix = originalFilename.substring(i);
        System.out.println(originalFilename);
        String filename = UUID.randomUUID().toString() + prefix;
        log.info(filename);
        File file1 = new File(path);
        if(!file1.exists()){
            file1.mkdir();
        }
        try {
            file.transferTo(new File(path+filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success(filename);
    }
    @GetMapping("/download")
    public void download(String name, HttpServletResponse httpServletResponse){
        try {
            FileInputStream inputStream = new FileInputStream(new File(path+name));
            ServletOutputStream outputStream = httpServletResponse.getOutputStream();
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = inputStream.read(bytes))!= -1){
                outputStream.write(bytes);
                outputStream.flush();
            }
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
