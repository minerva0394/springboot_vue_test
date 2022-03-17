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
      <el-input v-model="search" placeholder="请输入书名或作者名" style="width: 20%" clearable></el-input>
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
          prop="name"
          label="书名"
      >
      </el-table-column>
      <el-table-column
          prop="price"
          label="价格">
      </el-table-column>
      <el-table-column
          prop="author"
          label="作者">
      </el-table-column>
      <el-table-column
          prop="createTime"
          label="出版时间">
      </el-table-column>
      <el-table-column
          prop="cover"
          label="封面">
        <template #default="scope">
          <el-image
              style="width: 50%; height: 50%"
              :src="scope.row.cover"
              :preview-src-list="[scope.row.cover]">
          </el-image>
        </template>
      </el-table-column>



      <!--      功能区-->
      <el-table-column label="操作列表" width="300">
        <template #default="scope">
          <el-button size="mini" @click="handleEdit(scope.row)" ref="upload">编辑</el-button>
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

      <el-dialog title="新增书籍" :visible.sync="dialogVisible" width="30%">
        <el-form :model="form" label-width="120px" :rules="rules">
          <el-form-item prop="name" label="书名:">
            <el-input v-model="form.name" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item prop="price" label="价格:">
            <el-input v-model="form.price" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item prop="author" label="作者:">
            <el-input v-model="form.author" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item prop="createTime" label="出版时间:">
            <el-date-picker
                v-model="form.createTime"
                format="yyyy 年 MM 月 dd 日"
                value-format="yyyy-MM-dd"
                type="date"
                placeholder="选择日期">
            </el-date-picker>
          </el-form-item>
          <el-form-item prop="cover" label="封面:">
            <el-upload :on-preview="handlePreview" action="http://localhost:9090/files/upload/" list-type="picture"
                       :on-success="filesUploadSuccess"
                        ref="upload">
              <el-button type="primary">点击上传</el-button>
            </el-upload>
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
  name: 'Book',
  components: {},
  data() {
    return {
      // 图片list
      fileList: [{}],
      loading: true,
      form: {},
      dialogVisible: false,
      search: '',
      currentPage: 1,
      // current: 1,
      pageSize: 10,
      total: 0,
      tableData: [],
      rules: {
        name: [{
          required: true,
          message: "请输入书名",
          trigger: 'blur'
        }, {
          min: 1,
          max: 50,
          message: "请输入1~50个字符",
          trigger: 'blur'
        }],
        price: [{
          required: true,
          message: "请输入价格",
          trigger: 'blur'
        }],
        author: [{
          required: true,
          message: "请输入作者名",
          trigger: 'blur'
        }, {
          min: 1,
          max: 50,
          message: "请输入1~50个字符",
          trigger: 'blur'
        }],
        createTime: [{
          required: true,
          message: "请输入出版时间",
          trigger: 'blur'
        }]
      }
    }
  },
  created() {
    this.load()
  }
  ,
  methods: {
    // 上传图片成功
    filesUploadSuccess(res) {
      console.log(res)
      this.form.cover = res.data
      this.load()
    },


    load() {
      // this.loading = true
      request.get("/book", {
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
    }
    ,

    add() {
      this.dialogVisible = true
      this.form = {}
      // 清除历史文件列表
      this.$refs['upload'].clearFiles()
    }
    ,
    save() {
      if (this.form.id) {  // 更新
        request.put("/book", this.form).then(res => {
          console.log(res)
          if (res.code === '0') {
            this.$message({
              type: "success",
              message: "更新成功"
            })
          } else {
            this.$message({
              type: "error",
              message: res.msg
            })
          }
          this.load() // 刷新表格的数据
          this.dialogVisible = false  // 关闭弹窗
        })
      } else {  // 新增
        request.post("/book", this.form).then(res => {
          console.log(res)
          if (res.code === '0') {
            this.$message({
              type: "success",
              message: "新增成功"
            })
          } else {
            this.$message({
              type: "error",
              message: res.msg
            })
          }
          this.load() // 刷新表格的数据
          this.dialogVisible = false  // 关闭弹窗
        })
      }
    },
    // 上传图像预览
    handlePreview(file) {
      console.log(file);
    },
    handleEdit(row) {
      //深拷贝
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true
      this.$nextTick(()=>{
        // 清除历史文件列表
        this.$refs['upload'].clearFiles()
      })
    }
    ,
    handleSizeChange(pageSize) {  //改变当前每页个数
      this.pageSize = pageSize
      this.load()
    }
    ,
    handleCurrentChange(pageNum) {  // 改变当前页码触发
      this.currentPage = pageNum
      this.load()
    }
    ,
    handleDelete(id) {
      console.log("当前删除id为：" + id)
      request.delete("/book/" + id).then(res => {
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

    }
    ,

  }
}
</script>
