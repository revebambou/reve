new Vue({
    el: '#app',
    data(){
        return {
            tableData: [],
            currentPage: 1,
            total: 10,
            size: 10,
            searchMap: {},
            pojo: {},
            formVisible: false,
            imageUrl: '',
            templateId:0,
        }
    },
    created(){
        let search = window.location.search;
        let index = search.lastIndexOf("=")+1;
        let id = search.substring(index);
        this.templateId = id;
        // alert(id+" "+this.size+" "+this.currentPage)
        this.findByTemplateId();
    },
    methods:{

        findByTemplateId(){
            axios.get(`/spec/findByTemplateId.do?size=${this.size}&page=${this.currentPage}&id=${this.templateId}`).then(response=>{
                this.tableData = response.data.rows;
                this.total = response.data.total;
            })
        },

        fetchData (){
            axios.post(`/spec/findPage.do?page=${this.currentPage}&size=${this.size}`,this.searchMap).then(response => {
                this.tableData = response.data.rows;
                this.total = response.data.total;
            });
        },
        save (){
            // this.pojo.image= this.imageUrl; //如页面有图片上传功能放开注释
            axios.post(`/spec/${this.pojo.id==null?'add':'update'}.do`,this.pojo).then(response => {
                this.findByTemplateId (); //刷新列表
                this.formVisible = false ;//关闭窗口
            });
        },
        edit (id){
            this.formVisible = true // 打开窗口
            // 调用查询
            axios.get(`/spec/findById.do?id=${id}`).then(response => {
                this.pojo = response.data;
                // this.imageUrl=this.pojo.image //显示图片  如页面有图片上传功能放开注释
            })
        },
        dele (id){
            this.$confirm('确定要删除此记录吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then( () => {
                axios.get(`/spec/delete.do?id=${id}`).then(response => {
                    this.findByTemplateId (); //刷新列表
                })
            })
        },

        jumpToTemplate(){
            location.href="template.html";
        },
        /* ****图片上传相关代码  如页面有图片上传功能放开注释 ****
        handleAvatarSuccess(res, file) {
            this.imageUrl = file.response;
        },
        beforeAvatarUpload(file) {
            const isJPG = file.type === 'image/jpeg';
            const isLt2M = file.size / 1024 / 1024 < 2;

            if (!isJPG) {
                this.$message.error('上传头像图片只能是 JPG 格式!');
            }
            if (!isLt2M) {
                this.$message.error('上传头像图片大小不能超过 2MB!');
            }
            return isJPG && isLt2M;
        }*/
    }
})