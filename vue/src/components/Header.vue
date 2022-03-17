<template>
  <div style="height: 50px; line-height: 50px; border-bottom: 1px solid #ccc; display: flex">
    <div style="width: 200px; padding-left: 30px; font-weight: bold; color: dodgerblue">后台管理</div>
    <div style="flex: 1;"></div>
    <div style="width: 200px;">
      <el-dropdown @command="handleCommand">
        <span class="el-dropdown-link">
           {{ user.nickName }}
          <i class="el-icon-arrow-down el-icon--right"></i>
        </span>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item command="info">个人信息</el-dropdown-item>
          <el-dropdown-item command="logout">退出系统</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>

  </div>
</template>

<script>
export default {
  name: "Header",
  props: ['user'],
  data(){
    return{
      user:{}
    }
  },
  created() {
    let userStr = sessionStorage.getItem("user") || "{}"
    this.user = JSON.parse(userStr)
  },
  methods: {
    handleCommand(command) {
      this.$message('已退出系统'+ command);
      if(command == 'logout'){
        this.$router.push("/login");
      }
    }
  }
}
</script>

<style scoped>


</style>