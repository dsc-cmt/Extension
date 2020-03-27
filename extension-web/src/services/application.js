import request from '@/utils/request';

export async function getApplications() {
  return request('/api/applications', {
    method: 'get',
  });
}

export async function addApplications(data) {
  return request('/api/applications', {
    method: 'post',
    data: data
  });
}

