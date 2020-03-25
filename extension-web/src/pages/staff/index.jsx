import React, {Component} from 'react'
import {Table, Button, Divider, Popconfirm,Select} from 'antd';
import UserCreateForm from "./SearchForm";
import CustomResult from "../../components/CustomResult";

class User extends Component {
  state = {
    formVisible: false,
    failResultVisible: false,
    successResultVisible: false,
    row: {},
    msg: "",
    formData: [],
    options:[],
  }

  constructor(props){
    super(props)
    //this.initData()
  }

  initData=()=>{
    Request.get("getValidOptions").then(this.handleOptionResponse)
    Request.get("authorizedUsers").then(this.handleConfigResponse)
  }
  handleOptionResponse = (response) => {
    if (response.data.success) {
      let options=response.data.data.map(n=>{
        return {
          label:n,
          value:n
        }
      })
      this.setState({
        options:options
      })
    }
  }

  handleConfigResponse = (response) => {
    if (response.data.success) {
      let temp = response.data.data.map((value, index) => {
        return {
          ...value,
          "key": index
        }
      })
      this.setState({
        formData: temp
      })
    }
  }

  handleResult = (response) => {
    let msg = response.data.msg;
    if (response.data.success) {
      Request.get("authorizedUsers").then(this.handleConfigResponse)
      this.setState({
        successResultVisible: true,
      })
    } else {
      this.setState({
        failResultVisible: true,
        msg: msg,
      })
    }
  }

  showModal = (row) => {
    if(this.state.options.length>0){
      this.setState({formVisible: true, row: row})
      return
    }
    alert("只有管理员可以新增用户！")
  }

  checkAuth=(app)=>{
    let auth=false
    this.state.options.forEach(
      o=>{
        if(o.label===app){
          auth=true
        }
      }
    )
    return auth
  }

  clickEdit = (row) => {
    if(this.checkAuth(row.authorizedApps)){
      this.setState({formVisible: true, row: row})
      return
    }
    alert("您不是该应用管理员，无法修改！")
  }

  handleCancel = () => {
    this.setState({formVisible: false, row: {}});
  }

  handleCreate = (row) => {
    const {form} = this.formRef.props;
    form.validateFields((err, values) => {
      if (err) {
        return;
      }
      form.resetFields();
      this.setState({formVisible: false});
      if (JSON.stringify(row) === "{}") {
        let params={
          ...values,
        }
        Request.post("/createAuthorizedUser", params).then(this.handleResult);
      } else {
        let params={
          ...row,
          ...values,
        }
        Request.post("/updateAuthorizedUser", params).then(this.handleResult);
      }
    })
  }

  saveFormRef = formRef => {
    this.formRef = formRef;
  }

  handleConfirmResult = () => {
    this.setState({
      failResultVisible: false,
      successResultVisible: false,
      msg: "",
    })
  }


  deleteRequest = (record) => {
    if(this.state.options.indexOf(record.authorizedApps)){
      Request.post("/deleteAuthorizedUser",record).then(this.handleResult)
      return
    }
    alert("只有应用管理员可以删除！")
  }



  render() {
    const columns = [
      {
        title: '用户',
        dataIndex: 'userName',
        key: 'userName',
        sorter: (a, b) => {
          let stringA = a.userName.toUpperCase(); // ignore upper and lowercase
          let stringB = b.userName.toUpperCase(); // ignore upper and lowercase
          if (stringA < stringB) {
            return -1;
          }
          if (stringA > stringB) {
            return 1;
          }
          // names must be equal
          return 0;
        }
      },
      {
        title: '手机号',
        dataIndex: 'userMobile',
        key: 'userMobile',
      },
      {
        title: '角色',
        dataIndex: 'role',
        key: 'role',
      },
      {
        title: '授权应用',
        dataIndex: 'authorizedApps',
        key: 'authorizedApps',
      },
      {
        title: '操作',
        key: 'action',
        render: (text, record) => (
          <span>
                        <Button size={"small"}  onClick={() => this.clickEdit(record)}>编辑</Button>
                        <Divider type="vertical"/>
                        <Popconfirm
                          title="删除这条用户配置?"
                          onConfirm={() => this.deleteRequest(record)}
                          onCancel={this.deleteCancel}
                          okText="确认"
                          cancelText="取消"
                        >
                            <Button size={"small"}>删除</Button>
                        </Popconfirm>
                    </span>
        )
      }
    ]

    return (
      <div align={"left"}>
        <Button type="primary" onClick={() => this.showModal({})}>
          新增授权用户
        </Button>
        <UserCreateForm
          wrappedComponentRef={this.saveFormRef}
          visible={this.state.formVisible}
          onCancel={this.handleCancel}
          onCreate={() => {
            this.handleCreate(this.state.row)
          }}
          row={this.state.row}
          options={this.state.options}
        />
        <CustomResult successResultVisible={this.state.successResultVisible}
                      failResultVisible={this.state.failResultVisible}
                      handleConfirmResult={this.handleConfirmResult}
                      msg={this.state.msg}/>

        <Table dataSource={this.state.formData} columns={columns} key={"id"}/>
      </div>
    )

  }
}

export default User