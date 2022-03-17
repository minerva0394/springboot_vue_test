<template>
  <div>
    <div id="login" style="width: 100%; height: 100vh; overflow: hidden">
      <div style="width: 400px; margin: 100px auto">
        <div style="color: #ffffff;font-size: 30px;text-align: center; padding: 30px">欢迎登录</div>
        <el-form ref="form" :model="form" size="normal" :rules="rules">
          <el-form-item prop="username">
            <el-input prefix-icon="el-icon-user" v-model="form.username"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input prefix-icon="el-icon-lock" v-model="form.password" show-password></el-input>
          </el-form-item>
          <el-button style="width: 100%" type="primary" @click="login">登录</el-button>
        </el-form>
      </div>
    </div>

  </div>

</template>

<script>
import request from "@/utils/request";

export default {
  name: "Login",
  data() {
    return {
      form: {},
      rules: {
        username: [
          {required: true, message: "请输入用户名", trigger: 'blur'}
        ],
        password: [
          {required: true, message: "请输入密码", trigger: 'blur'}
        ],

      }
    }
  },
  methods: {
    login() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          request.post("/user/login", this.form).then(res => {
            console.log(res)
            if (res.code === '0') {
              this.$message({
                type: "success",
                message: "登录成功!"
              })
              sessionStorage.setItem("user", JSON.stringify(res.data))  //缓存用户信息
              this.$router.push("/")  // 登陆成功后进行页面跳转
            } else {
              this.$message({
                type: "error",
                message: res.msg
              })
            }
          })
        }
      })
    }
  }

}
</script>

<style scoped>
#login {
  width: 100%;
  height: 100%;
  background-image: url("../assets/02.jpg");
  background-size: cover;
  background-position: center;
  position: relative;
  /*filter:alpha(Opacity=95);*/
  /*-moz-opacity:0.95;*/
  /*opacity: 0.95;*/
}

</style>