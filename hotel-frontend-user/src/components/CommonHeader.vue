<template>
  <header>
    <div class="l-content">
      <i class="el-icon-s-unfold" v-if="show" @click="drawer=true" size="20%"></i>
      <span class="titles" v-if="show">Menu</span>
    </div>

    <!--------to be changed--------->
<!--    <div><img src="https://www.aman.com/themes/custom/aman/logo.svg" class="logo"/></div>-->
    <div><img src="../assets/images/ann.png" class="logo" style="transform:scale(1.35)"/></div>
<!--    <div>Annear</div>-->

    <!--homepage右上角的两个按钮-->
    <div class="r-content">
      <el-button type="text" @click="logOut" v-if="isInfo" id="logout">Logout</el-button>
      <img :src="avatar" @click="toInfo()" v-if="isLogin" id="avatar"/>
      <el-button type="text" @click="loginHandle" v-if="showLogin">Login</el-button>
      <el-button @click="toMap" v-if="!isMap">Map</el-button>
      <el-button @click="toSearch" v-if="showSearch">Search</el-button>
      <el-button @click="toHome" v-if="showHome">Home</el-button>
    </div>

    <!-- 登录弹窗 -->
    <!-- <el-dialog title="Login" :visible.sync="loginDialog" width="30%">
      <el-form ref="form" :model="form" label-width="80px">
        <el-form-item label="account"><el-input v-model="form.account"></el-input></el-form-item>
        <el-form-item label="password"><el-input v-model="form.password" type="password"></el-input></el-form-item>
        <el-form-item><el-button type="primary" @click="loginHandle" style="width:100%;">Login</el-button></el-form-item>
      </el-form>
    </el-dialog> -->

    <!-- 菜单抽屉 -->
    <el-drawer
        title="Menu"
        :visible.sync="drawer"
        direction="ltr" size="20px">
      <div style="padding:0 15px;">
        <!-- 第一级菜单 -->
        <el-tabs tab-position="left" @tab-click="menuClick">
          <el-tab-pane v-for="(item,index) in menu" :key="index" :name="item.url+'-'+index">
            <template slot="label">{{ item.name }}</template>
            <!-- 第二级菜单 -->
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-drawer>

  </header>
</template>

<script>
import cookie from "js-cookie";

export default {
  name: 'CommonHeader',
  data() {
    return {
      is404: false,
      avatar: 'https://ooad-1312953997.cos.ap-guangzhou.myqcloud.com/img/user-filling.png',
      loginDialog: false,
      isLogin: false,
      isMap: false,
      isInfo: false,
      drawer: false,
      showLogin: true,
      showSearch: true,
      showHome: false,
      show: true,
      form: {},
      menu: [
        {name: 'Home', url: '/'},
        {name: 'Search', url: '/search'},
        {name: 'Map', url: '/map'},
      ]
    }
  },
  methods: {
    //跳转Search
    toSearch() {
      this.$router.push('search')
    },
    //返回Home
    toHome() {
      this.$router.push('home')
    },
    toMap() {
      this.$router.push('map')
    },
    toInfo() {
      if (this.$route.name !== "userinfo") {
        this.$router.push('userinfo')
      }
    },
    // 登录
    loginHandle() {
      this.$router.push('login')
    },
    logOut() {
      cookie.remove('token');
      this.$message.success("Successfully Logout")
      this.$router.push('login');
    },
    //点击菜单
    menuClick(curMenu) {
      this.$router.push(curMenu.name.split('-')[0])
      this.drawer = false
    }
  },
  watch: {
    '$route': {
      handler: function () {
        this.is404 = (this.$route.name === '404');
        if (this.is404) {
          this.show = false;
          this.isLogin = false;
          this.showLogin = false;
          this.showHome = false;
          this.showSearch = false;
          return;
        }
        this.show = !(this.$route.name === 'login') && !(this.$route.name === 'register');
        this.isLogin = cookie.get('token');
        this.isMap = this.$route.name === 'map';
        this.isInfo = this.$route.name === 'userinfo';
        this.showLogin = !(this.$route.name === 'login') && !this.isLogin;
        this.showSearch = !(this.$route.name === 'search' || this.$route.name === 'login' || this.$route.name === 'register');
        this.showHome = (this.$route.name === 'search' || this.$route.name === 'login' || this.$route.name === 'register');
        if (this.isLogin) {
          this.$http({
            url: this.$http.adornUrl('/member/member/userinfo/avatar'),
            method: 'get',
            params: this.$http.adornParams({
              token: cookie.get('token')
            })
          }).then(data => {
            let resp = data.data;
            if (resp && resp.state === 200) {
              this.avatar = resp.data;
            } else {
              this.$message.error(resp.message);
            }
          }).catch(err => {
            this.$message.error("Network Error");
          })
        }
      },
      // 深度观察监听
      deep: true,
      immediate: true,
    }
  },
}
</script>

<style lang="scss" scoped>

#logout{
  margin-right: 10px;
}

#avatar {
  width: 40px;
  height: 40px;
  border-radius: 20px;
  margin-right: 10px;
  cursor: pointer;
}

header {
  display: flex;
  flex-direction: row;
  justify-content: space-between;

  .l-content {
    .titles {
      color: #333;
      display: inline-block;
      margin-left: 20px;
    }

    .el-icon-s-unfold {
      font-size: 22px;
      cursor: pointer;
    }

    .el-icon-s-unfold:hover {
      opacity: 0.8;
    }
  }

  .logo {
    width: 120px;
  }

  ::v-deep.el-tabs--left .el-tabs__item.is-left {
    font-size: 16px;
    color: #888;
    text-align: left;
  }

  ::v-deep.right-icon {
    float: right;
    vertical-align: middle;
    margin-top: 12px;
  }

  ::v-deep.el-tabs--left .el-tabs__item.is-left:hover {
    color: #333;
  }

  ::v-deep.el-drawer {
    width: 200px !important;
    background-color: #f3eee7;
  }

  .r-content {
    .el-dropdown-link {
      img {
        width: 40px;
        height: 40px;
        border-radius: 50%;
      }
    }
  }
}
</style>
