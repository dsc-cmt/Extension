import request from '@/utils/request';

export async function getApplications() {
  return request('/applications', {
    method: 'get',
  });
}

export async function addApplications(data) {
  return request('/applications', {
    method: 'post',
    data: data
  });
}

