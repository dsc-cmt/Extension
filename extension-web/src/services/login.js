import request from '@/utils/request';
export async function fakeAccountLogin(params) {
  return request('/api/account/action/login', {
    method: 'GET',
    params: params,
  });
}
