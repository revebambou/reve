// 左侧菜单组件
Vue.component('sidebar-item', {
    props: {
        item: {
            type: Object,
            required: true
        }
    },
    template: `
        <div v-if="item.children">
            <template v-if="item.children.length == 0">
                <el-menu-item :index="item.path">
                <i class="icon" :class="item.icon"></i>
                <a :href="item.linkUrl"
                 target="right">{{item.title}}</a>
                </el-menu-item>
            </template>
            <template v-else :index="item.path">
                <div slot="title" class="el-submenu__title">
                    <i class="icon" :class="item.icon"></i>
                    {{item.title}}
                </div>

                <template v-for="child in item.children">
                    <sidebar-item
                    v-if="child.children&&child.children.length>0"
                    :item="child"
                    :key="child.path"/>
                    <el-menu-item v-else :key="child.path" :index="child.path" style='padding-left:58px'>
                    <a :href="child.linkUrl" target="right">{{child.title}}</a>
                    </el-menu-item>
                </template>
            </template>          
        </div>`
})
new Vue({
    el: '#app',
    // router,
    data() {
        return {
            visible: false,
            isCollapse: false,
            tabWidth: 180,
            test1: 1,
            intelval: null,
            winfo:'winfo',
            data:[],
            menuList:[],
            defaultActiveIndex: "2",
            defaultActive:'2-1-2',
            openeds: [],
            linkUrl:'all-item-list.html',
            menu:[],
        }
    },
    created() {
        this.queryForMenu();
        let linkUrl=localStorage.getItem('linkUrl')
        let activeIndex=localStorage.getItem('activeIndex')
        if(linkUrl!=='undefined'){
            this.linkUrl=linkUrl
            this.defaultActiveIndex=activeIndex
        }
        // 获取导航数据
        this.menuList=this.menu.data
        // 导航默认选择
        this.data=this.menu.data[1]
        let data=[]
        for(let i=0;i<this.data.children.length;i++){
            data.push(this.data.children[i].path)
        }
        this.openeds=data
    },
    methods: {

        queryForMenu(){
            axios.post(`/index/queryForMenu.do`).then(response=>{
                this.menu = response.data;
                console.log(response.data)
                console.log(this.menu)
            })
        },

        handleOpen(key, keyPath) {
            console.log(key, keyPath);
        },
        handleClose(key, keyPath) {
            console.log(key, keyPath);
        },
        // 选择顶部导航菜单，联动左侧菜单
        handleSelect(key) {
            localStorage.setItem('linkUrl',key.linkUrl)
            localStorage.setItem('activeIndex',key.path)
            var data=[]
            for(var i=0;i<key.children.length;i++){
                data.push(key.children[i].path)
                this.defaultActive=key.children[0].children[0].path
                this.linkUrl=key.children[0].linkUrl

            }
            this.openeds=data
            this.data=key
        },

        logout(){
            axios.post(`/logout`).then(response=>{
                alert("Exit successfully");
                location.href="login.html";
            })
        }
    }
})

window.onload = windowHeight; //页面载入完毕执行函数
function windowHeight() {
    var wd = 220;
    var iframepage=document.getElementById("iframepage")
    var asieHeight=document.getElementById("asieHeight")
    iframepage.style.width=document.documentElement.clientWidth-wd + 'px'
    asieHeight.style.height=document.documentElement.clientHeight + 'px'
}