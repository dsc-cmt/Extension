import request from '@/utils/request';

export async function getValidOptions() {
  return request('/api/admin/validOptions', {
    method: 'GET',
  });
}

export async function getAuthorizedUsers(){
  return request('/api/staff/authorizedUsers', {
    method: 'GET',
  });
}

export async function createAuthorizedUser(data) {
  return request('/api/staff/createAuthorizedUser', {
    method: 'post',
    data: data
  });
}

export async function updateAuthorizedUser(data) {
  return request('/api/staff/updateAuthorizedUser', {
    method: 'patch',
    data: data
  });
}

export async function deleteAuthorizedUser(data) {
  return request('/api/staff/deleteAuthorizedUser', {
    method: 'patch',
    data: data
  });
}
