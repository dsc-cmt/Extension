import { DefaultFooter, getMenuData, getPageTitle } from '@ant-design/pro-layout';
import { Helmet } from 'react-helmet';
import { Link } from 'umi';
import React from 'react';
import { formatMessage } from 'umi-plugin-react/locale';
import { connect } from 'dva';
import SelectLang from '@/components/SelectLang';
//import logo from '../assets/logo.svg';
import styles from './UserLayout.less';
import {GithubOutlined} from "@ant-design/icons";

const UserLayout = props => {
  const {
    route = {
      routes: [],
    },
  } = props;
  const { routes = [] } = route;
  const {
    children,
    location = {
      pathname: '',
    },
  } = props;
  const { breadcrumb } = getMenuData(routes);
  const title = getPageTitle({
    pathname: location.pathname,
    formatMessage,
    breadcrumb,
    ...props,
  });
  return (
    <>
      <Helmet>
        <title>{title}</title>
        <meta name="description" content={title} />
      </Helmet>

      <div className={styles.container}>
        <div className={styles.lang}>
          <SelectLang />
        </div>
        <div className={styles.content}>
          <div className={styles.top}>
            <div className={styles.header}>
              <Link to="/">
{/*
                <img alt="logo" className={styles.logo} src={logo} />
*/}
                <span className={styles.title}>Extension</span>
              </Link>
            </div>
            <div className={styles.desc}>Extension是一个基于Dubbo的远程SPI调用解决方案</div>
          </div>
          {children}
        </div>
        <DefaultFooter
          copyright="2019 dsc-cmt"
          links={[
            {
              key: 'github地址',
              title: <GithubOutlined />,
              href: 'https://github.com/dsc-cmt/Extension',
              blankTarget: true,
            }
          ]}
        />
      </div>
    </>
  );
};

export default connect(({ settings }) => ({ ...settings }))(UserLayout);
