<template>
  <div class="page-main">
    <el-row class="card-search" :gutter="20" style="width:900px;margin:14px auto;">
      <el-col :span="22" style="background:#fff; padding: 0 0;height: 600px">
        <el-col class="login-title">Login In</el-col>
        <el-col class="privacy-info">We are committed to protecting the privacy and security of your personal
          information. More information
          about how we use and protect your data is provided in our Privacy Notice.
        </el-col>
        <el-col :span="24">
          <el-divider></el-divider>
        </el-col>
        <component :is="curType" style="height: 360px"></component>
        <div class="login-type">
          <a href="" :class="[curType==='LoginByPassword'?'active-type':'inactive-type']"
             @click.prevent="curType='LoginByPassword'">Password</a>
          &ensp;/&ensp;
          <a href="" :class="[curType==='LoginByVerificationCode'?'active-type':'inactive-type']"
             @click.prevent="curType='LoginByVerificationCode'">Phone
            Verification Code</a>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<style lang="scss" scoped>
.page-main {
  background: #f3eee7;
}

.privacy-info {
  width: 500px;
  margin-top: -10px;
  margin-left: 150px;
  text-align: center;
  font-size: 16px;
  font-family: "Times New Roman", serif;
}

.login-title {
  font-size: 34px;
  line-height: 50px;
  margin-bottom: 20px;
  text-align: center;
  font-family: "Times New Roman", serif;
}

.login-type {
  margin-left: 300px;
  text-align: center;
  font-family: "Times New Roman", serif;
  display: inline-flex;
  margin-top: 20px;
}

.active-type {
  font-weight: 800;
  font-size: 16px;
}

.inactive-type {
  font-weight: 400;
  font-size: 16px;
}
</style>

<script>
import LoginByPassword from "../../components/login/LoginByPassword";
import LoginByVerificationCode from "../../components/login/LoginByVerificationCode";

export default {
  name: "login",
  components: {LoginByPassword, LoginByVerificationCode},
  data() {
    return {
      curType: 'LoginByPassword',
    }
  }
}


</script>