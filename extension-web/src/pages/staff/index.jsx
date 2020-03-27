import React, {Component} from 'react'
import {Table, Button, Divider, Popconfirm, message} from 'antd';
import {getValidOptions, getAuthorizedUsers, createAuthorizedUser, updateAuthorizedUser, deleteAuthorizedUser} from '@/services/staff';
import StaffCreateForm from "./StaffCreateForm";
import {connect} from "react-redux";


class User extends Component {
  state = {
    formVisible: false,
    failResultVisible: false,
    successResultVisible: false,
    row: {},
    msg: "",
    formData: [],
    options:[],
    currentUser:{},
    toShow: false,
  }

  constructor(props){
    super(props);
    this.initData()
  }

  componentDidMount() {
    const { currentUser } = this.props;
    this.setState({currentUser});
  }

  initData= async () => {
    let validOptionsResponse = await getValidOptions();
    let authorizedUsersResponse = await getAuthorizedUsers();
    this.handleOptionResponse(validOptionsResponse);
    this.handleConfigResponse(authorizedUsersResponse);
  }

  handleOptionResponse = (response) => {
    if (response && response.success) {
      let options=response.data.map(n=>{
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
    if (response && response.success) {
      let temp = response.data.map((value, index) => {
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

  checkAuth = () => {
    let {currentUser} = this.state;
    if(currentUser && currentUser.currentAuthority === 'admin'){
      return true;
    }
    return false;
  }

  showModal = () => {
    if(this.checkAuth()){
      this.setState({toShow: true, formVisible: true, row: {}})
      return
    }
    message.info("您不是管理员无法新增员工");
  }

  clickEdit = (row) => {
    if(this.checkAuth()){
      this.setState({toShow:true, formVisible: true, row: row})
      return
    }
    message.info("您不是管理员无法编辑员工");
  }

  deleteRequest = async (record) => {
    if (this.checkAuth()) {
      let response = await deleteAuthorizedUser(record);
      this.handleResult(response);
      return
    }
    message.info("您不是管理员无法删除员工");
  }

  handleCancel = () => {
    this.setState({toShow: false, formVisible: false, row: {}});
  }

  handleCreate = async (values) => {
    const {row} = this.state;
    let data = null;
    let response = null;
    if (JSON.stringify(row) === "{}") {
      data = {
        ...values,
      }
      response = await createAuthorizedUser(data);
    } else {
      let data = {
        ...row,
        ...values,
      }
      response = await updateAuthorizedUser(data);
    }
    this.handleResult(response);
  }

  handleResult = async (response) => {
    if (response.success) {
      this.setState({formVisible: false, row: {}});
      let authorizedUsersResponse = await getAuthorizedUsers();
      this.handleConfigResponse(authorizedUsersResponse);
    } else {
      message.error(response.msg);
    }
  }



  render() {
    const {formVisible, row, options, formData, toShow} = this.state;
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
        <Button type="primary" onClick={() => this.showModal()}>
          新增授权用户
        </Button>
        {
          toShow ? <StaffCreateForm
            visible={formVisible}
            onCancel={this.handleCancel}
            onCreate={this.handleCreate}
            row={row}
            options={options}
          /> : null
        }

        <Table dataSource={formData} columns={columns} rowKey={"id"}/>
      </div>
    )

  }
}

export default connect(({ user }) => ({
  currentUser: user.currentUser,
}))(User);
