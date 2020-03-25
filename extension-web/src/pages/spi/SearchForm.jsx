import React,{Component} from "react";
import {Form, Modal, Input, Select} from 'antd';

class SearchForm extends Component{
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
              })(<Input disabled={edit}/>)
            }
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
}

export default Form.create()(SearchForm);

