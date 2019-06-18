package cn.reve.service.goods;

import cn.reve.entity.PageResult;
import cn.reve.pojo.goods.AlbumImg;

public interface AlbumImgService {
    PageResult<AlbumImg> getAllAlbumById(int albumId, int pageSize, int pageNum);

    void delShaShinById(int id);
}
