import request from '@/utils/request'

export const leaveApprovalPageListApi = (data) => {
  return request.post('/api/v1/approval/page/list', data)
}

export const applyLeaveApprovalApi = (data) => {
  return request.post('/api/v1/approval/leave', data)
}

export const approvePageListApi = (data) => {
  return request.post('/api/v1/approval/approve/page/list', data)
}

export const approveLeaveApi = (data) => {
  return request.post('/api/v1/approval/approve/leave', data)
}

export const approvalStatistics = () => {
  return request.get('/api/v1/approval/statistics')
}

