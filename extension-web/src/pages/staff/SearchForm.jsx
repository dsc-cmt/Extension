import React,{Component} from "react";
import {Form, Modal, Input, Select, Radio} from 'antd';
const { Option } = Select;

class SearchForm extends Component{

  render() {
    const {visible, onCancel, onCreate, form, row, options} = this.props;
    const {getFieldDecorator} = form;
    return (
      <Modal
        visible={visible}
        title=""
        okText="确定"
        onCancel={onCancel}
        onOk={onCreate}
      >
        <Form layout="vertical">
          <Form.Item label="用户">
            {getFieldDecorator('userName', {
              initialValue: row.userName,
              rules: [{required: true, message: '请输入用户姓名'}],
            })(<Input/>)}
          </Form.Item>
          <Form.Item label="手机号">
            {getFieldDecorator('userMobile', {
              initialValue: row.userMobile,
              rules: [{required: true, message: '请输入用户手机号'}],
            })(<Input/>)}
          </Form.Item>
          <Form.Item label="角色">
            {getFieldDecorator('role', {
              initialValue: row.role,
              rules: [{required: true, message: '请选择用户角色'}],
            })(<Radio.Group>
              <Radio value="admin">admin</Radio>
              <Radio value="visitor">visitor</Radio>
            </Radio.Group>)}
          </Form.Item>
          <Form.Item label={"授权应用"}>
            {getFieldDecorator('authorizedApps', {
              initialValue: row.authorizedApps,
              rules: [{required: true, message: '请选择授权应用'}],
            })(<Select>
              {
                options.map(n=>{
                  return (
                    <Option value={n.label}>{n.label}</Option>
                  )
                })}
            </Select>)}
          </Form.Item>
        </Form>
      </Modal>
    )
  }
}
export default Form.create()(SearchForm);

