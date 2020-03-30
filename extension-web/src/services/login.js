import request from '@/utils/request';
export async function fakeAccountLogin(params) {
  return request('/account/action/login', {
    method: 'GET',
    params: params,
  });
}

export async function logout() {
  return request('/account/action/logout', {
    method: 'GET',
  });
}
