import React from "react";
import {Form, Modal, Input, Select, Radio} from 'antd';
const { Option } = Select;

const StaffCreateForm = ({ visible, onCancel, onCreate, row, options }) =>{
  const [form] = Form.useForm();
  const authorizedApps = row.authorizedApps != null ? row.authorizedApps.split(",") : undefined;

  return (
    <Modal
      visible={visible}
      title=""
      okText="确定"
      cancelText="取消"
      destroyOnClose={true}
      onCancel={() =>{
        onCancel();
        form.resetFields();
      }}
      onOk={() =>{
        form.validateFields()
          .then(values => {
            values.authorizedApps = values.authorizedApps.join(",");
            onCreate(values);
          })
          .catch(info => {
            console.log('Validate Failed:', info);
          });

      }}
    >
      <Form
        form={form}
        name="staff_create_form"
        layout="vertical"
        initialValues={{
          userName: row.userName,
          userMobile: row.userMobile,
          role: row.role,
          authorizedApps: authorizedApps,
          password: row.password,
        }}
      >
        <Form.Item
          label="用户"
          name="userName"
          rules={[
            {required: true, message: '请输入用户姓名'}
          ]}
        >
          <Input/>
        </Form.Item>
        <Form.Item
          label="手机号"
          name="userMobile"
          rules={[{required: true, message: '请输入用户手机号'}]}
        >
          <Input/>
        </Form.Item>
        <Form.Item
          label="密码"
          name="password"
          rules={[{required: true, message: '请输入用户密码'}]}
        >
          <Input.Password/>
        </Form.Item>
        <Form.Item
          label="角色"
          name="role"
          rules={[{required: true, message: '请选择用户角色'}]}
        >
          <Radio.Group>
            <Radio value="admin">admin</Radio>
            <Radio value="user">user</Radio>
          </Radio.Group>
        </Form.Item>
        <Form.Item
          label="授权应用"
          name="authorizedApps"
          rules={[{required: true, message: '请选择授权应用'}]}
        >
         <Select
           mode="multiple"
         >
            {
              options.map((n,index)=>{
                return (
                  <Option key={index} value={n.label}>{n.label}</Option>
                )
              })}
          </Select>
        </Form.Item>
      </Form>
    </Modal>
  )
};
export default StaffCreateForm;
