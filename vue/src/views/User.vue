<template>
  <div style="padding: 10px">
    <!--    功能区-->
    <div style="margin: 10px 0">
      <el-button type="primary" @click="add">新增</el-button>
      <el-button type="primary">导入</el-button>
      <el-button type="primary">导出</el-button>
    </div>

    <!--    搜素区-->
    <div style="margin: 10px 0">
      <el-input v-model="search" placeholder="请输入昵称或者用户名" style="width: 20%" clearable></el-input>
      <el-button type="primary" style="margin-left: 5px" @click="load">查询</el-button>
    </div>
    <el-table
        :data="tableData"
        border
        stripe
        style="width: 100%;">
      <el-table-column
          prop="id"
          label="ID"
          sortable
      >
      </el-table-column>
      <el-table-column
          prop="username"
          label="用户名"
      >
      </el-table-column>
      <el-table-column
          prop="nickName"
          label="昵称">
      </el-table-column>
      <el-table-column
          prop="age"
          label="年龄">
      </el-table-column>
      <el-table-column
          prop="sex"
          label="性别">
      </el-table-column>
      <el-table-column
          prop="address"
          label="地址">
      </el-table-column>

      <!--      功能区-->
      <el-table-column label="角色列表" width="300">
        <template #default="scope">
          <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
          <el-popconfirm title="确定删除吗？" @confirm="handleDelete(scope.row.id)">
            <template #reference>
              <el-button size="mini" type="danger"
                         style="margin-left: 10px">删除
              </el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <!--    分页-->
    <div style="margin: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>

      <el-dialog title="新增用户" :visible.sync="dialogVisible" width="30%" ref="newUser">
        <el-form :model="form" label-width="120px" :rules="rules">
          <el-form-item label="用户名:" prop="username">
            <el-input v-model="form.username" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="昵称:" prop="nickName">
            <el-input v-model="form.nickName" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="年龄:" prop="age">
            <el-input v-model.number="form.age" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="性别:" prop="sex">
            <el-radio v-model="form.sex" label="1" style="width: 80%">男</el-radio>
            <el-radio v-model="form.sex" label="2" style="width: 80%">女</el-radio>
            <el-radio v-model="form.sex" label="3" style="width: 80%">未知</el-radio>
          </el-form-item>
          <el-form-item label="地址:">
            <el-input type="textarea" v-model="form.address" style="width: 80%"></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="save">确 定</el-button>
          </span>
      </el-dialog>

    </div>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: 'HomeView',
  components: {},
  data() {
    var checkAge = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('年龄不能为空'));
      }
      setTimeout(() => {
        if (!Number.isInteger(value)) {
          callback(new Error('请输入数字值'));
        } else {
          if (value > 100 || value < 18) {
            callback(new Error('必须在18~100岁之间'));
          } else {
            callback();
          }
        }
      }, 1000);
    };
    return {
      rules: {
        username: [{
          required: true,
          message: "请输入用户名",
          trigger: 'blur'
        }, {
          min: 3,
          max: 10,
          message: "请输入3~10个字符",
          trigger: 'blur'
        }],
        nickName: [{
          required: true,
          message: "请输入昵称",
          trigger: 'blur'
        }, {
          min: 3,
          max: 10,
          message: "请输入3~10个字符",
          trigger: 'blur'
        }],
        sex: [{
          required: true,
          message: "请选择性别",
          trigger: 'change'
        }],
        age: [{
          validator: checkAge,
          trigger: 'blur',
          required: true
        }]

      },
      loading: true,
      form: {},
      dialogVisible: false,
      search: '',
      currentPage: 1,
      // current: 1,
      pageSize: 10,
      total: 0,
      tableData: []
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      // this.loading = true
      request.get("/user", {
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.search
        }
      }).then(res => {
        // this.loading = false
        console.log(res)
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },

    add() {
      this.dialogVisible = true
      this.form = {}
    },
    save() {
      if (this.form.id) {
        request.put("/user", this.form).then(res => {
          console.log(res)
          if (res.code === '0') {
            this.$message({
              type: "success",
              message: "修改成功!"
            })
          } else {
            this.$message({
              type: "error",
              message: res.msg
            })
          }
          this.load()//刷新
          this.dialogVisible = false
        })
      } else {
        request.post("/user", this.form).then(res => {
          console.log(res)
          if (res.code === '0') {
            this.$message({
              type: "success",
              message: "新增成功!"
            })
          } else {
            this.$message({
              type: "error",
              message: res.msg
            })
          }
          this.load()//刷新
          this.dialogVisible = false
        })
      }

    },
    handleEdit(row) {
      //深拷贝
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true
    },
    handleSizeChange(pageSize) {  //改变当前每页个数
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {  // 改变当前页码触发
      this.currentPage = pageNum
      this.load()
    },
    handleDelete(id) {
      console.log("当前删除id为：" + id)
      request.delete("/user/" + id).then(res => {
        if (res.code === '0') {
          this.$message({
            type: "success",
            message: "修改成功!"
          })
        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
        this.load()//刷新
        this.dialogVisible = false
      })

    },

  }
}
</script>
