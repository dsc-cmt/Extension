import React, {Component} from 'react'
import {Button, Table} from "antd";
import AppCreateForm from "./SearchForm";
import CustomResult from "../../components/CustomResult";

class AppManager extends Component {
  state = {
    visible: false,
    successResultVisible: false,
    failResultVisible: false,
    msg: "",
    formData: []
  }

  constructor(props) {
    super(props)
    //this.initData()
  }

  initData = () => {
    Request.get("namespacesDetail").then(this.handleNamespacesDetailResponse)
  }

  handleNamespacesDetailResponse = (response) => {
    if (response.data.success) {
      this.setState({
        formData: response.data.data
      });
    }
  }

  handleClick = () => {
    this.setState({
      visible: true
    })
  }
  handleCreate = () => {
    const {form} = this.formRef.props;
    form.validateFields((err, values) => {
      if (err) {
        return;
      }
      form.resetFields()
      this.setState({visible: false})
      Request.get("addNamespace", {namespace: values.namespace}).then(this.handleResult)
    })
  }

  handleCancel = () => {
    this.setState({visible: false});
  }

  handleResult = (response) => {
    let msg = response.data.msg;
    if (response.data.success) {
      Request.get("namespacesDetail").then(this.handleNamespacesDetailResponse)
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

  saveFormRef = formRef => {
    this.formRef = formRef;
  }

  handleConfirmResult = () => {
    this.setState({
      failResultVisible: false,
      successResultVisible: false,
      msg: "",
    });
  };

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
{/*
        todo 权限控制
*/}
        <Button type="primary" onClick={this.handleClick} disabled={window.disabled}>
          新增SPI应用
        </Button>
        <AppCreateForm
          wrappedComponentRef={this.saveFormRef}
          visible={this.state.visible}
          onCancel={this.handleCancel}
          onCreate={this.handleCreate}
        />
        <CustomResult successResultVisible={this.state.successResultVisible}
                      failResultVisible={this.state.failResultVisible}
                      handleConfirmResult={this.handleConfirmResult}
                      msg={this.state.msg}/>
        <Table dataSource={this.state.formData} columns={columns} rowKey={"namespace"}/>
      </div>

    )
  }
}

export default AppManager
