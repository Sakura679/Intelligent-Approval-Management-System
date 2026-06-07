# app/core/security.py (建议新建此文件存放安全相关逻辑)
from fastapi import Header, HTTPException

from app.context.context_handler import set_user_token

async def verify_and_set_token(authorization: str = Header(None)):
    """
    依赖函数：
    1. 从请求头获取 Authorization
    2. 验证 Token (可选，这里仅做非空检查)
    3. 将 Token 存入 contextvars
    """
    if not authorization:
        raise HTTPException(status_code=401, detail="Missing Authorization Header")
    
    # 通常 Header 传过来可能是 "Bearer <token>" 格式，需要根据后端规范处理
    # 假设后端直接接收 Token 字符串，或者你需要去掉 "Bearer " 前缀
    token = authorization
    # if authorization.startswith("Bearer "):
    #     token = authorization.split(" ")[1]

    # 关键步骤：将 Token 设置到当前请求的上下文中
    set_user_token(token)
    
    return token