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
            shashinVisible: false,
            title:"Update",
            ids:[],
            multipleSelection:[],
            logisticsCompany:"reve"
        }
    },
    created(){
        this.fetchData();
    },
    methods:{
        fetchData (){
            axios.post(`/orderItem/findPage.do?page=${this.currentPage}&size=${this.size}`,this.searchMap).then(response => {
                this.tableData = response.data.rows;
                this.total = response.data.total;
                for(let i=0; i<response.data.rows.length; i++){
                    this.ids.push(response.data.rows[i].id)
                }
            });
        },
        save (){
            // this.pojo.image= this.imageUrl; //如页面有图片上传功能放开注释
            axios.post(`/orderItem/${this.pojo.id==null?'add':'update'}.do`,this.pojo).then(response => {
                this.fetchData (); //刷新列表
                this.formVisible = false ;//关闭窗口
            });
        },
        edit (id){
            this.formVisible = true; // 打开窗口
            // 调用查询
            axios.get(`/orderItem/findById.do?id=${id}`).then(response => {
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
                axios.get(`/orderItem/delete.do?id=${id}`).then(response => {
                    this.fetchData (); //刷新列表
                })
            })
        },
        deliverGoods(id){
            alert(id)
        },

        batchDeliverGoods(){
            alert(this.ids)
        },

        handleSelectionChange(val) {
            console.log(this.ids)
            this.ids = [];
            console.log(val)

                this.multipleSelection = val;
                for(let i = 0; i< val.length; i++){
                    // alert(this.multipleSelection[i].id)
                    this.ids.push(this.multipleSelection[i].id)
                }
                console.log(this.ids)// __ob__: Observer is displayed but not show when passing values
                // alert(this.ids)
        },

        toggleShashin(){
            if(this.shashinVisible) {
                this.shashinVisible = false;
            }else {
                this.shashinVisible = true;
            }
        },
    }
})