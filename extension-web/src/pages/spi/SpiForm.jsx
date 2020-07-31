import React from "react";
import {Form, Modal, Input, Select} from 'antd';

const SpiForm = ({visible, onCancel, onCreate,appName}) => {
  const [form] = Form.useForm();

  return (
    <Modal
      visible={visible}
      title=""
      okText="确定"
      destroyOnClose={true}
      onCancel={() =>{
        onCancel();
        form.resetFields();
      }}
      onOk={() =>{
        form.validateFields()
          .then(values => {
            onCreate(values);
          })
          .catch(info => {
            console.log('Validate Failed:', info);
          });
      }}
    >
      <Form
        form={form}
        name="spi_form"
        layout="vertical"
        initialValues={{
          appName:appName
        }}
      >
        <Form.Item
          label="SPI接口(接口类全限定名，如:com.cmt.finance.OrderService)"
          name="spiInterface"
          rules={[{required: true, message: '请输入接口类'},
            {
              pattern: new RegExp('^[\\w$-_]+$', 'g'),
              message: '接口路径不能包含中文空格及特殊符号'
            }]}
        >
          <Input />
        </Form.Item>

        <Form.Item
          label="接口描述"
          name="desc"
        >
          <Input/>
        </Form.Item>

        <Form.Item
          label="应用名称"
          name="appName"
          rules={[{required: true, message: '应用名称不可为空'}]}
        >
          <Input disabled={true}/>
        </Form.Item>
      </Form>
    </Modal>
  );
}

export default SpiForm;

