package cn.reve.controller.goods;

import cn.reve.entity.PageResult;
import cn.reve.entity.Result;
import cn.reve.pojo.goods.AlbumImg;
import cn.reve.pojo.user.User;
import cn.reve.service.goods.AlbumImgService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/albumImg")
public class AlbumImgController {

    @Autowired
    private HttpServletRequest request;

    @Reference
    private AlbumImgService albumImgService;

    @GetMapping("/getAlbum")
    public PageResult<AlbumImg> getAlbum(int albumId, int pageSize, int pageNum){
        PageResult<AlbumImg> imgPageResult =albumImgService.getAllAlbumById(albumId, pageSize, pageNum);
        return imgPageResult;
    }

    @GetMapping("/delete")
    public Result delete(int id){
        albumImgService.delShaShinById(id);
        return new Result();
    }

    @PostMapping("/upload")
    public void upload(@RequestParam("file")MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        System.out.println(originalFilename);
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        //This can be changed to uploadFilePath = user.getName() later, if user doesn't log in, this function should be deactivated.
        String uploadFileName = user!=null ? user.getName():"deesse";
        File newFile = new File("localhost:9101/img/"+uploadFileName+"/"+originalFilename);
        if(!newFile.exists()){
            newFile.mkdirs();
        }
        file.transferTo(newFile);
    }

}
