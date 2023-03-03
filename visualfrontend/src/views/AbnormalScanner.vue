<template>
  <div class="home" style="height:100%">
    <el-header>
      <header-layout></header-layout>
    </el-header>
    <el-container style="height:100%">
      <el-main style="text-align: center">
        <h1>异常日志查询</h1>
        <div style="margin: 1rem"></div>
        <iframe
          onload="this.style.height= (window.innerHeight * 0.8) +'px';"
          src="http://10.168.0.23:5601/app/kibana#/discover"
          style="width: 100%; border: hidden"
        ></iframe>
      </el-main>
    </el-container>

  </div>
</template>

<script>

import HeaderLayout from '../components/HeaderLayout.vue'

export default {
  name: 'home',
  components: {
    HeaderLayout
  },
  data () {
    return {}
  },
  methods: {},
  mounted () {
    const that = this
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

