import React,{Component} from "react";
import {Form, Modal, Input} from 'antd';


class SearchForm extends Component{

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
}

export default Form.create()(SearchForm);
