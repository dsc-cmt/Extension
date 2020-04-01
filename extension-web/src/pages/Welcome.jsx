import React from 'react';
import { PageHeaderWrapper } from '@ant-design/pro-layout';
import { Card, Typography, Alert } from 'antd';
import styles from './Welcome.less';

const CodePreview = ({ children }) => (
  <pre className={styles.pre}>
    <code>
      <Typography.Text copyable>{children}</Typography.Text>
    </code>
  </pre>
);

export default () => (
  <PageHeaderWrapper>
    <Card>
      <Alert
        message="extension1.0 现已发布至maven公库"
        type="success"
        showIcon
        banner
        style={{
          margin: -12,
          marginBottom: 24,
        }}
      />
      <Typography.Text strong>
        <a target="_blank" rel="noopener noreferrer" href="https://github.com/dsc-cmt/Extension">
          欢迎来提PR和issue，更多功能了解请前往GitHub
        </a>
      </Typography.Text>
    </Card>
  </PageHeaderWrapper>
);
