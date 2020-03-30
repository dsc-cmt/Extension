import request from '@/utils/request';

export async function getValidOptions() {
  return request('/api/spi/validOptions', {
    method: 'get',
  });
}

export async function getValidNamespaces() {
  return request('/api/spi/validNamespaces', {
    method: 'get'
  });
}

export async function getConfig(param) {
  return request('/api/spi/config', {
    method: 'get',
    params: param,
  });
}

export async function delConfig(data) {
  return request('/api/spi/config', {
    method: 'delete',
    data: data
  });
}

export async function addConfig(data) {
  return request('/api/spi/config', {
    method: 'post',
    data: data
  });
}

export async function updateConfig(data) {
  return request('/api/spi/config', {
    method: 'patch',
    data: data
  });
}

