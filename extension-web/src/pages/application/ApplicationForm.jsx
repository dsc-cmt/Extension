import React from "react";
import {Form, Modal, Input,} from 'antd';

const ApplicationForm = ({visible, onCancel, onCreate}) =>{
    const [form] = Form.useForm();

    return (
      <Modal
        visible={visible}
        title="新增应用"
        okText="确定"
        onCancel={() =>{
          onCancel();
          form.resetFields();
        }}
        onOk={() =>{
          form.validateFields()
            .then(values => {
              form.resetFields();
              onCreate(values);
            })
            .catch(info => {
              console.log('Validate Failed:', info);
            });

        }}
      >
        <Form
          form={form}
          name="application_create_form"
          layout="vertical"
        >
          <Form.Item
            label="应用名称"
            name="namespace"
            rules={[
              {required: true, message: '请输入应用名称'},
              {
                pattern: new RegExp('^[\\w$-_.]+$', 'g'),
                message: '应用名称只能由英文字母数字，-,_,$,.组成'
              }
            ]}
          >
          <Input/>
          </Form.Item>
        </Form>
      </Modal>
    )
}

export default ApplicationForm;
