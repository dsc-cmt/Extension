import React from "react";
import {Form, Modal, Input, Select} from 'antd';

const ConfigForm = ({visible, onCancel, onCreate, row, namespace,spiInterface}) => {
  const [form] = Form.useForm();
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
          spiInterface: spiInterface,
          isDefault: row.isDefault==null?"":row.isDefault+"",
          bizCode: row.bizCode,
          expireTime: row.expireTime,
          invokeMethod: row.invokeMethod,
          appName: namespace
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
          <Input disabled={edit}/>
        </Form.Item>
        <Form.Item
          label="业务标识"
          name="bizCode"
          rules={[
           {required: true, message: '请输入业务标识'},
          {
          pattern: new RegExp('^[\\w$-_]+$', 'g'),
          message: '业务标识不能包含中文空格及特殊符号'}
          ]}
        >
          <Input disabled={edit}/>
        </Form.Item>
        <Form.Item
          label="是否为默认实现(目前不支持远程设置默认实现，仅支持本地设置默认实现)"
          name="isDefault"
          rules={[{required: true, message: '请选择是否为默认调用方式'}]}
        >
          <Select allowClear={true}>
            <Select.Option key={"1"} value="1">是</Select.Option>
            <Select.Option key={"0"} value="0">否</Select.Option>
          </Select>
        </Form.Item>
        <Form.Item
          name="expireTime"
          label="超时时间(单位：ms,默认10s)"
          rules={[{
            pattern: new RegExp('^[\\d]+$', 'g'),
            message: '超时时间必须为正整数'
          }]}
        >
          <Input/>
        </Form.Item>
        <Form.Item
          label="调用方式"
          name="invokeMethod"
          rules={[{required: true, message: '请输入调用方式'}]}
        >
          <Select>
            <Select.Option value="local">local</Select.Option>
            <Select.Option value="dubbo">dubbo</Select.Option>
          </Select>
        </Form.Item>
        <Form.Item
          label="应用名称"
          name="appName"
          rules={[{required: true, message: '应用id不可为空'}]}
        >
          <Input disabled={true}/>
        </Form.Item>
      </Form>
    </Modal>
  );
}

export default ConfigForm;

