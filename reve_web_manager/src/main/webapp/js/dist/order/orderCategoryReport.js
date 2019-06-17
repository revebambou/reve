new Vue({
    el:"#app",
    data(){
        return {
            tableData:[],
            pickerOptions: {
                shortcuts: [{
                    text: 'Latest Week',
                    onClick(picker) {
                        const end = new Date();
                        const start = new Date();
                        start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
                        picker.$emit('pick', [start, end]);
                    }
                }, {
                    text: 'Latest Month',
                    onClick(picker) {
                        const end = new Date();
                        const start = new Date();
                        start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                        picker.$emit('pick', [start, end]);
                    }
                }, {
                    text: 'Last 3 Months',
                    onClick(picker) {
                        const end = new Date();
                        const start = new Date();
                        start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                        picker.$emit('pick', [start, end]);
                    }
                }]
            },
            date: [],
            sumSaleVolume:0,
            sumTurnover:0,
            nameLeft:"Sale Volume",
            flag:false,
        };
    },
    created(){
    },
    methods:{

        getFromDatePicker(){
            this.getCategoryReport(this.date[0], this.date[1]);
        },

        getFromLast90Days(){
        this.getFromTimeAssigned(3600 * 1000 * 24 * 90);
        },

        getFromLast30Days(){
          this.getFromTimeAssigned(3600 * 1000 * 24 * 30);
        },

        getFromLast7Days(){
          this.getFromTimeAssigned(3600 * 1000 * 24 * 7);
        },

        getFromTimeAssigned(timePast){
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - timePast);
            this.getCategoryReport(start, end)
        },

        getCategoryReport(startDate, endDate){

            let start = startDate.Format("yyyy-MM-dd");
            let end = endDate.Format("yyyy-MM-dd");
            axios(`/order/getCategoryReport.do?dateBefore=${start}&dateAfter=${end}`).then(response=>{
                let map = response.date;
                this.tableData = response.data;
                this.sumSaleVolume = 0;
                this.sumTurnover = 0;

                for(let i=0; i<response.data.length; i++){
                    this.sumSaleVolume += response.data[i].num;
                    this.sumTurnover += response.data[i].money;
                }
                this.flag = true;
                this.drawEChars("Sale Volume", "Turnover");
            })
        },

        test(){
            alert("Please waiting patiently!!!")
        },

        drawEChars(nameLeft, nameRight){

            //Ready for the data:
            let lengendData = [];
            let saleVolumeData = [];
            let turnoverData = [];

            for(let i = 0; i< this.tableData.length; i++){
                lengendData.push(this.tableData[i].categoryName);
                saleVolumeData.push({name:this.tableData[i].categoryName, value:this.tableData[i].num});
                turnoverData.push({name:this.tableData[i].categoryName, value:this.tableData[i].money});
            }

            // 基于准备好的dom，初始化echarts实例
            var myChart = echarts.init(document.getElementById('main'));

            // 指定图表的配置项和数据
            var option = {
                title : {
                    text: '一级分类示意图',
                    subtext: '',
                    x:'center'
                },
                tooltip : {
                    trigger: 'item',
                    formatter: "{a} <br/>{b} : {c} ({d}%)"
                },
                legend: {
                    x : 'center',
                    y : 'bottom',
                    data:lengendData
                },
                toolbox: {
                    show : true,
                    feature : {
                        mark : {show: true},
                        dataView : {show: true, readOnly: false},
                        magicType : {
                            show: true,
                            type: ['pie', 'funnel']
                        },
                        restore : {show: true},
                        saveAsImage : {show: true}
                    }
                },
                calculable : true,
                series : [
                    {
                        name:nameLeft,
                        type:'pie',
                        radius : [20, 110],
                        center : ['25%', '50%'],
                        // roseType : 'radius',
                        label: {
                            normal: {
                                show: false
                            },
                            emphasis: {
                                show: true
                            }
                        },
                        lableLine: {
                            normal: {
                                show: false
                            },
                            emphasis: {
                                show: true
                            }
                        },
                        data:saleVolumeData
                    },
                    {
                        name:nameRight,
                        type:'pie',
                        radius : [30, 110],
                        center : ['75%', '50%'],
                        // roseType : 'area',
                        data:turnoverData
                    }
                ]
            };

            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);
        }

    }
});