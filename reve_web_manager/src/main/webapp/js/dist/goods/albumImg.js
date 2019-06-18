new Vue({
    el:"#app",
    data(){
        return{
            albumId:1,
            tableData:[],
            pageSize:10,
            pageNum:1,
            total:1006,
            searchMap:{},
            pojo:{},
            formVisible:false,
            flag:true,
            showShashin:false,
            imageUrl:"",
            imgUrl:"",
            dialogImageUrl: '',
            dialogVisible: false
        }
    },
    created(){
        let search = window.location.search;
        let index = search.lastIndexOf("=")+1;
        let id = search.substring(index);
        this.albumId = id;
        this.getAlbum();
    },
    methods:{
        getAlbum(){
            let id = this.albumId;
            axios.get(`/albumImg/getAlbum.do?albumId=${id}&pageSize=${this.pageSize}&pageNum=${this.pageNum}`).then(response=>{
                this.tableData = response.data.rows;
                this.total = response.data.total;
            })
        },
        goBack() {
            console.log('go back');
        },
        setCover(image){
            let albumId = this.albumId;
            axios.get(`/album/updateCover.do?image=${image}&albumId=${this.albumId}`).then(response=>{
                alert(response.data.message);
            })
        },
        viewShashin(imgUrl){
            this.showShashin = true;
            this.imageUrl = imgUrl;
        },
        delShaShin(id){
            this.$confirm('确定要删除此记录吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then( () => {
                axios.get(`/albumImg/delete.do?id=${id}`).then(response => {
                    alert(response.data.message);
                    this.getAlbum (); //刷新列表
                })
            })
        },

        addShaShin(){
            alert(this.imgUrl)
        },

        handleRemove(file, fileList) {
            console.log(file, fileList);
        },
        handlePictureCardPreview(file) {
            this.dialogImageUrl = file.url;
            this.dialogVisible = true;
        },
        handleAvatarSuccess(res, file){
            this.imgUrl = file.response;
        },

        beforeAvatarUpload(file){
            const isJPG = file.type === 'image/jpeg';
            const isLt2M = file.size/1024/1024 < 2;
            if(!isJPG){
                this.$message.error("上传图片只能是JPG格式");
            }
            if(!isLt2M){
                this.message.error("上传图片大小不能超过2MB");
            }
            return isJPG && isLt2M;
        }
    }
})