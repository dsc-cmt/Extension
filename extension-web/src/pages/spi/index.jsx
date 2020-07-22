import React, {Component} from 'react'
import {Divider, Table, Button, Select, Popconfirm, message, Input,} from 'antd';
import {getValidOptions, getValidNamespaces, getConfig, delConfig, addConfig, updateConfig,getSpis,addSpi} from "@/services/spi"
import ConfigForm from './ConfigForm';
import {connect} from "react-redux";
import PlusOutlined from "@ant-design/icons/es/icons/PlusOutlined";
import SpiForm from "./SpiForm";


class SPIApp extends Component {
  state = {
    formVisible: false,
    row: {},
    formData: [],
    namespace: "",
    menus:[],
    authNamespaces:[],
    spis:[],
    spiInterface:null,
    currentUser: null,
    modalShow: false,
    spiFormVisible:false,
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
        authNamespaces:response.data.map(d=>d.appName)
      })
    }
  }

  handleNamespaceResponse=(response)=>{
    if(response.success){
      this.setState({menus:response.data})
    }
  }

  handleAppChange= async (n) => {
    this.setState({namespace: n});
    let spiRes=await getSpis({appName:n});
    this.handleSpiResponse(spiRes)
  }

  handleSpiChange = async (s)=>{
    this.setState({spiInterface:s})
    let response = await getConfig({appName: this.state.namespace,spiInterface:s});
    this.handleConfigResponse(response);
  }

  handleSpiResponse = (response) => {
    if (response.success) {
      this.setState({
        spis: response.data,
        spiInterface: null
      })
    }
  }

  handleConfigResponse = (response) => {
    if (response.success) {
      let temp = response.data.map((value, index) => {
        return {
          ...value,
          "key": index,
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
      let response = await getConfig({appName: this.state.namespace,spiInterface: this.state.spiInterface});
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
      appName:this.state.namespace,
      mobile: currentUser.mobile,
      spiInterface:this.state.spiInterface,
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
    if (this.state.spiInterface == null||this.state.spiInterface ==="" ) {
      message.info("请先选择扩展点");
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

  handleSpiCreate = async (values) =>{
    let response= await addSpi(values);
    if (response.success) {
      message.success("操作成功");
      this.setState({spiFormVisible: false});
      let spiRes=await getSpis({appName:this.state.namespace});
      this.handleSpiResponse(spiRes)
    } else {
      message.error(response.msg);
    }
  }

  handleCreate = async (values) => {
    let {row, currentUser} = this.state;
    let data = {
      ...values,
      extensionId:row.extensionId,
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

  showSpiForm = ()=>{
    this.setState({
      spiFormVisible:true
    })
  }
  render() {
    const {menus, formVisible, namespace, row, formData, modalShow,spis} = this.state;
    const columns = [
      {
        title: '业务标识',
        dataIndex: 'bizCode',
        key: 'bizCode',
      },
      {
        title: '调用方式',
        dataIndex: 'invokeMethod',
        key: 'invokeMethod',
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
        <Select placeholder={"请选择应用"} style={{ width: 120 }} onChange={this.handleAppChange}>
          {
            menus.map(m=>{
              return (
                <Select.Option key={m.appName} value={m.appName}>{m.appName}</Select.Option>
              )
            })}
        </Select>

        <Select placeholder={"请选择spi扩展点"} style={{ width: 280 }} onChange={this.handleSpiChange} value={this.state.spiInterface}
                dropdownRender={menu => (
                  <div>
                    {menu}
                    <Divider style={{ margin: '4px 0' }} />
                    <div style={{ display: 'flex', flexWrap: 'nowrap', padding: 8 }}>
                      <a
                        style={{ flex: 'none', padding: '8px', display: 'block', cursor: 'pointer' }}
                        onClick={this.showSpiForm}
                      >
                        <PlusOutlined /> 新增扩展点
                      </a>
                    </div>
                  </div>
                )}>
          {
            spis.map(spi=>{
              return (
                <Select.Option key={spi.spiInterface} value={spi.spiInterface}>{spi.spiInterface}</Select.Option>
              )
            })}
        </Select>

        <Button type="primary" onClick={() => this.showModal({})}>
          新增SPI实现
        </Button>
        <SpiForm visible={this.state.spiFormVisible}
        onCancel={()=>{this.setState({spiFormVisible:false})}}
                 onCreate={this.handleSpiCreate}
                 appName={this.state.namespace}
        />
        {
          modalShow ? <ConfigForm
            visible={formVisible}
            onCancel={this.handleCancel}
            onCreate={this.handleCreate}
            row={row}
            namespace={namespace}
            spiInterface={this.state.spiInterface}
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
