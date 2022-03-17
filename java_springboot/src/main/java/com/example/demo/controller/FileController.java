package com.example.demo.controller;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.example.demo.common.Result;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping(value = "/files")
public class FileController {
    @Value("${server.port}")
    private String port;

    private static final String IP = "http://localhost";

    //  文件上传
    @PostMapping("/upload")
    public Result<?> upload(MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();   // 获取名称
        //  定义文件唯一标识前缀
        String flag = IdUtil.fastSimpleUUID();
        //  获取上传路径
        String rootFilePath = System.getProperty("user.dir") + "/java_springboot/src/main/resources/file/" + flag + "_" + originalFileName;
        // 把文件写入上传路径
        FileUtil.writeBytes(file.getBytes(), rootFilePath);

        return Result.success(IP + ":" + port + "/files/" + flag);
    }

    //  文件下载
    @GetMapping("/{flag}")
    public void downloadFiles(@PathVariable String flag, HttpServletResponse response) {
        //  新建输出流对象
        OutputStream os;
        String basePath = System.getProperty("user.dir") + "/java_springboot/src/main/resources/file/";
        //  获取所有文件名
        List<String> fileNames = FileUtil.listFileNames(basePath);
        //  找到和flag参数一致的文件
        String fileName = fileNames.stream().filter(name -> name.contains(flag)).findAny().orElse("");
        try {
            if (StrUtil.isNotEmpty(fileName)) {
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
                response.setContentType("application/octet-stream");
                //  通过文件路径读取文件字节流
                byte[] bytes = FileUtil.readBytes(basePath + fileName);
                //  通过输出流返回文件
                os = response.getOutputStream();
                os.write(bytes);
                os.flush();
                os.close();
            }
        } catch (Exception e) {
            System.out.println("文件下载失败");
        }
    }
}
