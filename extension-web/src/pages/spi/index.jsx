import React, {Component} from 'react'
import {
  Divider,
  Table,
  Button,
  Modal,
  Form,
  Input,
  Select,
  Popconfirm,
} from 'antd'
import CustomResult from "../../components/CustomResult";


class SpiApp extends Component {
  state = {
    formVisible: false,
    failResultVisible: false,
    successResultVisible: false,
    row: {},
    msg: "",
    formData: [],
    namespace: "",
    menus:[],
    authNamespaces:[]
  };

  constructor(props){
    super(props)
    this.initMenu()
  }

  initMenu=()=>{
    Request.get("getValidOptions").then(this.handleOptionResponse)
    Request.get("validNamespaces").then(this.handleNamespaceResponse)
  }

  handleOptionResponse = (response) => {
    if (response.data.success) {
      this.setState({
        authNamespaces:response.data.data
      })
    }
  }

  handleNamespaceResponse=(response)=>{
    if(response.data.success){
      this.setState({menus:response.data.data})
    }
  }

  handleNamespaceChange=(n)=>{
    this.setState({namespace:n})
    Request.get("config", {params:{namespace: n}}).then(this.handleConfigResponse);
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
      Request.get("config", {params:{namespace: this.state.namespace}}).then(this.handleConfigResponse);
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


  deleteRequest = (record) => {
    if (this.state.authNamespaces.indexOf(this.state.namespace)<0){
      alert("没有该应用的管理权限！")
      return
    }
    let params={
      ...record,
      //todo
      mobile:"15700718397",
    }
    Request.delete("/config", {params: params}).then(this.handleResult)
  }

  deleteCancel = () => {

  }

  showModal = (row) => {
    if (this.state.namespace == null||this.state.namespace ==="" ) {
      alert('请先选择应用')
      return
    }
    if (this.state.authNamespaces.indexOf(this.state.namespace)<0){
      alert("没有该应用的管理权限！")
      return
    }

    this.setState({formVisible: true, row: row});
  }

  handleCancel = () => {
    this.setState({formVisible: false, row: {}});
  };

  handleCreate = (namespace, row) => {
    const {form} = this.formRef.props;
    form.validateFields((err, values) => {
      if (err) {
        return;
      }
      form.resetFields();
      this.setState({formVisible: false});
      //TODO
      let userInfo= {userPhone: "15700718309"};
      let params={
        ...values,
        mobile:userInfo.userPhone,
      }

      if (JSON.stringify(row) === "{}") {
        Request.post("/createConfig", params).then(this.handleResult);
      } else {
        Request.post("/updateConfig", params).then(this.handleResult);
      }
    });
  };

  saveFormRef = formRef => {
    this.formRef = formRef;
  };

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
        title: 'spi接口',
        dataIndex: 'spiInterface',
        key: 'spiInterface',
        sorter: (a, b) => {
          var stringA = a.spiInterface.toUpperCase(); // ignore upper and lowercase
          var stringB = b.spiInterface.toUpperCase(); // ignore upper and lowercase
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
        title: '调用方式',
        dataIndex: 'invokeMethod',
        key: 'invokeMethod',
      },
      {
        title: '业务标识',
        dataIndex: 'bizCode',
        key: 'bizCode',
      },
      {
        title: '超时时间',
        dataIndex: 'expireTime',
        key: 'expireTime',
      },
      {
        title: '是否默认',
        dataIndex: 'isDefaultDesc',
        key: 'isDefault',
      },
      {
        title: '操作',
        key: 'action',
        render: (text, record) => (
          <span>
                        <Button size={"small"} onClick={() => this.showModal(record)}>编辑</Button>
                        <Divider type="vertical"/>
                        <Popconfirm
                          title="删除这条SPI配置?"
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
    ];
    return (
      <div align={"left"}>
        <Select placeholder={"请选择应用"} style={{ width: 120 }} onChange={this.handleNamespaceChange}>
          {
            this.state.menus.map(m=>{
              return (
                <Select.Option value={m}>{m}</Select.Option>
              )
            })}
        </Select>
        <Button type="primary" onClick={() => this.showModal({})}>
          新增SPI实现
        </Button>
        <SpiCreateForm
          wrappedComponentRef={this.saveFormRef}
          visible={this.state.formVisible}
          onCancel={this.handleCancel}
          onCreate={() => {
            this.handleCreate(this.state.namespace, this.state.row)
          }}
          row={this.state.row}
          namespace={this.state.namespace}
        />
        <CustomResult successResultVisible={this.state.successResultVisible}
                      failResultVisible={this.state.failResultVisible}
                      handleConfirmResult={this.handleConfirmResult}
                      msg={this.state.msg}/>

        <Table dataSource={this.state.formData} columns={columns}/>
      </div>
    )

  }
}

//表单
const SpiCreateForm = Form.create({name: 'form_in_modal'})(
  class extends React.Component {
    render() {
      const {visible, onCancel, onCreate, form, row, namespace} = this.props;
      const {getFieldDecorator} = form;
      let edit = true;
      //row 为空时为新增
      if (JSON.stringify(row) === "{}") {
        edit = false;
      }
      return (
        <Modal
          visible={visible}
          title=""
          okText="确定"
          onCancel={onCancel}
          onOk={onCreate}
        >
          <Form layout="vertical">
            <Form.Item label="SPI接口(接口类全限定名，如:com.souche.finance.OrderService)">
              {getFieldDecorator('spiInterface', {
                initialValue: row.spiInterface,
                rules: [{required: true, message: '请输入接口类'},
                  {
                    pattern: new RegExp('^[\\w$-_]+$', 'g'),
                    message: '接口路径不能包含中文空格及特殊符号'
                  }],
              })(<Input disabled={edit}/>)}
            </Form.Item>
            <Form.Item label="业务标识">
              {getFieldDecorator('bizCode', {
                initialValue: row.bizCode,
                rules: [{required: true, message: '请输入业务标识'},
                  {
                    pattern: new RegExp('^[\\w$-_]+$', 'g'),
                    message: '业务标识不能包含中文空格及特殊符号'
                  }],
              })(<Input disabled={edit}/>)}
            </Form.Item>
            <Form.Item label="是否为默认实现(若选择是,当没有匹配的实现时将选择此实现!)">
              {getFieldDecorator('isDefault', {
                initialValue: row.isDefaultDesc,
                rules: [{required: true, message: '请选择是否为默认调用方式'},],
              })(
                <Select allowClear={true}>
                  <Select.Option value="1">是</Select.Option>
                  <Select.Option value="0">否</Select.Option>
                </Select>
                )}
            </Form.Item>
            <Form.Item label="超时时间(单位：ms,默认10s)">
              {getFieldDecorator('expireTime', {
                initialValue: row.expireTime,
                rules: [{
                  pattern: new RegExp('^[\\d]+$', 'g'),
                  message: '超时时间必须为正整数'
                }],
              })(<Input/>)}
            </Form.Item>
            <Form.Item label="调用方式">
              {getFieldDecorator('invokeMethod', {
                initialValue: row.invokeMethod,
                rules: [{required: true, message: '请输入调用方式'}],
              })(
                <Select>
                  <Select.Option value="local">local</Select.Option>
                  <Select.Option value="dubbo">dubbo</Select.Option>
                </Select>)}
            </Form.Item>
            <Form.Item label="应用名称">
              {getFieldDecorator('appId', {
                initialValue: namespace,
                rules: [{required: true, message: '应用id不可为空'}],
              })(<Input disabled={true}/>)}
            </Form.Item>
          </Form>
        </Modal>
      );
    }
  },
);

export default SpiApp
