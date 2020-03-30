import request from '@/utils/request';

export async function getValidOptions() {
  return request('/rightfulness_options', {
    method: 'GET',
  });
}

export async function getAuthorizedUsers(){
  return request('/authorization_users', {
    method: 'GET',
  });
}

export async function createAuthorizedUser(data) {
  return request('/authorization_users', {
    method: 'post',
    data: data
  });
}

export async function updateAuthorizedUser(data) {
  return request('/authorization_users', {
    method: 'patch',
    data: data
  });
}

export async function deleteAuthorizedUser(data) {
  return request('/authorization_users', {
    method: 'delete',
    data: data
  });
}
