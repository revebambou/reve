package cn.reve.pojo.goods;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name="tb_album_img")
public class AlbumImg implements Serializable{

    @Id
    private Integer id;
    private String image;
    private Integer albumId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Integer albumId) {
        this.albumId = albumId;
    }

    @Override
    public String toString() {
        return "AlbumImg{" +
                "id=" + id +
                ", image='" + image + '\'' +
                ", albumId=" + albumId +
                '}';
    }
}
