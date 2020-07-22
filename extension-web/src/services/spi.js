import request from '@/utils/request';

export async function getValidOptions() {
  return request('/api/rightfulness_options', {
    method: 'get',
  });
}

export async function getValidNamespaces() {
  return request('/api/rightfulness_namespaces', {
    method: 'get'
  });
}

export async function getConfig(param) {
  return request('/api/configs', {
    method: 'get',
    params: param,
  });
}

export async function delConfig(data) {
  return request('/api/configs', {
    method: 'delete',
    data: data
  });
}

export async function addConfig(data) {
  return request('/api/configs', {
    method: 'post',
    data: data
  });
}

export async function updateConfig(data) {
  return request('/api/configs', {
    method: 'patch',
    data: data
  });
}

export async function getSpis(param) {
  return request('api/spis',{
    method:'get',
    params:param,
  });
}

export async function addSpi(data){
  return request('/api/spis', {
    method: 'post',
    data: data
  });
}
