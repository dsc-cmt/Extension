import React, {Component} from 'react'
import {Button, message, Table} from "antd";
import AppCreateForm from "./ApplicationCreateForm";
import {getApplications, addApplications} from "@/services/application"
import {connect} from "react-redux";

class AppManager extends Component {
  state = {
    visible: false,
    formData: [],
    currentUser: null
  }

  constructor(props) {
    super(props)
    this.initData()
  }

  componentDidMount() {
    const { currentUser } = this.props;
    this.setState({currentUser});
  }

  initData = async () => {
    let response = await getApplications();
    this.handleNamespacesDetailResponse(response);
  }

  disable = () => {
    let {currentUser} = this.state;
    if(currentUser && currentUser.currentAuthority === 'admin'){
      return false;
    }
    return true;
  }

  handleNamespacesDetailResponse = (response) => {
    if (response.success) {
      this.setState({
        formData: response.data
      });
    }
  }

  handleClick = () => {
    this.setState({
      visible: true
    })
  }
  handleCreate = async (values) => {
    let response = await addApplications(values.namespace);
    this.handleResult(response);
  }

  handleCancel = () => {
    this.setState({visible: false});
  }

  handleResult = (response) => {
    let msg = response.msg;
    if (response.success) {
      this.setState({visible: false});
      this.initData();
    } else {
      message.error(msg);
    }
  }

  render() {

    const columns = [
      {
        title: '应用名称',
        dataIndex: 'namespace',
        key: 'namespace',
      },
      {
        title: '创建人',
        dataIndex: 'creator',
        key: 'creator',
      },
      {
        title: '创建时间',
        dataIndex: 'createTime',
        key: 'createTime',
      }
    ]
    return (
      <div align={"left"}>
        <Button type="primary" onClick={this.handleClick} disabled={this.disable()}>
          新增SPI应用
        </Button>
        <AppCreateForm
          visible={this.state.visible}
          onCancel={this.handleCancel}
          onCreate={this.handleCreate}
        />
        <Table dataSource={this.state.formData} columns={columns} rowKey={"namespace"}/>
      </div>

    )
  }
}

export default connect(({ user }) => ({
  currentUser: user.currentUser,
}))(AppManager);
