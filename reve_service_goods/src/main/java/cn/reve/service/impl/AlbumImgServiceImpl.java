package cn.reve.service.impl;

import cn.reve.dao.AlbumImgMapper;
import cn.reve.entity.PageResult;
import cn.reve.pojo.goods.AlbumImg;
import cn.reve.service.goods.AlbumImgService;
import cn.reve.utils.MapperUtils;
import cn.reve.utils.PageUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class AlbumImgServiceImpl implements AlbumImgService{

    @Autowired
    private AlbumImgMapper albumImgMapper;

    @Override
    public PageResult<AlbumImg> getAllAlbumById(int albumId, int pageSize, int pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        Example example = MapperUtils.andEqualToWithSingleValue(AlbumImg.class,"albumId", albumId);
        Page<AlbumImg> albumImgPage = (Page<AlbumImg>) albumImgMapper.selectByExample(example);
        return PageUtils.setPageResult(albumImgPage);
    }

    @Override
    public void delShaShinById(int id) {
        albumImgMapper.deleteByPrimaryKey(id);
    }
}
