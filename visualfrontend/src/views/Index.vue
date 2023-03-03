<template>
  <div class="home" style="height:100%">
    <el-header>
      <header-layout></header-layout>
    </el-header>
    <el-container style="height:100%">
      <el-main style="text-align: center">
        <h1>日志收集概况</h1>
        <el-row style="width: 90%; height: auto; text-align: center; margin: 0 auto">

        </el-row>
        <el-row style="width: 90%; height: auto; text-align: center; margin: 0 auto">
          <el-col :span="12">
            <h3>已收集日志时间戳范围
              <el-tag type="success">
                {{ minTime }}
              </el-tag>
              <i class="el-icon-right" style="margin-left: 0.5rem; margin-right: 0.5rem"></i>
              <el-tag type="success">
                {{ maxTime }}
              </el-tag>
            </h3>
            <div>

              &nbsp;
              <el-date-picker
                v-model="minTime"
                disabled=""
                placeholder="选择日期时间"
                type="datetime"
              >
              </el-date-picker>
              <el-tag>
                {{ minTime % 1000 }}
              </el-tag>
              <i class="el-icon-right" style="margin-left: 0.5rem; margin-right: 0.5rem"></i>
              <el-date-picker
                v-model="maxTime"
                disabled=""
                placeholder="选择日期时间"
                type="datetime"
              >
              </el-date-picker>
              <el-tag>
                {{ maxTime % 1000 }}
              </el-tag>
            </div>
            <el-row>
              <el-col :span="8">
                <h3 style="text-align: center">服务名称列表</h3>
                <el-card style="margin: 0.5rem; text-align: left">
                  <el-tag
                    v-for="item in appNames"
                    :key="item"
                    style="margin: 0.3rem; "
                    type="primary"
                  >
                    {{ item }}
                  </el-tag>
                </el-card>
              </el-col>
              <el-col :span="8">
                <h3 style="text-align: center">容器名列表</h3>
                <el-card style="margin: 0.5rem; text-align: left">
                <span
                  v-for="item in containerNames"
                  :key="item"
                  style="margin: 0.3rem; "
                  type="primary"
                >
                  {{ item }}<br>
                </span>
                </el-card>
              </el-col>
              <el-col :span="8">
                <h3 style="text-align: center">日志级别列表</h3>
                <el-card style="margin: 0.5rem; text-align: left">
                  <el-tag
                    v-for="item in levels"
                    :key="item"
                    effect="dark"
                    style="margin: 0.3rem; "
                    type="primary"
                  >
                    {{ item }}
                  </el-tag>
                </el-card>
              </el-col>
            </el-row>
          </el-col>
          <el-col :span="12">
            <div style="margin: 0.2rem"></div>
            <div id="container1" style="height: 350px; width: 700px; margin: 0 auto;  text-align: center"></div>
          </el-col>
        </el-row>
        <el-row style="width: 90%; height: auto; text-align: center; margin: 0 auto">
          <el-col :span="12">
            <h3 style="text-align: center; margin: 0.5rem">按服务名称/容器ID查看日志级别比例</h3>
            <el-cascader
              v-model="levelFilterValue"
              :options="filterLevelOptions"
              filterable
              style="width: 100%; margin: 0.5rem"
              @change="handleLevelFilterChange"
            >
            </el-cascader>
            <div id="container3" style="height: 400px; width: 700px; margin: 0 auto;  text-align: center"></div>
          </el-col>
          <el-col :span="12">
            <div style="margin: 0.5rem"></div>
            <div id="container2" style="height: 500px; width: 700px; margin: 0 auto;  text-align: center"></div>
          </el-col>
        </el-row>
      </el-main>
    </el-container>

  </div>
</template>

<script>

import HeaderLayout from '../components/HeaderLayout.vue'
import * as echarts from 'echarts'
import { filterSum, levelFilterSum } from '@/api/stat'
import { getTimeRange } from '@/api/list'

export default {
  name: 'home',
  components: {
    HeaderLayout
  },
  data () {
    return {
      mychart: {},
      appNameRatioMap: [],
      levelRatioMap: [],
      containerIdRatioMap: [],
      containerNameRatioMap: [],
      levelFilterRatioMap: [],
      appNames: [],
      levels: [],
      containerIds: [],
      containerNames: [],
      minTime: 0,
      maxTime: 0,
      startTime: 0,
      endTime: 0,
      filterLevelOptions: [],
      levelFilterValue: []
    }
  },
  methods: {
    drawPic1 () {
      const that = this
      const dom = document.getElementById('container1')
      that.mychart = echarts.init(dom)
      let option

      option = {
        title: {
          text: '按服务（左）与按级别日志（右）比例饼图',
          subtext: '鼠标移到具体色块上会显示比例，点击右上角第一个按钮可以查看数据表',
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} 条 ({d}%)'
        },
        legend: {
          left: 'center',
          top: 'bottom',
        },
        toolbox: {
          show: true,
          feature: {
            mark: { show: true },
            dataView: {
              show: true,
              readOnly: false
            },
            restore: { show: true },
            saveAsImage: { show: true }
          }
        },
        series: [
          {
            name: '按服务占比',
            type: 'pie',
            radius: [20, 50],
            center: ['25%', '50%'],
            roseType: 'area',
            label: {
              show: true
            },
            emphasis: {
              label: {
                show: true
              }
            },
            itemStyle: {
              borderRadius: 5
            },
            data: that.appNameRatioMap
          },
          {
            name: '按级别占比',
            type: 'pie',
            radius: [20, 50],
            center: ['75%', '50%'],
            roseType: 'area',
            label: {
              show: true
            },
            emphasis: {
              label: {
                show: true
              }
            },
            itemStyle: {
              borderRadius: 5
            },
            data: that.levelRatioMap
          }
        ]
      }

      if (option && typeof option === 'object') {
        that.mychart.setOption(option)
      }
    },
    drawPic2 () {
      const that = this
      const dom = document.getElementById('container2')
      that.mychart = echarts.init(dom)
      let option

      option = {
        title: {
          text: '按容器名（左）和容器ID（右）日志比例饼图',
          subtext: '鼠标移到具体色块上会显示比例，点击右上角第一个按钮可以查看数据表',
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} 条 ({d}%)'
        },
        legend: {
          left: 'center',
          top: 'bottom',
        },
        toolbox: {
          show: true,
          feature: {
            mark: { show: true },
            dataView: {
              show: true,
              readOnly: false
            },
            restore: { show: true },
            saveAsImage: { show: true }
          }
        },
        series: [
          {
            name: '按容器名占比',
            type: 'pie',
            radius: [20, 50],
            center: ['25%', '25%'],
            roseType: 'area',
            label: {
              show: true
            },
            emphasis: {
              label: {
                show: true
              }
            },
            itemStyle: {
              borderRadius: 5
            },
            data: that.containerNameRatioMap
          },
          {
            name: '按容器ID占比',
            type: 'pie',
            radius: [20, 50],
            center: ['75%', '25%'],
            roseType: 'area',
            label: {
              show: true
            },
            emphasis: {
              label: {
                show: true
              }
            },
            itemStyle: {
              borderRadius: 5
            },
            data: that.containerIdRatioMap
          }
        ]
      }

      if (option && typeof option === 'object') {
        that.mychart.setOption(option)
      }
    },
    drawPic3 () {
      const that = this
      const dom = document.getElementById('container3')
      if (dom != null) {
        that.mychart = echarts.init(dom)
      }
      let option

      option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} 条 ({d}%)'
        },
        legend: {
          left: 'center',
          top: 'bottom',
        },
        toolbox: {
          show: true,
          feature: {
            mark: { show: true },
            dataView: {
              show: true,
              readOnly: false
            },
            restore: { show: true },
            saveAsImage: { show: true }
          }
        },
        series: [
          {
            name: '按容器ID占比',
            type: 'pie',
            radius: [20, 50],
            center: ['50%', '25%'],
            roseType: 'area',
            label: {
              show: true
            },
            emphasis: {
              label: {
                show: true
              }
            },
            itemStyle: {
              borderRadius: 5
            },
            data: that.levelFilterRatioMap
          }
        ]
      }

      if (option && typeof option === 'object') {
        that.mychart.setOption(option)
      }
    },
    handleLevelFilterChange () {
      const column = this.levelFilterValue[0]
      const name = this.levelFilterValue[1]
      console.log(this.levelFilterValue)
      const that = this
      levelFilterSum(column, name).then(res0 => {
        const realData = res0.data
        console.log(realData)
        that.levelFilterRatioMap = []
        for (const realDataKey of realData) {
          // console.log(realDataKey)
          that.levelFilterRatioMap.push({
            value: realDataKey.count,
            name: realDataKey.name
          })
        }
        this.drawPic3()
      })
    },
    drawTotal () {
      const that = this
      filterSum('appName').then(res0 => {
        // console.log(res0.data)
        const realData = res0.data
        for (const realDataKey of realData) {
          // console.log(realDataKey)
          that.appNameRatioMap.push({
            value: realDataKey.count,
            name: realDataKey.name
          })
          that.appNames.push(realDataKey.name)
        }
        let appNameChildren = []
        for (const item of that.appNames) {
          appNameChildren.push({
            value: item,
            label: item
          })
        }
        that.filterLevelOptions.push({
          value: 'appName',
          label: '服务名称',
          children: appNameChildren
        })
        // that.appNameRatioMap.push({value: res0.td_3m_bal, name: '定期3个月'})
        filterSum('level').then(res0 => {
          const realData = res0.data
          for (const realDataKey of realData) {
            // console.log(realDataKey)
            that.levelRatioMap.push({
              value: realDataKey.count,
              name: realDataKey.name
            })
            that.levels.push(realDataKey.name)
          }
          this.drawPic1()
        })
      })
      filterSum('containerName').then(res0 => {
        // console.log(res0.data)
        const realData = res0.data
        for (const realDataKey of realData) {
          // console.log(realDataKey)
          that.containerNameRatioMap.push({
            value: realDataKey.count,
            name: realDataKey.name
          })
          that.containerNames.push(realDataKey.name)
        }
        // 这个参好像传过去了MyBatis也会把它转义掉，然后是拿不到结果的。罪魁祸首就是 / 符号。使用DataGrip倒是可以获取
        // let containerNameChildren = []
        // for (const item of that.containerNames){
        //   containerNameChildren.push({value: item, label: item})
        // }
        // that.filterLevelOptions.push({value: 'containerName', label: '容器名称', children: containerNameChildren})
        // that.appNameRatioMap.push({value: res0.td_3m_bal, name: '定期3个月'})
        filterSum('containerId').then(res0 => {
          const realData = res0.data
          for (const realDataKey of realData) {
            // console.log(realDataKey)
            that.containerIdRatioMap.push({
              value: realDataKey.count,
              name: realDataKey.name
            })
            that.containerIds.push(realDataKey.name)
          }
          let containerIdChildren = []
          for (const item of that.containerIds) {
            containerIdChildren.push({
              value: item,
              label: item
            })
          }
          that.filterLevelOptions.push({
            value: 'containerId',
            label: '容器ID',
            children: containerIdChildren
          })
          this.drawPic2()
        })
      })
    }
  }
  ,
  mounted () {
    const that = this
    this.drawTotal()
    getTimeRange().then(res0 => {
      that.minTime = res0.data.minTime
      that.maxTime = res0.data.maxTime
    })
  }
}


</script>

<style>

.el-header {
  background-color: #B3C0D1;
  color: #333;
  text-align: center;
  line-height: 60px;
  margin-top: 0%;
  padding: 0;
  width: 100%;
}

.el-aside {
  background-color: #D3DCE6;
  color: #333;
  text-align: center;
  line-height: 200px;
}

.el-main {
  background-color: #E9EEF3;
  color: #333;
  text-align: center;
}

body > .el-container {
  margin-bottom: 40px;
}

.el-container:nth-child(5) .el-aside,
.el-container:nth-child(6) .el-aside {
  line-height: 260px;
}

.el-container:nth-child(7) .el-aside {
  line-height: 320px;
}
</style>

