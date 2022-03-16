<template>
  <div style="width: 100%; height: 100vh; background-color: #39a554;overflow: hidden">
    <div style="width: 400px; margin: 100px auto">
      <div style="color: #ffffff;font-size: 30px;text-align: center; padding: 30px">欢迎登陆</div>
      <el-form ref="form" :model="form" size="normal">
        <el-form-item>
          <el-input prefix-icon="el-icon-user" v-model="form.username"></el-input>
        </el-form-item>
      </el-form>

      <el-form ref="form" :model="form" size="normal">
        <el-form-item>
          <el-input prefix-icon="el-icon-lock" v-model="form.password" show-password></el-input>
        </el-form-item>
      </el-form>

      <el-button style="width: 100%" type="primary" @click="register">登录</el-button>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Register",
  data() {
    return {
      form: {}
    }
  },
  methods: {
    register() {
      if(this.form.password !== this.form.confirm){
        this.$message({
          type:"error",
          message:"两次密码不一致"
        })
      }


      request.post("/user/register", this.form).then(res =>{
        console.log(res)
        if (res.code === '0') {
          this.$message({
            type: "success",
            message: "注册成功!"
          })
          this.$router.push("/login")  // 登陆成功后进行页面跳转
        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
      })
    }
  }

}
</script>

<style scoped>

</style>