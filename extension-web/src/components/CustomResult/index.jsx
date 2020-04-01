import React from "react";
import {Button, Icon, Modal, Result, Typography} from "antd";

const {Paragraph} = Typography;

class CustomResult extends React.Component {

  render() {
    let customStatus = "";
    let customTitle = "";
    let visible = false;
    const {successResultVisible, failResultVisible, handleConfirmResult, msg} = this.props
    if (successResultVisible) {
      visible = true;
      customStatus = "success";
      customTitle = "操作成功"
    }
    if (failResultVisible) {
      visible = true;
      customStatus = "error";
      customTitle = "操作失败";
    }
    return (
      <div style={{width: 100}}>
        <Modal
          visible={visible}
          onOk={handleConfirmResult}
          onCancel={handleConfirmResult}
          footer={[
            <div align={"center"}>
              <Button key="back" onClick={handleConfirmResult}>
                确认
              </Button>
            </div>,
          ]}
        >
          <Result
            status={customStatus}
            title={customTitle}
          >
            <div className="desc" hidden={!failResultVisible}>
              <Paragraph>
                <Icon style={{color: 'red'}} type="close-circle"/> {msg}
              </Paragraph>
            </div>
          </Result>
        </Modal>
      </div>
    );
  }
}

export default CustomResult
