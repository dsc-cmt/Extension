import React, {Component} from 'react'
import {Button, Form, Input, Modal, Table} from "antd";
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
    this.initData()
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

const AppCreateForm = Form.create({name: 'create_app_in_modal'})(
  class extends React.Component {
    render() {
      const {visible, onCancel, onCreate, form} = this.props;
      const {getFieldDecorator} = form;
      return (
        <Modal
          visible={visible}
          title="新增应用"
          okText="确定"
          onCancel={onCancel}
          onOk={onCreate}
        >
          <Form layout="vertical">
            <Form.Item label="应用名称">
              {getFieldDecorator('namespace', {
                rules: [{required: true, message: '请输入应用名称'},
                  {
                    pattern: new RegExp('^[\\w$-_.]+$', 'g'),
                    message: '应用名称只能由英文字母数字，-,_,$,.组成'
                  }
                ],
              })(<Input/>)}
            </Form.Item>
          </Form>
        </Modal>
      );
    }
  });
export default AppManager
