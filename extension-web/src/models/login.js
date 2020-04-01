import { stringify } from 'querystring';
import { router } from 'umi';
import { fakeAccountLogin, logout } from '@/services/login';
import { setAuthority } from '@/utils/authority';
import { getPageQuery } from '@/utils/utils';
import {message} from "antd";
const Model = {
  namespace: 'login',
  state: {
    status: undefined,
  },
  effects: {
    //登录
    *login({ payload }, { call, put }) {
      let response = yield call(fakeAccountLogin, payload);
      let userInfo = response.data;
      yield put({
        type: 'changeLoginStatus',
        payload: userInfo,
      }); // Login successfully

      if (userInfo.status === 'ok') {
        const urlParams = new URL(window.location.href);
        const params = getPageQuery();
        let { redirect } = params;

        if (redirect) {
          const redirectUrlParams = new URL(redirect);

          if (redirectUrlParams.origin === urlParams.origin) {
            redirect = redirect.substr(urlParams.origin.length);

            if (redirect.match(/^\/.*#/)) {
              redirect = redirect.substr(redirect.indexOf('#') + 1);
            }
          } else {
            window.location.href = '/';
            return;
          }
        }

        router.replace(redirect || '/');
      }
    },

    //退出应用
    *logout({ payload }, { call, put }) {
      const { redirect } = getPageQuery();
      let response = yield call(logout, payload);
      if(!response.success){
        message.error("退出应用失败");
      }
      yield put({
        type: 'resetLoginStatus',
      }); // Login successfully
      if (window.location.pathname !== '/user/login' && !redirect) {
        router.replace({
          pathname: '/user/login',
          search: stringify({
            redirect: window.location.href,
          }),
        });
      }
    },
  },
  reducers: {
    changeLoginStatus(state, { payload }) {
      setAuthority(payload.currentAuthority);
      return { ...state, status: payload.status};
    },

    resetLoginStatus(state, { payload }) {
      setAuthority(undefined);
      return { status:undefined};
    },
  },
};
export default Model;
