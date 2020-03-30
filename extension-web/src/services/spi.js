import request from '@/utils/request';

export async function getValidOptions() {
  return request('/rightfulness_options', {
    method: 'get',
  });
}

export async function getValidNamespaces() {
  return request('/rightfulness_namespaces', {
    method: 'get'
  });
}

export async function getConfig(param) {
  return request('/configs', {
    method: 'get',
    params: param,
  });
}

export async function delConfig(data) {
  return request('/configs', {
    method: 'delete',
    data: data
  });
}

export async function addConfig(data) {
  return request('/configs', {
    method: 'post',
    data: data
  });
}

export async function updateConfig(data) {
  return request('/configs', {
    method: 'patch',
    data: data
  });
}

