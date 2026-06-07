
from datetime import datetime
    
from langchain.tools import tool
import requests

from app.context.context_handler import get_user_token

BACKEND_BASE_URL = "http://127.0.0.1:8081/api/v1"

@tool
def get_local_datetime():
    """
    获取系统当前日期时间
    """
    time = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    return f'当前时间是: {time}'

@tool
def get_approval_list(pageNum: int = 1, pageSize: int = 10):
    """
    获取我的申请列表
    
    :param pageNum: Description
    :type pageNum: int
    :param pageSize: Description
    :type pageSize: int
    """
    res = requests.post(
        f"{BACKEND_BASE_URL}/approval/page/list",
        json={
            "pageNum": pageNum,
            "pageSize": pageSize
        },
        headers={
            "Authorization": get_user_token()
        }
    )

    res.raise_for_status()
    return res.json()

@tool
def get_approve_list(pageNum: int = 1, pageSize: int = 10):
    """
    获取审批列表
    
    :param pageNum: 分页页码
    :type pageNum: int
    :param pageSize: 分页大小
    :type pageSize: int
    """
    res = requests.post(
        f"{BACKEND_BASE_URL}/approval/approve/page/list",
        json={
            "pageNum": pageNum,
            "pageSize": pageSize
        },
        headers={
            "Authorization": get_user_token()
        }
    )

    res.raise_for_status()
    return res.json()

def create_approval(title: str, typeId: int, startTime: str, endTime: str, reason: str):
    """
    创建申请
    
    :param title: 标题
    :type title: str
    :param typeId: 类型id  1:请假 2:报销 3:其他假
    :type typeId: int
    :param startTime: 开始时间  格式: yyyy-MM-dd HH:mm:ss
    :type startTime: str
    :param endTime: 结束时间  格式: yyyy-MM-dd HH:mm:ss
    :type endTime: str
    :param reason: 原因
    :type reason: str
    """
    res = requests.post(
        f"{BACKEND_BASE_URL}/approval/leave",
        json={
            "title": title,
            "typeId": typeId,
            "startTime": startTime,
            "endTime": endTime,
            "reason": reason
        },
        headers={
            "Authorization": get_user_token()
        }
    )

    res.raise_for_status()
    return res.json()

def approve(id: int, taskId: int, flag: bool):
    """
    审批
    
    :param id: 业务id
    :type id: int
    :param taskId: 任务id
    :type taskId: int
    :param flag: 是否通过  true:通过 false:拒绝
    :type flag: bool
    """
    res = requests.post(
        f"{BACKEND_BASE_URL}/approval/approve/leave",
        json={
            "id": id,
            "taskId": taskId,
            "flag": flag
        },
        headers={
            "Authorization": get_user_token()
        }
    )

    res.raise_for_status()
    return res.json()


