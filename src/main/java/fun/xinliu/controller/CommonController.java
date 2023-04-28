package fun.xinliu.controller;

import fun.xinliu.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${takeaway.path}")
    private String basePath;
    /**
     * create by: Xin Liu
     * description: 文件上传功能
     * create time: 2023/4/15 2:55 PM
     *
      * @param file
     * @return fun.xinliu.common.Result<java.lang.String>
     */
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file){
        //此时获取的file是一个临时文件，如果不进行转存的话，会在本次请求完成后直接删除
        log.info(file.toString());

        //原始文件名
        String originalFilename = file.getOriginalFilename();

        //获取原始文件后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        //通过UUID随机生成一个新的文件名，以避免重复文件上传导致的覆盖
        String fileName = UUID.randomUUID().toString() + suffix;

        //目录存在判断
        File dir = new File(basePath);
        if(!dir.exists()) {
            dir.mkdirs();
        }

        //进行文件转移
        try {
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 前端页面需要这个文件名称
        return Result.success(fileName);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse httpServletResponse) throws IOException {
        // 不需要返回值，因为我们通过输出流的方式给浏览器写图片

        FileInputStream is = null;
        ServletOutputStream os = null;
        //首先通过输入流读取文件内容
        try {
             is = new FileInputStream(new File(basePath + name));
             os = httpServletResponse.getOutputStream();

            httpServletResponse.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while((len = is.read(bytes)) != -1) {
                os.write(bytes, 0, len);
                os.flush();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if(is != null) {
                is.close();
            }
            if(os != null) {
                os.close();
            }
        }
        //然后通过输出流将文件写回浏览器展示
    }
}
