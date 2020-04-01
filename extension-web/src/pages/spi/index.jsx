import React, {Component} from 'react'
import {Divider, Table, Button, Select, Popconfirm, message,} from 'antd';
import {getValidOptions, getValidNamespaces, getConfig, delConfig, addConfig, updateConfig} from "@/services/spi"
import SpiForm from './SpiForm';
import {connect} from "react-redux";


class SPIApp extends Component {
  state = {
    formVisible: false,
    row: {},
    formData: [],
    namespace: "",
    menus:[],
    authNamespaces:[],
    currentUser: null,
    modalShow: false,
  };

  constructor(props){
    super(props)
    this.initMenu()
  }

  componentDidMount() {
    const { currentUser } = this.props;
    this.setState({currentUser});
  }

  initMenu= async () => {
    let validResponse = await getValidOptions();
    let validNameSpace = await getValidNamespaces();
    this.handleOptionResponse(validResponse);
    this.handleNamespaceResponse(validNameSpace);
  }

  handleOptionResponse = (response) => {
    if (response.success) {
      this.setState({
        authNamespaces:response.data
      })
    }
  }

  handleNamespaceResponse=(response)=>{
    if(response.success){
      this.setState({menus:response.data})
    }
  }

  handleNamespaceChange= async (n) => {
    this.setState({namespace: n});
    let response = await getConfig({namespace: n});
    this.handleConfigResponse(response);
  }

  handleConfigResponse = (response) => {
    if (response.success) {
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

  handleResult = async (response) => {
    let msg = response.msg;
    if (response.success) {
      message.success("操作成功");
      this.setState({modalShow: false, formVisible: false});
      let response = await getConfig({namespace: this.state.namespace});
      this.handleConfigResponse(response);
    } else {
      message.error(msg);
    }
  }

  checkAuth = () =>{
    let {authNamespaces, namespace} = this.state;
    if (authNamespaces.indexOf(namespace) < 0) {
        message.error("没有该应用的管理权限！");
        return false
    }
    return true;
  }

  deleteRequest = async (record) => {
    let {currentUser} = this.state;
    if (!this.checkAuth()) {
      return
    }
    let params = {
      ...record,
      mobile: currentUser.mobile,
    }
    let response = await delConfig(params);
    this.handleResult(response);
  }

  deleteCancel = () => {

  }

  showModal = (row) => {
    if (this.state.namespace == null||this.state.namespace ==="" ) {
      message.info("请先选择应用");
      return
    }
    if (!this.checkAuth()) {
      return
    }
    this.setState({modalShow: true, formVisible: true, row: row});
  }

  handleCancel = () => {
    this.setState({modalShow: false, formVisible: false, row: {}});
  };

  handleCreate = async (values) => {
    let {row, currentUser} = this.state;
    let data = {
      ...values,
      mobile: currentUser.mobile,
    }
    let response = null;
    //新增
    if (JSON.stringify(row) === "{}") {
      response = await addConfig(data);
    } else {
      //编辑
      response = await updateConfig(data);
    }
    this.handleResult(response);
  };

  render() {
    const {menus, formVisible, namespace, row, formData, modalShow} = this.state;
    const columns = [
      {
        title: 'spi接口',
        dataIndex: 'spiInterface',
        key: 'spiInterface',
        sorter: (a, b) => {
          let stringA = a.spiInterface.toUpperCase(); // ignore upper and lowercase
          let stringB = b.spiInterface.toUpperCase(); // ignore upper and lowercase
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
            menus.map(m=>{
              return (
                <Select.Option key={m} value={m}>{m}</Select.Option>
              )
            })}
        </Select>
        <Button type="primary" onClick={() => this.showModal({})}>
          新增SPI实现
        </Button>
        {
          modalShow ? <SpiForm
            visible={formVisible}
            onCancel={this.handleCancel}
            onCreate={this.handleCreate}
            row={row}
            namespace={namespace}
          /> : null
        }
        <Table dataSource={formData} columns={columns}/>
      </div>
    )

  }
}

export default connect(({ user }) => ({
  currentUser: user.currentUser,
}))(SPIApp);
