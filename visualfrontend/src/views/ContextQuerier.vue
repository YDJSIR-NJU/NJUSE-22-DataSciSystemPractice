<template>
  <div class="home" style="height:100%">
    <el-header>
      <header-layout></header-layout>
    </el-header>
    <el-container style="height:100%">
      <el-main style="text-align: center">
        <h1>上下文关联查</h1>

        <div style="margin: 1rem">
          <h3>根据时间段获得全部或特定服务名称/容器的日志JSON</h3>
          <p>
            请尽可能压缩时间段，只看特定服务名称/容器的日志。获取的结果是没有分页的，太多的结果会导致响应缓慢。
            一次最大下载不超过5MB或10000条记录的json。
          </p>
          <el-row style="width: 90%; margin: 0 auto">
            <el-col :span="5">
              <h3>
                最早时间戳 &nbsp;
                <el-tag style="width: auto" type="success">
                  {{ minTime }}
                </el-tag> &nbsp;
              </h3>
            </el-col>
            <el-col :span="6" style="text-align: right">
              <h3>
                <el-date-picker
                  v-model="minTime"
                  disabled=""
                  placeholder="选择日期时间"
                  type="datetime"
                >
                </el-date-picker>
                <el-tag style="width: 3rem">
                  {{ minTime % 1000 }}
                </el-tag>
              </h3>
            </el-col>
            <el-col :span="2">
              <i class="el-icon-right" style="margin: 1rem"></i>
            </el-col>
            <el-col :span="6" style="text-align: left">
              <h3>
                <el-date-picker
                  v-model="maxTime"
                  disabled=""
                  placeholder="选择日期时间"
                  type="datetime"
                >
                </el-date-picker>
                <el-tag style="width: 3rem">
                  {{ maxTime % 1000 }}
                </el-tag>
              </h3>
            </el-col>
            <el-col :span="5">
              <h3>
                最晚时间戳 &nbsp;
                <el-tag style="width: auto" type="success">
                  {{ maxTime }}
                </el-tag> &nbsp;
              </h3>
            </el-col>
          </el-row>

          <el-row style="width: 90%; margin: 0 auto">
            <el-col :span="5">
              <el-input-number
                v-model="startTime"
                label="开始时间戳"
                style="width: auto"
              >
              </el-input-number>
            </el-col>
            <el-col :span="6" style="text-align: right">
              <el-date-picker
                v-model="startTime"
                disabled=""
                placeholder="选择日期时间"
                type="datetime"
              >
              </el-date-picker>
              <el-tag style="width: 3rem">
                {{ startTime % 1000 }}
              </el-tag>
            </el-col>
            <el-col :span="2">
              <i class="el-icon-right" style="margin: 1rem"></i>
            </el-col>
            <el-col :span="6" style="text-align: left">
              <el-date-picker
                v-model="endTime"
                disabled=""
                placeholder="选择日期时间"
                type="datetime"
              >
              </el-date-picker>
              <el-tag style="width: 3rem">
                {{ endTime % 1000 }}
              </el-tag>
            </el-col>
            <el-col :span="5">
              <el-input-number
                v-model="endTime"
                label="开始时间戳"
                style="width: auto"
              >
              </el-input-number>
            </el-col>
          </el-row>

          <el-row style="width: 90%; margin: 0 auto">
            <el-col :span="16">
              <el-cascader-panel
                v-model="levelFilterValue"
                :options="filterLevelOptions"
                filterable
                style="width: 100%; margin: 0.5rem"
              >
              </el-cascader-panel>
            </el-col>
            <el-col :span="8">
              <el-button type="success" @click="queryByRange">
                获取全部日志列表JSON
              </el-button>
              <el-button type="primary" @click="queryByRangeFilter">
                获取选中标准日志JSON
              </el-button>
            </el-col>
          </el-row>


        </div>
        <div style="margin: 1rem">
          <el-row style="width: 90%; height: auto; text-align: center; margin: 0 auto;">
            <h3>根据UUID查询日志原文全文</h3>
            <el-col :span="10">
              <el-input v-model="uuid" placeholder="请输入UUID"></el-input>
            </el-col>
            <el-col :span="2">
              <el-button type="primary" @click="selectByUUid(uuid)">查询</el-button>
            </el-col>
            <el-col :span="12" style="text-align: right">
              <el-date-picker
                v-model="uuidTimestamp"
                disabled=""
                placeholder="选择日期时间"
                type="datetime"
              >
              </el-date-picker>
              <el-tag>
                {{ uuidTimestamp % 1000 }}
              </el-tag>
            </el-col>
          </el-row>
          <!--          <pre v-highlightjs="recordContentStr"><code class="javascript"></code></pre>-->
          <div style="margin: 0.5rem"></div>
          <json-viewer
            :expand-depth=5
            :value="recordContent"
            boxed
            copyable
            sort
            style="text-align: left; width: 90%; margin: 0 auto;"
          ></json-viewer>
        </div>

      </el-main>
    </el-container>

  </div>
</template>

<script>

import HeaderLayout from '../components/HeaderLayout.vue'
import { getByUuid, rangeFilterCount } from '@/api/query'
import { getEnumName, getTimeRange } from '@/api/list'
import { rangeFilter } from '../api/query'

export default {
  name: 'home',
  components: {
    HeaderLayout
  },
  data () {
    return {
      mychart: {},
      uuid: '',
      recordContent: {},
      recordContentStr: '',
      uuidTimestamp: 0,
      minTime: 0,
      maxTime: 0,
      startTime: 0,
      endTime: 0,
      filterLevelOptions: [],
      levelFilterValue: []
    }
  },
  methods: {
    selectByUUid (uuid) {
      const that = this
      getByUuid(uuid).then(res0 => {
        that.recordContent = res0.data
        // console.log(that.recordContent)
        that.recordContentStr = JSON.stringify(that.recordContent, null, 4)
        console.log(that.recordContentStr)
        that.uuidTimestamp = res0.data.timestamp
      })
    },
    queryByRange () {
      rangeFilterCount(this.startTime, this.endTime, 'all', 'all').then(res0 => {
        console.log(res0.data)
        if (res0.data > 10000) {
          this.$message({
            message: '该标准下共有' + res0.data + '条日志，数目超10000无法导出',
            type: 'warning',
            center: true
          })
        } else {
          rangeFilter(this.startTime, this.endTime, 'all', 'all').then(res0 => {
            const realData = res0.data
            // console.log(realData)
            const tmp = document.createElement('a')
            tmp.setAttribute('href', 'data:text/json;charset=utf-8,\uFEFF' +
              JSON.stringify(realData, null, 4))
            tmp.download = this.startTime + '-' + this.endTime + '-' + new Date().getTime() + '.json'
            tmp.click()
          })
        }
      })
    },
    queryByRangeFilter () {
      if (this.levelFilterValue.length < 2) {
        this.$message({
          message: '请选择筛选标准和值！',
          type: 'warning',
          center: true
        })
      } else {
        rangeFilterCount(this.startTime, this.endTime, this.levelFilterValue[0], this.levelFilterValue[1]).then(res0 => {
          if (res0.data > 10000) {
            console.log(res0.data)
            this.$message({
              message: '该标准下共有' + res0.data + '条日志，数目超10000无法导出',
              type: 'warning',
              center: true
            })
          } else {
            this.$message({
              message: '正在下载' + res0.data + '条日志对应的Json',
              type: 'success',
              center: true
            })
            rangeFilter(this.startTime, this.endTime, this.levelFilterValue[0], this.levelFilterValue[1]).then(res0 => {
              const realData = res0.data
              // console.log(realData)
              const tmp = document.createElement('a')
              tmp.setAttribute('href', 'data:text/json;charset=utf-8,\uFEFF' +
                JSON.stringify(realData, null, 4))
              tmp.download = this.startTime + '-' + this.endTime + this.levelFilterValue[0] + '-' +
                this.levelFilterValue[1] + '-' + new Date().getTime() + '.json'
              tmp.click()
            })
          }
        })
      }

    },
    handleStartChange (value) {
      console.log(value)
    }
  }
  ,
  mounted () {
    const that = this
    getTimeRange().then(res0 => {
      that.minTime = res0.data.minTime
      that.maxTime = res0.data.maxTime
      that.startTime = res0.data.minTime
      that.endTime = res0.data.maxTime
    })
    getEnumName('appName').then(res0 => {
      let appNameChildren = []
      for (const item of res0.data) {
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
    })
    getEnumName('containerId').then(res0 => {
      let containerIdChildren = []
      for (const item of res0.data) {
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

.text-wrapper {
  white-space: pre-wrap;
}

</style>

