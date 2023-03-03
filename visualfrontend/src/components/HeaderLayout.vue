<template>
  <el-menu
    :default-active="activeIndex"
    active-text-color="#ffd04b"
    background-color="#1E90FF"
    class="el-menu-demo"
    mode="horizontal"
    style="margin: 0 auto; width: 100%"
    text-color="#fff"
  >

    <el-menu-item v-for="item in menu" :key="item.path" :index="item.path" @click="clickMenu(item)">
      <span slot="title">{{ item.label }}</span>
    </el-menu-item>

  </el-menu>

</template>
<script>
export default {
  data () {
    return {
      activeIndex: '1',
      menu: [
        {
          path: '/index',
          name: 'index',
          label: '日志收集概况',
        },
        {
          path: '/abnormalScanner',
          name: 'abnormalScanner',
          label: '异常日志查询',
        },
        {
          path: '/contextQuerier',
          name: 'contextQuerier',
          label: '上下文关联查',
        },
        {
          path: '/consumeChecker',
          name: 'consumeChecker',
          label: '消费处理情况',
        }
      ],
    }
  },
  methods: {

    clickMenu (item) {
      this.$router.push({ name: item.name })
    }
  }
}
</script>
